<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Detail</title>
    <link href="${pageContext.request.contextPath}/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2>User Detail</h2>
    <table class="table table-bordered">
        <tr><th>Profile ID</th><td>${profile.profileId}</td></tr>
        <tr><th>Full Name</th><td>${profile.fullName}</td></tr>
        <tr><th>Phone</th><td>${profile.phone}</td></tr>
        <tr><th>Date of Birth</th><td>${profile.dob}</td></tr>
        <tr><th>Gender</th><td>${profile.gender}</td></tr>
        <tr><th>Driver License</th><td>${profile.driverLicenseNumber}</td></tr>
<%--        <tr><th>Email</th><td>${profile.user.email}</td></tr>--%>
<%--        <tr><th>Created At</th><td>${profile.createdAt}</td></tr>--%>
    </table>
    <a href="${pageContext.request.contextPath}/accountDB" class="btn btn-secondary">Back</a>
</div>
</body>
</html>
