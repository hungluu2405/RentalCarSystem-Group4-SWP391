<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Dashboard</title>

    <!-- Custom fonts for this template-->
    <link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Page level plugin CSS-->
    <link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="${pageContext.request.contextPath}/css/sb-admin.css" rel="stylesheet">

    <link rel="stylesheet" href="css/colReorder-bootstrap4.css">

</head>

<body id="page-top">

<nav class="navbar navbar-expand navbar-dark bg-dark static-top">

    <a class="navbar-brand mr-1" href="${pageContext.request.contextPath}/home">Car rental system</a>

    <button class="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle" href="#">
        <i class="fas fa-bars"></i>
    </button>

    <!-- Navbar Search -->
    <form class="d-none d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0">

    </form>

    <!-- Navbar -->
    <jsp:include page="../common/admin/navbar.jsp"></jsp:include>

</nav>

<div id="wrapper">

    <!-- Sidebar -->
    <jsp:include page="../common/admin/sidebar.jsp"></jsp:include>


    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Breadcrumbs-->
            <jsp:include page="../common/admin/breadcrumb.jsp"></jsp:include>

            <!-- Icon Cards-->
            <jsp:include page="../common/admin/iconCard.jsp"></jsp:include>



            <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

            <!-- Cars Data Table -->
            <div class="card mb-3">
                <div class="card-header">
                    <i class="fas fa-fw fa-table"></i>
                    Booking history
                </div>

                <div class="card-body">

                    <form action="bookingDB" method="get" class="form-inline mb-3">
                        <label for="dateRange" class="mr-2 font-weight-bold">
                            <i class="fas fa-calendar-alt mr-1"></i>Date:
                        </label>
                        <select name="dateRange" id="dateRange" class="form-control mr-3">
                            <option value="">All</option>
                            <option value="today" <c:if test="${selectedDateRange == 'today'}">selected</c:if>>Today</option>
                            <option value="week" <c:if test="${selectedDateRange == 'week'}">selected</c:if>>This Week</option>
                            <option value="month" <c:if test="${selectedDateRange == 'month'}">selected</c:if>>This Month</option>
                            <option value="year" <c:if test="${selectedDateRange == 'year'}">selected</c:if>>This Year</option>
                        </select>

                        <label for="price" class="mr-2 font-weight-bold">
                            <i class="fas fa-tags mr-1"></i>Price:
                        </label>
                        <select name="price" id="price" class="form-control mr-3">
                            <option value="">All</option>
                            <option value="under1tr" <c:if test="${selectedPrice == 'under1tr'}">selected</c:if>>Under 1,000,000 VND</option>
                            <option value="1trto1tr5" <c:if test="${selectedPrice == '1trto1tr5'}">selected</c:if>>1,000,000 to 1,500,000 VND</option>
                            <option value="over1tr5" <c:if test="${selectedPrice == 'over1tr5'}">selected</c:if>>Over 1,500,000 VND</option>
                        </select>

                        <button type="submit" class="btn btn-primary mr-2">
                            <i class="fas fa-search"></i> Search
                        </button>
                        <a href="bookingDB" class="btn btn-outline-secondary">
                            <i class="fas fa-undo"></i> Reset
                        </a>
                    </form>



                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead class="thead-dark">
                            <tr>
                                <th>Booking ID</th>
                                <th>User Name</th>
                                <th>Car Model</th>
                                <th>Start Date</th>
                                <th>End Date</th>
                                <th>Pickup Time</th>
                                <th>Dropoff Time</th>
                                <th>Total Price</th>
                                <th>Status</th>
                                <th>Created At</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="b" items="${listB}">
                                <tr>
                                    <td>${b.bookingId}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/manageUser?action=view&userId=${b.userId}">
                                                ${b.userFullName}
                                        </a>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/manageCar?id=${b.carId}">
                                                ${b.carModel}
                                        </a>
                                    </td>

                                    <td>${b.startDate}</td>
                                    <td>${b.endDate}</td>
                                    <td>${b.pickupTime}</td>
                                    <td>${b.dropoffTime}</td>
                                    <td>${b.totalPrice}</td>
                                    <td>${b.status}</td>
                                    <td>${b.createdAt}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

                <!-- /.container-fluid -->

            <!-- Sticky Footer -->
            <jsp:include page="../common/admin/footer.jsp"></jsp:include>

        </div>
        <!-- /.content-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <!-- Logout Modal-->
    <jsp:include page="../common/admin/logoutModal.jsp"></jsp:include>

    <!-- Bootstrap core JavaScript-->
    <script src="${pageContext.request.contextPath}/vendor/jquery/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="${pageContext.request.contextPath}/vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Page level plugin JavaScript-->
    <script src="${pageContext.request.contextPath}/vendor/chart.js/Chart.min.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/datatables/jquery.dataTables.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="${pageContext.request.contextPath}/js/sb-admin.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/colReorder-bootstrap4-min.js"></script>
    <script src="${pageContext.request.contextPath}/js/colReorder-dataTables-min.js"></script>

    <!-- Demo scripts for this page-->
    <script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>
    <script src="${pageContext.request.contextPath}/js/demo/chart-area-demo.js"></script>
    <script src="${pageContext.request.contextPath}/js/colReorder-dataTables-min.js"></script>
    <script src="${pageContext.request.contextPath}/js/colReorder-bootstrap4-min.js"></script>


</body>

</html>