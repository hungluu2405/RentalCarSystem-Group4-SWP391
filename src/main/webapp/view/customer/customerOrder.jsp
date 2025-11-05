<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/customer/_head.jsp"/>
    <style>
        /* CSS Cũ Giữ Nguyên */
        .tab-container { display: flex; gap: 30px; border-bottom: 2px solid #eaeaea; margin-top: 10px; margin-bottom: 20px; }
        .tab-btn { background: none; border: none; font-weight: 600; font-size: 16px; padding: 8px 0; cursor: pointer; color: #222; transition: all 0.3s ease; position: relative; }
        .tab-btn.active { color: #00b074; }
        .tab-btn.active::after { content: ""; position: absolute; bottom: -2px; left: 0; height: 3px; width: 100%; background-color: #00b074; border-radius: 5px; }
        .tab-content { display: none; }
        .tab-content.active { display: block; }
        .badge { padding: 5px 10px; border-radius: 10px; font-size: 12px; }

        /* Màu sắc trạng thái */
        .bg-warning { background-color: #FFD54F; color: #222; }
        .bg-info-dark { background-color: #5bc0de; color: white; }
        .bg-primary-dark { background-color: #007bff; color: white; }
        .bg-success { background-color: #66BB6A; color: white; }
        .bg-danger { background-color: #dc3545; color: white; }

        /* ========== CSS MỚI CHO MODAL ========== */
        .owner-modal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            animation: fadeIn 0.3s;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        .owner-modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 0;
            border-radius: 12px;
            width: 90%;
            max-width: 450px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
            animation: slideDown 0.3s;
        }

        @keyframes slideDown {
            from {
                transform: translateY(-50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .owner-modal-header {
            padding: 20px 25px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 12px 12px 0 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .owner-modal-header h3 {
            margin: 0;
            font-size: 20px;
        }

        .owner-modal-close {
            font-size: 28px;
            cursor: pointer;
            color: white;
            transition: transform 0.2s;
        }

        .owner-modal-close:hover {
            transform: scale(1.2);
        }

        .owner-modal-body {
            padding: 30px;
        }

        .owner-info-item {
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }

        .owner-info-item:last-child {
            border-bottom: none;
        }

        .owner-info-label {
            font-size: 12px;
            color: #888;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.5px;
            margin-bottom: 5px;
        }

        .owner-info-value {
            font-size: 16px;
            color: #333;
            font-weight: 600;
        }

        .owner-avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #f0f0f0;
            margin: 0 auto 20px;
            display: block;
        }

        .owner-actions {
            display: flex;
            gap: 10px;
            margin-top: 25px;
        }

        .btn-call, .btn-sms {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            transition: all 0.3s;
        }

        .btn-call {
            background-color: #28a745;
            color: white;
        }

        .btn-call:hover {
            background-color: #218838;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(40, 167, 69, 0.3);
        }

        .btn-sms {
            background-color: #007bff;
            color: white;
        }

        .btn-sms:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 123, 255, 0.3);
        }

        .car-name-link {
            color: #0d6efd;
            cursor: pointer;
            text-decoration: none;
            font-weight: 700;
            display: inline-flex;
            align-items: center;
            gap: 5px;
        }

        .car-name-link:hover {
            text-decoration: underline;
            color: #0a58ca;
        }

        .loading-spinner {
            text-align: center;
            padding: 40px;
        }

        .loading-spinner i {
            font-size: 32px;
            color: #667eea;
        }
    </style>
</head>

<body>
<div id="wrapper">

    <jsp:include page="../common/customer/_header.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>My Orders</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/customer/_sidebar.jsp">
                            <jsp:param name="activePage" value="orders"/>
                        </jsp:include>
                    </div>

                    <div class="col-lg-9">
                        <div class="row mb25">
                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-check-o fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${upcoming}</span><br>Upcoming Orders
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar fa-2x text-success mb10"></i>
                                    <span class="h2 mb0">${total}</span><br>Total Orders
                                </div>
                            </div>

                            <div class="col-lg-4 col-6 mb25">
                                <div class="card padding30 text-center rounded-5">
                                    <i class="fa fa-calendar-times-o fa-2x text-danger mb10"></i>
                                    <span class="h2 mb0">${cancelled}</span><br>Cancel Orders
                                </div>
                            </div>
                        </div>

                        <div class="card padding30 rounded-5 mb25">
                            <h4>My Orders</h4>

                            <div class="tab-container">
                                <button class="tab-btn active" id="tabCurrent">Current Trips</button>
                                <button class="tab-btn" id="tabHistory">Trip History</button>
                            </div>

                            <div id="currentTrips" class="tab-content active">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Car Name</th>
                                            <th>Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="order" items="${allBookings}">
                                            <c:set var="status" value="${order.status}"/>
                                            <c:set var="isCurrent" value="${status == 'Pending' || status == 'Approved' || status == 'Paid'}"/>

                                            <c:if test="${isCurrent}">
                                                <tr>
                                                    <!-- ========== SỬA TÊN XE THÀNH LINK ========== -->
                                                    <td>
                                                        <a class="car-name-link"
                                                           onclick="showOwnerInfo(${order.bookingId}, '${order.carName}')">
                                                            <c:out value="${order.carName}"/>
                                                            <i class="fa fa-info-circle"></i>
                                                        </a>
                                                    </td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td>$<c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${status == 'Pending'}">
                                                                <span class="badge bg-warning text-dark">Pending</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Approved'}">
                                                                <span class="badge bg-info-dark">Approved</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Paid'}">
                                                                <span class="badge bg-primary-dark">Paid</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-info text-dark">${status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:if test="${status == 'Pending'}">
                                                            <a href="${pageContext.request.contextPath}/customer/cancelBooking?bookingId=${order.bookingId}"
                                                               onclick="return confirm('Are you sure you want to cancel this booking?');"
                                                               class="btn btn-sm btn-danger">Cancel</a>
                                                        </c:if>
                                                        <c:if test="${status == 'Approved'}">
                                                            <a href="${pageContext.request.contextPath}/customer/create-payment?bookingId=${order.bookingId}" class="btn btn-sm btn-info">
                                                                Payment
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${status == 'Paid'}">
                                                            <a href="${pageContext.request.contextPath}/customer/returnCar?bookingId=${order.bookingId}"
                                                               onclick="return confirm('Complete This Rental?');"
                                                               class="btn btn-sm btn-success">Return Car</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <div id="tripHistory" class="tab-content">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Car Name</th>
                                            <th>Location</th>
                                            <th>Pick Up Date</th>
                                            <th>Return Date</th>
                                            <th>Price</th>
                                            <th>Status</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="order" items="${allBookings}">
                                            <c:set var="status" value="${order.status}"/>
                                            <c:set var="isHistory" value="${status == 'Completed' || status == 'Rejected' || status == 'Cancelled'}"/>

                                            <c:if test="${isHistory}">
                                                <tr>
                                                    <td><strong><c:out value="${order.carName}"/></strong></td>
                                                    <td><c:out value="${order.location}"/></td>
                                                    <td><c:out value="${order.startDate}"/> ${order.pickupTime}</td>
                                                    <td><c:out value="${order.endDate}"/> ${order.dropoffTime}</td>
                                                    <td>$<c:out value="${order.totalPrice}"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${status == 'Completed'}">
                                                                <span class="badge bg-success">Completed</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Cancelled'}">
                                                                <span class="badge bg-danger">Cancelled</span>
                                                            </c:when>
                                                            <c:when test="${status == 'Rejected'}">
                                                                <span class="badge bg-danger">Rejected</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-danger">${status}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <jsp:include page="../common/customer/_footer_scripts.jsp"/>
</div>

<!-- ========== THÊM MODAL MỚI ========== -->
<div id="ownerModal" class="owner-modal">
    <div class="owner-modal-content">
        <div class="owner-modal-header">
            <h3>Owner Contact Information</h3>
            <span class="owner-modal-close" onclick="closeOwnerModal()">&times;</span>
        </div>
        <div class="owner-modal-body" id="ownerInfoContent">
            <!-- Content loaded by JS -->
        </div>
    </div>
</div>

<!-- ========== JAVASCRIPT ========== -->
<script>
    // Tab switching (Giữ nguyên)
    document.addEventListener("DOMContentLoaded", function () {
        const tabCurrent = document.getElementById("tabCurrent");
        const tabHistory = document.getElementById("tabHistory");
        const currentTrips = document.getElementById("currentTrips");
        const tripHistory = document.getElementById("tripHistory");

        const urlParams = new URLSearchParams(window.location.search);
        const tabParam = urlParams.get('tab');

        function activateTab(tabId) {
            const currentTripDiv = document.getElementById("currentTrips");
            const historyTripDiv = document.getElementById("tripHistory");

            if (tabId === 'history') {
                tabHistory.classList.add("active");
                tabCurrent.classList.remove("active");
                historyTripDiv.classList.add("active");
                currentTripDiv.classList.remove("active");
            } else {
                tabCurrent.classList.add("active");
                tabHistory.classList.remove("active");
                currentTripDiv.classList.add("active");
                historyTripDiv.classList.remove("active");
            }
        }

        activateTab(tabParam);

        tabCurrent.addEventListener("click", function () {
            window.location.href = window.location.pathname + "?tab=current";
        });

        tabHistory.addEventListener("click", function () {
            window.location.href = window.location.pathname + "?tab=history";
        });
    });

    // ========== THÊM FUNCTION MỚI ĐỂ HIỂN THỊ OWNER INFO ==========
    function showOwnerInfo(bookingId, carName) {
        const modal = document.getElementById('ownerModal');
        const content = document.getElementById('ownerInfoContent');

        content.innerHTML = `
            <div class="loading-spinner">
                <i class="fa fa-spinner fa-spin"></i>
                <p style="margin-top: 15px; color: #666;">Loading owner information...</p>
            </div>
        `;
        modal.style.display = 'block';

        fetch('${pageContext.request.contextPath}/customer/owner-info?bookingId=' + bookingId)
            .then(response => {
                if (!response.ok) throw new Error('Network error');
                return response.json();
            })
            .then(data => {
                const avatarUrl = data.avatar && data.avatar.trim() !== ''
                    ? '${pageContext.request.contextPath}' + data.avatar
                    : '${pageContext.request.contextPath}/images/profile/default.jpg';

                content.innerHTML = `
                    <div style="text-align: center;">
                        <img src="` + avatarUrl + `"
                             class="owner-avatar"
                             onerror="this.src='${pageContext.request.contextPath}/images/profile/default.jpg'">
                    </div>

                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-car"></i> Car
                        </div>
                        <div class="owner-info-value">` + carName + `</div>
                    </div>

                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-user"></i> Owner Name
                        </div>
                        <div class="owner-info-value">` + (data.fullName || 'N/A') + `</div>
                    </div>

                    <div class="owner-info-item">
                        <div class="owner-info-label">
                            <i class="fa fa-phone"></i> Phone Number
                        </div>
                        <div class="owner-info-value">` + (data.phone || 'N/A') + `</div>
                    </div>


                `;
            })
            .catch(error => {
                console.error('Error:', error);
                content.innerHTML = `
                    <div style="text-align: center; padding: 40px; color: #dc3545;">
                        <i class="fa fa-exclamation-circle" style="font-size: 48px; margin-bottom: 15px;"></i>
                        <p style="font-weight: 600;">Failed to load owner information</p>
                        <p style="color: #666; font-size: 14px;">Please try again later</p>
                        <button onclick="closeOwnerModal()"
                                style="margin-top: 20px; padding: 10px 30px; background: #6c757d;
                                       color: white; border: none; border-radius: 8px; cursor: pointer;">
                            Close
                        </button>
                    </div>
                `;
            });
    }

    function closeOwnerModal() {
        document.getElementById('ownerModal').style.display = 'none';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('ownerModal');
        if (event.target == modal) {
            closeOwnerModal();
        }
    }

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            closeOwnerModal();
        }
    });
</script>
</body>
</html>