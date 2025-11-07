package service.booking;

import util.PayPalConfig;
import dao.implement.BookingDAO;
import model.Booking;
import com.paypal.base.rest.APIContext;
import com.paypal.api.payments.*;

import java.util.ArrayList;
import java.util.List;

public class PayPalService {

    // ========== EXCHANGE RATE CONSTANT ==========
    private static final double VND_TO_USD_RATE = 26000.0;

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

        // ========== CONVERT VND → USD ==========
        double priceInVND = booking.getTotalPrice();

        if (priceInVND <= 0) {
            throw new Exception("Total price must be greater than zero.");
        }

        // Convert sang USD cho PayPal
        double priceInUSD = priceInVND / VND_TO_USD_RATE;


        String totalAmountStr = String.format("%.2f", priceInUSD);

        Amount amount = new Amount();
        amount.setCurrency(PayPalConfig.CURRENCY);
        amount.setTotal(totalAmountStr);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Car Rental Payment - Booking #" + bookingId
                + " (" + String.format("%.0f", priceInVND) + " VND)");

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

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {

            // ========== CONVERT USD → VND ==========

            double paidAmountUSD = Double.parseDouble(
                    executedPayment.getTransactions().get(0).getAmount().getTotal()
            );

            // Convert về VND để lưu DB (giữ consistency với data khác)
            double paidAmountVND = paidAmountUSD * VND_TO_USD_RATE; // VD: 30.00 × 26,000 = 780,000 VND

            String paypalTransactionId = executedPayment.getId();

            // Lưu giá VND vào database
            boolean paymentInserted = bookingDAO.insertPaymentRecord(
                    bookingId,
                    paypalTransactionId,
                    paidAmountVND, // Lưu VND
                    "Paid"
            );

            return paymentInserted;
        }

        return false;
    }
}