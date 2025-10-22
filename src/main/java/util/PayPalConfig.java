package util;

/**
 * Chứa các hằng số cấu hình cho việc kết nối với PayPal Sandbox.
 * Các Servlet/Service sẽ dùng class này để lấy thông tin xác thực.
 */
public class PayPalConfig {

    // 🔴 1. Thông tin Xác thực Sandbox (CẦN PHẢI THAY THẾ bằng thông tin của bạn)

    /** * Client ID (ID Khách hàng) của ứng dụng Sandbox bạn đã tạo.
     */
    public static final String CLIENT_ID = "Ab8GuUIdUlVNi1RtJbepSt93PkfoBsmDMv3lXP2Uhlo5o0zRJbJGFY5ssuVutJY2vOiOaVVfyn5VTOOW";

    /** * Client Secret (Mã bí mật) của ứng dụng Sandbox bạn đã tạo.
     * GIỮ BÍ MẬT TUYỆT ĐỐI.
     */
    public static final String CLIENT_SECRET = "EF4489cR7UoTGit6qwNDRYLZxSnDopCOBRDxzNqc8l11QBA38zvd2aow8abxT1lzGETrW1RkWMjf3FZ6";

    // 🔴 2. Cấu hình Môi trường

    /** * Địa chỉ cơ sở API cho môi trường Sandbox.
     * Mọi yêu cầu API sẽ được gửi đến BASE_URL này.
     */
    public static final String BASE_URL = "https://api-m.sandbox.paypal.com";

    /** * Chế độ hoạt động của PayPal SDK (cần thiết cho một số SDK cũ hơn).
     */
    public static final String MODE = "sandbox";

    // 🔴 3. Cấu hình Giao dịch Mặc định

    /** * Loại tiền tệ mặc định cho các giao dịch (PayPal ưu tiên USD).
     */
    public static final String CURRENCY = "USD";
}