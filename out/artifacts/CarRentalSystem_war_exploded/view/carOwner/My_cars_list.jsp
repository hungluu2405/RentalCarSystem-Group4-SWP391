<%@ page import="dao.implement.CarDAO" %>
<%@ page import="model.CarViewModel" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/10/2025
  Time: 2:07 PM
  To change this template use File | Settings | File Templates.
--%>

<%
    CarDAO carDAO = new CarDAO();

    String name = request.getParameter("name");
    String brand = request.getParameter("brand");
    String modelParam = request.getParameter("model");
    String capacity = request.getParameter("capacity");
    String fuel = request.getParameter("fuel");
    String price = request.getParameter("price");

    int currentPage = 1;
    int pageSize = 4; // Giảm pageSize để phù hợp với layout 3 cột
    if(request.getParameter("page") != null){
        try { currentPage = Integer.parseInt(request.getParameter("page")); } catch(Exception e){ currentPage = 1; }
    }

    List<CarViewModel> carList = carDAO.findCars(name, brand, modelParam, capacity, fuel, price, currentPage, pageSize);
    int totalCars = carDAO.countCars(name, brand, modelParam, capacity, fuel, price);
    int totalPages = (int)Math.ceil(totalCars*1.0 / pageSize);

    List<String> brands = carDAO.getAllBrands();
    List<String> models = carDAO.getAllModels();
    List<Integer> capacities = carDAO.getAllCapacities();
    List<String> fuels = carDAO.getAllFuelTypes();

    request.setAttribute("carList", carList);
    request.setAttribute("brands", brands);
    request.setAttribute("models", models);
    request.setAttribute("capacities", capacities);
    request.setAttribute("fuels", fuels);
    request.setAttribute("currentPage", currentPage);
    request.setAttribute("totalPages", totalPages);
%>
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
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img"
                 alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Car Owner Dashboard</h1></div>
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

                                <c:choose>
                                    <c:when test="${not empty carList}">
                                        <div class="table-responsive">
                                            <table class="table table-bordered align-middle text-center">
                                                <thead class="table-light">
                                                <tr>
                                                    <th>Car ID</th>
                                                    <th>Image</th>
                                                    <th>Name</th>
                                                    <th>License Plate</th>
                                                    <th>Price/Day</th>
                                                    <th>Status</th>
                                                    <th>Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="c" items="${carList}">
                                                    <tr>
                                                        <td>${c.carId}</td>
                                                        <td>
                                                            <img src="${c.imageUrl}" alt="${c.carName}" class="rounded-3"
                                                                 style="width:80px; height:60px; object-fit:cover;">
                                                        </td>
                                                        <td>${c.carName}</td>
                                                        <td>${c.licensePlate}</td>
                                                        <td>${c.pricePerDay}$</td>
                                                        <td>
                                    <span class="badge ${c.status eq 'Available' ? 'bg-success' : 'bg-secondary'}">
                                            ${c.status}
                                    </span>
                                                        </td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/owner/editCar?id=${c.carId}"
                                                               class="btn btn-sm btn-warning me-2">
                                                                <i class="fa fa-edit"></i> Edit
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted text-center mt-3">You haven’t added any cars yet.</p>
                                    </c:otherwise>
                                </c:choose>
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