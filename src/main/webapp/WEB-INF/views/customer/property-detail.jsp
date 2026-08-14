<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <title><c:out value="${property.title}"/> - REMS Real Estate</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <link href="${pageContext.request.contextPath}/assets/customer/img/favicon.ico" rel="icon">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600&family=Inter:wght@700;800&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/customer/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Inter', 'Heebo', sans-serif;
        }
        .main-gallery-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05); background: #ffffff; overflow: hidden; }
        .feature-box { background: #ffffff; border-radius: 20px; padding: 30px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04); border: none; }
        .price-tag { color: #00B98E; font-size: 2.2rem; font-weight: 800; }
        .thumb-img { border: 2px solid transparent; opacity: 0.65; transition: all 0.25s ease; }
        .thumb-img:hover { opacity: 1; transform: translateY(-2px); }
        .active-thumb { border-color: #00B98E !important; opacity: 1 !important; }
        .btn-deposit { background-color: #ff9800; color: #ffffff; font-weight: 700; border-radius: 12px; padding: 14px 24px; border: none; transition: all 0.2s ease; }
        .btn-deposit:hover { background-color: #e68a00; color: #ffffff; transform: translateY(-1px); }
        .btn-buy { background-color: #00B98E; color: #ffffff; font-weight: 700; border-radius: 12px; padding: 14px 24px; border: none; transition: all 0.2s ease; }
        .btn-buy:hover { background-color: #009673; color: #ffffff; transform: translateY(-1px); }
        .btn-back-home { background-color: #ffffff; color: #2b303a; border: 1px solid #e0e0e0; border-radius: 30px; padding: 8px 22px; font-weight: 600; text-decoration: none; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04); transition: all 0.2s ease; }
        .btn-back-home:hover { background-color: #00B98E; color: #ffffff; border-color: #00B98E; }
    </style>
</head>

<body>

    <div class="container py-4">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <a href="${pageContext.request.contextPath}/home" class="btn-back-home">
                <i class="bi bi-arrow-left me-2"></i> Quay lại trang chủ
            </a>
            <span class="text-muted fw-bold"><i class="bi bi-building me-1 text-primary"></i> CHI TIẾT BẤT ĐỘNG SẢN</span>
        </div>

        <c:if test="${not empty sessionScope.txSuccess}">
            <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4 p-3 rounded-3" role="alert">
                <i class="bi bi-check-circle-fill me-2 fs-5"></i> ${sessionScope.txSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("txSuccess"); %>
        </c:if>
        <c:if test="${not empty sessionScope.txError}">
            <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm mb-4 p-3 rounded-3" role="alert">
                <i class="bi bi-exclamation-octagon-fill me-2 fs-5"></i> ${sessionScope.txError}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <% session.removeAttribute("txError"); %>
        </c:if>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="main-gallery-card mb-4">
                    <div class="position-relative bg-dark" style="height: 460px;">
                        <c:choose>
                            <c:when test="${not empty property.imageUrls}">
                                <img id="mainImage" src="${pageContext.request.contextPath}/${property.imageUrls[0]}"
                                     class="w-100 h-100 object-fit-cover" alt="Main Property Image">
                            </c:when>
                            <c:otherwise>
                                <img id="mainImage" src="${pageContext.request.contextPath}/assets/customer/img/property-1.jpg"
                                     class="w-100 h-100 object-fit-cover" alt="Default Property Image">
                            </c:otherwise>
                        </c:choose>

                        <div class="position-absolute bottom-0 end-0 m-3 px-3 py-1.5 bg-dark bg-opacity-75 text-white rounded-pill small fw-bold">
                            <i class="bi bi-images me-1"></i> <span id="currentImgIndex">1</span>/${not empty property.imageUrls ? property.imageUrls.size() : 1} Ảnh
                        </div>
                    </div>

                    <c:if test="${not empty property.imageUrls && property.imageUrls.size() > 1}">
                        <div class="p-3 bg-light border-top">
                            <div class="d-flex gap-2 overflow-auto py-1" style="scrollbar-width: thin;">
                                <c:forEach var="imgUrl" items="${property.imageUrls}" varStatus="status">
                                    <img src="${pageContext.request.contextPath}/${imgUrl}"
                                         class="thumb-img rounded-3 cursor-pointer ${status.first ? 'active-thumb' : ''}"
                                         style="width: 90px; height: 65px; object-fit: cover;"
                                         onclick="changeMainImage(this, '${pageContext.request.contextPath}/${imgUrl}', ${status.index + 1})"
                                         alt="Thumbnail ${status.index + 1}">
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="feature-box mb-4">
                    <h2 class="fw-bold text-dark mb-2"><c:out value="${property.title}"/></h2>
                    <p class="text-muted"><i class="fa fa-map-marker-alt text-success me-2"></i><c:out value="${property.address}"/></p>
                    <hr class="my-4">
                    <div class="row text-center py-2">
                        <div class="col-4 border-end">
                            <small class="text-muted d-block mb-1">Diện tích</small>
                            <strong class="fs-5 text-dark"><c:out value="${property.area}"/> m²</strong>
                        </div>
                        <div class="col-4 border-end">
                            <small class="text-muted d-block mb-1">Loại hình</small>
                            <strong class="fs-5 text-dark"><c:out value="${property.propertyType}"/></strong>
                        </div>
                        <div class="col-4">
                            <small class="text-muted d-block mb-1">Trạng thái</small>
                            <strong class="fs-5 text-success"><c:out value="${property.status}"/></strong>
                        </div>
                    </div>
                </div>

                <div class="feature-box">
                    <h4 class="fw-bold text-dark mb-3">Mô tả chi tiết</h4>
                    <p class="text-secondary mb-0" style="line-height: 1.8; font-size: 0.95rem;">
                        <c:out value="${property.description}" default="Chưa có thông tin mô tả chi tiết cho bất động sản này."/>
                    </p>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="feature-box sticky-top" style="top: 30px;">
                    <small class="text-muted fw-bold d-block mb-1">MỨC GIÁ NIÊM YẾT</small>
                    <div class="price-tag mb-3">
                        <fmt:formatNumber value="${property.price}" pattern="#,###"/> VNĐ
                    </div>

                    <div class="p-3 bg-light rounded-3 mb-4 border">
                        <small class="text-muted d-block mb-1"><i class="bi bi-shield-check text-success me-1"></i> Tiền đặt cọc giữ chỗ (10%):</small>
                        <strong class="text-dark fs-6"><fmt:formatNumber value="${property.price * 0.1}" pattern="#,###"/> VNĐ</strong>
                    </div>

                    <c:if test="${not empty sessionScope.currentUser}">
                        <c:if test="${!sessionScope.currentUser.isVerified}">
                            <div class="alert alert-warning border-0 shadow-sm d-flex align-items-center small py-2 mb-4" role="alert">
                                <i class="fas fa-exclamation-triangle me-2 text-warning fs-5"></i>
                                <div>
                                    Vui lòng <a href="${pageContext.request.contextPath}/customer/profile" class="alert-link text-decoration-underline fw-bold">xác thực tài khoản</a> để thực hiện giao dịch đặt cọc hoặc mua!
                                </div>
                            </div>
                        </c:if>
                    </c:if>

                    <!-- TÌM ĐẾN KHU VỰC HIỂN THỊ NÚT ĐẶT CỌC / MUA NGAY VÀ SỬA THÀNH NHƯ SAU -->
                    <c:choose>
                        <c:when test="${hasPendingTx}">
                            <!-- Nếu đang có giao dịch Pending: Hiển thị NÚT HỦY -->
                            <div class="alert alert-warning mb-3">
                                <i class="fa-solid fa-clock me-2"></i> Bạn đang có 1 yêu cầu chờ xác nhận.
                            </div>
                            <form action="${pageContext.request.contextPath}/customer/property/transaction/cancel" method="post">
                                <input type="hidden" name="propertyId" value="${property.id}">
                                <button type="submit" class="btn btn-danger w-100 fw-bold py-2">
                                    <i class="fa-solid fa-times-circle me-1"></i> Hủy yêu cầu đang chờ
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <!-- Nếu KHÔNG có giao dịch Pending: Hiển thị 2 nút Đặt cọc / Mua bình thường -->
                            <form action="${pageContext.request.contextPath}/customer/property/transaction" method="post" class="mb-2">
                                <input type="hidden" name="propertyId" value="${property.id}">
                                <input type="hidden" name="type" value="DEPOSIT">
                                <button type="submit" class="btn btn-warning w-100 fw-bold py-2 text-white">
                                    <i class="fa-solid me-1"></i> Đặt cọc giữ chỗ
                                </button>
                            </form>

                            <form action="${pageContext.request.contextPath}/customer/property/transaction" method="post">
                                <input type="hidden" name="propertyId" value="${property.id}">
                                <input type="hidden" name="type" value="BUY">
                                <button type="submit" class="btn btn-success w-100 fw-bold py-2">
                                    <i class="fa-solid fa-cart-shopping me-1"></i> Mua ngay
                                </button>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <div class="mt-4 pt-3 border-top text-center text-muted small">
                        <i class="bi bi-headset me-1 text-primary"></i> Hỗ trợ tư vấn 24/7: <strong>0123 456 789</strong>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        function changeMainImage(element, imageSrc, index) {
            document.getElementById('mainImage').src = imageSrc;
            document.getElementById('currentImgIndex').innerText = index;

            var thumbs = document.querySelectorAll('.thumb-img');
            thumbs.forEach(function(thumb) {
                thumb.classList.remove('active-thumb');
            });
            element.classList.add('active-thumb');
        }
    </script>
</body>
</html>