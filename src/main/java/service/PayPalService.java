package service;

import model.Car;
import util.PayPalConfig;
import dao.implement.BookingDAO;
import dao.implement.NotificationDAO; // <-- THÊM: Import Notification DAO
import model.Booking;
import model.Notification; // <-- THÊM: Import Model Notification
import com.paypal.base.rest.APIContext;
import com.paypal.api.payments.*;
import java.util.ArrayList;
import java.util.List;

public class PayPalService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO(); // <-- KHỞI TẠO DAO
    Car car = new Car();

    private APIContext getApiContext() {
        return new APIContext(PayPalConfig.CLIENT_ID, PayPalConfig.CLIENT_SECRET, PayPalConfig.MODE);
    }

    public Payment createOrder(int bookingId, String contextPath) throws Exception {
        // ... (Hàm này giữ nguyên) ...
        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null || !booking.getStatus().equalsIgnoreCase("Approved")) {
            throw new Exception("Booking ID " + bookingId + " is not approved or does not exist.");
        }

        double vndAmount = booking.getTotalPrice();

        final double EXCHANGE_RATE = 26000.00;

        if (vndAmount <= 0) {
            throw new Exception("Total price must be greater than zero.");
        }

        double usdAmount = vndAmount / EXCHANGE_RATE;

        String totalAmountStr = String.format("%.2f", usdAmount);

        Amount amount = new Amount();
        amount.setCurrency(PayPalConfig.CURRENCY);
        amount.setTotal(totalAmountStr);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Payment for Booking ID: " + bookingId);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        String baseUrl = "http://localhost:8080" + contextPath;

        RedirectUrls redirectUrls = new RedirectUrls();

        redirectUrls.setReturnUrl(baseUrl + "/customer/execute-payment?bookingId=" + bookingId);

        redirectUrls.setCancelUrl(baseUrl + "/customer/customerOrder?id=" + bookingId + "&payment=cancel");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(getApiContext());
    }

    public boolean executeAndRecordPayment(int bookingId, String paymentId, String payerId) throws Exception {

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(getApiContext(), paymentExecution);

        if (executedPayment.getState().equalsIgnoreCase("approved")) {

            double paidAmount = Double.parseDouble(executedPayment.getTransactions().get(0).getAmount().getTotal());
            String paypalTransactionId = executedPayment.getId();

            boolean statusUpdated = bookingDAO.updateStatus(bookingId, "Paid");

            boolean paymentInserted = bookingDAO.insertPaymentRecord(
                    bookingId,
                    paypalTransactionId,
                    paidAmount,
                    "Paid"
            );


            if (statusUpdated && paymentInserted) {
                try {
                    // Cần load lại booking để lấy Customer ID
                    Booking booking = bookingDAO.getBookingById(bookingId);

                    // THÔNG BÁO CHO CUSTOMER: Thanh toán thành công
                    notificationDAO.insertNotification(new Notification(
                            booking.getUserId(), // Customer ID
                            "PAYMENT_SUCCESS",
                            "Payment Successful!",
                            "Payment for your booking vehicle" + " completed successfully. Enjoy your trip!",
                            "/customer/customerOrder?id=" + bookingId
                    ));
                } catch (Exception e) {
                    System.err.println("Lỗi tạo thông báo sau khi thanh toán: " + e.getMessage());
                    // Tiếp tục trả về kết quả thành công của PayPal dù lỗi thông báo
                }
            }


            return statusUpdated && paymentInserted;
        }
        return false;
    }
}
