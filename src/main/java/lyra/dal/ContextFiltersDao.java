package lyra.dal;

import lyra.model.ContextFilters;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContextFiltersDao {
    protected ConnectionManager connectionManager;
    private static ContextFiltersDao instance = null;

    protected ContextFiltersDao() { this.connectionManager = new ConnectionManager(); }

    public static ContextFiltersDao getInstance() {
        if (instance == null) { instance = new ContextFiltersDao(); }
        return instance;
    }

    public ContextFilters create(ContextFilters cf) throws SQLException {
        String sql = "INSERT INTO ContextFilters(songId,party,study,relaxation,exercise," +
                "running,yoga,driving,social,morning) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cf.getSongId());
            ps.setBoolean(2, cf.isParty());
            ps.setBoolean(3, cf.isStudy());
            ps.setBoolean(4, cf.isRelaxation());
            ps.setBoolean(5, cf.isExercise());
            ps.setBoolean(6, cf.isRunning());
            ps.setBoolean(7, cf.isYoga());
            ps.setBoolean(8, cf.isDriving());
            ps.setBoolean(9, cf.isSocial());
            ps.setBoolean(10, cf.isMorning());
            ps.executeUpdate();
            return cf;
        }
    }

    public ContextFilters getBySongId(int songId) throws SQLException {
        String sql = "SELECT songId,party,study,relaxation,exercise,running,yoga,driving,social,morning " +
                "FROM ContextFilters WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ContextFilters(
                            rs.getInt(1), rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4),
                            rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7),
                            rs.getBoolean(8), rs.getBoolean(9), rs.getBoolean(10)
                    );
                }
            }
        }
        return null;
    }

    public List<ContextFilters> getAll() throws SQLException {
        String sql = "SELECT songId,party,study,relaxation,exercise,running,yoga,driving,social,morning FROM ContextFilters";
        List<ContextFilters> out = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new ContextFilters(
                        rs.getInt(1), rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4),
                        rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7),
                        rs.getBoolean(8), rs.getBoolean(9), rs.getBoolean(10)
                ));
            }
        }
        return out;
    }

    public ContextFilters updateParty(ContextFilters cf, boolean party) throws SQLException {
        String sql = "UPDATE ContextFilters SET party=? WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, party);
            ps.setInt(2, cf.getSongId());
            ps.executeUpdate();
            cf.setParty(party);
            return cf;
        }
    }

    public void delete(ContextFilters cf) throws SQLException {
        String sql = "DELETE FROM ContextFilters WHERE songId=?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cf.getSongId());
            ps.executeUpdate();
        }
    }
}
