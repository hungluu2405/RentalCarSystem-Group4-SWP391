package util;


import model.Booking;
import model.Invoice;
import model.Payment;

import java.time.format.DateTimeFormatter;


public class InvoiceHTMLGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Generate HTML invoice với thông tin đầy đủ
     */
    public static String generateInvoiceHTML(Invoice invoice, Booking booking, Payment payment,

                                             String customerName, String customerEmail, String carName) {


        StringBuilder html = new StringBuilder();


        html.append("<!DOCTYPE html>\n");

        html.append("<html lang=\"vi\">\n");

        html.append("<head>\n");

        html.append("    <meta charset=\"UTF-8\">\n");

        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");

        html.append("    <title>Invoice ").append(invoice.getInvoiceNumber()).append("</title>\n");

        html.append("    <style>\n");

        html.append(getInvoiceCSS());

        html.append("    </style>\n");

        html.append("</head>\n");

        html.append("<body>\n");

        html.append("    <div class=\"invoice-container\">\n");


        // Header

        html.append("        <div class=\"invoice-header\">\n");

        html.append("            <h1>HÓA ĐƠN THUÊ XE</h1>\n");

        html.append("            <p class=\"company-name\">CAR RENTAL SYSTEM</p>\n");

        html.append("        </div>\n");


        // Invoice Info

        html.append("        <div class=\"invoice-info\">\n");

        html.append("            <div class=\"info-left\">\n");

        html.append("                <p><strong>Số hóa đơn:</strong> ").append(invoice.getInvoiceNumber()).append("</p>\n");

        html.append("                <p><strong>Ngày phát hành:</strong> ").append(invoice.getIssueDate().format(DATETIME_FORMATTER)).append("</p>\n");

        if (invoice.getDueDate() != null) {

            html.append("                <p><strong>Ngày đến hạn:</strong> ").append(invoice.getDueDate().format(DATETIME_FORMATTER)).append("</p>\n");

        }

        html.append("            </div>\n");

        html.append("            <div class=\"info-right\">\n");

        html.append("                <p><strong>Khách hàng:</strong> ").append(customerName != null ? customerName : "N/A").append("</p>\n");

        if (customerEmail != null) {

            html.append("                <p><strong>Email:</strong> ").append(customerEmail).append("</p>\n");

        }

        html.append("                <p><strong>Booking ID:</strong> #").append(booking.getBookingId()).append("</p>\n");

        html.append("            </div>\n");

        html.append("        </div>\n");


        // Booking Details

        html.append("        <div class=\"booking-details\">\n");

        html.append("            <h2>THÔNG TIN THUÊ XE</h2>\n");

        html.append("            <table>\n");

        html.append("                <tr>\n");

        html.append("                    <th>Xe</th>\n");

        html.append("                    <th>Ngày nhận</th>\n");

        html.append("                    <th>Ngày trả</th>\n");

        html.append("                    <th>Địa điểm</th>\n");

        html.append("                </tr>\n");

        html.append("                <tr>\n");

        html.append("                    <td>").append(carName != null ? carName : "N/A").append("</td>\n");

        html.append("                    <td>").append(booking.getStartDate().format(DATE_FORMATTER));

        if (booking.getPickupTime() != null) {

            html.append(" ").append(booking.getPickupTime());

        }

        html.append("</td>\n");

        html.append("                    <td>").append(booking.getEndDate().format(DATE_FORMATTER));

        if (booking.getDropoffTime() != null) {

            html.append(" ").append(booking.getDropoffTime());

        }

        html.append("</td>\n");

        html.append("                    <td>").append(booking.getLocation() != null ? booking.getLocation() : "N/A").append("</td>\n");

        html.append("                </tr>\n");

        html.append("            </table>\n");

        html.append("        </div>\n");


        // Invoice Breakdown

        html.append("        <div class=\"invoice-breakdown\">\n");

        html.append("            <h2>CHI TIẾT HÓA ĐƠN</h2>\n");

        html.append("            <table>\n");

        html.append("                <tr>\n");

        html.append("                    <th>Mô tả</th>\n");

        html.append("                    <th>Số tiền</th>\n");

        html.append("                </tr>\n");


        // Calculate subtotal (total before tax)

        double subtotal = booking.getTotalPrice();

        double taxAmount = invoice.getTaxAmount() != null ? invoice.getTaxAmount() : 0;


        html.append("                <tr>\n");

        html.append("                    <td>Phí thuê xe</td>\n");

        html.append("                    <td>").append(formatCurrency(subtotal)).append("</td>\n");

        html.append("                </tr>\n");


        if (taxAmount > 0) {

            html.append("                <tr>\n");

            html.append("                    <td>Thuế VAT (10%)</td>\n");

            html.append("                    <td>").append(formatCurrency(taxAmount)).append("</td>\n");

            html.append("                </tr>\n");

        }


        html.append("                <tr class=\"total-row\">\n");

        html.append("                    <td><strong>TỔNG CỘNG</strong></td>\n");

        html.append("                    <td><strong>").append(formatCurrency(invoice.getTotalAmount())).append("</strong></td>\n");

        html.append("                </tr>\n");

        html.append("            </table>\n");

        html.append("        </div>\n");


        // Payment Info

        if (payment != null) {

            html.append("        <div class=\"payment-info\">\n");

            html.append("            <h2>THÔNG TIN THANH TOÁN</h2>\n");

            html.append("            <p><strong>Phương thức:</strong> ").append(payment.getMethod() != null ? payment.getMethod() : "N/A").append("</p>\n");

            html.append("            <p><strong>Trạng thái:</strong> <span class=\"status-").append(payment.getStatus()).append("\">").append(payment.getStatus()).append("</span></p>\n");

            if (payment.getPaidAt() != null) {

                html.append("            <p><strong>Ngày thanh toán:</strong> ").append(payment.getPaidAt().format(DATETIME_FORMATTER)).append("</p>\n");

            }

            if (payment.getPaypalTransactionId() != null) {

                html.append("            <p><strong>Mã giao dịch:</strong> ").append(payment.getPaypalTransactionId()).append("</p>\n");

            }

            html.append("        </div>\n");

        }


        // Notes

        if (invoice.getNotes() != null && !invoice.getNotes().isEmpty()) {

            html.append("        <div class=\"notes\">\n");

            html.append("            <p><em>").append(invoice.getNotes()).append("</em></p>\n");

            html.append("        </div>\n");

        }


        // Footer

        html.append("        <div class=\"invoice-footer\">\n");

        html.append("            <p>Cảm ơn quý khách đã sử dụng dịch vụ của chúng tôi!</p>\n");

        html.append("            <p>Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ: support@carrentalsystem.com</p>\n");

        html.append("        </div>\n");


        // Print Button

        html.append("        <div class=\"print-button\">\n");

        html.append("            <button onclick=\"window.print()\">In hóa đơn</button>\n");

        html.append("        </div>\n");


        html.append("    </div>\n");

        html.append("</body>\n");

        html.append("</html>");


        return html.toString();

    }


    /**
     * CSS styling cho invoice
     */

    private static String getInvoiceCSS() {

        return """
                
                    * {
                
                        margin: 0;
                
                        padding: 0;
                
                        box-sizing: border-box;
                
                    }
                
                
                
                    body {
                
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                
                        background-color: #f5f5f5;
                
                        padding: 20px;
                
                    }
                
                
                
                    .invoice-container {
                
                        max-width: 900px;
                
                        margin: 0 auto;
                
                        background-color: white;
                
                        padding: 40px;
                
                        box-shadow: 0 0 20px rgba(0,0,0,0.1);
                
                    }
                
                
                
                    .invoice-header {
                
                        text-align: center;
                
                        border-bottom: 3px solid #333;
                
                        padding-bottom: 20px;
                
                        margin-bottom: 30px;
                
                    }
                
                
                
                    .invoice-header h1 {
                
                        color: #333;
                
                        font-size: 32px;
                
                        margin-bottom: 10px;
                
                    }
                
                
                
                    .company-name {
                
                        color: #666;
                
                        font-size: 18px;
                
                        font-weight: 600;
                
                    }
                
                
                
                    .invoice-info {
                
                        display: flex;
                
                        justify-content: space-between;
                
                        margin-bottom: 30px;
                
                        padding: 20px;
                
                        background-color: #f9f9f9;
                
                    }
                
                
                
                    .invoice-info p {
                
                        margin-bottom: 8px;
                
                        color: #555;
                
                    }
                
                
                
                    .booking-details, .invoice-breakdown, .payment-info {
                
                        margin-bottom: 30px;
                
                    }
                
                
                
                    h2 {
                
                        color: #333;
                
                        font-size: 18px;
                
                        margin-bottom: 15px;
                
                        padding-bottom: 10px;
                
                        border-bottom: 2px solid #eee;
                
                    }
                
                
                
                    table {
                
                        width: 100%;
                
                        border-collapse: collapse;
                
                        margin-top: 10px;
                
                    }
                
                
                
                    th, td {
                
                        padding: 12px;
                
                        text-align: left;
                
                        border-bottom: 1px solid #eee;
                
                    }
                
                
                
                    th {
                
                        background-color: #f5f5f5;
                
                        font-weight: 600;
                
                        color: #333;
                
                    }
                
                
                
                    .total-row {
                
                        background-color: #f9f9f9;
                
                        font-size: 18px;
                
                    }
                
                
                
                    .total-row td {
                
                        padding: 15px 12px;
                
                    }
                
                
                
                    .payment-info p {
                
                        margin-bottom: 10px;
                
                        color: #555;
                
                    }
                
                
                
                    .status-Paid {
                
                        color: #28a745;
                
                        font-weight: 600;
                
                    }
                
                
                
                    .status-Pending {
                
                        color: #ffc107;
                
                        font-weight: 600;
                
                    }
                
                
                
                    .notes {
                
                        padding: 20px;
                
                        background-color: #fffef0;
                
                        border-left: 4px solid #ffc107;
                
                        margin-bottom: 30px;
                
                    }
                
                
                
                    .invoice-footer {
                
                        text-align: center;
                
                        padding: 20px;
                
                        color: #666;
                
                        border-top: 2px solid #eee;
                
                        margin-top: 30px;
                
                    }
                
                
                
                    .invoice-footer p {
                
                        margin-bottom: 5px;
                
                    }
                
                
                
                    .print-button {
                
                        text-align: center;
                
                        margin-top: 30px;
                
                    }
                
                
                
                    .print-button button {
                
                        background-color: #007bff;
                
                        color: white;
                
                        border: none;
                
                        padding: 12px 30px;
                
                        font-size: 16px;
                
                        border-radius: 5px;
                
                        cursor: pointer;
                
                        transition: background-color 0.3s;
                
                    }
                
                
                
                    .print-button button:hover {
                
                        background-color: #0056b3;
                
                    }
                
                
                
                    @media print {
                
                        body {
                
                            background-color: white;
                
                            padding: 0;
                
                        }
                
                
                
                        .invoice-container {
                
                            box-shadow: none;
                
                            padding: 20px;
                
                        }
                
                
                
                        .print-button {
                
                            display: none;
                
                        }
                
                    }
                
                """;

    }


    /**
     * Format số tiền theo định dạng VND
     */

    private static String formatCurrency(double amount) {

        return String.format("%,.0f VNĐ", amount);

    }

}