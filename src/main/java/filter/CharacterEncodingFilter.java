package filter;



import jakarta.servlet.*;

import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;



/**

 * UTF-8 Encoding Filter

 * Đảm bảo tất cả request và response đều sử dụng UTF-8 encoding

 * để hỗ trợ tiếng Việt đầy đủ

 */

@WebFilter("/*")

public class CharacterEncodingFilter implements Filter {


    private static final String ENCODING = "UTF-8";


    @Override

    public void init(FilterConfig filterConfig) throws ServletException {

        // Initialization code if needed

    }


    @Override

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)

            throws IOException, ServletException {


        // Set encoding cho request (dữ liệu từ client gửi lên)

        request.setCharacterEncoding(ENCODING);


        // Set encoding cho response (dữ liệu server trả về)

        response.setCharacterEncoding(ENCODING);

//        response.setContentType("text/html; charset=UTF-8");


        // Tiếp tục xử lý request

        chain.doFilter(request, response);

    }


    @Override

    public void destroy() {

        // Cleanup code if needed

    }
}