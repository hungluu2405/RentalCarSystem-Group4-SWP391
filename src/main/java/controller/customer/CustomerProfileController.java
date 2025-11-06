package controller.customer;

import dao.implement.Driver_LicenseDAO;
import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Driver_License;
import model.User;
import model.UserProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

@WebServlet("/customer/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 1024 * 1024 * 10,   // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class CustomerProfileController extends HttpServlet {

    // --- Phương thức tiện ích để lưu input và forward khi có lỗi ---
    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        // 1. Lưu thông báo lỗi
        request.setAttribute("error", errorMessage);

        // 2. Lưu lại toàn bộ dữ liệu đã nhập (Sticky Form)
        request.setAttribute("input_fullName", request.getParameter("fullName"));
        request.setAttribute("input_phone", request.getParameter("phone"));
        request.setAttribute("input_dob", request.getParameter("dob"));
        request.setAttribute("input_driverLicenseNumber", request.getParameter("driverLicenseNumber"));
        request.setAttribute("input_gender", request.getParameter("gender"));

        // 3. Forward trở lại trang form
        request.getRequestDispatcher("/view/customer/myProfile.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserProfileDAO dao = new UserProfileDAO();
        UserProfile profile = dao.findByUserId(user.getUserId());
        Driver_LicenseDAO licenseDAO= new Driver_LicenseDAO();

        Driver_License license = licenseDAO.getLicenseByUserId(user.getUserId());
        request.setAttribute("license", license);


        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getUserId());
            profile.setProfileImage("/images/profile/default.jpg");
        }

        user.setUserProfile(profile);
        session.setAttribute("user", user);
        request.getRequestDispatcher("/view/customer/myProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }


        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String driverLicenseNumber = request.getParameter("driverLicenseNumber");
        String gender = request.getParameter("gender");




        if (phone != null && !phone.isEmpty() && (phone.length() != 10 || !phone.matches("\\d+"))) {
            forwardWithError(request, response, "❌ Please enter a 10-digit phone number.!");
            return;
        }

        if (driverLicenseNumber != null && !driverLicenseNumber.isEmpty() && driverLicenseNumber.length() != 12) {
            forwardWithError(request, response, "❌ The driver's license number must be exactly 12 characters.!");
            return;
        }

        // ✅ Validate age >= 18 years old
        if (dobString != null && !dobString.isEmpty()) {
            try {
                Date dob = Date.valueOf(dobString);
                int age = Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
                if (age < 18) {
                    forwardWithError(request, response, "❌ You must be at least 18 years old to use this service!");
                    return;
                }
            } catch (IllegalArgumentException e) {
                forwardWithError(request, response, "❌ Invalid date of birth format!");
                return;
            }
        }

        Part filePart = request.getPart("profileImage");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = request.getServletContext().getRealPath("/images/profile");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            filePart.write(uploadPath + File.separator + fileName);
            imagePath = "/images/profile/" + fileName;
        }


        UserProfileDAO dao = new UserProfileDAO();
        UserProfile profile = dao.findByUserId(user.getUserId());

        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getUserId());
            profile.setFullName(fullName);
            profile.setPhone(phone);
            if (dobString != null && !dobString.isEmpty()) {
                profile.setDob(Date.valueOf(dobString));
            }
            profile.setDriverLicenseNumber(driverLicenseNumber);
            profile.setGender(gender);
            profile.setProfileImage(imagePath != null ? imagePath : "/images/profile/default.jpg");
            profile.setIsVerified(false);
            dao.insertProfile(profile);
        } else {
            profile.setFullName(fullName);
            profile.setPhone(phone);
            if (dobString != null && !dobString.isEmpty()) {
                profile.setDob(Date.valueOf(dobString));
            }
            profile.setDriverLicenseNumber(driverLicenseNumber);
            profile.setGender(gender);
            if (imagePath != null) {
                profile.setProfileImage(imagePath);
            }
            dao.updateProfile(profile);
        }

        user.setUserProfile(profile);
        session.setAttribute("user", user);

        response.sendRedirect(request.getContextPath() + "/customer/profile?status=success");
    }
}