<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
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
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

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
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
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


                            <form action="${pageContext.request.contextPath}/owner/license"
                                  method="post"
                                  enctype="multipart/form-data"
                                  class="form-border p-4 bg-light rounded shadow-sm">

                                <!-- License Images -->
                                <div class="text-center mb-4">
                                    <div class="license-image-container">
                                        <c:set var="frontImageUrl"
                                               value="${license.front_image_url != null ? license.front_image_url : '/images/license/default-front.jpg'}" />
                                        <c:set var="backImageUrl"
                                               value="${license.back_image_url != null ? license.back_image_url : '/images/license/default-back.jpg'}" />

                                        <div class="mb-3">
                                            <label>Front Image:</label><br>
                                            <img id="frontImagePreview"
                                                 src="${pageContext.request.contextPath}${frontImageUrl}"
                                                 alt="Front License"
                                                 class="license-preview mb-2"
                                                 width="150">
                                            <input type="file" name="front_image" class="form-control">
                                        </div>

                                        <div class="mb-3">
                                            <label>Back Imagesssssssss:</label><br>
                                            <img id="backImagePreview"
                                                 src="${pageContext.request.contextPath}${backImageUrl}"
                                                 alt="Back License"
                                                 class="license-preview mb-2"
                                                 width="150">
                                            <input type="file" name="back_image" class="form-control">
                                        </div>
                                    </div>
                                </div>

                                <!-- License & User Info -->
                                <div class="row">
                                    <div class="col-lg-6 mb-3">
                                        <label for="fullName" class="form-label">Full Name:</label>
                                        <input type="text" id="fullName" name="fullName"
                                               class="form-control"
                                               value="${license.fullName}" readonly>
                                    </div>

                                    <div class="col-lg-6 mb-3">
                                        <label for="gender" class="form-label">Gender:</label>
                                        <input type="text" id="gender" name="gender"
                                               class="form-control"
                                               value="${license.gender}" readonly>
                                    </div>

                                    <div class="col-lg-6 mb-3">
                                        <label for="dob" class="form-label">Date of Birth:</label>
                                        <input type="date" id="dob" name="dob"
                                               class="form-control"
                                               value="${license.dob}" readonly>
                                    </div>

                                    <div class="col-lg-6 mb-3">
                                        <label for="license_number" class="form-label">License Number:</label>
                                        <input type="text" id="license_number" name="license_number"
                                               class="form-control"
                                               value="${license.license_number}" required>
                                    </div>

                                    <div class="col-lg-6 mb-3">
                                        <label for="issue_date" class="form-label">Issue Date:</label>
                                        <input type="date" id="issue_date" name="issue_date"
                                               class="form-control"
                                               value="${license.issue_date}">
                                    </div>

                                    <div class="col-lg-6 mb-3">
                                        <label for="expiry_date" class="form-label">Expiry Date:</label>
                                        <input type="date" id="expiry_date" name="expiry_date"
                                               class="form-control"
                                               value="${license.expiry_date}">
                                    </div>
                                </div>

                                <div class="text-center mt-4">
                                    <button type="submit" class="btn-main">Update License</button>
                                </div>
                            </form>




                            <script>
                                document.querySelector('input[name="front_image"]').addEventListener('change', function(e) {
                                    const file = e.target.files[0];
                                    if (file) {
                                        document.getElementById('frontImagePreview').src = URL.createObjectURL(file);
                                    }
                                });

                                document.querySelector('input[name="back_image"]').addEventListener('change', function(e) {
                                    const file = e.target.files[0];
                                    if (file) {
                                        document.getElementById('backImagePreview').src = URL.createObjectURL(file);
                                    }
                                });
                            </script>


                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
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