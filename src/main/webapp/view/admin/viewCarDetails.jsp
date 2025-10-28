<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Car Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" />
</head>
<body>
<div class="container mt-4">
    <h2>Car detail</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:if test="${not empty car}">
        <div class="row">
            <div class="col-md-5">
                <c:if test="${not empty car.imageUrl}">
                    <img src="${pageContext.request.contextPath}/images/car/${car.imageUrl}" alt="car" class="img-fluid"/>
                </c:if>
            </div>
            <div class="col-md-7">
                <table class="table table-striped">
                    <tr><th>Car ID</th><td>${car.carId}</td></tr>
                    <tr><th>Type</th><td>${car.carTypeName}</td></tr>
                    <tr><th>Brand</th><td>${car.brand}</td></tr>
                    <tr><th>Model</th><td>${car.model}</td></tr>
                    <tr><th>Year</th><td>${car.year}</td></tr>
                    <tr><th>License plate</th><td>${car.licensePlate}</td></tr>
                    <tr><th>Capacity</th><td>${car.capacity}</td></tr>
                    <tr><th>Transmission</th><td>${car.transmission}</td></tr>
                    <tr><th>Fuel type</th><td>${car.fuelType}</td></tr>
                    <tr><th>Price/day</th><td>${car.pricePerDay}</td></tr>
                    <tr><th>Location</th><td>${car.location}</td></tr>
                    <tr><th>Description</th><td><pre style="white-space:pre-wrap">${car.description}</pre></td></tr>
                </table>

                <form method="post" action="${pageContext.request.contextPath}/admin/manageCar" onsubmit="return confirm('Xác nhận xóa xe này?');">
                    <input type="hidden" name="deleteId" value="${car.carId}" />
                    <button type="submit" class="btn btn-danger">Delete</button>
                    <a href="${pageContext.request.contextPath}/carDB" class="btn btn-secondary">Back to list</a>
                </form>
            </div>
        </div>
    </c:if>

</div>
</body>
</html>
