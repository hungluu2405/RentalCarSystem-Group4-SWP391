
<%-- _customer_sidebar.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="col-lg-3 mb30">
    <div class="card padding30 rounded-5">
        <div class="profile_avatar">
            <div class="profile_img">
                <img src="images/profile/1.jpg" alt="">
                <img src="${pageContext.request.contextPath}/images/profile/1.jpg" alt="">

            </div>
            <div class="profile_name">
                <h4>
                    Monica Lucas
                    <span class="profile_username text-gray">monica@rentaly.com</span>
                </h4>
            </div>
        </div>
        <div class="spacer-20"></div>
        <ul class="menu-col">
            <li><a href="#" class="active"><i class="fa fa-home"></i>Dashboard</a></li>
            <li><a href="account-profile.html"><i class="fa fa-user"></i>My Profile</a></li>
            <li><a href="account-booking.html"><i class="fa fa-calendar"></i>My Orders</a></li>
            <li><a href="account-favorite.html"><i class="fa fa-car"></i>My Favorite Cars</a></li>
            <li><a href="login.html"><i class="fa fa-sign-out"></i>Sign Out</a></li>
        </ul>
    </div>
</div>