package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResetCodeStore {

    private static class CodeInfo {
        String code;
        long expiryTime;

        public CodeInfo(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
    }

    private static final Map<String, CodeInfo> store = new ConcurrentHashMap<>();
    private static final long EXPIRE_TIME_MS = 15 * 60 * 1000; // 15 phút

    public static void saveCode(String email, String code) {
        long expiryTime = System.currentTimeMillis() + EXPIRE_TIME_MS;
        store.put(email, new CodeInfo(code, expiryTime));
    }

    public static boolean validateCode(String email, String code) {
        CodeInfo info = store.get(email);
        if (info == null) return false;

        if (info.code.equals(code) && System.currentTimeMillis() < info.expiryTime) {
            store.remove(email); // Xóa mã sau khi dùng
            return true;
        }
        
        return false;
    }
}