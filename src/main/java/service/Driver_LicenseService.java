package service;

import model.Driver_License;

import java.util.Date;
import java.util.regex.Pattern;
import dao.implement.Driver_LicenseDAO;
import java.util.Calendar;


public class Driver_LicenseService {

    private Driver_LicenseDAO driverLicenseDAO = new Driver_LicenseDAO();
    // 🧠 Hàm validate dữ liệu
    public String validateLicense(Driver_License dl) {

        // 1️⃣ License number: đúng 12 chữ số
        if (dl.getLicense_number() == null || !Pattern.matches("\\d{12}", dl.getLicense_number())) {
            return "Số bằng lái phải bao gồm chính xác 12 chữ số.";
        }

        // 2️⃣ Ngày cấp & hết hạn không được null
        if (dl.getIssue_date() == null || dl.getExpiry_date() == null) {
            return "Ngày cấp và ngày hết hạn không được phép để trống.";
        }

        // 3️⃣ Issue date < Expiry date
        if (dl.getIssue_date().after(dl.getExpiry_date())) {
            return "Ngày cấp phải trước ngày hết hạn.";
        }

        // 🔹 Ngày hết hạn phải sau ngày hôm nay
        Date today = new Date();
        if (!dl.getExpiry_date().after(today)) {
            return "Bằng lái đã hết hạn.";
        }

        // 🔹 Thời hạn bằng lái phải ít nhất 5 năm
        Calendar cal = Calendar.getInstance();
        cal.setTime(dl.getIssue_date());
        cal.add(Calendar.YEAR, 5); // issue_date + 5 năm
        Date minExpiry = cal.getTime();

        if (dl.getExpiry_date().before(minExpiry)) {
            return "Thời hạn bằng lái ít nhất phải 5 năm.";
        }
//check dup
        String licenseNumber = dl.getLicense_number();
        Driver_License existing = driverLicenseDAO.findByLicenseNumber(licenseNumber);
        if (existing != null && existing.getLicense_id() != dl.getLicense_id()) {
            return "Bằng lái đã tồn tại.";
        }
// check null
        if (dl.getLicenseClass() == null || dl.getLicenseClass().trim().isEmpty()) {
            return "Hạng bằng không được trống.";
        }
        if (dl.getAddress() == null || dl.getAddress().trim().isEmpty()) {
            return "Nơi cư trú không được trống.";
        }
        if (dl.getNationality() == null || dl.getNationality().trim().isEmpty()) {
            return "Quốc tịch không được trống.";
        }

        // ✅ Nếu qua hết thì hợp lệ
        return null;
    }
}
