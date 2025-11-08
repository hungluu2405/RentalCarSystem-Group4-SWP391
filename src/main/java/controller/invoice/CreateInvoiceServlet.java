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

            Invoice existingInvoice = invoiceService.getInvoiceByBookingId(bookingId);
            if (existingInvoice != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"Invoice already exists\", \"invoiceId\": " + existingInvoice.getInvoiceId() + "}");
                return;
            }


            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
            Integer paymentId = (payment != null) ? payment.getPaymentId() : null;

            Invoice invoice = invoiceService.createInvoiceForBooking(bookingId, paymentId);

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