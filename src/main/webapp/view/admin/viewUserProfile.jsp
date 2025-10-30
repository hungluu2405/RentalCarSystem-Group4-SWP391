<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <title>Rentaly - My Profile</title>

    <style>
        /* ================= Profile Image Section ================= */
        .profile-avatar-container {
            position: relative;
            display: inline-block;
            text-align: center;
        }

        .profile-avatar {
            width: 160px;
            height: 160px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #28a745;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
        }

        .profile-avatar-container:hover .profile-avatar {
            opacity: 0.9;
        }

        .change-photo-btn {
            position: absolute;
            bottom: 5px;
            right: 5px;
            background-color: #28a745;
            color: white;
            width: 42px;
            height: 42px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .change-photo-btn:hover {
            background-color: #218838;
            transform: scale(1.1);
        }

        /* ================= Form Styling ================= */
        h5 {
            font-weight: 600;
            margin-bottom: 6px;
        }

        .form-control {
            border-radius: 8px;
            border: 1px solid #ccc;
            padding: 10px;
            transition: 0.2s ease;
        }

        .form-control:focus {
            border-color: #28a745;
            box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
        }

        .btn-main {
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 10px 25px;
            font-weight: 600;
            transition: all 0.3s;
        }

        .btn-main:hover {
            background-color: #218838;
        }

        .alert-success {
            border-radius: 8px;
        }
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
                    <!-- SIDEBAR -->
                    <div class="col-lg-3 mb30">
                        <div class="profile_avatar mb-3">
                            <div class="profile_img mb-2">
                                <img src="${pageContext.request.contextPath}${profile.profileImage != null ? profile.profileImage : '/images/profile/default.jpg'}"
                                     alt="Profile Image"
                                     class="rounded-circle"
                                     width="120" height="120">
                            </div>
                            <div class="profile_name">
                                <h4 class="mb-0">${sessionScope.user.userProfile.fullName != null ? sessionScope.user.userProfile.fullName : 'Unknown User'}</h4>
                                <span class="profile_username text-gray">${email}</span>
                            </div>
                        </div>


                    </div>

                    <!-- MAIN PROFILE FORM -->
                    <div class="col-lg-9">
                        <div class="card padding40 rounded-5 shadow-sm">

                            <c:if test="${param.status == 'success'}">
                                <div class="alert alert-success text-center mb-4">
                                    ✅ Profile updated successfully!
                                </div>
                            </c:if>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/customer/profile"
                                  enctype="multipart/form-data"
                                  class="form-border">

                                <!-- ================= Profile Image Section ================= -->
                                <div class="text-center mb-4">
                                    <div class="profile-avatar-container">
                                        <!-- Ảnh đại diện -->
                                        <c:choose>
                                            <c:when test="${not empty profile.profileImage}">
                                                <img id="profileImagePreview"
                                                     src="${pageContext.request.contextPath}${profile.profileImage}"
                                                     alt="Profile"
                                                     class="profile-avatar">
                                            </c:when>
                                            <c:otherwise>
                                                <img id="profileImagePreview"
                                                     src="${pageContext.request.contextPath}/images/profile/default.jpg"
                                                     alt="Default Profile"
                                                     class="profile-avatar">
                                            </c:otherwise>
                                        </c:choose>

                                        <!-- Nút chọn ảnh -->

                                        <input type="file" id="profileImageUpload" name="profileImage" accept="image/*"
                                               style="display: none;">
                                    </div>


                                </div>

                                <!-- ================= Thông tin cá nhân ================= -->
                                <div class="row">
                                    <div class="col-lg-6 mb20">
                                        <h5>Full Name</h5>
                                        <input type="text" name="fullName" class="form-control"
                                               value="${profile.fullName}"
                                               placeholder="Enter your full name">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Email Address</h5>
                                        <input type="text" name="email" class="form-control"
                                               value="${email}" readonly>
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Phone Number</h5>
                                        <input type="text" name="phone" class="form-control"
                                               value="${profile.phone}"
                                               placeholder="Enter your phone number">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Date of Birth</h5>
                                        <input type="date" name="dob" class="form-control"
                                               value="${profile.dob}">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Driver License Number</h5>
                                        <input type="text" name="driverLicenseNumber" class="form-control"
                                               value="${profile.driverLicenseNumber}"
                                               placeholder="Enter your driver license number">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Gender</h5>
                                        <select name="gender" class="form-control">
                                            <option value="">-- Select Gender --</option>
                                            <option value="Male" ${profile.gender == 'Male' ? 'selected' : ''}>Male</option>
                                            <option value="Female" ${profile.gender == 'Female' ? 'selected' : ''}>Female</option>
                                            <option value="Other" ${sprofile.gender == 'Other' ? 'selected' : ''}>Other</option>
                                        </select>
                                    </div>
                                </div>


                            </form>



                                <form><a href="${pageContext.request.contextPath}/accountDB" class="btn btn-secondary">Back to list</a></form>

                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<!-- ================= JS: Preview Image ================= -->
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
