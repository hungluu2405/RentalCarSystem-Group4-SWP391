<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <title>Rentaly - My Profile</title>

    <style>

        .profile-avatar-container { position: relative; display: inline-block; text-align: center; }
        .profile-avatar { width: 160px; height: 160px; border-radius: 50%; object-fit: cover; border: 4px solid #28a745; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2); transition: all 0.3s ease; }
        .profile-avatar-container:hover .profile-avatar { opacity: 0.9; }
        .change-photo-btn { position: absolute; bottom: 5px; right: 5px; background-color: #28a745; color: white; width: 42px; height: 42px; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; transition: all 0.3s ease; }
        .change-photo-btn:hover { background-color: #218838; transform: scale(1.1); }
        h5 { font-weight: 600; margin-bottom: 6px; }
        .form-control { border-radius: 8px; border: 1px solid #ccc; padding: 10px; transition: 0.2s ease; }
        .form-control:focus { border-color: #28a745; box-shadow: 0 0 5px rgba(40, 167, 69, 0.3); }
        .btn-main { background-color: #28a745; color: white; border: none; border-radius: 8px; padding: 10px 25px; font-weight: 600; transition: all 0.3s; }
        .btn-main:hover { background-color: #218838; }
        .alert-success { border-radius: 8px; }
        .alert-danger { border-radius: 8px; }
    </style>
</head>

<body>
<div id="wrapper">
    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y text-center">
                <div class="container">
                    <h1>My Profile</h1>
                </div>
            </div>
        </section>

        <section id="section-settings" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="profile"/>
                        </jsp:include>
                    </div>

                    <div class="col-lg-9">
                        <div class="card padding40 rounded-5 shadow-sm">


                            <c:if test="${param.status == 'success'}">
                                <div class="alert alert-success text-center mb-4">
                                    ✅ Profile updated successfully!
                                </div>
                            </c:if>


                            <c:if test="${not empty requestScope.error}">
                                <div class="alert alert-danger text-center mb-4">
                                        ${requestScope.error}
                                </div>
                            </c:if>

                            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                            <form action="${pageContext.request.contextPath}/customer/license"
                                  method="post" enctype="multipart/form-data">
                                <label>License Number:</label>
                                <input type="text" name="license_number" value="${license.license_number}" required><br>

                                <label>Issue Date:</label>
                                <input type="date" name="issue_date" value="${license.issue_date}"><br>

                                <label>Expiry Date:</label>
                                <input type="date" name="expiry_date" value="${license.expiry_date}"><br>

                                <label>Front Image:</label>
                                <input type="file" name="front_image"><br>
                                <c:if test="${not empty license.front_image_url}">
                                    <img src="${pageContext.request.contextPath}${license.front_image_url}" width="150">
                                </c:if><br>

                                <label>Back Image:</label>
                                <input type="file" name="back_image"><br>
                                <c:if test="${not empty license.back_image_url}">
                                    <img src="${pageContext.request.contextPath}${license.back_image_url}" width="150">
                                </c:if><br>

                                <button type="submit">Update License</button>
                            </form>


                            <script>
                                // Preview ảnh bằng lái
                                document.getElementById('licenseImageUpload').addEventListener('change', function (event) {
                                    const file = event.target.files[0];
                                    if (file) {
                                        const preview = document.getElementById('licenseImagePreview');
                                        preview.src = URL.createObjectURL(file);
                                    }
                                });
                            </script>

                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<script>
    document.getElementById("profileImageUpload").addEventListener("change", function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                document.getElementById("profileImagePreview").src = event.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
</script>
</body>
</html>