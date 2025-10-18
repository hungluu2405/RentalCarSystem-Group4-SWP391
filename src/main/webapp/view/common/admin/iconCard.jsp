<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 10/16/2025
  Time: 10:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="row">
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card text-white bg-primary o-hidden h-100">
            <div class="card-body">
                <div class="card-body-icon">
                    <i class="fas fa-id-card"></i>
                </div>
                <div class="mr-5">${totalUsers} User Accounts</div>
            </div>
            <a class="card-footer text-white clearfix small z-1" href="${pageContext.request.contextPath}/accountDB">
                <span class="float-left">View Details</span>
                <span class="float-right">
                  <i class="fas fa-angle-right"></i>
                </span>
            </a>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card text-white bg-warning o-hidden h-100">
            <div class="card-body">
                <div class="card-body-icon">
                    <i class="fas fa-car"></i>
                </div>
                <div class="mr-5">${totalCars} Cars</div>
            </div>
            <a class="card-footer text-white clearfix small z-1" href="${pageContext.request.contextPath}/carDB">
                <span class="float-left">View Details</span>
                <span class="float-right">
                  <i class="fas fa-angle-right"></i>
                </span>
            </a>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card text-white bg-success o-hidden h-100">
            <div class="card-body">
                <div class="card-body-icon">
                    <i class="fas fa-fw fa-table"></i>
                </div>
                <div class="mr-5">${totalBookings} Bookings</div>
            </div>
            <a class="card-footer text-white clearfix small z-1" href="${pageContext.request.contextPath}/bookingDB">
                <span class="float-left">View Details</span>
                <span class="float-right">
                  <i class="fas fa-angle-right"></i>
                </span>
            </a>
        </div>
    </div>
    <div class="col-xl-3 col-sm-6 mb-3">
        <div class="card text-white bg-danger o-hidden h-100">
            <div class="card-body">
                <div class="card-body-icon">
                    <i class="fas fa-fw fa-life-ring"></i>
                </div>
                <div class="mr-5">Report</div>
            </div>
            <a class="card-footer text-white clearfix small z-1" href="${pageContext.request.contextPath}/accountDB">
                <span class="float-left">View Details</span>
                <span class="float-right">
                  <i class="fas fa-angle-right"></i>
                </span>
            </a>
        </div>
    </div>
</div>
