package service;

import util.PayPalConfig;
import dao.implement.BookingDAO;
import model.Booking;
import com.paypal.base.rest.APIContext;
import com.paypal.api.payments.*;
import java.util.ArrayList;
import java.util.List;

public class PayPalService {

    private final BookingDAO bookingDAO = new BookingDAO();

    private APIContext getApiContext() {

        return new APIContext(PayPalConfig.CLIENT_ID, PayPalConfig.CLIENT_SECRET, PayPalConfig.MODE);
    }

    public Payment createOrder(int bookingId, String contextPath) throws Exception {


        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null || !booking.getStatus().equalsIgnoreCase("Approved")) {
            throw new Exception("Booking ID " + bookingId + " is not approved or does not exist.");
        }

        double vndAmount = booking.getTotalPrice();


        final double EXCHANGE_RATE = 25000.00;

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

   boolean executeAndRecordPayment(int bookingId, String paymentId, String payerId) throws Exception {

        // 1. Thực thi Payment trên PayPal
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        // Gọi API Execute Payment để xác nhận giao dịch
        Payment executedPayment = payment.execute(getApiContext(), paymentExecution);

        // 2. XỬ LÝ KẾT QUẢ VÀ CẬP NHẬT DB
        // Kiểm tra xem PayPal có trả về trạng thái phê duyệt (APPROVED) không
        if (executedPayment.getState().equalsIgnoreCase("approved")) {

            // Lấy thông tin thanh toán từ phản hồi PayPal
            double paidAmount = Double.parseDouble(executedPayment.getTransactions().get(0).getAmount().getTotal());
            String paypalTransactionId = executedPayment.getId();

            // Cập nhật trạng thái đơn hàng trong DB từ 'Approved' -> 'Completed' (dựa trên Check Constraint)
            boolean statusUpdated = bookingDAO.updateStatus(bookingId, "Completed");

            // Chèn bản ghi vào bảng PAYMENT
            boolean paymentInserted = bookingDAO.insertPaymentRecord(
                    bookingId,
                    paypalTransactionId,
                    paidAmount,
                    "COMPLETED" // Trạng thái bảng PAYMENT
            );

            return statusUpdated && paymentInserted;
        }
        return false;
    }
}