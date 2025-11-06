package service;

import model.Driver_License;
import java.util.Date;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Driver_LicenseService {

    // 🧠 Hàm validate dữ liệu
    public String validateLicense(Driver_License dl) {

        // 1️⃣ License number: đúng 12 chữ số
        if (dl.getLicense_number() == null || !Pattern.matches("\\d{12}", dl.getLicense_number())) {
            return "License number must be exactly 12 digits.";
        }

        // 2️⃣ Ngày cấp & hết hạn không được null
        if (dl.getIssue_date() == null || dl.getExpiry_date() == null) {
            return "Issue date and expiry date are required.";
        }

        // 3️⃣ Issue date < Expiry date
        if (dl.getIssue_date().after(dl.getExpiry_date())) {
            return "Issue date must be before expiry date.";
        }



        // ✅ Nếu qua hết thì hợp lệ
        return null;
    }
}
