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
        <%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!-- Cars Data Table -->
<div class="card mb-3">
  <div class="card-header">
    <i class="fas fa-car"></i>
    Danh sách xe
  </div>

    <div class="card-body">

        <form action="carDB" method="get" class="form-inline mb-3">
            <label for="type" class="mr-2 font-weight-bold">
                <i class="fas fa-filter mr-1"></i>Loại:
            </label>
            <select name="type" id="type" class="form-control mr-3">
                <option value="">Tất cả</option>
                <option value="SUV" <c:if test="${selectedType == 'SUV'}">selected</c:if>>SUV</option>
                <option value="Sedan" <c:if test="${selectedType == 'Sedan'}">selected</c:if>>Sedan</option>
                <option value="Truck" <c:if test="${selectedType == 'Truck'}">selected</c:if>>Xe tải</option>
                <option value="Coupe" <c:if test="${selectedType == 'Coupe'}">selected</c:if>>Xe hai chỗ</option>
                <option value="Convertible" <c:if test="${selectedType == 'Convertible'}">selected</c:if>>Convertible</option>
            </select>

            <label for="price" class="mr-2 font-weight-bold">Giá:</label>
            <select name="price" id="price" class="form-control mr-3">
                <option value="">Tất cả</option>
                <option value="under1tr" <c:if test="${selectedPrice == 'under1tr'}">selected</c:if>>Dưới 1 000 000vnd/Ngày</option>
                <option value="1trto1tr5" <c:if test="${selectedPrice == '1trto1tr5'}">selected</c:if>>1 000 000vnd Tới 1 500 000vnd/Ngày</option>
                <option value="over1tr5" <c:if test="${selectedPrice == 'over1tr5'}">selected</c:if>>Over 1 500 000vnd/Ngày</option>
            </select>

            <button type="submit" class="btn btn-primary mr-2">
                <i class="fas fa-search"></i> Tìm kiếm
            </button>
            <a href="carlist" class="btn btn-outline-secondary">
                <i class="fas fa-undo"></i> Tạo lại
            </a>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead class="thead-dark">
                <tr>
                    <th>ID xe</th>
                    <th>Model</th>
                    <th>Loại</th>
                    <th>Hãng</th>
                    <th>Tên chủ xe</th>
                    <th>Năm</th>
                    <th>Biển số xe</th>
                    <th>Số chỗ ngồi</th>
                    <th>Loại nguyên liệu</th>
                    <th>Giá/Ngày</th>
                    <th>Trạng thái</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="car" items="${listC}">
                    <tr>
                        <td>${car.carId}</td>
                        <td><a href="${pageContext.request.contextPath}/admin/manageCar?id=${car.carId}">${car.model}</a></td>
                        <td>${car.typeNameVN}</td>
                        <td>${car.brand}</td>
                        <td>${car.carOwnerName}</td>
                        <td>${car.year}</td>
                        <td>${car.licensePlate}</td>
                        <td>${car.capacity}</td>
                        <td>${car.fuelTypeVN}</td>
                        <td>${car.pricePerDay}</td>

                <td>
                <c:choose>
                  <c:when test="${car.availability}">
                    <span class="badge badge-success">Sãn sàng</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge badge-danger">Chưa sãn sàng</span>
                  </c:otherwise>
                </c:choose>
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
  <script src="${pageContext.request.contextPath}/js/demo/chart-area-demo.js"></script>
  <script src="${pageContext.request.contextPath}/js/demo/datatables-demo.js"></script>
  <script src="${pageContext.request.contextPath}/js/colReorder-dataTables-min.js"></script>
  <script src="${pageContext.request.contextPath}/js/colReorder-bootstrap4-min.js"></script>


</body>

</html>