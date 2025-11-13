package lyra.dal;

import lyra.model.AudioFeatures;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AudioFeaturesDao {
    protected ConnectionManager connectionManager;
    private static AudioFeaturesDao instance = null;

    protected AudioFeaturesDao() { this.connectionManager = new ConnectionManager(); }

    public static AudioFeaturesDao getInstance() {
        if (instance == null) { instance = new AudioFeaturesDao(); }
        return instance;
    }

    public AudioFeatures create(AudioFeatures af) throws SQLException {
        String sql = "INSERT INTO AudioFeatures(songId,popularity,energy,danceability,positiveness," +
                "speechiness,liveness,acousticness,instrumentalness) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, af.getSongId());
            ps.setInt(2, af.getPopularity());
            ps.setInt(3, af.getEnergy());
            ps.setInt(4, af.getDanceability());
            ps.setInt(5, af.getPositiveness());
            ps.setInt(6, af.getSpeechiness());
            ps.setInt(7, af.getLiveness());
            ps.setInt(8, af.getAcousticness());
            ps.setInt(9, af.getInstrumentalness());
            ps.executeUpdate();
            return af;
        }
    }

    public AudioFeatures getBySongId(int songId) throws SQLException {
        String sql = "SELECT songId,popularity,energy,danceability,positiveness," +
                "speechiness,liveness,acousticness,instrumentalness FROM AudioFeatures WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AudioFeatures(
                            rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
                            rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9)
                    );
                }
            }
        }
        return null;
    }

    public List<AudioFeatures> getAll() throws SQLException {
        String sql = "SELECT songId,popularity,energy,danceability,positiveness," +
                "speechiness,liveness,acousticness,instrumentalness FROM AudioFeatures";
        List<AudioFeatures> out = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new AudioFeatures(
                        rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
                        rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9)
                ));
            }
        }
        return out;
    }

    public AudioFeatures updatePopularity(AudioFeatures af, int newPopularity) throws SQLException {
        String sql = "UPDATE AudioFeatures SET popularity=? WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newPopularity);
            ps.setInt(2, af.getSongId());
            ps.executeUpdate();
            af.setPopularity(newPopularity);
            return af;
        }
    }

    public void delete(AudioFeatures af) throws SQLException {
        String sql = "DELETE FROM AudioFeatures WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, af.getSongId());
            ps.executeUpdate();
        }
    }
}
