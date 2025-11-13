<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 11/14/2025
  Time: 12:06 AM
  To change this template use File | Settings | File Templates.
--%>
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
                                <img src="${pageContext.request.contextPath}/images/promotion.jpg"
                                     alt="Profile Image"
                                     class="rounded-circle"
                                     width="120" height="120">
                            </div>
                            <div class="profile_name">
                                <h4 class="mb-0">admin@gmail.com</h4>
                                <span class="profile_username text-gray">${email}</span>
                            </div>
                        </div>


                    </div>

                    <!-- MAIN FORM -->

                    <div class="col-lg-9">
                        <div class="card padding40 rounded-5 shadow-sm">

                            <h3 class="text-center mb-4">Tạo Khuyến Mãi Mới</h3>

                            <!-- ================= FORM CREATE PROMOTION ================= -->
                            <form method="post"
                                  action="${pageContext.request.contextPath}/promotionCreate"
                                  class="form-border">

                                <div class="row">

                                    <!-- CODE -->
                                    <div class="col-lg-6 mb20">
                                        <h5>Mã Promotion</h5>
                                        <input type="text" name="code" class="form-control"
                                               placeholder="Nhập mã khuyến mãi"
                                               required>
                                    </div>

                                    <!-- DISCOUNT RATE -->
                                    <div class="col-lg-6 mb20">
                                        <h5>Mức Giảm (%)</h5>
                                        <input type="number" name="rate" class="form-control"
                                               placeholder="Ví dụ: 10"
                                               step="0.01" min="0"
                                               required>
                                    </div>

                                    <!-- DESCRIPTION -->
                                    <div class="col-lg-12 mb20">
                                        <h5>Mô Tả</h5>
                                        <textarea name="description" class="form-control" rows="3"
                                                  placeholder="Nhập mô tả khuyến mãi"
                                                  required></textarea>
                                    </div>



                                    <!-- START DATE -->
                                    <div class="col-lg-6 mb20">
                                        <h5>Ngày Bắt Đầu</h5>
                                        <input type="date" name="start" class="form-control" required>
                                    </div>

                                    <!-- END DATE -->
                                    <div class="col-lg-6 mb20">
                                        <h5>Ngày Kết Thúc</h5>
                                        <input type="date" name="end" class="form-control" required>
                                    </div>

                                    <!-- ACTIVE -->
                                    <div class="col-lg-6 mb20">
                                        <h5>Trạng Thái</h5>
                                        <select name="active" class="form-control">
                                            <option value="true">Active</option>
                                            <option value="false">Inactive</option>
                                        </select>
                                    </div>

                                </div>

                                <!-- BUTTONS -->
                                <button type="submit" class="btn btn-success mt-3">
                                    Tạo Khuyến Mãi
                                </button>

                                <a href="${pageContext.request.contextPath}/promotionDB"
                                   class="btn btn-secondary mt-3 ml-2">
                                    Quay Lại Danh Sách
                                </a>
                            </form>

                        </div>
                    </div>


                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<!-- ================= JS: Preview Image ================= -->
</body>
</html>


