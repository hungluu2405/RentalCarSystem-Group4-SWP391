<%@ page import="dao.implement.CarDAO" %>
<%@ page import="model.CarViewModel" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/10/2025
  Time: 2:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Car Owner Dashboard</title>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Manage My Car</h1></div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <%-- Sidebar --%>
                    <div class="col-lg-3 mb30">
                        <%-- Sửa lại đuôi file thành .jspf --%>
                            <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                                <jsp:param name="activePage" value="manageMyCar"/>
                            </jsp:include>

                    </div>

                    <!-- MAIN CONTENT -->
                    <div class="container mt-4">
                        <div class="dashboard-content">
                            <h3 class="fw-bold mb-3 text-secondary">
                                <i class="fa fa-car"></i> My Cars
                            </h3>

                            <c:if test="${empty carList}">
                                <p>You don't have any cars yet.</p>
                            </c:if>

                            <c:forEach var="car" items="${carList}">
                                <div class="car-card">
                                    <img src="${pageContext.request.contextPath}/images/car/${car.imageUrl}" alt="${car.model}" width="300" height="200">
                                    <div class="car-info">
                                        <h3>${car.brand} ${car.model}</h3>
                                        <p>Seats: ${car.capacity}</p>
                                        <p>Fuel: ${car.fuelType}</p>
                                        <p>Transmission: ${car.transmission}</p>
                                        <p>Type: ${car.carTypeName}</p>
                                        <p>Daily rate: $${car.pricePerDay}</p>
                                        <a href="${pageContext.request.contextPath}/owner/viewCar?carId=${car.carId}" class="btn-view">View My Car</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div> <!-- End container -->
                </body>
            </html>


        </div>


        </div>
        <%--    </div> --%>
        </section>
        </div>

<!-- FOOTER -->
<jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>

</body>
</html>