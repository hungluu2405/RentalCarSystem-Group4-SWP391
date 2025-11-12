<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <title>Rentaly - Tài Khoản Của Tôi</title>

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
                    <h1>Tài Khoản Của Tôi</h1>
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
                                    ✅ Cập Nhật Thông Tin Thành Công!
                                </div>
                            </c:if>


                            <c:if test="${not empty requestScope.error}">
                                <div class="alert alert-danger text-center mb-4">
                                        ${requestScope.error}
                                </div>
                            </c:if>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/customer/profile"
                                  enctype="multipart/form-data"
                                  class="form-border">

                                <div class="text-center mb-4">
                                    <div class="profile-avatar-container">


                                        <c:set var="profileImageUrl"
                                               value="${sessionScope.user.userProfile.profileImage != null ? sessionScope.user.userProfile.profileImage : '/images/profile/default.jpg'}" />

                                        <img id="profileImagePreview"
                                             src="${pageContext.request.contextPath}${profileImageUrl}"
                                             alt="Profile"
                                             class="profile-avatar">

                                        <label for="profileImageUpload" class="change-photo-btn">
                                            <i class="fa fa-camera"></i>
                                        </label>
                                        <input type="file" id="profileImageUpload" name="profileImage" accept="image/*"
                                               style="display: none;">
                                    </div>
                                    <p style="font-size: 14px; color: #777; margin-top: 10px;">
                                        Bấm Vào Biểu Tượng Camera Để Đổi Ảnh Của Bạn
                                    </p>
                                </div>

                                <div class="row">
                                    <div class="col-lg-6 mb20">
                                        <h5>Họ và Tên</h5>

                                        <input type="text" name="fullName" class="form-control"
                                               value="${not empty input_fullName ? input_fullName : sessionScope.user.userProfile.fullName}"
                                               placeholder="Nhập Họ và Tên Của Bạn">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Email</h5>
                                        <input type="text" name="email" class="form-control"
                                               value="${sessionScope.user.email}" readonly>
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Số Điện Thoại</h5>

                                        <input type="text" name="phone" class="form-control"
                                               value="${not empty input_phone ? input_phone : sessionScope.user.userProfile.phone}"
                                               placeholder="Nhập Số Điện Thoại Của Bạn">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Ngày Sinh</h5>

                                        <input type="date" name="dob" class="form-control"
                                               value="${not empty input_dob ? input_dob : sessionScope.user.userProfile.dob}">
                                    </div>

                                    <div class="col-lg-6 mb20">
                                        <h5>Bằng Lái Xe</h5>

                                        <input type="text"
                                               class="form-control"
                                               value="${license != null ? license.license_number : 'Not registered'}"
                                               readonly>

                                        <small class="text-muted">
                                            Để Xác Minh Bằng Lái Xe,Vui Lòng Bấm
                                            <a href="${pageContext.request.contextPath}/customer/license" class="text-primary">
                                                Xác Minh Bằng Lái Xe
                                            </a>.
                                        </small>
                                    </div>


                                    <div class="col-lg-6 mb20">

                                        <c:set var="currentGender"
                                               value="${not empty input_gender ? input_gender : sessionScope.user.userProfile.gender}" />

                                        <h5>Giới Tính</h5>
                                        <select name="gender" class="form-control">
                                            <option value="" ${empty currentGender ? 'selected' : ''}>-- Giới Tính --</option>
                                            <option value="Male" ${currentGender == 'Male' ? 'selected' : ''}>Nam</option>
                                            <option value="Female" ${currentGender == 'Female' ? 'selected' : ''}>Nữ</option>
                                            <option value="Other" ${currentGender == 'Other' ? 'selected' : ''}>Khác</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="text-center mt-4">
                                    <input type="submit" class="btn-main" value="Cập Nhật">
                                </div>
                            </form>
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