package controller.customer;

import dao.implement.Driver_LicenseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Driver_License;
import model.User;
import service.account.Driver_LicenseService;

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

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage, Driver_License dl)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.setAttribute("license", dl);
        request.setAttribute("activePage", "license");
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
            dl.setFullName(user.getUserProfile().getFullName());
            dl.setGender(user.getUserProfile().getGender());
            dl.setDob(user.getUserProfile().getDob());

        }


        request.setAttribute("license", dl);
        request.setAttribute("activePage", "license");
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
        String licenseClass = request.getParameter("license_class");
        String address = request.getParameter("address");
        String nationality = request.getParameter("nationality");


        // 🖼 Upload ảnh
        String uploadPath = request.getServletContext().getRealPath("/images/license");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String frontImagePath = null;
        String backImagePath = null;
        try {
            frontImagePath = uploadFile(request.getPart("front_image"), uploadPath);
            backImagePath = uploadFile(request.getPart("back_image"), uploadPath);
        } catch (IOException e) {
            Driver_License dl = licenseDAO.getLicenseByUserId(user.getUserId());
            if (dl == null) {
                dl = new Driver_License();
                dl.setUser_id(user.getUserId());
            }

            request.setAttribute("license", dl);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/view/customer/Driver_License.jsp").forward(request, response);
            return;
        }


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
            dl.setLicenseClass(licenseClass);
            dl.setAddress(address);
            dl.setNationality(nationality);

            // Giữ nguyên ảnh cũ nếu không upload mới
            if (frontImagePath != null) {
                dl.setFront_image_url("/images/license/" + frontImagePath);
            } else if (dl.getFront_image_url() == null) {
                // Nếu chưa có trong DB (record mới)
                dl.setFront_image_url(null);
            }

            if (backImagePath != null) {
                dl.setBack_image_url("/images/license/" + backImagePath);
            } else if (dl.getBack_image_url() == null) {
                dl.setBack_image_url(null);
            }


            // 🧠 Validate qua service

            String validationMsg = licenseService.validateLicense(dl);
            if (validationMsg != null) {
                forwardWithError(request, response, validationMsg, dl);
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
            Driver_License dl = new Driver_License();  // thêm dòng này để tránh null
            dl.setUser_id(user.getUserId());
            forwardWithError(request, response, "❌ Error updating license: " + e.getMessage(), dl);
        }
    }

    private String uploadFile(Part filePart, String uploadPath) throws IOException {
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String contentType = filePart.getContentType();

            // ✅ Chỉ chấp nhận ảnh
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") ||
                            contentType.equals("image/png") ||
                            contentType.equals("image/jpg") ||
                            contentType.equals("image/webp"))) {
                throw new IOException("Invalid file type. Please upload an image (JPEG, PNG, WEBP).");
            }

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            filePart.write(uploadPath + File.separator + fileName);

            File destFile = new File(uploadDir, fileName);
            if (destFile.exists()) {
                // Báo lỗi trùng file
                throw new IOException("File " + fileName + " đã tồn tại. Vui lòng đổi tên file hoặc chọn file khác.");
            }
            filePart.write(destFile.getAbsolutePath());
            return fileName;
        }
        return null;
}
}
