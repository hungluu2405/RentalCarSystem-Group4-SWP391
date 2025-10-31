package controller.payment;

import service.booking.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Links;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/create-payment")
public class CreatePaymentServlet extends HttpServlet {


    private final PayPalService paypalService = new PayPalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String bookingIdStr = request.getParameter("bookingId");
        String contextPath = request.getContextPath();

        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Booking ID for payment.");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);

            Payment payment = paypalService.createOrder(bookingId, contextPath);


            String approvalLink = null;
            for (Links link : payment.getLinks()) {
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    approvalLink = link.getHref();
                    break;
                }
            }


            if (approvalLink != null) {
                response.sendRedirect(approvalLink);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot get PayPal approval URL.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Booking ID format.");
        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Payment creation failed: " + e.getMessage());
        }
    }
}