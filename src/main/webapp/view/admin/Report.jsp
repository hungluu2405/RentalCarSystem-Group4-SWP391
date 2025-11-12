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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/colReorder-bootstrap4.css">

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
            <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!-- Cars Data Table -->
            <div class="card shadow mb-4">
                <div class="card-header py-3 d-flex justify-content-between align-items-center">
                    <h4 class="m-0 font-weight-bold text-primary">Report Revenue</h4>

                    <!-- Bộ lọc chọn loại -->
                    <form action="reportDB" method="get" class="form-inline">
                        <label for="type" class="mr-2 font-weight-bold">By:</label>
                        <select name="type" id="type" class="form-control mr-2">
                            <option value="week" <c:if test="${type == 'week'}">selected</c:if>>Week</option>
                            <option value="month" <c:if test="${type == 'month'}">selected</c:if>>Month</option>
                        </select>
                        <button type="submit" class="btn btn-primary mr-2">Search</button>
                        <a href="reportDB" class="btn btn-secondary">Reset</a>
                    </form>
                </div>

                <div class="card-body">
                    <!-- Tổng doanh thu -->
                    <c:if test="${not empty totalRevenue}">
                        <div class="alert alert-success mb-3">
                            <strong>
                                Total Revenue
                                (<c:out value="${type == 'week' ? 'By week' : 'By month'}"/>):
                            </strong>
                            <fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="0"/> $
                        </div>
                    </c:if>

                    <!-- Bảng dữ liệu -->
                    <div class="table-responsive" style="max-height: 500px; overflow-y: auto;">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead class="thead-dark">
                            <tr>
                                <th>Payment ID</th>
                                <th>Booking ID</th>
                                <th>Amount</th>
                                <th>Method</th>
                                <th>Status</th>
                                <th>Paid At</th>
                                <th>PayPal Transaction ID</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="p" items="${listRP}">
                                <tr>
                                    <td>${p.paymentId}</td>
                                    <td>${p.bookingId}</td>
                                    <td>${p.amount} $</td>
                                    <td>${p.method}</td>
                                    <td>${p.status}</td>
                                    <td>${p.paidAt}</td>
                                    <td>${p.paypalTransactionId}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
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