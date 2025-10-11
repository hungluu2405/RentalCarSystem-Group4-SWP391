
package controller.account;


import dao.implement.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Đường dẫn đã chính xác tới file jsp trong thư mục view/homePage
        request.getRequestDispatcher("view/account/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.checkLogin(email, password);

        if (user == null) {
            // Đăng nhập thất bại, trả về trang login với thông báo lỗi
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("view/account/login.jsp").forward(request, response);
        } else {
            // Đăng nhập thành công, lưu user vào session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            int role = user.getRoleId(); // hoặc user.getUserRole() nếu bạn đặt tên khác

            if (role == 1) {
                // Chuyển hướng đến dashboard admin
                response.sendRedirect(request.getContextPath() + "/accountDB");
            } else {
                // Người dùng thường -> về trang home
                response.sendRedirect(request.getContextPath() + "/home");
            }
            // Chuyển hướng về trang chủ một cách an toàn
//            response.sendRedirect(request.getContextPath() + "/home"); 
        }
    }
}