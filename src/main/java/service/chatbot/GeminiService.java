package service.chatbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GeminiService {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
    private final String apiKey;
    private final Gson gson;

    public GeminiService(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public String generateResponse(String userMessage) {
        try {
            String systemContext = buildSystemContext();
            String fullPrompt = systemContext + "\n\nUser Question: " + userMessage;

            JsonObject requestBody = buildRequestBody(fullPrompt);
            String responseText = callGeminiAPI(requestBody);

            return responseText != null ? responseText : "Xin lỗi, tôi không thể xử lý yêu cầu của bạn lúc này. Vui lòng thử lại sau.";

        } catch (Exception e) {
            System.err.println("Error generating response: " + e.getMessage());
            e.printStackTrace();
            return "Đã xảy ra lỗi khi xử lý yêu cầu của bạn. Vui lòng thử lại.";
        }
    }

    private String buildSystemContext() {
        return "Bạn là trợ lý AI thông minh của hệ thống thuê xe Rentaly - một nền tảng cho thuê xe hàng đầu tại Việt Nam.\n\n" +
                "THÔNG TIN VỀ HỆ THỐNG RENTALY:\n" +
                "- Rentaly là nền tảng kết nối chủ xe và khách hàng muốn thuê xe\n" +
                "- Cung cấp đa dạng các loại xe: SUV, Sedan, Mui trần, Xe tải nhỏ, Coupe\n" +
                "- Hỗ trợ 24/7 trên mọi hành trình\n" +
                "- Dịch vụ đón - trả xe miễn phí\n" +
                "- Giá cả cạnh tranh, chất lượng cao\n\n" +

                "CÁCH ĐẶT XE:\n" +
                "1. Chọn địa điểm nhận và trả xe\n" +
                "2. Chọn ngày giờ nhận xe và trả xe\n" +
                "3. Tìm kiếm xe phù hợp trong danh sách\n" +
                "4. Xem chi tiết xe và đánh giá từ khách hàng khác\n" +
                "5. Đặt xe và thanh toán online hoặc thanh toán khi nhận xe\n" +
                "6. Nhận xe tại địa điểm đã chọn\n\n" +

                "THANH TOÁN:\n" +
                "- Hỗ trợ thanh toán online qua thẻ tín dụng/ghi nợ\n" +
                "- Thanh toán khi nhận xe (tùy chủ xe)\n" +
                "- Có khoản đặt cọc tạm thời để đảm bảo\n" +
                "- Hoàn trả đặt cọc khi trả xe trong tình trạng tốt\n\n" +

                "YÊU CẦU KHI THUÊ XE:\n" +
                "- Có giấy phép lái xe hợp lệ\n" +
                "- Đủ 18 tuổi trở lên\n" +
                "- Cung cấp CMND/CCCD\n" +
                "- Đặt cọc theo quy định\n\n" +

                "CHÍNH SÁCH HỦY/THAY ĐỔI:\n" +
                "- Không thể hủy hoặc chỉnh sửa đặt xe online sau khi xác nhận\n" +
                "- Liên hệ sớm với hỗ trợ nếu cần thay đổi\n" +
                "- Có thể áp dụng phí hủy tùy thời điểm\n\n" +

                "LIÊN HỆ:\n" +
                "- Hotline: 033 5821918 hoặc +208 333 9296\n" +
                "- Email: contact@rentaly.com\n" +
                "- Thời gian hỗ trợ: T2 - CN 06:00 - 22:00\n\n" +

                "TRỞ THÀNH CHỦ XE:\n" +
                "- Khách hàng có thể đăng ký làm chủ xe để cho thuê\n" +
                "- Cần cung cấp thông tin xe và giấy tờ hợp lệ\n" +
                "- Được hưởng hoa hồng từ việc cho thuê xe\n\n" +

                "NHIỆM VỤ CỦA BẠN:\n" +
                "- Trả lời các câu hỏi về dịch vụ thuê xe\n" +
                "- Hướng dẫn cách đặt xe, thanh toán\n" +
                "- Giải đáp thắc mắc về chính sách\n" +
                "- Hỗ trợ khách hàng một cách thân thiện, chuyên nghiệp\n" +
                "- Luôn trả lời bằng tiếng Việt\n" +
                "- Nếu không chắc chắn về thông tin, hãy khuyên khách hàng liên hệ hotline để được hỗ trợ trực tiếp\n\n" +

                "HÃY TRẢ LỜI NGẮN GỌN, RÕ RÀNG VÀ HỮU ÍCH!";
    }

    private JsonObject buildRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();

        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);

        requestBody.add("contents", contents);

        // Generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 1024);
        requestBody.add("generationConfig", generationConfig);

        // Safety settings
        JsonArray safetySettings = new JsonArray();
        String[] categories = {"HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                              "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT"};
        for (String category : categories) {
            JsonObject setting = new JsonObject();
            setting.addProperty("category", category);
            setting.addProperty("threshold", "BLOCK_MEDIUM_AND_ABOVE");
            safetySettings.add(setting);
        }
        requestBody.add("safetySettings", safetySettings);

        return requestBody;
    }

    private String callGeminiAPI(JsonObject requestBody) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(GEMINI_API_URL + "?key=" + apiKey);
            request.setHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(gson.toJson(requestBody), StandardCharsets.UTF_8);
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                if (response.getStatusLine().getStatusCode() == 200) {
                    return extractTextFromResponse(responseBody);
                } else {
                    System.err.println("API Error: " + response.getStatusLine().getStatusCode());
                    System.err.println("Response: " + responseBody);
                    return null;
                }
            }
        }
    }

    private String extractTextFromResponse(String responseBody) {
        try {
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            if (jsonResponse.has("candidates")) {
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates.size() > 0) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    if (firstCandidate.has("content")) {
                        JsonObject content = firstCandidate.getAsJsonObject("content");
                        if (content.has("parts")) {
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                JsonObject firstPart = parts.get(0).getAsJsonObject();
                                if (firstPart.has("text")) {
                                    return firstPart.get("text").getAsString();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
        }
        return null;
    }
}
