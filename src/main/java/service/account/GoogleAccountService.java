package service.account;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.implement.UserDAO;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.GoogleUser;
import model.User;
import model.UserProfile;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import util.Constants;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class GoogleAccountService {

    public static class LoginGoogleHandlerService {

        public String getAccessToken(String code) throws Exception {
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

        public GoogleUser getGoogleUserInfo(String accessToken) throws Exception {
            String responseStr = Request.Get(Constants.GOOGLE_LINK_GET_USER_INFO + accessToken)
                    .execute().returnContent().asString();
            return new Gson().fromJson(responseStr, GoogleUser.class);
        }
    }

    public static class CreateGoogleAccountService {

        private final UserDAO userDAO;

        public CreateGoogleAccountService() {
            this.userDAO = new UserDAO();
        }

        /**
         * ✅ Kiểm tra dữ liệu nhập từ form hoàn tất đăng ký Google
         */
        public String validateForm(java.util.Map<String, String> data, GoogleUser googleUser) {
            String username = data.get("username");
            String password = data.get("password");
            String rePassword = data.get("re_password");
            String phone = data.get("phone");
            String dobString = data.get("dob");
            String gender = data.get("gender");
            String addressLine = data.get("address_line");
            String city = data.get("city");
            String country = data.get("country");
            String roleParam = data.get("role_id");

            // 🟩 Kiểm tra trống
            if (username == null || username.isEmpty()
                    || password == null || password.isEmpty()
                    || rePassword == null || rePassword.isEmpty()
                    || phone == null || phone.isEmpty()
                    || dobString == null || dobString.isEmpty()
                    || gender == null || gender.isEmpty()
                    || addressLine == null || addressLine.isEmpty()
                    || city == null || city.isEmpty()
                    || country == null || country.isEmpty()
                    || roleParam == null || roleParam.isEmpty()) {
                return "Vui lòng điền đầy đủ tất cả các trường bắt buộc!";
            }

            // 🟩 Kiểm tra mật khẩu
            if (!password.equals(rePassword))
                return "Mật khẩu nhập lại không khớp!";
            if (password.length() < 6)
                return "Mật khẩu phải có ít nhất 6 ký tự!";

            // 🟩 Kiểm tra username
            if (!username.matches("^[a-zA-Z0-9_]{4,20}$"))
                return "Tên đăng nhập phải từ 4–20 ký tự và chỉ chứa chữ, số hoặc dấu gạch dưới!";

            // 🟩 Kiểm tra số điện thoại
            if (!phone.matches("^\\d{9,11}$"))
                return "Số điện thoại phải gồm từ 9 đến 11 chữ số!";

            // 🟩 Kiểm tra ngày sinh hợp lệ
            Date dob;
            try {
                dob = Date.valueOf(dobString);
            } catch (IllegalArgumentException e) {
                return "Ngày sinh không hợp lệ!";
            }

            // 🟩 Kiểm tra tuổi hợp lệ
            int age = Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
            if (age < 18 || age > 70)
                return "Độ tuổi hợp lệ để đăng ký là từ 18 đến 70 tuổi!";

            // 🟩 Kiểm tra giới tính
            if (!gender.equalsIgnoreCase("male")
                    && !gender.equalsIgnoreCase("female")
                    && !gender.equalsIgnoreCase("other")) {
                return "Giới tính không hợp lệ!";
            }

            // 🟩 Kiểm tra role ID hợp lệ
            try {
                Integer.parseInt(roleParam);
            } catch (NumberFormatException e) {
                return "Vai trò được chọn không hợp lệ!";
            }

            // 🟩 Kiểm tra trùng username/email
            if (userDAO.findUserByUsername(username) != null)
                return "Tên đăng nhập này đã được sử dụng!";
            if (userDAO.findUserByEmail(googleUser.getEmail()) != null)
                return "Email này đã được đăng ký!";

            return null;
        }

        /**
         * ✅ Tạo tài khoản người dùng mới sau khi xác thực Google
         */
        public boolean createAccount(GoogleUser googleUser, java.util.Map<String, String> data) {
            User user = new User();
            user.setUsername(data.get("username"));
            user.setEmail(googleUser.getEmail());
            user.setPassword(data.get("password"));
            user.setRoleId(Integer.parseInt(data.get("role_id")));

            UserProfile profile = new UserProfile();
            profile.setFullName(data.get("full_name"));
            profile.setPhone(data.get("phone"));

            String dobString = data.get("dob");
            if (dobString != null && !dobString.isEmpty()) {
                profile.setDob(Date.valueOf(dobString));
            }

            profile.setGender(data.get("gender"));

            Address address = new Address(
                    data.get("address_line"),
                    data.get("city"),
                    data.get("city"),
                    data.get("postal_code"),
                    data.get("country")
            );
            System.out.println("👉 Full name nhận được từ form: " + data.get("full_name"));


            return userDAO.registerUser(user, profile, address);
        }
    }

    /**
     * ✅ Service kiểm tra người dùng Google trong session.
     */
    public static class CompleteRegistrationService {

        /**
         * Kiểm tra xem người dùng Google đã có trong session chưa.
         */
        public boolean hasGoogleUser(HttpSession session) {
            return session != null && session.getAttribute("googleUser") != null;
        }
    }
}
