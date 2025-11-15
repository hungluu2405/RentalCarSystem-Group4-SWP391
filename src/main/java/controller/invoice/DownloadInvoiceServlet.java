package controller.invoice;
import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import service.invoice.InvoiceService;
import util.InvoiceHTMLGenerator;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/customer/download-invoice")

public class DownloadInvoiceServlet extends HttpServlet {


    private final InvoiceService invoiceService = new InvoiceService();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        String bookingIdStr = request.getParameter("bookingId");

        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing bookingId parameter");
            return;
        }


        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                return;
            }



            if (booking.getUserId() != currentUser.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to view this invoice");
                return;
            }

            Invoice invoice = invoiceService.getInvoiceByBookingId(bookingId);
            if (invoice == null) {
                invoice = invoiceService.createInvoiceForBooking(bookingId, null);
                if (invoice == null) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create invoice");
                    return;
                }
            }




            Payment payment = null;
            if (invoice.getPaymentId() != null) {
                payment = paymentDAO.getPaymentById(invoice.getPaymentId());
            } else {
                payment = paymentDAO.getPaymentByBookingId(bookingId);

            }

            String customerName = currentUser.getUserProfile() != null ?
                    currentUser.getUserProfile().getFullName() : "N/A";
            String customerEmail = currentUser.getEmail();


            CarViewModel car = carDAO.getCarById(booking.getCarId());
            String carName = car != null ? (car.getBrand() + " " + car.getModel()) : "N/A";


            String invoiceHTML = InvoiceHTMLGenerator.generateInvoiceHTML(
                    invoice, booking, payment, customerName, customerEmail, carName
            );


            // Set response để download hoặc hiển thị
            String action = request.getParameter("action");
            if ("download".equals(action)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"invoice-" + invoice.getInvoiceNumber() + ".html\"");
            } else {
                response.setContentType("text/html; charset=UTF-8");
            }

            PrintWriter out = response.getWriter();
            out.println(invoiceHTML);
            out.flush();
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error generating invoice: " + e.getMessage());
        }
    }

}