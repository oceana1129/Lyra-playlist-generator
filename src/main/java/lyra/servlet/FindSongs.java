package lyra.servlet;

import lyra.tools.SongFinder;
import lyra.dal.*;
import lyra.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/results")
public class FindSongs extends HttpServlet {

    private static final int MIN_SONGS = 5;
    private static final int MAX_SONGS = 20;
    private static final int MAX_TITLE_DUPLICATES = 3;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String[] selectedEmotions = req.getParameterValues("emotion");
            String[] selectedGenres = req.getParameterValues("genre");
            String[] selectedActivity = req.getParameterValues("activity");

            int popularity       = Integer.parseInt(req.getParameter("popularity"));
            int energy           = Integer.parseInt(req.getParameter("energy"));
            int danceability     = Integer.parseInt(req.getParameter("danceability"));
            int positivity       = Integer.parseInt(req.getParameter("positivity"));
            int instrumentalness = Integer.parseInt(req.getParameter("instrumentalness"));

            if (selectedEmotions == null || selectedEmotions.length == 0 ||
                    selectedGenres == null   || selectedGenres.length == 0) {

                req.setAttribute("error", "Please select at least one emotion and one genre.");
                req.getRequestDispatcher("form.jsp").forward(req, resp);
                return;
            }

            SongFinder finder = new SongFinder();
            Song matchedSong = finder.findMatchingSong(
                    selectedEmotions, selectedGenres,
                    popularity, energy, danceability, positivity, instrumentalness
            );

            List<Song> recommendedSongs = new ArrayList<>();
            List<List<Artist>> artistsPerSong = new ArrayList<>();

            if (matchedSong != null) {
                SongDao songDao = SongDao.getInstance();
                RecommendationDao recommendationDao = RecommendationDao.getInstance();
                SongArtistDao songArtistDao = SongArtistDao.getInstance();
                ArtistDao artistDao = ArtistDao.getInstance();

                // add best match first
                recommendedSongs.add(matchedSong);
                artistsPerSong.add(getArtistsForSong(matchedSong, songArtistDao, artistDao));

                List<Recommendation> recommendations =
                        recommendationDao.getRecommendationsBySongId(matchedSong.getSongId());

                recommendations.sort(
                        (a, b) -> Double.compare(b.getSimilarityScore(), a.getSimilarityScore()));

                for (Recommendation r : recommendations) {
                    Song song = songDao.getSongById(r.getSimilarSongId());
                    if (song != null) {
                        recommendedSongs.add(song);
                        artistsPerSong.add(getArtistsForSong(song, songArtistDao, artistDao));
                    }
                }
            }

            // limit duplicate titles
            List<Song> filteredSongs = new ArrayList<>();
            List<List<Artist>> filteredArtists = new ArrayList<>();
            Map<String, Integer> titleCounts = new HashMap<>();

            for (int i = 0; i < recommendedSongs.size(); i++) {
                Song s = recommendedSongs.get(i);
                String titleKey = s.getTitle() == null
                        ? ""
                        : s.getTitle().toLowerCase().trim();

                int count = titleCounts.getOrDefault(titleKey, 0);
                if (count >= MAX_TITLE_DUPLICATES) {
                    continue;
                }
                titleCounts.put(titleKey, count + 1);
                filteredSongs.add(s);
                filteredArtists.add(artistsPerSong.get(i));
            }

            recommendedSongs = filteredSongs;
            artistsPerSong = filteredArtists;

            // ensure at least MIN_SONGS by filling with random songs if needed
            if (recommendedSongs.size() < MIN_SONGS) {
                SongDao songDao = SongDao.getInstance();
                SongArtistDao songArtistDao = SongArtistDao.getInstance();
                ArtistDao artistDao = ArtistDao.getInstance();

                List<Song> randomSongs = songDao.getRandomSongs(MIN_SONGS * 5);

                for (Song s : randomSongs) {
                    if (recommendedSongs.size() >= MIN_SONGS) {
                        break;
                    }

                    boolean alreadyIncluded = false;
                    for (Song existing : recommendedSongs) {
                        if (existing.getSongId() == s.getSongId()) {
                            alreadyIncluded = true;
                            break;
                        }
                    }
                    if (!alreadyIncluded) {
                        recommendedSongs.add(s);
                        artistsPerSong.add(getArtistsForSong(s, songArtistDao, artistDao));
                    }
                }
            }

            // cap total results
            if (recommendedSongs.size() > MAX_SONGS) {
                recommendedSongs = new ArrayList<>(recommendedSongs.subList(0, MAX_SONGS));
                artistsPerSong = new ArrayList<>(artistsPerSong.subList(0, MAX_SONGS));
            }

            req.setAttribute("recommendedSongs", recommendedSongs);
            req.setAttribute("artistsPerSong", artistsPerSong);

            req.getRequestDispatcher("/results.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Database error while finding songs", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid number format in form input", e);
        }
    }

    private List<Artist> getArtistsForSong(
            Song song,
            SongArtistDao songArtistDao,
            ArtistDao artistDao
    ) throws SQLException {

        List<Artist> artists = new ArrayList<>();
        List<SongArtist> mappings = songArtistDao.getBySongId(song.getSongId());

        for (SongArtist map : mappings) {
            Artist artist = artistDao.getArtistById(map.getArtistId());
            if (artist != null) {
                artists.add(artist);
            }
        }
        return artists;
    }
}
