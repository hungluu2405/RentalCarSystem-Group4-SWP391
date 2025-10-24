package controller.customer;

import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import dao.implement.CarDAO;
import model.UserProfile;

import java.io.IOException;

@WebServlet("/booking-confirmation")
public class BookingConfirmationServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final UserProfileDAO uDAO = new UserProfileDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Booking booking = (Booking) session.getAttribute("confirmedBooking");

        if (booking == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        UserProfile u = uDAO.findByUserId(booking.getUserId());
        CarViewModel car = carDAO.getCarSingleById(booking.getCarId());
        request.setAttribute("car", car);
        request.setAttribute("booking", booking);
        request.setAttribute("u", u);

        Double discount = (Double) session.getAttribute("bookingDiscount");
        String promoCode = (String) session.getAttribute("bookingPromoCode");

        if (discount != null) {
            request.setAttribute("discount", discount);
        }
        if (promoCode != null) {
            request.setAttribute("promoCode", promoCode);
        }


        session.removeAttribute("confirmedBooking");
        session.removeAttribute("bookingDiscount");
        session.removeAttribute("bookingPromoCode");


        request.getRequestDispatcher("/view/booking/confirmation.jsp").forward(request, response);
    }
}