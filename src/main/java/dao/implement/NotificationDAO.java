package dao.implement;

import model.Notification;
import dao.DBContext; // Sử dụng lớp DBContext của bạn
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class NotificationDAO extends DBContext {

    // --- SQL QUERIES ---
    private static final String INSERT_NOTIFICATION_SQL =
            "INSERT INTO NOTIFICATION (USER_ID, TYPE, TITLE, CONTENT, LINK_URL) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_LATEST_NOTIFICATIONS_BY_USERID =
            "SELECT TOP 5 NOTIFICATION_ID, USER_ID, TYPE, TITLE, CONTENT, LINK_URL, CREATED_AT, IS_READ " + // ✅ THÊM IS_READ
                    "FROM NOTIFICATION " +
                    "WHERE USER_ID = ? " +
                    "ORDER BY CREATED_AT DESC";

    private static final String COUNT_UNREAD_SQL =
            "SELECT COUNT(*) FROM NOTIFICATION WHERE USER_ID = ? AND IS_READ = 0";

    private static final String MARK_AS_READ_SQL =
            "UPDATE NOTIFICATION SET IS_READ = 1 WHERE NOTIFICATION_ID = ?";


    // =========================================================================
    // 1. INSERT: Lưu thông báo mới (Logic giữ nguyên)
    // =========================================================================

    public void insertNotification(Notification noti) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(INSERT_NOTIFICATION_SQL);

            ps.setInt(1, noti.getUserId());
            ps.setString(2, noti.getType());
            ps.setString(3, noti.getTitle());
            ps.setString(4, noti.getContent());
            ps.setString(5, noti.getLinkUrl());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi INSERT Notification:");
            e.printStackTrace();
            throw e;
        } finally {
            closeConnection(connection, ps, null);
        }
    }


    // =========================================================================
    // 2. SELECT: Lấy 5 thông báo mới nhất (Cập nhật IS_READ)
    // =========================================================================

    public List<Notification> getLatestNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(SELECT_LATEST_NOTIFICATIONS_BY_USERID);

            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                Notification noti = mapRowToNotification(rs);
                notifications.add(noti);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SELECT Notification:");
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, rs);
        }
        return notifications;
    }


    // =========================================================================
    // 3. GET UNREAD COUNT (MỚI)
    // =========================================================================

    public int getUnreadCountByUserId(int userId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(COUNT_UNREAD_SQL);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Trả về kết quả COUNT(*)
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SELECT Unread Count:");
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, rs);
        }
        return 0;
    }


    // =========================================================================
    // 4. MARK AS READ (MỚI)
    // =========================================================================

    public void markAsRead(int notificationId) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(MARK_AS_READ_SQL);
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi UPDATE Mark As Read:");
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, null);
        }
    }


    // =========================================================================
    // 5. HELPER METHODS (Cập nhật IS_READ)
    // =========================================================================

    private Notification mapRowToNotification(ResultSet rs) throws SQLException {
        Notification noti = new Notification();
        noti.setNotificationId(rs.getInt("NOTIFICATION_ID"));
        noti.setUserId(rs.getInt("USER_ID"));
        noti.setType(rs.getString("TYPE"));
        noti.setTitle(rs.getString("TITLE"));
        noti.setContent(rs.getString("CONTENT"));
        noti.setLinkUrl(rs.getString("LINK_URL"));

        // ✅ ÁNH XẠ IS_READ
        noti.setRead(rs.getBoolean("IS_READ"));

        Timestamp timestamp = rs.getTimestamp("CREATED_AT");
        if (timestamp != null) {
            noti.setCreatedAt(timestamp.toLocalDateTime());
        }

        return noti;
    }

    /**
     * Hàm tiện ích để đóng Connection, PreparedStatement và ResultSet.
     */
    private void closeConnection(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi đóng kết nối: " + e.getMessage());
        }
    }
}
