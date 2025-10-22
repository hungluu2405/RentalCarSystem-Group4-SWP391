package controller.payment;

import service.PayPalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/execute-payment")
public class PaymentExecuteServlet extends HttpServlet {

    private final PayPalService paypalService = new PayPalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String cancelStatus = request.getParameter("cancel");
        String bookingIdOnCancel = request.getParameter("bookingId");
        if ("true".equalsIgnoreCase(cancelStatus) && bookingIdOnCancel != null) {

            response.sendRedirect(request.getContextPath() + "/my-booking-detail?id=" + bookingIdOnCancel + "&payment=cancelled");
            return;
        }


        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        String bookingIdStr = request.getParameter("bookingId");

        if (paymentId == null || payerId == null || bookingIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/error-page?msg=Missing PayPal return details.");
            return;
        }

        int bookingId = Integer.parseInt(bookingIdStr);

        try {

            boolean success = paypalService.executeAndRecordPayment(bookingId, paymentId, payerId);
            final String ORDER_DETAIL_URL = "/customer/customerOrder";

            if (success) {

                response.sendRedirect(request.getContextPath() + ORDER_DETAIL_URL + "?id=" + bookingId + "&payment=success");
            } else {

                response.sendRedirect(request.getContextPath() + ORDER_DETAIL_URL + "?id=" + bookingId + "&payment=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect(request.getContextPath() + "/error-page?msg=Payment Execution Error: " + e.getMessage());
        }
    }
}