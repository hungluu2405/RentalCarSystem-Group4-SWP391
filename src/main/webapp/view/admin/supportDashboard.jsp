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
                    <i class="fas fa-comments"></i>
                    Support Table
                </div>

                <div class="card-body">

                    <form action="contactDB" method="get" class="form-inline mb-3">
                        <label for="status" class="mr-2 font-weight-bold">
                            <i class="fas fa-filter mr-1"></i>Status:
                        </label>
                        <select name="status" id="status" class="form-control mr-3">
                            <option value="">All</option>
                            <option value="todo" <c:if test="${selectedStatus == 'todo'}">selected</c:if>>To do</option>
                            <option value="done" <c:if test="${selectedStatus == 'done'}">selected</c:if>>Done</option>
                        </select>

                        <button type="submit" class="btn btn-primary mr-2">
                            <i class="fas fa-search"></i> Search
                        </button>
                        <a href="contactDB" class="btn btn-outline-secondary">
                            <i class="fas fa-undo"></i> Reset
                        </a>
                    </form>


                    <div class="table-responsive">
                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead class="thead-dark">
                            <tr>
                                <th>Ticket ID</th>
                                <th>Name</th>
                                <th>Phone</th>
                                <th>Email</th>
                                <th>Message</th>
                                <th>Created At</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="c" items="${contactList}">
                                <tr>
                                    <td>${c.ticketId}</td>
                                    <td>${c.name}</td>
                                    <td>${c.phoneNumber}</td>
                                    <td>${c.email}</td>
                                    <td>${c.message}</td>
                                    <td>
                                        <fmt:formatDate value="${c.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${c.status}">
                                                <span class="badge badge-success">Done</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">To Do</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/contactDB" method="post" style="display:inline;">
                                            <input type="hidden" name="ticketId" value="${c.ticketId}">
                                            <input type="hidden" name="status" value="${!c.status}">
                                            <button type="submit" class="btn btn-sm
                                <c:choose>
                                    <c:when test="${c.status}">btn-warning</c:when>
                                    <c:otherwise>btn-success</c:otherwise>
                                </c:choose>">
                                                <c:choose>
                                                    <c:when test="${c.status}">To Do</c:when>
                                                    <c:otherwise>Done</c:otherwise>
                                                </c:choose>
                                            </button>
                                        </form>
                                    </td>
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