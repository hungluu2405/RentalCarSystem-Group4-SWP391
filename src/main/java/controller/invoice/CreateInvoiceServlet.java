package controller.invoice;

import dao.implement.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Invoice;
import model.Payment;
import service.invoice.InvoiceService;
import java.io.IOException;

@WebServlet("/invoice/create")
public class CreateInvoiceServlet extends HttpServlet {

    private final InvoiceService invoiceService = new InvoiceService();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing bookingId parameter");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);

            // ============ THÊM DEBUG ============
            System.out.println("\n==================== CREATE INVOICE DEBUG ====================");
            System.out.println("📌 Step 1: Received bookingId = " + bookingId);

            Invoice existingInvoice = invoiceService.getInvoiceByBookingId(bookingId);
            if (existingInvoice != null) {
                System.out.println("⚠️ Invoice already exists, returning existing one");
                System.out.println("==============================================================\n");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"Invoice already exists\", \"invoiceId\": " + existingInvoice.getInvoiceId() + "}");
                return;
            }

            System.out.println("📌 Step 2: No existing invoice, creating new one...");

            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            System.out.println("📌 Step 3: Query payment for bookingId=" + bookingId);

            if (payment != null) {
                System.out.println("✅ Payment FOUND!");
                System.out.println("   - Payment ID: " + payment.getPaymentId());
                System.out.println("   - Booking ID: " + payment.getBookingId());
                System.out.println("   - Amount: " + payment.getAmount());
                System.out.println("   - Status: " + payment.getStatus());
                System.out.println("   - Method: " + payment.getMethod());
                System.out.println("   - Paid At: " + payment.getPaidAt());
            } else {
                System.out.println("❌ Payment NOT FOUND!");
                System.out.println("   Checking all payments in DB...");

                var allPayments = paymentDAO.getAll();
                System.out.println("   Total payments in database: " + allPayments.size());

                if (allPayments.size() > 0) {
                    System.out.println("   Recent payments:");
                    for (int i = 0; i < Math.min(5, allPayments.size()); i++) {
                        Payment p = allPayments.get(i);
                        System.out.println("   [" + i + "] PaymentId=" + p.getPaymentId() +
                                ", BookingId=" + p.getBookingId() +
                                ", Status=" + p.getStatus());
                    }
                }
            }

            Integer paymentId = (payment != null) ? payment.getPaymentId() : null;
            System.out.println("📌 Step 4: Passing paymentId=" + paymentId + " to InvoiceService");

            Invoice invoice = invoiceService.createInvoiceForBooking(bookingId, paymentId);

            System.out.println("📌 Step 5: Invoice creation result: " + (invoice != null ? "SUCCESS" : "FAILED"));
            if (invoice != null) {
                System.out.println("   - Invoice ID: " + invoice.getInvoiceId());
                System.out.println("   - Invoice Number: " + invoice.getInvoiceNumber());
                System.out.println("   - Payment ID in invoice: " + invoice.getPaymentId());
            }



            if (invoice != null) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"success\": true, \"message\": \"Invoice created successfully\", \"invoiceId\": " + invoice.getInvoiceId() + "}");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create invoice");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error creating invoice: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}