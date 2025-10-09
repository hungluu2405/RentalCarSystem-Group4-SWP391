<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 1. GỌI PHẦN ĐẦU TRANG (HEAD & HEADER) --%>
<jsp:include page="/view/common/customer/_head_and_top_header.jsp" />

<%-- 2. NỘI DUNG CHÍNH CỦA TRANG NÀY --%>
<div class="no-bottom no-top" id="content">
    <div id="top"></div>
    
    <section id="subheader" class="jarallax text-light">
        <img src="${pageContext.request.contextPath}/images/background/2.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Cars</h1>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
    </section>

    <section id="section-cars">
        <div class="container">
            <div class="row">
                <div class="col-lg-3">
                    <form action="${pageContext.request.contextPath}/cars" method="GET" class="p-3" style="background: #f5f5f5; border-radius: 5px;">
                        
                        <div class="item_filter_group">
                            <h4>Vehicle Type</h4>
                            <div class="de_form">
                                <c:forEach var="type" items="${carTypeList}">
                                    <div class="de_checkbox">
                                        <input id="type_${type.typeId}" name="typeId" type="radio" value="${type.typeId}" ${param.typeId == type.typeId ? 'checked' : ''}>
                                        <label for="type_${type.typeId}">${type.name}</label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="item_filter_group">
                            <h4>Brand</h4>
                            <input type="text" name="brand" class="form-control" placeholder="e.g. Toyota" value="${param.brand}">
                        </div>

                        <div class="item_filter_group">
                            <h4>Model</h4>
                            <input type="text" name="model" class="form-control" placeholder="e.g. Vios" value="${param.model}">
                        </div>

                        <div class="item_filter_group">
                            <h4>Capacity</h4>
                            <input type="number" name="capacity" class="form-control" placeholder="e.g. 4" value="${param.capacity}">
                        </div>

                        <div class="item_filter_group">
                            <h4>Price ($)</h4>
                            <div class="price-input">
                                <div class="field">
                                    <span>Min</span>
                                    <input type="number" class="input-min" name="minPrice" value="${param.minPrice}">
                                </div>
                                <div class="field">
                                    <span>Max</span>
                                    <input type="number" class="input-max" name="maxPrice" value="${param.maxPrice}">
                                </div>
                            </div>
                        </div>
                        <div class="spacer-20"></div>
                        <input type="submit" class="btn-main" value="Filter Cars">
                    </form>
                </div>

                <div class="col-lg-9">
                    <div class="row">
                        <c:forEach var="car" items="${carList}">
                            <div class="col-lg-12">
                                <div class="de-item-list mb30">
                                    <div class="d-img">
                                        <img src="${pageContext.request.contextPath}/${not empty car.imageUrl ? car.imageUrl : 'images/cars/default.jpg'}" class="img-fluid" alt="${car.brand} ${car.model}">
                                    </div>
                                    <div class="d-info">
                                        <div class="d-text">
                                            <h4>${car.brand} ${car.model}</h4>
                                            <div class="d-atr-group">
                                                <ul class="d-atr">
                                                    <li><span>Seats:</span>${car.capacity}</li>
                                                    <li><span>Transmission:</span>${car.transmission}</li>
                                                    <li><span>Fuel:</span>${car.fuelType}</li>
                                                    <li><span>Type:</span>${car.carTypeName}</li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="d-price">
                                        Daily rate from <span>$${car.pricePerDay}</span>
                                        <a class="btn-main" href="${pageContext.request.contextPath}/car-details?id=${car.carId}">Rent Now</a>
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                        </c:forEach>

                        <c:if test="${empty carList}">
                            <div class="col-lg-12">
                                <div class="alert alert-warning" role="alert">
                                  No cars found matching your criteria.
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

<%-- 3. GỌI PHẦN CUỐI TRANG (FOOTER & SCRIPTS) --%>
<jsp:include page="/view/common/customer/_footer_and_scripts.jsp" />