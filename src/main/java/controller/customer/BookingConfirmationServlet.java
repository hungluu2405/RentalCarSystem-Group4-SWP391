package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import dao.implement.CarDAO;

import java.io.IOException;

@WebServlet("/booking-confirmation")
public class BookingConfirmationServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin booking từ session
        HttpSession session = request.getSession();
        Booking booking = (Booking) session.getAttribute("confirmedBooking");

        // Loại bỏ các lệnh in ra console

        if (booking == null) {
            // Nếu không có booking trong session, redirect về trang chủ
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Lấy thông tin xe
        CarViewModel car = carDAO.getCarById(booking.getCarId());
        request.setAttribute("car", car);
        request.setAttribute("booking", booking);

        // Lấy thông tin giảm giá nếu có
        Double discount = (Double) session.getAttribute("bookingDiscount");
        String promoCode = (String) session.getAttribute("bookingPromoCode");

        if (discount != null) {
            request.setAttribute("discount", discount);
        }
        if (promoCode != null) {
            request.setAttribute("promoCode", promoCode);
        }

        // Xóa thông tin booking khỏi session sau khi hiển thị
        session.removeAttribute("confirmedBooking");
        session.removeAttribute("bookingDiscount");
        session.removeAttribute("bookingPromoCode");

        // Forward đến trang confirmation
        request.getRequestDispatcher("/view/booking/confirmation.jsp").forward(request, response);
    }
}