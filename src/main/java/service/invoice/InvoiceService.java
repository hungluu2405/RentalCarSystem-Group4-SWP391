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


            // Lấy thông tin booking

            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                return null;
            }
            // Tạo invoice mới

            Invoice invoice = new Invoice();
            invoice.setBookingId(bookingId);
            invoice.setPaymentId(paymentId);


            // Generate invoice number (format: INV-YYYYMMDD-BOOKINGID)

            String invoiceNumber = generateInvoiceNumber(bookingId);
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setIssueDate(LocalDateTime.now());
            invoice.setDueDate(LocalDateTime.now().plusDays(7)); // Due date 7 days from now


            // Calculate amounts

            double subtotal = booking.getTotalPrice();
            double taxRate = 0.10; // 10% VAT
            double taxAmount = subtotal * taxRate;
            double totalAmount = subtotal + taxAmount;

            invoice.setTotalAmount(totalAmount);
            invoice.setTaxAmount(taxAmount);
            invoice.setStatus("Issued");
            invoice.setNotes("Thank you for your business!");

            boolean inserted = invoiceDAO.insertInvoice(invoice);
            if (inserted) {
                return invoiceDAO.getInvoiceByNumber(invoiceNumber);
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