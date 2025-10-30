/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeAgoUtil {

    public static String formatTimeAgo(LocalDateTime pastTime) {
        if (pastTime == null) return "";

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(pastTime, now);

        if (duration.toMinutes() < 1) {
            return "Vừa xong";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + " phút trước";
        } else if (duration.toDays() < 1) {
            return duration.toHours() + " giờ trước";
        } else if (duration.toDays() < 7) {
            return duration.toDays() + " ngày trước";
        } else if (duration.toDays() < 30) {
            long weeks = duration.toDays() / 7;
            return weeks + " tuần trước";
        } else {
            // Hiển thị ngày nếu quá lâu
            return pastTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}
