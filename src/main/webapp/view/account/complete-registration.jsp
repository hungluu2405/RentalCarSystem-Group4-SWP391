<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zxx">
<head>
    <title>Rentaly - Complete Your Account</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icon.png" type="image/gif" sizes="16x16">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/mdb.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/plugins.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
    <link id="colors" href="${pageContext.request.contextPath}/css/colors/scheme-01.css" rel="stylesheet"
          type="text/css">
</head>

<body>
<div id="wrapper">
    <div id="de-preloader"></div>

    <header class="transparent scroll-light has-topbar"></header>

    <div class="no-bottom no-top" id="content">
        <div id="top"></div>

        <!-- Header banner -->
        <section id="subheader" class="jarallax text-light">
            <img src="${pageContext.request.contextPath}/images/background/subheader.jpg" class="jarallax-img" alt="">
            <div class="center-y relative text-center">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 text-center">
                            <h1>Complete Registration</h1>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Form section -->
        <section aria-label="section">
            <div class="container">
                <div class="row">
                    <div class="col-md-10 offset-md-1">
                        <div class="padding40 rounded-3 shadow-soft" data-bgcolor="#ffffff">
                            <div class="text-center mb-3">
                                <h3>Almost there!</h3>
                                <p>Your email has been verified with Google. Please provide the remaining details and
                                    create a password for your Rentaly account.</p>
                            </div>

                            <p class="text-center text-danger fw-bold">${requestScope.error}</p>

                            <form name="completeForm" class="form-border" method="post"
                                  action="${pageContext.request.contextPath}/create-google-account">
                                <div class="row">
                                    <!-- Left column -->
                                    <div class="col-lg-6">
                                        <h5>Personal Details</h5>

                                        <div class="field-set">
                                            <label>Full Name (from Google)</label>
                                            <input type="text" name="full_name" class="form-control"
                                                   value="${sessionScope.googleUser.name}" readonly>
                                        </div>

                                        <div class="field-set">
                                            <label>Username <span class="text-danger">*</span></label>
                                            <input type="text" name="username" class="form-control"
                                                   value='${formData.username}' required>
                                        </div>

                                        <div class="field-set">
                                            <label>Phone Number <span class="text-danger">*</span></label>
                                            <input type="tel" name="phone" class="form-control"
                                                   value='${formData.phone}' required>
                                        </div>

                                        <div class="field-set">
                                            <label>Date of Birth <span class="text-danger">*</span></label>
                                            <input type="date" name="dob" class="form-control" value='${formData.dob}'
                                                   required>
                                        </div>

                                        <div class="field-set">
                                            <label>Gender <span class="text-danger">*</span></label>
                                            <select name="gender" class="form-control" required>
                                                <option value="">-- Select --</option>
                                                <option value="Male" ${formData.gender == 'Male' ? 'selected' : ''}>
                                                    Male
                                                </option>
                                                <option value="Female" ${formData.gender == 'Female' ? 'selected' : ''}>
                                                    Female
                                                </option>
                                                <option value="Other" ${formData.gender == 'Other' ? 'selected' : ''}>
                                                    Other
                                                </option>
                                            </select>
                                        </div>

                                        <div class="field-set">
                                            <label>Driver's License Number <span class="text-danger">*</span></label>
                                            <input type="text" name="driver_license_number" class="form-control"
                                                   value='${formData.driver_license_number}' required>
                                        </div>
                                    </div>

                                    <!-- Right column -->
                                    <div class="col-lg-6">
                                        <h5>Account & Address</h5>

                                        <div class="field-set">
                                            <label>Email Address (from Google)</label>
                                            <input type="email" name="email" class="form-control"
                                                   value="${sessionScope.googleUser.email}" readonly>
                                        </div>

                                        <div class="field-set">
                                            <label>Create Password <span class="text-danger">*</span></label>
                                            <input type="password" name="password" class="form-control" required>
                                        </div>

                                        <div class="field-set">
                                            <label>Re-enter Password <span class="text-danger">*</span></label>
                                            <input type="password" name="re_password" class="form-control" required>
                                        </div>

                                        <div class="field-set">
                                            <label>Role <span class="text-danger">*</span></label>
                                            <select name="role_id" class="form-control" required>
                                                <option value="">-- Select Role --</option>
                                                <option value="3" ${formData.role_id == '3' ? 'selected' : ''}>
                                                    Customer
                                                </option>
                                                <option value="2" ${formData.role_id == '2' ? 'selected' : ''}>Car
                                                    Owner
                                                </option>
                                            </select>
                                        </div>

                                        <div class="field-set">
                                            <label>Address Line <span class="text-danger">*</span></label>
                                            <input type='text' name='address_line' class="form-control"
                                                   value='${formData.address_line}' required>
                                        </div>

                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="field-set">
                                                    <label>City / Province <span class="text-danger">*</span></label>
                                                    <input type='text' name='city' class="form-control"
                                                           value='${formData.city}' required>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="field-set">
                                                    <label>Country <span class="text-danger">*</span></label>
                                                    <input type='text' name='country' class="form-control"
                                                           value='${formData.country != null ? formData.country : "Vietnam"}'
                                                           required>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="field-set">
                                            <label>Postal Code</label>
                                            <input type='text' name='postal_code' class="form-control"
                                                   value='${formData.postal_code}'>
                                        </div>
                                    </div>


                                    <div class="col-lg-12 text-center mt-4">
                                        <input type="submit" value="Create Account"
                                               class="btn-main color-2 btn-fullwidth rounded-3">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <footer class="text-light"></footer>
</div>

<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
<script src="${pageContext.request.contextPath}/js/designesia.js"></script>
</body>
</html>
