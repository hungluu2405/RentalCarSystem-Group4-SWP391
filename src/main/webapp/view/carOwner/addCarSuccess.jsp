<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/19/2025
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="car" class="model.CarViewModel" scope="request" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <jsp:include page="/view/common/carOwner/_headOwner.jsp"/>
    <title>Thêm xe thành công - Rentaly</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
            'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        h1, h2, h3, h4, h5, h6 {
            font-family: 'Inter', sans-serif;
            font-weight: 700;
        }

        p, span, div {
            font-family: 'Inter', sans-serif;
        }
        .container-content {
            max-width: 1200px;
            margin: 0 auto;
            padding: 30px 15px;
        }
        .success-box {
            background: #d1fae5;
            border: 2px solid #10b981;
            padding: 30px;
            border-radius: 8px;
            text-align: center;
            margin-bottom: 30px;
        }
        .success-icon {
            width: 80px;
            height: 80px;
            background: #10b981;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 40px;
            margin-bottom: 15px;
        }
        .info-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .info-row:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 500;
            color: #6b7280;
        }
        .info-value {
            font-weight: 600;
            color: #1f2937;
            text-align: right;
        }
        .btn-home {
            background: #10b981;
            color: white;
            padding: 12px 30px;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            margin-top: 20px;
        }
        .btn-home:hover {
            background: #059669;
            color: white;
        }
        .car-image {
            width: 100%;
            border-radius: 10px;
            margin-bottom: 15px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div id="wrapper">
    <jsp:include page="/view/common/carOwner/_headerOwner.jsp"/>

    <div id="content" class="no-bottom no-top">
        <section id="subheader" class="jarallax text-light"
                 style="background-image: url('${pageContext.request.contextPath}/images/background/2.jpg');">
            <div class="center-y relative text-center">
                <div class="container-content">
                    <h1>Thêm xe thành công</h1>
                </div>
            </div>
        </section>

        <section style="padding: 60px 0; background: #f5f5f5;">
            <div class="container-content container">

                <c:choose>
                    <c:when test="${car == null}">
                        <div class="alert alert-danger">
                            <h3>❌ Không tìm thấy thông tin xe!</h3>
                            <p>Vui lòng thử lại.</p>
                            <a href="${pageContext.request.contextPath}/owner/addCar" class="btn-home">Thêm lại xe</a>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="success-box">
                            <div class="success-icon">✓</div>
                            <h2 style="color:#10b981;">Xe đã được thêm thành công!</h2>
                            <p style="color:#059669;">Chiếc xe của bạn đã sẵn sàng để hiển thị. Hãy truy cập vào Manage my car - Quản lý xe của tôi để xem chi tiết</p>
                        </div>

                        <div class="info-section" style="text-align: center;">
                            <!-- Nút về trang chủ -->
                            <a href="${pageContext.request.contextPath}/home" class="btn-home">
                                🔙 Về trang quản lý
                            </a>

                            <!-- Nút quản lý xe -->
                            <a href="${pageContext.request.contextPath}/owner/manageMyCar" class="btn-home" style="margin-left: 10px;">
                                🚘 Quản lý xe của tôi
                            </a>

                            <!-- Nút thêm xe mới -->
                            <a href="${pageContext.request.contextPath}/owner/addCar" class="btn-home" style="margin-left: 10px;">
                                ➕ Thêm xe mới
                            </a>
                        </div>

                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </div>

    <jsp:include page="/view/common/carOwner/_footer_scriptsOwner.jsp"/>
</div>
</body>
</html>

