package lyra.dal;

import lyra.model.Lyrics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LyricsDao {
    protected ConnectionManager connectionManager;
    private static LyricsDao instance = null;

    protected LyricsDao() { this.connectionManager = new ConnectionManager(); }

    public static LyricsDao getInstance() {
        if (instance == null) { instance = new LyricsDao(); }
        return instance;
    }

    public Lyrics create(Lyrics lyr) throws SQLException {
        String sql = "INSERT INTO Lyrics(songId,content) VALUES(?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lyr.getSongId());
            ps.setString(2, lyr.getContent());
            ps.executeUpdate();
            return lyr;
        }
    }

    public Lyrics getBySongId(int songId) throws SQLException {
        String sql = "SELECT songId,content FROM Lyrics WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Lyrics(rs.getInt(1), rs.getString(2));
                }
            }
        }
        return null;
    }

    public List<Lyrics> getAll() throws SQLException {
        String sql = "SELECT songId,content FROM Lyrics";
        List<Lyrics> out = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Lyrics(rs.getInt(1), rs.getString(2)));
            }
        }
        return out;
    }

    public Lyrics updateContent(Lyrics lyr, String newContent) throws SQLException {
        String sql = "UPDATE Lyrics SET content=? WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newContent);
            ps.setInt(2, lyr.getSongId());
            ps.executeUpdate();
            lyr.setContent(newContent);
            return lyr;
        }
    }

    public void delete(Lyrics lyr) throws SQLException {
        String sql = "DELETE FROM Lyrics WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lyr.getSongId());
            ps.executeUpdate();
        }
    }
}
