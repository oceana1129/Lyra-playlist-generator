package lyra.servlet;

import lyra.tools.SongFinder;
import lyra.dal.*;
import lyra.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/results")
public class FindSongs extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Get all selected inputs from form
            String[] selectedEmotions = req.getParameterValues("emotion");
            String[] selectedGenres = req.getParameterValues("genre");
            String[] selectedActivity = req.getParameterValues("activity");
            int popularity = Integer.parseInt(req.getParameter("popularity"));
            int energy = Integer.parseInt(req.getParameter("energy"));
            int danceability = Integer.parseInt(req.getParameter("danceability"));
            int positivity = Integer.parseInt(req.getParameter("positivity"));
            int instrumentalness = Integer.parseInt(req.getParameter("instrumentalness"));

            // Find the best matching song
            SongFinder finder = new SongFinder();
            Song matchedSong = finder.findMatchingSong(
                    selectedEmotions, selectedGenres, popularity, energy, danceability, positivity, instrumentalness
            );

            List<Song> recommendedSongs = new ArrayList<>();
            List<List<Artist>> artistsPerSong = new ArrayList<>();

            if (matchedSong != null) {

                SongDao songDao = SongDao.getInstance();
                RecommendationDao recommendationDao = RecommendationDao.getInstance();
                SongArtistDao songArtistDao = SongArtistDao.getInstance();
                ArtistDao artistDao = ArtistDao.getInstance();

                // add matched song first
                recommendedSongs.add(matchedSong);
                artistsPerSong.add(getArtistsForSong(matchedSong, songArtistDao, artistDao));

                // get recommended songs
                List<Recommendation> recommendations =
                        recommendationDao.getRecommendationsBySongId(matchedSong.getSongId());
                
                // sort recommended songs on similarity score
                recommendations.sort((a, b) -> Double.compare(b.getSimilarityScore(), a.getSimilarityScore()));

                for (Recommendation r : recommendations) {
                    Song song = songDao.getSongById(r.getSimilarSongId());
                    if (song != null) {
                        recommendedSongs.add(song);

                        // Load and store artists for this song
                        List<Artist> artists = getArtistsForSong(song, songArtistDao, artistDao);
                        artistsPerSong.add(artists);
                    }
                }
            }

            // pass to JSP
            req.setAttribute("recommendedSongs", recommendedSongs);
            req.setAttribute("artistsPerSong", artistsPerSong);

            // forward to results.jsp
            req.getRequestDispatcher("/results.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Database error while finding songs", e);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid number format in form input", e);
        }
    }
    
    /** Load artists for a Song using SongArtist and Artist tables */
    private List<Artist> getArtistsForSong(
            Song song,
            SongArtistDao songArtistDao,
            ArtistDao artistDao
    ) throws SQLException {

        List<Artist> artists = new ArrayList<>();

        List<SongArtist> mappings =
                songArtistDao.getBySongId(song.getSongId());

        for (SongArtist map : mappings) {
            Artist artist = artistDao.getArtistById(map.getArtistId());
            if (artist != null) {
                artists.add(artist);
            }
        }
        return artists;
    }
}
