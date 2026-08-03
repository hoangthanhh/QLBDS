<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <title>REMS - Hệ Thống Quản Lý Bất Động Sản</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <link href="${pageContext.request.contextPath}/assets/customer/img/favicon.ico" rel="icon">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600&family=Inter:wght@700;800&display=swap" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/assets/customer/lib/animate/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/customer/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/assets/customer/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/customer/css/style.css" rel="stylesheet">
</head>

<body id="top">
    <div class="container-xxl bg-white p-0">

        <div id="spinner" class="show bg-white position-fixed translate-middle w-100 vh-100 top-50 start-50 d-flex align-items-center justify-content-center">
            <div class="spinner-border text-primary" style="width: 3rem; height: 3rem;" role="status">
                <span class="sr-only">Đang tải...</span>
            </div>
        </div>

        <div class="container-fluid nav-bar bg-transparent">
            <nav class="navbar navbar-expand-lg bg-white navbar-light py-0 px-4">
                <a href="${pageContext.request.contextPath}/home" class="navbar-brand d-flex align-items-center text-center">
                    <div class="icon p-2 me-2">
                        <img class="img-fluid" src="${pageContext.request.contextPath}/assets/customer/img/icon-deal.png" alt="Icon" style="width: 30px; height: 30px;">
                    </div>
                    <h1 class="m-0 text-primary">REMS</h1>
                </a>
                <button type="button" class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarCollapse">
                    <div class="navbar-nav ms-auto">
                        <a href="#top" class="nav-item nav-link active">Trang chủ</a>
                        <a href="#property-list" class="nav-item nav-link">Bất động sản</a>
                        <a href="#about-us" class="nav-item nav-link">Về chúng tôi</a>
                    </div>
                    <div class="d-flex align-items-center ms-3">
                        <c:choose>
                            <c:when test="${empty sessionScope.currentUser}">
                                <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-primary px-3 d-none d-lg-flex">Đăng nhập</a>
                                <a href="${pageContext.request.contextPath}/auth/register" class="btn btn-dark px-3 ms-2 d-none d-lg-flex">Đăng ký</a>
                            </c:when>
                            <c:otherwise>
                                <div class="nav-item dropdown">
                                    <a href="#" class="nav-link dropdown-toggle btn btn-primary text-white px-4 rounded-pill" data-bs-toggle="dropdown">
                                        <i class="fa fa-user-circle me-2"></i><c:out value="${sessionScope.currentUser.fullName}"/>
                                    </a>
                                    <div class="dropdown-menu dropdown-menu-end rounded-0 m-0">
                                        <a href="${pageContext.request.contextPath}/customer/profile" class="dropdown-item">Thông tin cá nhân</a>
                                        <a href="${pageContext.request.contextPath}/customer/transaction-history" class="dropdown-item">Lịch sử giao dịch</a>
                                        <a href="${pageContext.request.contextPath}/customer/view-history" class="dropdown-item">Lịch sử xem BĐS</a>
                                        <hr class="dropdown-divider">
                                        <a href="${pageContext.request.contextPath}/auth/logout" class="dropdown-item text-danger">Đăng xuất</a>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </nav>
        </div>
        <div class="container-fluid header bg-white p-0">
            <div class="row g-0 align-items-center flex-column-reverse flex-md-row">
                <div class="col-md-6 p-5 mt-lg-5">
                    <h1 class="display-5 animated fadeIn mb-4">Tìm Một <span class="text-primary">Ngôi Nhà Hoàn Hảo</span> Để Sống Cùng Gia Đình Bạn</h1>
                    <p class="animated fadeIn mb-4 pb-2">Hệ thống cung cấp trải nghiệm tìm kiếm, giao dịch bất động sản nhanh chóng, minh bạch và an toàn hàng đầu.</p>
                    <a href="#property-list" class="btn btn-primary py-3 px-5 me-3 animated fadeIn">Bắt đầu ngay</a>
                </div>
                <div class="col-md-6 animated fadeIn">
                    <div class="owl-carousel header-carousel">
                        <div class="owl-carousel-item">
                            <img class="img-fluid" src="${pageContext.request.contextPath}/assets/customer/img/carousel-1.jpg" alt="">
                        </div>
                        <div class="owl-carousel-item">
                            <img class="img-fluid" src="${pageContext.request.contextPath}/assets/customer/img/carousel-2.jpg" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid bg-primary mb-5 wow fadeIn" data-wow-delay="0.1s" style="padding: 35px;">
            <div class="container">
                <form action="${pageContext.request.contextPath}/property/search" method="GET">
                    <div class="row g-2">
                        <div class="col-md-10">
                            <div class="row g-2">
                                <div class="col-md-4">
                                    <input type="text" name="address" class="form-control border-0 py-3" placeholder="Nhập địa chỉ, vị trí...">
                                </div>
                                <div class="col-md-4">
                                    <select name="priceRange" class="form-select border-0 py-3">
                                        <option value="" selected>-- Chọn khoảng giá --</option>
                                        <option value="Dưới 1 Tỷ">Dưới 1 Tỷ</option>
                                        <option value="1 Tỷ - 3 Tỷ">1 Tỷ - 3 Tỷ</option>
                                        <option value="3 Tỷ - 7 Tỷ">3 Tỷ - 7 Tỷ</option>
                                        <option value="Trên 7 Tỷ">Trên 7 Tỷ</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <select name="propertyType" class="form-select border-0 py-3">
                                        <option value="" selected>-- Chọn loại hình BĐS --</option>
                                        <option value="Căn hộ">Căn hộ</option>
                                        <option value="Nhà riêng">Nhà riêng</option>
                                        <option value="Đất nền">Đất nền</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-dark border-0 w-100 py-3">Tìm Kiếm</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="container-xxl py-5" id="property-list">
            <div class="container">
                <div class="row g-0 gx-5 align-items-end">
                    <div class="col-lg-6">
                        <div class="text-start mx-auto mb-5 wow slideInLeft" data-wow-delay="0.1s">
                            <h1 class="mb-3">Danh Sách Bất Động Sản</h1>
                            <p>Khám phá bộ sưu tập những bất động sản nổi bật hàng đầu hiện nay với đầy đủ thông tin chi tiết.</p>
                        </div>
                    </div>
                </div>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane fade show p-0 active">
                        <div class="row g-4">
                            <c:choose>
                                <c:when test="${not empty realEstates}">
                                    <c:forEach var="item" items="${realEstates}">
                                        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.1s">
                                            <div class="property-item rounded overflow-hidden">
                                                <div class="position-relative overflow-hidden">
                                                    <a href="${pageContext.request.contextPath}/property/detail?id=${item.id}">
                                                        <img class="img-fluid w-100" src="${pageContext.request.contextPath}/assets/customer/img/property-1.jpg" alt="" style="height: 250px; object-fit: cover;">
                                                    </a>
                                                    <div class="bg-primary rounded text-white position-absolute start-0 top-0 m-4 py-1 px-3">
                                                        <c:out value="${item.type}"/>
                                                    </div>
                                                </div>
                                                <div class="p-4 pb-0">
                                                    <h5 class="text-primary mb-3">
                                                        <fmt:formatNumber value="${item.price}" pattern="#,###"/> VNĐ
                                                    </h5>
                                                    <a class="d-block h5 mb-2 text-truncate" href="${pageContext.request.contextPath}/property/detail?id=${item.id}">
                                                        <c:out value="${item.title}"/>
                                                    </a>
                                                    <p class="text-truncate"><i class="fa fa-map-marker-alt text-primary me-2"></i><c:out value="${item.address}"/></p>
                                                </div>
                                                <div class="d-flex border-top">
                                                    <small class="flex-fill text-center border-end py-2"><i class="fa fa-ruler-combined text-primary me-2"></i><c:out value="${item.area}"/> m²</small>
                                                    <small class="flex-fill text-center border-end py-2"><i class="fa fa-bed text-primary me-2"></i><c:out value="${item.bedrooms}"/> PN</small>
                                                    <small class="flex-fill text-center py-2"><i class="fa fa-bath text-primary me-2"></i><c:out value="${item.bathrooms}"/> WC</small>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-12 text-center py-5">
                                        <h5 class="text-muted">Hiện chưa có dữ liệu bất động sản nào trong hệ thống.</h5>
                                    </div>
                                </c:otherwise>
                            </c:choose>


                                <div class="col-12 text-center wow fadeInUp" data-wow-delay="0.1s">
                                    <a class="btn btn-primary py-3 px-5" href="${pageContext.request.contextPath}/property/search">Xem thêm bất động sản</a>
                                </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-xxl py-5" id="about-us">
            <div class="container">
                <div class="row g-5 align-items-center">
                    <div class="col-lg-6 wow fadeIn" data-wow-delay="0.1s">
                        <div class="position-relative overflow-hidden p-5 pe-0">
                            <img class="img-fluid w-100" src="${pageContext.request.contextPath}/assets/customer/img/about.jpg">
                        </div>
                    </div>
                    <div class="col-lg-6 wow fadeIn" data-wow-delay="0.5s">
                        <h1 class="mb-4">Nơi Uy Tín Tìm Đến Những Không Gian Sống Tốt Nhất</h1>
                        <p class="mb-4">Chúng tôi tự hào mang lại giá trị thực cho từng khách hàng, kết nối những giao dịch bất động sản an toàn và bền vững nhất trên thị trường hiện nay.</p>
                        <p><i class="fa fa-check text-primary me-3"></i>Thông tin bất động sản chính xác, minh bạch.</p>
                        <p><i class="fa fa-check text-primary me-3"></i>Đội ngũ hỗ trợ và xử lý giao dịch chuyên nghiệp.</p>
                        <p><i class="fa fa-check text-primary me-3"></i>Quy trình bảo mật nghiêm ngặt và tối ưu tài sản.</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-fluid bg-dark text-white-50 footer pt-5 mt-5 wow fadeIn" data-wow-delay="0.1s">
            <div class="container py-5">
                <div class="row g-5">
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Liên Hệ</h5>
                        <p class="mb-2"><i class="fa fa-map-marker-alt me-3"></i>123 Đường ABC, Hà Nội, Việt Nam</p>
                        <p class="mb-2"><i class="fa fa-phone-alt me-3"></i>+012 345 67890</p>
                        <p class="mb-2"><i class="fa fa-envelope me-3"></i>info@makaan.com</p>
                        <div class="d-flex pt-2">
                            <a class="btn btn-outline-light btn-social" href=""><i class="fab fa-twitter"></i></a>
                            <a class="btn btn-outline-light btn-social" href=""><i class="fab fa-facebook-f"></i></a>
                            <a class="btn btn-outline-light btn-social" href=""><i class="fab fa-youtube"></i></a>
                            <a class="btn btn-outline-light btn-social" href=""><i class="fab fa-linkedin-in"></i></a>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Đường Dẫn</h5>
                        <a class="btn btn-link text-white-50" href="">Giới thiệu</a>
                        <a class="btn btn-link text-white-50" href="">Liên hệ</a>
                        <a class="btn btn-link text-white-50" href="">Dịch vụ của chúng tôi</a>
                        <a class="btn btn-link text-white-50" href="">Chính sách bảo mật</a>
                        <a class="btn btn-link text-white-50" href="">Điều khoản & Điều kiện</a>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Thư Viện Ảnh</h5>
                        <div class="row g-2 pt-2">
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-1.jpg" alt="">
                            </div>
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-2.jpg" alt="">
                            </div>
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-3.jpg" alt="">
                            </div>
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-4.jpg" alt="">
                            </div>
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-5.jpg" alt="">
                            </div>
                            <div class="col-4">
                                <img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-6.jpg" alt="">
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Bản Tin</h5>
                        <p>Đăng ký nhận thông tin về các dự án bất động sản mới nhất và các ưu đãi hấp dẫn.</p>

                    </div>
                </div>
            </div>
            <div class="container">
                <div class="copyright">
                    <div class="row">
                        <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                            &copy; <a class="border-bottom" href="#">Makaan Real Estate</a>, Bảo lưu mọi quyền.
                        </div>
                        <div class="col-md-6 text-center text-md-end">
                            <div class="footer-menu">
                                <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
                                <a href="">Cookies</a>
                                <a href="">Hỗ trợ</a>
                                <a href="">Câu hỏi thường gặp</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <a href="#top" class="btn btn-lg btn-primary btn-lg-square back-to-top"><i class="bi bi-arrow-up"></i></a>
    </div>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/customer/lib/wow/wow.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/customer/lib/easing/easing.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/customer/lib/waypoints/waypoints.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/customer/lib/owlcarousel/owl.carousel.min.js"></script>

    <script src="${pageContext.request.contextPath}/assets/customer/js/main.js"></script>
</body>

</html>