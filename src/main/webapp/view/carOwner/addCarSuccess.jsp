<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/19/2025
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Car Successfully</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 80%;
            margin: 50px auto;
            background: #fff;
            padding: 30px 50px;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #28a745;
            text-align: center;
            margin-bottom: 30px;
        }
        .car-info {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
        }
        .car-images {
            flex: 1;
            min-width: 300px;
        }
        .car-images img {
            width: 100%;
            height: 250px;
            object-fit: cover;
            border-radius: 10px;
            margin-bottom: 15px;
        }
        .car-details {
            flex: 2;
            min-width: 300px;
        }
        .car-details p {
            font-size: 16px;
            margin: 8px 0;
        }
        .highlight {
            font-weight: bold;
            color: #333;
        }
        .back-btn {
            display: block;
            text-align: center;
            margin-top: 30px;
        }
        .back-btn a {
            text-decoration: none;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            transition: 0.3s;
        }
        .back-btn a:hover {
            background-color: #0056b3;
        }
    </style>
</head>

<body>
<div class="container">
    <h1>✅ Car Added Successfully!</h1>

    <!-- Kiểm tra nếu có đối tượng car -->
    <c:if test="${not empty car}">
        <div class="car-info">

            <!-- Hiển thị danh sách hình ảnh -->
            <div class="car-images">
                <c:choose>
                    <c:when test="${not empty car.images}">
                        <c:forEach var="img" items="${car.images}">
                            <img src="${img.imageUrl}" alt="Car Image">
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/no-image.png" alt="No Image Available">
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Hiển thị thông tin chi tiết xe -->
            <div class="car-details">
                <p><span class="highlight">Brand:</span> ${car.brand}</p>
                <p><span class="highlight">Model:</span> ${car.model}</p>
                <p><span class="highlight">Type:</span> ${car.carTypeName}</p>
                <p><span class="highlight">Transmission:</span> ${car.transmission}</p>
                <p><span class="highlight">Fuel Type:</span> ${car.fuelType}</p>
                <p><span class="highlight">Capacity:</span> ${car.capacity} people</p>
                <p><span class="highlight">License Plate:</span> ${car.licensePlate}</p>
                <p><span class="highlight">Price/Day:</span> $${car.pricePerDay}</p>
                <p><span class="highlight">Location:</span> ${car.location}</p>
                <p><span class="highlight">Description:</span> ${car.description}</p>
            </div>
        </div>
    </c:if>

    <!-- Nút quay lại trang danh sách xe -->
    <div class="back-btn">
        <a href="${pageContext.request.contextPath}/owner/manageCar">← Back to My Cars</a>
    </div>
</div>
</body>
</html>

