package controller.booking;

import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import dao.implement.CarDAO;
import model.UserProfile;

import java.io.IOException;
import java.net.URL;

@WebServlet("/booking-confirmation")
public class BookingConfirmationServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final UserProfileDAO uDAO = new UserProfileDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Booking booking = (Booking) session.getAttribute("confirmedBooking");

        // Nếu không có booking -> về trang chủ
        if (booking == null) {
            System.err.println("❌ No booking found in session.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Lấy dữ liệu người dùng & xe
        UserProfile u = uDAO.findByUserId(booking.getUserId());
        CarViewModel car = carDAO.getCarSingleById(booking.getCarId());

        // Set attributes
        request.setAttribute("car", car);
        request.setAttribute("booking", booking);
        request.setAttribute("u", u);

        // Lấy discount và promoCode nếu có
        Double discount = (Double) session.getAttribute("bookingDiscount");
        String promoCode = (String) session.getAttribute("bookingPromoCode");

        System.out.println("💰 Discount (session): " + discount);
        System.out.println("🎫 Promo Code (session): " + promoCode);

        if (discount != null && discount > 0) {
            request.setAttribute("discount", discount);
        }
        if (promoCode != null && !promoCode.isEmpty()) {
            request.setAttribute("promoCode", promoCode);
        }

        // Xoá session tạm
        session.removeAttribute("confirmedBooking");
        session.removeAttribute("bookingDiscount");
        session.removeAttribute("bookingPromoCode");


        // === FORWARD ===
        request.getRequestDispatcher("/view/booking/confirmation.jsp").forward(request, response);
    }
}
