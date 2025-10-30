package controller.payment;

import service.PayPalService;
import service.BookingService;  // ← THÊM IMPORT
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/execute-payment")
public class PaymentExecuteServlet extends HttpServlet {

    private final PayPalService paypalService = new PayPalService();
    private final BookingService bookingService = new BookingService();  // ← THÊM DÒNG NÀY

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
            // 1. XỬ LÝ PAYPAL
            boolean paypalSuccess = paypalService.executeAndRecordPayment(bookingId, paymentId, payerId);

            final String ORDER_DETAIL_URL = "/customer/customerOrder";

            if (paypalSuccess) {
                // 2. UPDATE STATUS + GỬI THÔNG BÁO
                boolean statusUpdated = bookingService.markAsPaid(bookingId);

                if (statusUpdated) {
                    response.sendRedirect(request.getContextPath() + ORDER_DETAIL_URL + "?id=" + bookingId + "&payment=success");
                } else {
                    response.sendRedirect(request.getContextPath() + ORDER_DETAIL_URL + "?id=" + bookingId + "&payment=failed");
                }
            } else {
                response.sendRedirect(request.getContextPath() + ORDER_DETAIL_URL + "?id=" + bookingId + "&payment=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error-page?msg=Payment Execution Error: " + e.getMessage());
        }
    }
}