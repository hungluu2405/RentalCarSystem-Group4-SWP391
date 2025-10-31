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
    // XÓA: NotificationDAO, CarDAO, Car car - KHÔNG CẦN NỮA

    private APIContext getApiContext() {
        return new APIContext(PayPalConfig.CLIENT_ID, PayPalConfig.CLIENT_SECRET, PayPalConfig.MODE);
    }

    public Payment createOrder(int bookingId, String contextPath) throws Exception {
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

    // CHỈ XỬ LÝ PAYPAL + LƯU PAYMENT RECORD
    public boolean executeAndRecordPayment(int bookingId, String paymentId, String payerId) throws Exception {

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(getApiContext(), paymentExecution);

        if (executedPayment.getState().equalsIgnoreCase("approved")) {

            double paidAmount = Double.parseDouble(
                    executedPayment.getTransactions().get(0).getAmount().getTotal()
            );
            String paypalTransactionId = executedPayment.getId();

            // CHỈ LƯU PAYMENT RECORD - KHÔNG UPDATE STATUS
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