package service.booking;

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

        if (booking == null) {
            throw new Exception("Booking ID " + bookingId + " does not exist.");
        }

        if (!"Approved".equalsIgnoreCase(booking.getStatus())) {
            throw new Exception("Booking ID " + bookingId + " is not approved yet.");
        }

        double usdAmount = booking.getTotalPrice();
        if (usdAmount <= 0) {
            throw new Exception("Total price must be greater than zero.");
        }

        // Format số USD theo chuẩn PayPal (2 chữ số thập phân)
        String totalAmountStr = String.format("%.2f", usdAmount);

        Amount amount = new Amount();
        amount.setCurrency(PayPalConfig.CURRENCY); // "USD"
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

    /**
     * Xác nhận thanh toán PayPal & lưu record vào DB
     */
    public boolean executeAndRecordPayment(int bookingId, String paymentId, String payerId) throws Exception {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(getApiContext(), paymentExecution);

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            double paidAmount = Double.parseDouble(
                    executedPayment.getTransactions().get(0).getAmount().getTotal()
            );

            String paypalTransactionId = executedPayment.getId();

            // Lưu record thanh toán (giá trị đã là USD)
            boolean paymentInserted = bookingDAO.insertPaymentRecord(
                    bookingId,
                    paypalTransactionId,
                    paidAmount,
                    "Paid"
            );

            return paymentInserted;
        }

        return false;
    }
}
