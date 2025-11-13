package filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import model.User;

/**
 * Handles root redirects and enforces role-based access by URL prefix.
 */
@WebFilter(filterName = "AccessControlFilter", urlPatterns = {"/*"})
public class AccessControlFilter extends HttpFilter {

    private static final Map<String, Set<Integer>> ROLE_PATH_RULES = Map.of(
            "/admin", Set.of(1),
            "/owner", Set.of(2),
            "/customer", Set.of(3)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath() == null ? "" : req.getContextPath();
        String uri = req.getRequestURI();

        // ✅ BỎ QUA STATIC RESOURCES (CSS / JS / IMG / FONTS / VENDOR)
        if (uri.startsWith(contextPath + "/css")
                || uri.startsWith(contextPath + "/js")
                || uri.startsWith(contextPath + "/images")
                || uri.startsWith(contextPath + "/fonts")
                || uri.startsWith(contextPath + "/vendor")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".gif")
                || uri.endsWith(".svg")
                || uri.endsWith(".ico")) {
            chain.doFilter(request, response);
            return;
        }

        // === ROOT REDIRECT HANDLING ===

        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String requestUri = req.getRequestURI();
        String normalizedUri = requestUri;

        if (normalizedUri != null) {
            int semicolonIndex = normalizedUri.indexOf(';');
            if (semicolonIndex >= 0) {
                normalizedUri = normalizedUri.substring(0, semicolonIndex);
            }
        }

        boolean isRootServletPath = servletPath == null || servletPath.isEmpty() || "/".equals(servletPath);
        boolean hasNoExtraPath = pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo);
        boolean isRootRequest = isRootServletPath && hasNoExtraPath;
        boolean isRootByUri = normalizedUri != null
                && (normalizedUri.equals(contextPath) || normalizedUri.equals(contextPath + "/"));
        boolean shouldHandleRoot = isRootRequest || isRootByUri;
        boolean isGetMethod = "GET".equalsIgnoreCase(req.getMethod());
        boolean alreadyAtHome = requestUri != null && requestUri.startsWith(contextPath + "/home");

        if (shouldHandleRoot && isGetMethod && !alreadyAtHome) {
            resp.sendRedirect(contextPath + "/home");
            return;
        }

        // === ROLE RESTRICTION HANDLING ===

        String relativeUri = normalizedUri;
        if (relativeUri != null && !contextPath.isEmpty() && relativeUri.startsWith(contextPath)) {
            relativeUri = relativeUri.substring(contextPath.length());
        }
        if (relativeUri == null || relativeUri.isEmpty()) {
            relativeUri = "/";
        }

        Set<Integer> permittedRoles = resolvePermittedRoles(relativeUri);
        if (permittedRoles != null) {
            HttpSession session = req.getSession(false);
            User user = session != null ? (User) session.getAttribute("user") : null;

            if (user == null) {
                resp.sendRedirect(contextPath + "/login");
                return;
            }

            if (!permittedRoles.contains(user.getRoleId())) {
                resp.sendRedirect(contextPath + "/home");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private Set<Integer> resolvePermittedRoles(String relativeUri) {
        for (Map.Entry<String, Set<Integer>> entry : ROLE_PATH_RULES.entrySet()) {
            String prefix = entry.getKey();
            if (relativeUri.equals(prefix) || relativeUri.startsWith(prefix + "/")) {
                return entry.getValue();
            }
        }
        return null;
    }
}
