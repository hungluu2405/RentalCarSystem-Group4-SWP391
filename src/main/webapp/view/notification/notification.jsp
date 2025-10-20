<%-- 
    Document   : notification.jsp
    Created on : Oct 15, 2025, 2:06:31 AM
    Author     : Lenovo
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
  <title>Thông báo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="container" style="max-width:860px;margin:24px auto">
    <h2>Thông báo</h2>

    <c:if test="${empty sessionScope.user}">
      <p>Bạn cần <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a> để xem thông báo.</p>
    </c:if>

    <c:if test="${not empty sessionScope.user}">
      <c:choose>
        <c:when test="${empty requestScope.notifications}">
          <p>Hiện chưa có thông báo.</p>
        </c:when>
        <c:otherwise>
          <c:forEach items="${requestScope.notifications}" var="n">
            <div class="notify-item" style="display:flex;gap:12px;padding:12px 0;border-bottom:1px solid #eee">
              <div class="notify-icon"><i class="fa-regular fa-bell"></i></div>
              <div class="notify-content">
                <div class="notify-title">${n.title}</div>
                <div class="notify-text">${n.message}</div>
                <div class="notify-time">${n.createdAt}</div>
                <c:if test="${not empty n.link}">
                  <div style="margin-top:6px">
                    <a href="${n.link}">Mở liên quan</a>
                  </div>
                </c:if>
              </div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </c:if>
  </div>
</body>
</html>
