package service.invoice;

import dao.implement.BookingDAO;
import dao.implement.InvoiceDAO;
import dao.implement.PaymentDAO;
import model.Booking;
import model.Invoice;
import model.Payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private final BookingDAO bookingDAO;
    private final PaymentDAO paymentDAO;

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
        this.bookingDAO = new BookingDAO();
        this.paymentDAO = new PaymentDAO();
    }

    public Invoice createInvoiceForBooking(int bookingId, Integer paymentId) {

        try {
            Invoice existingInvoice = invoiceDAO.getInvoiceByBookingId(bookingId);
            if (existingInvoice != null) {
                return existingInvoice;
            }

            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                return null;
            }

            System.out.println("=== INVOICE SERVICE DEBUG ===");
            System.out.println("Creating invoice for bookingId: " + bookingId);
            System.out.println("Received paymentId parameter: " + paymentId);

            // ✅ FIX: Nếu paymentId là null, tự động query từ database
            if (paymentId == null) {

                Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

                if (payment != null) {
                    paymentId = payment.getPaymentId();
                    System.out.println("✅ Found payment! PaymentId: " + paymentId);
                } else {
                    System.out.println("❌ Still no payment found for bookingId=" + bookingId);
                    System.out.println("⚠️ Creating invoice WITHOUT payment_id");
                }
            }

            Invoice invoice = new Invoice();
            invoice.setBookingId(bookingId);
            invoice.setPaymentId(paymentId);

            System.out.println("Invoice object paymentId after setPaymentId: " + invoice.getPaymentId());

            String invoiceNumber = generateInvoiceNumber(bookingId);
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setIssueDate(LocalDateTime.now());
            invoice.setDueDate(LocalDateTime.now().plusDays(7));

            double totalAmount = booking.getTotalPrice();
            invoice.setTotalAmount(totalAmount);
            invoice.setTaxAmount(null);
            invoice.setStatus("Issued");
            invoice.setNotes("Thank you for your business!");

            boolean inserted = invoiceDAO.insertInvoice(invoice);

            System.out.println("Invoice inserted: " + inserted);

            if (inserted) {
                Invoice createdInvoice = invoiceDAO.getInvoiceByNumber(invoiceNumber);
                if (createdInvoice != null) {
                    System.out.println("Created invoice ID: " + createdInvoice.getInvoiceId());
                    System.out.println("Created invoice PAYMENT_ID in DB: " + createdInvoice.getPaymentId());
                }
                System.out.println("=============================");
                return createdInvoice;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateInvoiceNumber(int bookingId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDateTime.now().format(formatter);
        return String.format("INV-%s-%d", dateStr, bookingId);
    }

    public Invoice getInvoiceByBookingId(int bookingId) {
        return invoiceDAO.getInvoiceByBookingId(bookingId);
    }

    public boolean updateInvoiceStatus(int invoiceId, String status) {
        Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
        if (invoice != null) {
            invoice.setStatus(status);
            return invoiceDAO.updateInvoice(invoice);
        }
        return false;
    }
}