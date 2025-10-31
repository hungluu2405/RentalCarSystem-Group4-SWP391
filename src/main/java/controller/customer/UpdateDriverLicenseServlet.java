package controller.customer;

import dao.implement.Driver_LicenseDAO;
import model.Driver_License;
//import service.Driver_LicenseService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/update_dlServlet")
public class UpdateDriverLicenseServlet extends HttpServlet {

    private Driver_LicenseDAO licenseDAO;
//    private Driver_LicenseService licenseService;

    @Override
    public void init() throws ServletException {
        licenseDAO = new Driver_LicenseDAO();
//        licenseService = new Driver_LicenseService();
    }

    // 🟩 Hiển thị trang Driver License (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = (int) session.getAttribute("userId");
            Driver_License dl = licenseDAO.getLicenseByUserId(userId);
            request.setAttribute("license", dl);

            request.getRequestDispatcher("view/customer/Driver_License.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading license data");
        }
    }

    // 🟨 Xử lý cập nhật (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = (int) session.getAttribute("userId");
            String licenseNumber = request.getParameter("license_number");
            String issueDateStr = request.getParameter("issue_date");
            String expiryDateStr = request.getParameter("expiry_date");
            String imageUrl = request.getParameter("image_url");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = null, expiryDate = null;
            if (issueDateStr != null && !issueDateStr.isEmpty()) issueDate = sdf.parse(issueDateStr);
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) expiryDate = sdf.parse(expiryDateStr);

            // 🧩 Tạo object
            Driver_License dl = new Driver_License();
            dl.setUser_id(userId);
            dl.setLicense_number(licenseNumber);
            dl.setIssue_date(issueDate);
            dl.setExpiry_date(expiryDate);
            dl.setImage_url(imageUrl);

            // 🧠 Validate qua service
            String validationMsg = licenseService.validateLicense(dl);
            if (validationMsg != null) {
                request.setAttribute("error", validationMsg);
                request.setAttribute("license", dl);
                request.getRequestDispatcher("view/customer/Driver_License.jsp")
                        .forward(request, response);
                return;
            }

            // ✅ Update DB
            boolean success = licenseDAO.updateLicense(dl);
            if (success) {
                request.setAttribute("success", "Driver license updated successfully!");
            } else {
                request.setAttribute("error", "Failed to update driver license.");
            }

            // Hiển thị lại trang
            Driver_License updated = licenseDAO.getLicenseByUserId(userId);
            request.setAttribute("license", updated);
            request.getRequestDispatcher("view/customer/Driver_License.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("view/customer/Driver_License.jsp")
                    .forward(request, response);
        }
    }
}
