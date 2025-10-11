package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects requests hitting the application root to the home page.
 */
@WebFilter(filterName = "RootRedirectFilter", urlPatterns = {"/*"})
public class RootRedirectFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath() == null ? "" : req.getContextPath();
        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String requestUri = req.getRequestURI();
        String normalizedUSri = requestUri;

        if (normalizedUSri != null) {
            int semicolonIndex = normalizedUSri.indexOf(';');
            if (semicolonIndex >= 0) {
                normalizedUSri = normalizedUSri.substring(0, semicolonIndex);
            }
        }

        boolean isRootServletPath = servletPath == null || servletPath.isEmpty() || "/".equals(servletPath);
        boolean hasNoExtraPath = pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo);
        boolean isRootRequest = isRootServletPath && hasNoExtraPath;
        boolean isRootByUri = normalizedUSri != null
                && (normalizedUSri.equals(contextPath) || normalizedUSri.equals(contextPath + "/"));
        boolean shouldHandleRoot = isRootRequest || isRootByUri;
        boolean isGetMethod = "GET".equalsIgnoreCase(req.getMethod());
        boolean alreadyAtHome = requestUri != null && requestUri.startsWith(contextPath + "/home");

        if (shouldHandleRoot && isGetMethod && !alreadyAtHome) {
            resp.sendRedirect(contextPath + "/home");
            return;
        }

        chain.doFilter(request, response);
    }
}
