<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        /* ==== Tabs ==== */
        .tab-container {
            display: flex;
            gap: 30px;
            border-bottom: 2px solid #eaeaea;
            margin-top: 10px;
            margin-bottom: 20px;
        }

        .tab-btn {
            background: none;
            border: none;
            font-weight: 600;
            font-size: 16px;
            padding: 8px 0;
            cursor: pointer;
            color: #222;
            transition: all 0.3s ease;
            position: relative;
        }

        .tab-btn.active {
            color: #00b074;
        }

        .tab-btn.active::after {
            content: "";
            position: absolute;
            bottom: -2px;
            left: 0;
            height: 3px;
            width: 100%;
            background-color: #00b074;
            border-radius: 5px;
        }

        .tab-content {
            display: none;
        }

        .tab-content.active {
            display: block;
        }

        .badge {
            padding: 5px 10px;
            border-radius: 10px;
            font-size: 12px;
        }

        .bg-warning {
            background-color: #FFD54F;
        }

        .bg-success {
            background-color: #66BB6A;
            color: white;
        }
    </style>
</head>

<body>
<div id="wrapper">

    <%-- Header --%>
    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <%-- === PHẦN DASHBOARD HEADER (ảnh nền + chữ “Dashboard”) === --%>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Dashboard</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <%-- === PHẦN CHÍNH === --%>
        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">

                    <%-- Sidebar bên trái --%>
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="orders"/>
                        </jsp:include>
                    </div>

                    <%-- Nội dung bên phải --%>
                    <div class="col-lg-9">

                        <%-- Thống kê các đơn hàng --%>
                        <div class="row mb25">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar-check-o fa-2x text-success"></i>
                                    </div>
                                    <span class="h2 mb0">${upcoming}</span><br>
                                    <span>Upcoming Orders</span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar fa-2x text-success"></i>
                                    </div>
                                    <span class="h2 mb0">${total}</span><br>
                                    <span>Total Orders</span>
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <div class="symbol mb10">
                                        <i class="fa fa-calendar-times-o fa-2x text-danger"></i>
                                    </div>
                                    <span class="h2 mb0">${cancelled}</span><br>
                                    <span>Cancel Orders</span>
                                </div>
                            </div>
                        </div>

                        <%-- ==== My Orders + Tabs ==== --%>
                        <div class="card padding30 rounded-5 mb25">
                            <h4>My Orders</h4>

                            <!-- Tabs -->
                            <div class="tab-container">
                                <button class="tab-btn active" id="tabCurrent">Current Trips</button>
                                <button class="tab-btn" id="tabHistory">Trip History</button>
                            </div>

                            <!-- Current Trips -->
                            <div id="currentTrips" class="tab-content active">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Car Name</th>
                                            <th>Pick Up Location</th>
                                            <th>Drop Off Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Status</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>#001</td>
                                            <td><strong>Toyota Vios</strong></td>
                                            <td>Chicago</td>
                                            <td>Ha Noi</td>
                                            <td>2025-10-10</td>
                                            <td>2025-10-15</td>
                                            <td><span class="badge bg-warning text-dark">Pending</span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Trip History -->
                            <div id="tripHistory" class="tab-content">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Car Name</th>
                                            <th>Pick Up Location</th>
                                            <th>Drop Off Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Status</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>#002</td>
                                            <td><strong>Honda City</strong></td>
                                            <td>Da Nang</td>
                                            <td>Hue</td>
                                            <td>2025-08-05</td>
                                            <td>2025-08-07</td>
                                            <td><span class="badge bg-success">Completed</span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <%-- Favorites giữ nguyên --%>
                        <div class="card padding30 rounded-5">
                            <h4>My Favorites</h4>
                            <div class="spacer-10"></div>
                            <div class="de-item-list no-border mb30">
                                <div class="d-img">
                                    <img src="${pageContext.request.contextPath}/images/cars/jeep-renegade.jpg"
                                         class="img-fluid" alt="">
                                </div>
                                <div class="d-info">
                                    <div class="d-text">
                                        <h4>Jeep Renegade</h4>
                                    </div>
                                </div>
                                <div class="d-price">
                                    Daily rate from <span>$265</span>
                                    <a class="btn-main" href="#">Rent Now</a>
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>

                    </div> <%-- end col-lg-9 --%>
                </div>
            </div>
        </section>
    </div>

    <%-- Footer scripts --%>
    <jsp:include page="../common/customer/_footer_scripts.jsp"/>

</div>

<script>
    // ==== JS toggle tab ====
    document.addEventListener("DOMContentLoaded", function () {
        const tabCurrent = document.getElementById("tabCurrent");
        const tabHistory = document.getElementById("tabHistory");
        const currentTrips = document.getElementById("currentTrips");
        const tripHistory = document.getElementById("tripHistory");

        tabCurrent.addEventListener("click", function () {
            tabCurrent.classList.add("active");
            tabHistory.classList.remove("active");
            currentTrips.classList.add("active");
            tripHistory.classList.remove("active");
        });

        tabHistory.addEventListener("click", function () {
            tabHistory.classList.add("active");
            tabCurrent.classList.remove("active");
            tripHistory.classList.add("active");
            currentTrips.classList.remove("active");
        });
    });
</script>

</body>
</html>
