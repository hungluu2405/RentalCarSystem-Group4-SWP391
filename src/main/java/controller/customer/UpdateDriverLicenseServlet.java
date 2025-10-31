package controller.customer;

import dao.implement.Driver_LicenseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Driver_License;
import model.User;
import service.Driver_LicenseService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/customer/license")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 15
)
public class UpdateDriverLicenseServlet extends HttpServlet {

    private Driver_LicenseDAO licenseDAO;
    private Driver_LicenseService licenseService;

    @Override
    public void init() throws ServletException {
        licenseDAO = new Driver_LicenseDAO();
        licenseService = new Driver_LicenseService();
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/view/customer/Driver_License.jsp").forward(request, response);
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

        Driver_License dl = licenseDAO.getLicenseByUserId(user.getUserId());
        if (dl == null) {
            dl = new Driver_License();
            dl.setUser_id(user.getUserId());
        }

        request.setAttribute("license", dl);
        request.getRequestDispatcher("/view/customer/Driver_License.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String licenseNumber = request.getParameter("license_number");
        String issueDateStr = request.getParameter("issue_date");
        String expiryDateStr = request.getParameter("expiry_date");

        // 🖼 Upload ảnh
        String uploadPath = request.getServletContext().getRealPath("/images/license");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String frontImagePath = uploadFile(request.getPart("front_image"), uploadPath);
        String backImagePath = uploadFile(request.getPart("back_image"), uploadPath);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = (issueDateStr != null && !issueDateStr.isEmpty()) ? sdf.parse(issueDateStr) : null;
            Date expiryDate = (expiryDateStr != null && !expiryDateStr.isEmpty()) ? sdf.parse(expiryDateStr) : null;

            Driver_License dl = licenseDAO.getLicenseByUserId(user.getUserId());
            if (dl == null) dl = new Driver_License();

            dl.setUser_id(user.getUserId());
            dl.setLicense_number(licenseNumber);
            dl.setIssue_date(issueDate);
            dl.setExpiry_date(expiryDate);
            if (frontImagePath != null) dl.setFront_image_url("/images/license/" + frontImagePath);
            if (backImagePath != null) dl.setBack_image_url("/images/license/" + backImagePath);

            // 🧠 Validate qua service
//            String validationMsg = licenseService.validateLicense(dl);
//            if (validationMsg != null) {
//                request.setAttribute("error", validationMsg);
//                request.setAttribute("license", dl);
//                request.getRequestDispatcher("view/customer/Driver_License.jsp")
//                        .forward(request, response);
//                return;
//            }
            String validationMsg = licenseService.validateLicense(dl);
            if (validationMsg != null) {
                forwardWithError(request, response, validationMsg);
                return;
            }

            if (licenseDAO.getLicenseByUserId(user.getUserId()) == null) {
                licenseDAO.insertLicense(dl);
            } else {
                licenseDAO.updateLicense(dl);
            }

            response.sendRedirect(request.getContextPath() + "/customer/license?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            forwardWithError(request, response, "❌ Error updating license: " + e.getMessage());
        }
    }

    private String uploadFile(Part filePart, String uploadPath) throws IOException {
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            filePart.write(uploadPath + File.separator + fileName);
            return fileName;
        }
        return null;
    }
}
