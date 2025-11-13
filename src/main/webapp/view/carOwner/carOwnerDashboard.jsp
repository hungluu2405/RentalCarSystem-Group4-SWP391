<%--
  Created by IntelliJ IDEA.
  User: tunge
  Date: 10/9/2025
  Time: 11:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Car Owner Dashboard - My Profile</title>

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
    <div id="de-preloader"></div>
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img"
                 alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center"><h1>Thông tin của tôi </h1></div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <%-- Sidebar --%>
                    <div class="col-lg-3 mb30">
                        <%-- Sửa lại đuôi file thành .jspf --%>
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                            <jsp:param name="activePage" value="profile"/>
                        </jsp:include>
                    </div>

                        <!-- MAIN PROFILE FORM -->
                        <div class="col-lg-9">
                            <div class="card padding40 rounded-5 shadow-sm">

                                <c:if test="${param.status == 'success'}">
                                    <div class="alert alert-success text-center mb-4">
                                         Thay đổi thông tin thành công!
                                    </div>
                                </c:if>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/owner/profile"
                                      enctype="multipart/form-data"
                                      class="form-border">

                                    <!-- ================= Profile Image Section ================= -->
                                    <div class="text-center mb-4">
                                        <div class="profile-avatar-container">
                                            <!-- Ảnh đại diện -->
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.user.userProfile.profileImage}">
                                                    <img id="profileImagePreview"
                                                         src="${pageContext.request.contextPath}${sessionScope.user.userProfile.profileImage}"
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
                                            <label for="profileImageUpload" class="change-photo-btn">
                                                <i class="fa fa-camera"></i>
                                            </label>
                                            <input type="file" id="profileImageUpload" name="profileImage" accept="image/*"
                                                   style="display: none;">
                                        </div>

                                        <p style="font-size: 14px; color: #777; margin-top: 10px;">
                                           Click vào ảnh để thay đổi ảnh đại diện
                                        </p>
                                    </div>

                                    <!-- ================= Thông tin cá nhân ================= -->
                                    <div class="row">
                                        <div class="col-lg-6 mb20">
                                            <h5>Tên đầy đủ</h5>
                                            <input type="text" name="fullName" class="form-control"
                                                   value="${sessionScope.user.userProfile.fullName}"
                                                   placeholder="Enter your full name">
                                        </div>

                                        <div class="col-lg-6 mb20">
                                            <h5>Email </h5>
                                            <input type="text" name="email" class="form-control"
                                                   value="${sessionScope.user.email}" readonly>
                                        </div>

                                        <div class="col-lg-6 mb20">
                                            <h5>Số điện thoại </h5>
                                            <input type="text" name="phone" class="form-control"
                                                   value="${sessionScope.user.userProfile.phone}"
                                                   placeholder="Enter your phone number">
                                        </div>

                                        <div class="col-lg-6 mb20">
                                            <h5>Ngày sinh</h5>
                                            <input type="date" name="dob" class="form-control"
                                                   value="${sessionScope.user.userProfile.dob}">
                                        </div>

                                        <div class="col-lg-6 mb20">
                                            <h5>Bằng lái xe</h5>

                                            <input type="text"
                                                   class="form-control"
                                                   value="${license != null ? license.license_number : 'Not registered'}"
                                                   readonly>

                                            <small class="text-muted">
                                                To update your license, please visit
                                                <a href="${pageContext.request.contextPath}/owner/license" class="text-primary">
                                                    License Management
                                                </a>.
                                            </small>
                                        </div>

                                        <div class="col-lg-6 mb20">
                                            <h5>Giới tính</h5>
                                            <select name="gender" class="form-control">
                                                <option value="">-- Select Gender --</option>
                                                <option value="Male" ${sessionScope.user.userProfile.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                <option value="Female" ${sessionScope.user.userProfile.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                <option value="Other" ${sessionScope.user.userProfile.gender == 'Other' ? 'selected' : ''}>Other</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="text-center mt-4">
                                        <input type="submit" class="btn-main" value="Update Profile">
                                    </div>
                                </form>
                            </div>
                        </div>
                </div>
            </div>
        </section>
    </div>

    <!-- FOOTER -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
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
