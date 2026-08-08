<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <title>REMS - Hệ Thống Quản Lý Bất Động Sản</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

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
                        <a href="#top" class="nav-item nav-link scroll-link active">Trang chủ</a>
                        <a href="#property-list" class="nav-item nav-link scroll-link">Bất động sản</a>
                        <a href="#about-us" class="nav-item nav-link scroll-link">Về chúng tôi</a>
                    </div>
                    <div class="d-flex align-items-center ms-3">
                        <c:choose>
                            <c:when test="${empty sessionScope.currentUser}">
                                <a href="${pageContext.request.contextPath}/acc/login" class="btn btn-primary px-3 d-none d-lg-flex">Đăng nhập</a>
                                <a href="${pageContext.request.contextPath}/acc/register" class="btn btn-dark px-3 ms-2 d-none d-lg-flex">Đăng ký</a>
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
                <form action="${pageContext.request.contextPath}/home" method="GET" id="searchForm">
                    <div class="row g-2">
                        <div class="col-md-10">
                            <div class="row g-2">
                                <div class="col-md-4">
                                    <input type="text" name="address" class="form-control border-0 py-3"
                                           placeholder="Nhập địa chỉ, vị trí, tên BĐS..."
                                           value="${param.address}"> </div>
                                <div class="col-md-4">
                                    <select name="priceRange" class="form-select border-0 py-3">
                                        <option value="">-- Chọn khoảng giá --</option>
                                        <option value="UNDER_1B" <c:if test="${param.priceRange == 'UNDER_1B'}">selected</c:if>>Dưới 1 Tỷ</option>
                                        <option value="1B_3B" <c:if test="${param.priceRange == '1B_3B'}">selected</c:if>>1 Tỷ - 3 Tỷ</option>
                                        <option value="3B_7B" <c:if test="${param.priceRange == '3B_7B'}">selected</c:if>>3 Tỷ - 7 Tỷ</option>
                                        <option value="OVER_7B" <c:if test="${param.priceRange == 'OVER_7B'}">selected</c:if>>Trên 7 Tỷ</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <select name="propertyType" class="form-select border-0 py-3">
                                        <option value="">-- Chọn loại hình BĐS --</option>
                                        <option value="APARTMENT" <c:if test="${param.propertyType == 'APARTMENT'}">selected</c:if>>Căn hộ</option>
                                        <option value="HOUSE" <c:if test="${param.propertyType == 'HOUSE'}">selected</c:if>>Nhà riêng</option>
                                        <option value="LAND" <c:if test="${param.propertyType == 'LAND'}">selected</c:if>>Đất nền</option>
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
                        <div id="property-display-container">
                            <jsp:include page="property-grid.jsp" />
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
                        <h5 class="text-primary mb-3">Về Chúng Tôi</h5>
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
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Đường Dẫn</h5>
                        <a class="btn btn-link text-white-50" href="">Giới thiệu</a>
                        <a class="btn btn-link text-white-50" href="">Liên hệ</a>
                        <a class="btn btn-link text-white-50" href="">Dịch vụ của chúng tôi</a>
                        <a class="btn btn-link text-white-50" href="">Chính sách bảo mật</a>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Thư Viện Ảnh</h5>
                        <div class="row g-2 pt-2">
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-1.jpg" alt=""></div>
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-2.jpg" alt=""></div>
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-3.jpg" alt=""></div>
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-4.jpg" alt=""></div>
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-5.jpg" alt=""></div>
                            <div class="col-4"><img class="img-fluid rounded bg-light p-1" src="${pageContext.request.contextPath}/assets/customer/img/property-6.jpg" alt=""></div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6">
                        <h5 class="text-white mb-4">Bản Tin</h5>
                        <p>Đăng ký nhận thông tin về các dự án bất động sản mới nhất và ưu đãi hấp dẫn.</p>
                    </div>
                </div>
            </div>
            <div class="container">
                <div class="copyright">
                    <div class="row">
                        <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                            &copy; <a class="border-bottom" href="#">Makaan Real Estate</a>, Bảo lưu mọi quyền.
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

    <script>
    $(document).ready(function() {
        // Tự động cuộn xuống danh sách khi thực hiện tìm kiếm bằng Form
        if(window.location.search.indexOf('priceRange=') > -1) {
            $('html, body').animate({
                scrollTop: $("#property-list").offset().top - 80
            }, 500);
        }

        $(document).on('click', '.ajax-page', function(e) {
            e.preventDefault();

            var page = $(this).data('page');
            var contextPath = "${pageContext.request.contextPath}";

            // Lấy thêm dữ liệu từ bộ lọc hiện tại để phân trang đúng
            var address = $('input[name="address"]').val();
            var priceRange = $('select[name="priceRange"]').val();
            var propertyType = $('select[name="propertyType"]').val();

            $.ajax({
                url: contextPath + "/home",
                type: "GET",
                data: {
                    page: page,
                    address: address,
                    priceRange: priceRange,
                    propertyType: propertyType
                },
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                },
                success: function(data) {
                    $('#property-display-container').html(data);

                    // Cuộn nhẹ lên đầu khu vực BĐS
                    $('html, body').animate({
                        scrollTop: $("#property-list").offset().top - 80
                    }, 200);
                },
                error: function(xhr) {
                    console.log("Lỗi tải trang AJAX:", xhr);
                }
            });
        });
    });
    </script>

    <script>
        $(document).ready(function () {
            // 1. Xử lý khi Click vào Menu
            $('.scroll-link').on('click', function (e) {
                // Xóa màu xanh ở tất cả các thẻ
                $('.scroll-link').removeClass('active');
                // Thêm màu xanh vào thẻ vừa click
                $(this).addClass('active');
            });

            // 2. Xử lý khi Cuộn chuột (ScrollSpy)
            $(window).on('scroll', function () {
                var scrollPos = $(document).scrollTop() + 100; // Cộng thêm 100px bù cho độ cao của thanh menu dính cố định

                $('.scroll-link').each(function () {
                    var currLink = $(this);
                    var targetId = currLink.attr("href"); // Lấy id (VD: #property-list)

                    // Bỏ qua nếu link không bắt đầu bằng # (chuyển trang)
                    if (targetId.indexOf("#") !== 0) return;

                    var refElement = $(targetId);

                    if (refElement.length) {
                        // Nếu vị trí cuộn đang nằm trong vùng của thẻ DIV đó
                        if (refElement.position().top <= scrollPos && refElement.position().top + refElement.height() > scrollPos) {
                            $('.scroll-link').removeClass("active"); // Xóa hết màu xanh
                            currLink.addClass("active"); // Gắn màu xanh cho menu tương ứng
                        }
                    }
                });
            });
        });
        </script>
</body>

</html>