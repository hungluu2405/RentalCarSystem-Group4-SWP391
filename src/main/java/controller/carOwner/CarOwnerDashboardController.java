package controller.carOwner;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import model.BookingDetail;
import model.CarViewModel;
import model.User;
import model.UserProfile;

@WebServlet("/owner/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1MB
        maxFileSize = 1024 * 1024 * 10,   // 10MB
        maxRequestSize = 1024 * 1024 * 15 // 15MB
)
public class CarOwnerDashboardController extends HttpServlet {

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
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(user.getUserId());
            profile.setProfileImage("/images/profile/default.jpg");
        }

        user.setUserProfile(profile);
        session.setAttribute("user", user);
        request.getRequestDispatcher("/view/carOwner/carOwnerDashboard.jsp").forward(request, response);

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

        // Ảnh upload
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

        response.sendRedirect(request.getContextPath() + "/owner/profile?status=success");
    }

}
