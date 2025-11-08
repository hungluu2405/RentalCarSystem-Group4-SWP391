package controller.account;

import dao.implement.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleUser;
import model.User;
import service.account.CreateGoogleAccountService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/create-google-account"})
public class CreateGoogleAccountServlet extends HttpServlet {

    private final CreateGoogleAccountService service = new CreateGoogleAccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        GoogleUser googleUser = (session != null) ? (GoogleUser) session.getAttribute("googleUser") : null;

        // ✅ Kiểm tra nếu không có thông tin Google trong session (bị lỗi flow)
        if (googleUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ✅ Lấy dữ liệu form
        Map<String, String> data = new HashMap<>();
        String[] fields = {
                "username", "password", "re_password", "phone", "dob", "gender",
                "role_id", "address_line", "city", "country", "postal_code"
        };
        for (String f : fields) {
            data.put(f, request.getParameter(f) != null ? request.getParameter(f).trim() : "");
        }

        request.setAttribute("formData", data); // Giữ lại dữ liệu nếu có lỗi để đổ lại form

        // ✅ Kiểm tra hợp lệ form bằng service
        String error = service.validateForm(data, googleUser);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        // ✅ Tạo tài khoản trong DB
        boolean success = service.createAccount(googleUser, data);
        if (success) {
            // Lấy user mới từ DB để lưu vào session
            User newUser = new UserDAO().findUserByEmail(googleUser.getEmail());

            // Hủy session cũ để tránh giữ lại googleUser
            session.invalidate();

            // Tạo session mới và lưu user
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", newUser);

            // ✅ Chuyển hướng về trang chủ
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // Trường hợp lỗi DB
            request.setAttribute("error", "Error creating account. Please try again.");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
        }
    }
}
