package dao.implement;

import dao.DBContext;
import model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO extends DBContext {

    public List<Invoice> getAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT INVOICE_ID, BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES FROM INVOICE";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT INVOICE_ID, BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES FROM INVOICE WHERE INVOICE_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Invoice getInvoiceByBookingId(int bookingId) {
        String sql = "SELECT INVOICE_ID, BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES FROM INVOICE WHERE BOOKING_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Invoice getInvoiceByNumber(String invoiceNumber) {
        String sql = "SELECT INVOICE_ID, BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES FROM INVOICE WHERE INVOICE_NUMBER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, invoiceNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertInvoice(Invoice invoice) {
        String sql = "INSERT INTO INVOICE (BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoice.getBookingId());

            if (invoice.getPaymentId() != null) {
                ps.setInt(2, invoice.getPaymentId());
                System.out.println("✅ Setting PAYMENT_ID = " + invoice.getPaymentId());
            } else {
                ps.setNull(2, Types.INTEGER);
                System.out.println("⚠️ Setting PAYMENT_ID = NULL");
            }

            ps.setString(3, invoice.getInvoiceNumber());
            ps.setTimestamp(4, Timestamp.valueOf(invoice.getIssueDate()));

            if (invoice.getDueDate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(invoice.getDueDate()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            ps.setDouble(6, invoice.getTotalAmount());
            ps.setString(7, invoice.getStatus());
            ps.setString(8, invoice.getNotes());

            int rows = ps.executeUpdate();
            System.out.println("✅ Rows affected: " + rows);
            System.out.println("========================================\n");

            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ SQL ERROR:");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInvoice(Invoice invoice) {

        String sql = "UPDATE INVOICE SET BOOKING_ID = ?, PAYMENT_ID = ?, INVOICE_NUMBER = ?, ISSUE_DATE = ?, " +
                "DUE_DATE = ?, TOTAL_AMOUNT = ?, STATUS = ?, NOTES = ? WHERE INVOICE_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoice.getBookingId());
            if (invoice.getPaymentId() != null) {
                ps.setInt(2, invoice.getPaymentId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, invoice.getInvoiceNumber());
            ps.setTimestamp(4, Timestamp.valueOf(invoice.getIssueDate()));
            if (invoice.getDueDate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(invoice.getDueDate()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setDouble(6, invoice.getTotalAmount());
            ps.setString(7, invoice.getStatus());
            ps.setString(8, invoice.getNotes());
            ps.setInt(9, invoice.getInvoiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM INVOICE WHERE INVOICE_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM INVOICE";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Invoice> getInvoicesByStatus(String status) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT INVOICE_ID, BOOKING_ID, PAYMENT_ID, INVOICE_NUMBER, ISSUE_DATE, DUE_DATE, " +
                "TOTAL_AMOUNT, STATUS, NOTES FROM INVOICE WHERE STATUS = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Invoice mapInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("INVOICE_ID"));
        invoice.setBookingId(rs.getInt("BOOKING_ID"));
        int paymentId = rs.getInt("PAYMENT_ID");
        if (!rs.wasNull()) {
            invoice.setPaymentId(paymentId);
        }
        invoice.setInvoiceNumber(rs.getString("INVOICE_NUMBER"));
        Timestamp issueTs = rs.getTimestamp("ISSUE_DATE");
        if (issueTs != null) {
            invoice.setIssueDate(issueTs.toLocalDateTime());
        }

        Timestamp dueTs = rs.getTimestamp("DUE_DATE");
        if (dueTs != null) {
            invoice.setDueDate(dueTs.toLocalDateTime());
        }

        invoice.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
        invoice.setStatus(rs.getString("STATUS"));
        invoice.setNotes(rs.getString("NOTES"));
        return invoice;
    }
}