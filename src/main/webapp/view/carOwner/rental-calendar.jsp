<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.YearMonth" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.Booking" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../common/carOwner/_headOwner.jsp"/>
    <title>Lịch Thuê Xe</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI',
            'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        h1, h2, h3, h4, h5, h6 {
            font-family: 'Inter', sans-serif;
            font-weight: 700;
        }

        p, span, div {
            font-family: 'Inter', sans-serif;
        }
        /* ================= Filter Section ================= */
        .filter-section {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .filter-section label {
            font-weight: 600;
            margin-right: 10px;
            color: #333;
        }

        .filter-section select {
            padding: 8px 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        .filter-section select:focus {
            border-color: #28a745;
            outline: none;
            box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
        }

        /* Navigation Buttons */
        .btn-nav {
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 50%;
            width: 35px;
            height: 35px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .btn-nav:hover {
            background-color: #218838;
            transform: scale(1.1);
        }

        .btn-nav i {
            font-size: 14px;
        }

        .btn-calendar-picker {
            background-color: #0d6efd;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 8px 15px;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            font-weight: 600;
        }

        .btn-calendar-picker:hover {
            background-color: #0b5ed7;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .btn-today {
            background-color: #6c757d;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 8px 15px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 600;
        }

        .btn-today:hover {
            background-color: #5a6268;
        }

        /* ================= Calendar ================= */
        .calendar {
            background: white;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .calendar-header {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 10px;
            margin-bottom: 10px;
            font-weight: bold;
            text-align: center;
            padding: 15px 0;
            border-bottom: 2px solid #28a745;
            color: #28a745;
        }

        .calendar-body {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            gap: 10px;
        }

        .calendar-day {
            aspect-ratio: 1;
            border: 2px solid #ddd;
            border-radius: 8px;
            padding: 10px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .calendar-day:hover:not(.empty) {
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }

        .calendar-day.empty {
            border: none;
            cursor: default;
        }

        /* Status Colors */
        .calendar-day.available {
            background-color: #d4edda;
            border-color: #28a745;
        }

        .calendar-day.pending {
            background-color: #fff3cd;
            border-color: #ffc107;
        }

        .calendar-day.approved {
            background-color: #cfe2ff;
            border-color: #0d6efd;
        }

        .calendar-day.paid {
            background-color: #d1ecf1;
            border-color: #17a2b8;
        }

        .calendar-day.completed {
            background-color: #d3d3d3;
            border-color: #6c757d;
        }

        .calendar-day.cancelled,
        .calendar-day.rejected {
            background-color: #f8d7da;
            border-color: #dc3545;
        }

        .day-number {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .day-status {
            font-size: 12px;
            color: #666;
            font-weight: 500;
        }

        .multiple-bookings {
            font-size: 10px;
            color: #888;
            margin-top: 5px;
            font-style: italic;
        }

        /* ================= Legend ================= */
        .legend {
            display: flex;
            gap: 20px;
            justify-content: center;
            margin-top: 20px;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            flex-wrap: wrap;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
        }

        .legend-color {
            width: 24px;
            height: 24px;
            border-radius: 4px;
            border: 2px solid #ddd;
        }

        .legend-color.available {
            background-color: #d4edda;
            border-color: #28a745;
        }

        .legend-color.pending {
            background-color: #fff3cd;
            border-color: #ffc107;
        }

        .legend-color.approved {
            background-color: #cfe2ff;
            border-color: #0d6efd;
        }

        .legend-color.paid {
            background-color: #d1ecf1;
            border-color: #17a2b8;
        }

        .legend-color.completed {
            background-color: #d3d3d3;
            border-color: #6c757d;
        }

        /* ================= Month Picker Modal ================= */
        #monthPickerModal {
            display: none;
            position: fixed;
            z-index: 9999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .month-picker-content {
            background-color: white;
            margin: 10% auto;
            padding: 30px;
            border-radius: 12px;
            width: 90%;
            max-width: 400px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
        }

        .month-picker-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .year-selector {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 15px;
            margin-bottom: 20px;
        }

        .year-btn {
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 50%;
            width: 35px;
            height: 35px;
            cursor: pointer;
            transition: all 0.3s;
        }

        .year-btn:hover {
            background-color: #218838;
            transform: scale(1.1);
        }

        .month-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 10px;
            margin-bottom: 20px;
        }

        .month-item {
            padding: 15px;
            border: 2px solid #ddd;
            border-radius: 8px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: 600;
        }

        .month-item:hover {
            background-color: #28a745;
            color: white;
            border-color: #28a745;
            transform: scale(1.05);
        }

        .month-item.selected {
            background-color: #0d6efd;
            color: white;
            border-color: #0d6efd;
        }

        /* ================= Responsive ================= */
        @media (max-width: 768px) {
            .calendar-day {
                font-size: 12px;
                padding: 5px;
            }

            .day-number {
                font-size: 16px;
            }

            .day-status {
                font-size: 10px;
            }
        }
    </style>
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>
    <jsp:include page="../common/carOwner/_headerOwner.jsp"/>

    <div class="no-bottom no-top zebra" id="content">
        <div id="top"></div>

        <!-- SUBHEADER -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/14.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Lịch Thuê Xe</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- MAIN CONTENT -->
        <section id="section-cars" class="bg-gray-100">
            <div class="container">
                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-lg-3 mb30">
                        <jsp:include page="../common/carOwner/_sidebarOwner.jsp">
                            <jsp:param name="activePage" value="rental-calendar"/>
                        </jsp:include>
                    </div>

                    <!-- Main Content -->
                    <div class="col-lg-9">
                        <!-- Filter Section -->
                        <div class="filter-section">
                            <form method="get" action="${pageContext.request.contextPath}/owner/rental-calendar"
                                  style="display: flex; align-items: center; gap: 15px; flex-wrap: wrap;">

                                <!-- Car Selection -->
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <label for="carSelect">Xe:</label>
                                    <select name="carId" id="carSelect" onchange="this.form.submit()"
                                            style="min-width: 200px;">
                                        <c:if test="${empty ownerCars}">
                                            <option value="">No cars available</option>
                                        </c:if>
                                        <c:forEach var="car" items="${ownerCars}">
                                            <option value="${car.carId}" ${car.carId == selectedCarId ? 'selected' : ''}>
                                                    ${car.brand} ${car.model} - ${car.licensePlate}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- Month Navigation -->
                                <div style="display: flex; align-items: center; gap: 10px;">
                                    <label>Month:</label>

                                    <!-- Previous Button -->
                                    <button type="button" class="btn-nav" onclick="changeMonth(-1)"
                                            title="Previous Month">
                                        <i class="fa fa-chevron-left"></i>
                                    </button>

                                    <!-- Current Month Display -->
                                    <span style="font-weight: bold; font-size: 16px; min-width: 120px; text-align: center;">
                                        <c:choose>
                                            <c:when test="${selectedMonth == 1}">Tháng 1</c:when>
                                            <c:when test="${selectedMonth == 2}">Tháng 2</c:when>
                                            <c:when test="${selectedMonth == 3}">Tháng 3</c:when>
                                            <c:when test="${selectedMonth == 4}">Tháng 4</c:when>
                                            <c:when test="${selectedMonth == 5}">Tháng 5</c:when>
                                            <c:when test="${selectedMonth == 6}">Tháng 6</c:when>
                                            <c:when test="${selectedMonth == 7}">Tháng 7</c:when>
                                            <c:when test="${selectedMonth == 8}">Tháng 8</c:when>
                                            <c:when test="${selectedMonth == 9}">Tháng 9</c:when>
                                            <c:when test="${selectedMonth == 10}">Tháng 10</c:when>
                                            <c:when test="${selectedMonth == 11}">Tháng 11</c:when>
                                            <c:when test="${selectedMonth == 12}">Tháng 12</c:when>
                                        </c:choose>
                                        ${selectedYear}
                                    </span>

                                    <!-- Next Button -->
                                    <button type="button" class="btn-nav" onclick="changeMonth(1)" title="Tháng Sau">
                                        <i class="fa fa-chevron-right"></i>
                                    </button>

                                    <!-- Calendar Picker Button -->
                                    <button type="button" class="btn-calendar-picker" onclick="openMonthPicker()"
                                            title="Jump to Month">
                                        <i class="fa fa-calendar"></i>
                                    </button>

                                    <!-- Today Button -->
                                    <button type="button" class="btn-today" onclick="goToToday()" title="Hôm Nay">
                                        <i class="fa fa-calendar-day"></i> Hôm Nay
                                    </button>

                                    <!-- Hidden Month Input -->
                                    <input type="hidden" name="month" id="monthInput"
                                           value="${selectedYear}-${String.format('%02d', selectedMonth)}">
                                </div>
                            </form>
                        </div>

                        <!-- Calendar -->
                        <div class="card padding40 rounded-5 shadow-sm">
                            <c:if test="${empty ownerCars}">
                                <div class="alert alert-warning text-center">
                                    <i class="fa fa-exclamation-triangle"></i>
                                    Bạn không có xe hiện tại.Vui lòng thêm xe trước.
                                </div>
                            </c:if>

                            <c:if test="${not empty ownerCars}">
                                <div class="calendar">
                                    <!-- Calendar Header -->
                                    <div class="calendar-header">
                                        <div>Mon</div>
                                        <div>Tue</div>
                                        <div>Wed</div>
                                        <div>Thu</div>
                                        <div>Fri</div>
                                        <div>Sat</div>
                                        <div>Sun</div>
                                    </div>

                                    <!-- Calendar Body -->
                                    <div class="calendar-body">
                                        <%
                                            @SuppressWarnings("unchecked")
                                            Map<LocalDate, List<Booking>> calendarData =
                                                    (Map<LocalDate, List<Booking>>) request.getAttribute("calendarData");

                                            Integer yearObj = (Integer) request.getAttribute("selectedYear");
                                            Integer monthObj = (Integer) request.getAttribute("selectedMonth");

                                            if (yearObj != null && monthObj != null) {
                                                int year = yearObj.intValue();
                                                int month = monthObj.intValue();

                                                YearMonth yearMonth = YearMonth.of(year, month);
                                                LocalDate firstDay = yearMonth.atDay(1);
                                                int daysInMonth = yearMonth.lengthOfMonth();
                                                int startDayOfWeek = firstDay.getDayOfWeek().getValue();

                                                // Empty cells before first day
                                                for (int i = 1; i < startDayOfWeek; i++) {
                                        %>
                                        <div class="calendar-day empty"></div>
                                        <%
                                            }

                                            // Days of month
                                            for (int day = 1; day <= daysInMonth; day++) {
                                                LocalDate date = yearMonth.atDay(day);
                                                List<Booking> dayBookings = calendarData != null ? calendarData.get(date) : null;

                                                String status = "available";
                                                String statusText = "Available";

                                                if (dayBookings != null && !dayBookings.isEmpty()) {
                                                    Booking booking = dayBookings.get(0);
                                                    if (booking != null && booking.getStatus() != null && !booking.getStatus().isEmpty()) {
                                                        status = booking.getStatus().toLowerCase();
                                                        statusText = booking.getStatus();
                                                    }
                                                }

                                                String cssClass = "calendar-day " + status;
                                        %>
                                        <div class="<%= cssClass %>" data-date="<%= date %>">
                                            <div class="day-number"><%= day %></div>
                                            <div class="day-status"><%= statusText %></div>
                                            <%
                                                if (dayBookings != null && dayBookings.size() > 1) {
                                            %>
                                            <div class="multiple-bookings">
                                                +<%= dayBookings.size() - 1 %> more
                                            </div>
                                            <%
                                                }
                                            %>
                                        </div>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <div class="col-12">
                                            <div class="alert alert-warning">
                                                Unable to load calendar. Please try again.
                                            </div>
                                        </div>
                                        <%
                                            }
                                        %>
                                    </div>
                                </div>

                                <!-- Legend -->
                                <div class="legend">
                                    <div class="legend-item">
                                        <span class="legend-color available"></span> Trống
                                    </div>
                                    <div class="legend-item">
                                        <span class="legend-color pending"></span> Đang chờ duyệt
                                    </div>
                                    <div class="legend-item">
                                        <span class="legend-color approved"></span> Được Chấp Nhận
                                    </div>
                                    <div class="legend-item">
                                        <span class="legend-color paid"></span> Đã Thanh Toán
                                    </div>
                                    <div class="legend-item">
                                        <span class="legend-color completed"></span> Đã Hoàn Thành
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- Month Picker Modal -->
    <div id="monthPickerModal">
        <div class="month-picker-content">
            <div class="month-picker-header">
                <h4 style="margin: 0;">Select Month</h4>
                <span onclick="closeMonthPicker()"
                      style="font-size: 28px; cursor: pointer; color: #666;">&times;</span>
            </div>

            <!-- Year Selector -->
            <div class="year-selector">
                <button type="button" class="year-btn" onclick="changeYear(-1)">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <span id="pickerYear" style="font-size: 20px; font-weight: bold; min-width: 80px; text-align: center;">
                    ${selectedYear}
                </span>
                <button type="button" class="year-btn" onclick="changeYear(1)">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>

            <!-- Month Grid -->
            <div class="month-grid">
                <div class="month-item" onclick="selectMonth(1)">Jan</div>
                <div class="month-item" onclick="selectMonth(2)">Feb</div>
                <div class="month-item" onclick="selectMonth(3)">Mar</div>
                <div class="month-item" onclick="selectMonth(4)">Apr</div>
                <div class="month-item" onclick="selectMonth(5)">May</div>
                <div class="month-item" onclick="selectMonth(6)">Jun</div>
                <div class="month-item" onclick="selectMonth(7)">Jul</div>
                <div class="month-item" onclick="selectMonth(8)">Aug</div>
                <div class="month-item" onclick="selectMonth(9)">Sep</div>
                <div class="month-item" onclick="selectMonth(10)">Oct</div>
                <div class="month-item" onclick="selectMonth(11)">Nov</div>
                <div class="month-item" onclick="selectMonth(12)">Dec</div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        let currentPickerYear = ${selectedYear};
        let selectedPickerMonth = ${selectedMonth};

        // Change month with prev/next buttons
        function changeMonth(delta) {
            const form = document.querySelector('.filter-section form');
            const monthInput = document.getElementById('monthInput');
            const [year, month] = monthInput.value.split('-').map(Number);

            let newMonth = month + delta;
            let newYear = year;

            if (newMonth > 12) {
                newMonth = 1;
                newYear++;
            } else if (newMonth < 1) {
                newMonth = 12;
                newYear--;
            }

            monthInput.value = newYear + '-' + String(newMonth).padStart(2, '0');
            form.submit();
        }

        // Go to today
        function goToToday() {
            const form = document.querySelector('.filter-section form');
            const monthInput = document.getElementById('monthInput');
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');

            monthInput.value = year + '-' + month;
            form.submit();
        }

        // Open month picker modal
        function openMonthPicker() {
            const modal = document.getElementById('monthPickerModal');
            currentPickerYear = ${selectedYear};
            selectedPickerMonth = ${selectedMonth};

            document.getElementById('pickerYear').textContent = currentPickerYear;
            highlightSelectedMonth();

            modal.style.display = 'block';
        }

        // Close month picker modal
        function closeMonthPicker() {
            document.getElementById('monthPickerModal').style.display = 'none';
        }

        // Change year in picker
        function changeYear(delta) {
            currentPickerYear += delta;
            document.getElementById('pickerYear').textContent = currentPickerYear;
            highlightSelectedMonth();
        }

        // Select month from picker
        function selectMonth(month) {
            const form = document.querySelector('.filter-section form');
            const monthInput = document.getElementById('monthInput');

            monthInput.value = currentPickerYear + '-' + String(month).padStart(2, '0');
            closeMonthPicker();
            form.submit();
        }

        // Highlight selected month in picker
        function highlightSelectedMonth() {
            const monthItems = document.querySelectorAll('.month-item');
            monthItems.forEach((item, index) => {
                if (index + 1 === selectedPickerMonth && currentPickerYear === ${selectedYear}) {
                    item.classList.add('selected');
                } else {
                    item.classList.remove('selected');
                }
            });
        }

        // Close modal when clicking outside
        window.onclick = function (event) {
            const modal = document.getElementById('monthPickerModal');
            if (event.target == modal) {
                closeMonthPicker();
            }
        }

        // Make calendar days clickable
        document.addEventListener('DOMContentLoaded', function () {
            const calendarDays = document.querySelectorAll('.calendar-day:not(.empty)');

            calendarDays.forEach(day => {
                day.style.cursor = 'pointer';

                day.addEventListener('click', function () {
                    const date = this.getAttribute('data-date');
                    const carId = ${selectedCarId};

                    // Redirect to booking list filtered by date
                    window.location.href = '${pageContext.request.contextPath}/owner/ownerBooking?carId=' + carId + '&date=' + date;
                });
            });
        });
    </script>

    <!-- FOOTER -->
    <jsp:include page="../common/carOwner/_footer_scriptsOwner.jsp"/>
</div>
</body>
</html>