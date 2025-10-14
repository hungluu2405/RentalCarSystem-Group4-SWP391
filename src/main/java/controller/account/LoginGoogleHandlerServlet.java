package controller.account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.implement.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleUser;
import model.User;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import util.Constants;

@WebServlet(urlPatterns = {"/login-google-handler"})
public class LoginGoogleHandlerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String accessToken = getToken(code);
            GoogleUser googleUser = getUserInfo(accessToken);

            UserDAO userDAO = new UserDAO();
            User user = userDAO.findUserByEmail(googleUser.getEmail());

            HttpSession session = request.getSession();

            if (user == null) {
                // Lần đầu đăng nhập bằng Google: chuyển đến trang hoàn tất đăng ký
                session.setAttribute("googleUser", googleUser);
                response.sendRedirect(request.getContextPath() + "/complete-registration");
            } else {
                // Đã có tài khoản: đăng nhập
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private String getToken(String code) throws IOException {
        String responseStr = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(responseStr, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    private GoogleUser getUserInfo(String accessToken) throws IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String responseStr = Request.Get(link).execute().returnContent().asString();
        return new Gson().fromJson(responseStr, GoogleUser.class);
    }
}