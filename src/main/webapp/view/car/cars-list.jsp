<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/view/common/customer/_head_and_top_header.jsp" />

<div class="no-bottom no-top" id="content">
    <div id="top"></div>
    
    <!-- Subheader -->
    <section id="subheader" class="jarallax text-light">
        <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
        <div class="center-y relative text-center">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h1>Cars</h1>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Cars Section -->
    <section id="section-cars" class="pt-4 pb-5">
        <div class="container">
            <div class="row">
                <!-- Sidebar Filter -->
                <div class="col-lg-3 mb-4">
                    <form action="${pageContext.request.contextPath}/cars" method="GET" class="p-3" style="background: #f5f5f5; border-radius: 10px;">
                        <h4 class="mb-3">Filter Cars</h4>

                        <!-- Vehicle Type -->
                        <div class="mb-3">
                            <h5>Vehicle Type</h5>
                            <c:forEach var="type" items="${carTypeList}">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="typeId" id="type_${type.typeId}" value="${type.typeId}" 
                                        ${param.typeId == type.typeId ? 'checked' : ''}>
                                    <label class="form-check-label" for="type_${type.typeId}">${type.name}</label>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <h5>Brand</h5>
                            <input type="text" name="brand" class="form-control" placeholder="e.g. Toyota" value="${param.brand}">
                        </div>

                        <!-- Model -->
                        <div class="mb-3">
                            <h5>Model</h5>
                            <input type="text" name="model" class="form-control" placeholder="e.g. Vios" value="${param.model}">
                        </div>

                        <!-- Capacity -->
                        <div class="mb-3">
                            <h5>Capacity</h5>
                            <input type="number" name="capacity" class="form-control" placeholder="e.g. 4" value="${param.capacity}">
                        </div>

                        <!-- Price -->
                        <div class="mb-3">
                            <h5>Price ($)</h5>
                            <div class="d-flex gap-2">
                                <input type="number" name="minPrice" class="form-control" placeholder="Min" value="${param.minPrice}">
                                <input type="number" name="maxPrice" class="form-control" placeholder="Max" value="${param.maxPrice}">
                            </div>
                        </div>

                        <button type="submit" class="btn btn-primary w-100 mt-2">Filter Cars</button>
                    </form>
                </div>

                <!-- Car List -->
                <div class="col-lg-9">
                    <div class="row">
                        <c:if test="${empty carList}">
                            <div class="col-12">
                                <div class="alert alert-warning text-center">No cars found matching your criteria.</div>
                            </div>
                        </c:if>

                        <c:forEach var="car" items="${carList}">
                            <div class="col-lg-12 mb-4">
                                <div class="card shadow-sm border-0 hover-shadow" style="border-radius: 10px; overflow: hidden;">
                                    <div class="row g-0">
                                        <div class="col-md-4">
                                            <img src="${pageContext.request.contextPath}/${not empty car.imageUrl ? car.imageUrl : 'images/cars/default.jpg'}" class="img-fluid" alt="${car.brand} ${car.model}">
                                        </div>
                                        <div class="col-md-5">
                                            <div class="card-body">
                                                <h5 class="card-title">${car.brand} ${car.model}</h5>
                                                <ul class="list-unstyled mb-0">
                                                    <li><strong>Seats:</strong> ${car.capacity}</li>
                                                    <li><strong>Transmission:</strong> ${car.transmission}</li>
                                                    <li><strong>Fuel:</strong> ${car.fuelType}</li>
                                                    <li><strong>Type:</strong> ${car.carTypeName}</li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div class="col-md-3 d-flex flex-column justify-content-center align-items-center p-3">
                                            <h5 class="text-primary">$${car.pricePerDay}/day</h5>
                                            <a href="${pageContext.request.contextPath}/car-details?id=${car.carId}" class="btn btn-success mt-2 w-100">Rent Now</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="/view/common/customer/_footer_and_scripts.jsp" />

<style>
    /* Hover effect pastel */
    .hover-shadow:hover {
        transform: translateY(-3px);
        transition: all 0.3s ease;
        box-shadow: 0 8px 20px rgba(0,0,0,0.15) !important;
    }
    #section-cars .card {
        background-color: #fff8f0;
    }
    #section-cars h5 {
        color: #333;
    }
    #section-cars .btn-primary {
        background-color: #ff9f80;
        border: none;
    }
    #section-cars .btn-primary:hover {
        background-color: #ff7f50;
    }
    #section-cars .btn-success {
        background-color: #80cfff;
        border: none;
    }
    #section-cars .btn-success:hover {
        background-color: #3399ff;
    }
</style>
