package controller.admin;

import dao.implement.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.User;

@WebServlet(name = "AccountDashBoard", urlPatterns = {"/accountDB"})
public class AccountDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1️⃣ Tạo DAO để lấy danh sách người dùng
        UserDAO userDAO = new UserDAO();
        List<User> listU = userDAO.getAllUsersForAdmin();

        // 2️⃣ Gửi danh sách sang JSP
        request.setAttribute("listU", listU);

        // 3️⃣ Forward sang JSP hiển thị
        request.getRequestDispatcher("/view/admin/accountdashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
