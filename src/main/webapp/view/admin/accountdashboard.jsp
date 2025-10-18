<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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



       <!-- DataTables Example -->
<div class="card mb-3">
  <div class="card-header">
    <i class="fas fa-id-card"></i>
    User Profile Table
  </div>
  <div class="card-body">
    <div class="table-responsive">
      <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
        <thead class="thead-dark">
          <tr>
            <th>Profile ID</th>
            <th>Role</th>
            <th>Full Name</th>
            <th>Phone</th>
            <th>Date of Birth</th>
            <th>Gender</th>
            <th>Driver License</th>
            <th>Email</th>
            <th>Created At</th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${listU}">
          <c:if test="${u.userProfile.profileId != 0}">
            <tr>
              <td>${u.userProfile.profileId}</td>
              <td>${u.roleName}</td>
              <td>${u.userProfile.fullName}</td>
              <td>${u.userProfile.phone}</td>
              <td>${u.userProfile.dob}</td>
              <td>${u.userProfile.gender}</td>
              <td>${u.userProfile.driverLicenseNumber}</td>
              <td>${u.email}</td>
              <td>${u.createdAt}</td>
            </tr>
          </c:if>
        </c:forEach>
      </tbody>

      </table>
    </div>
  </div>
  <div class="card-footer small text-muted">
    Car Rental System
  </div>
</div>
<!-- /.container-fluid -->


      <!-- Footer -->
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