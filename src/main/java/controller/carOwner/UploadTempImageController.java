package controller.carOwner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;

@WebServlet("/owner/upload-temp-image")
@MultipartConfig
public class UploadTempImageController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        for (Part part : request.getParts()) {
            if ("tempImage".equals(part.getName()) && part.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
                String tempDirPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "temp";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) tempDir.mkdirs();

                String filePath = tempDirPath + File.separator + fileName;
                part.write(filePath);

                // Trả về đường dẫn web để hiển thị preview
                String imageUrl = request.getContextPath() + "/images/temp/" + fileName;
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write(imageUrl);
                return;
            }
        }
    }
}
