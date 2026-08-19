<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="staffHeader.jsp" %>

<div class="container-fluid pt-3">
    <!-- TIÊU ĐỀ & NÚT THÊM MỚI -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h3 mb-0 text-dark fw-bold"><i class="fa-solid fa-building me-2 text-primary"></i>Quản lý Bất Động Sản (Staff)</h1>
        <button type="button" class="btn btn-success fw-bold px-3 shadow-sm" onclick="openAddModal()">
            <i class="fa-solid fa-plus me-1"></i> Thêm BĐS Mới
        </button>
    </div>

    <!-- ALERT THÔNG BÁO -->
    <c:if test="${not empty sessionScope.msg}">
        <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show shadow-sm border-0 mb-3 py-2 px-3"
             role="alert" style="border-radius: 8px;">
            <div>
                <strong>${sessionScope.msgType == 'success' ? '✅ Thành công:' : '⚠️ Thông báo:'}</strong> ${sessionScope.msg}
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="msg" scope="session"/>
        <c:remove var="msgType" scope="session"/>
    </c:if>

    <!-- FORM TÌM KIẾM NÂNG CAO -->
    <div class="card shadow-sm mb-4 border-0" style="border-radius: 12px;">
        <div class="card-body p-3">
            <form action="${pageContext.request.contextPath}/staff/bds" method="get" class="row g-2 align-items-center">
                <div class="col-md-3">
                    <input type="text" name="keyword" class="form-control rounded-pill px-3 shadow-none"
                           placeholder="Nhập địa chỉ, tiêu đề..." value="${keyword}">
                </div>
                <div class="col-md-2">
                    <select name="propertyType" class="form-select rounded-pill px-3 shadow-none">
                        <option value="">-- Tất cả loại hình --</option>
                        <option value="APARTMENT" ${propertyType == 'APARTMENT' ? 'selected' : ''}>Căn hộ (APARTMENT)</option>
                        <option value="HOUSE" ${propertyType == 'HOUSE' ? 'selected' : ''}>Nhà riêng (HOUSE)</option>
                        <option value="LAND" ${propertyType == 'LAND' ? 'selected' : ''}>Đất nền (LAND)</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select name="priceRange" class="form-select rounded-pill px-3 shadow-none">
                        <option value="">-- Tất cả khoảng giá --</option>
                        <option value="UNDER_1B" ${priceRange == 'UNDER_1B' ? 'selected' : ''}>Dưới 1 Tỷ</option>
                        <option value="1B_3B" ${priceRange == '1B_3B' ? 'selected' : ''}>1 Tỷ - 3 Tỷ</option>
                        <option value="3B_7B" ${priceRange == '3B_7B' ? 'selected' : ''}>3 Tỷ - 7 Tỷ</option>
                        <option value="OVER_7B" ${priceRange == 'OVER_7B' ? 'selected' : ''}>Trên 7 Tỷ</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select name="status" class="form-select rounded-pill px-3 shadow-none">
                        <option value="ALL" ${currentStatus == 'ALL' ? 'selected' : ''}>-- Tất cả BĐS đang hoạt động --</option>
                        <option value="AVAILABLE" ${currentStatus == 'AVAILABLE' ? 'selected' : ''}>🟢 Đang mở bán</option>
                        <option value="DEPOSITED" ${currentStatus == 'DEPOSITED' ? 'selected' : ''}>🟡 Đã nhận cọc</option>
                        <option value="SOLD" ${currentStatus == 'SOLD' ? 'selected' : ''}>🔴 Đã bán đứt</option>
                        <option value="DELETED" ${currentStatus == 'DELETED' ? 'selected' : ''}>⚪ Đã xóa (Xóa mềm)</option>
                    </select>
                </div>
                <div class="col-md-2 text-end">
                    <button type="submit" class="btn btn-primary rounded-pill px-3 shadow-sm">
                        <i class="fa-solid fa-magnifying-glass me-1"></i> Tìm kiếm
                    </button>
                    <a href="${pageContext.request.contextPath}/staff/bds"
                       class="btn btn-outline-secondary rounded-pill px-2 ms-1 shadow-sm" title="Tải lại">
                        <i class="fa-solid fa-rotate-left"></i>
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- BẢNG DANH SÁCH BĐS -->
    <div class="card shadow-sm border-0 mb-4" style="border-radius: 12px; overflow: hidden;">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0 text-center">
                    <thead class="table-light">
                    <tr>
                        <th style="width: 5%;" class="text-center">STT</th>
                        <th style="width: 8%;" class="text-center">Ảnh</th>
                        <th style="width: 22%;" class="text-center">Tiêu đề BĐS</th>
                        <th style="width: 18%;" class="text-center">Địa chỉ chi tiết</th>
                        <th style="width: 9%;" class="text-center">Loại hình</th>
                        <th style="width: 8%;" class="text-center">Diện tích</th>
                        <th style="width: 10%;" class="text-center">Giá bán</th>
                        <th style="width: 10%;" class="text-center">Trạng thái</th>
                        <th style="width: 10%;" class="text-center">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="startIndex" value="${(currentPage - 1) * (empty pageSize ? 5 : pageSize)}"/>
                    <c:forEach var="p" items="${productList}" varStatus="loop">
                        <tr>
                            <td class="fw-bold text-center">${startIndex + loop.index + 1}</td>
                            <td class="text-center">
                                <c:set var="imgPath" value="${p.thumbnail}"/>
                                <c:choose>
                                    <c:when test="${not empty imgPath && (fn:startsWith(imgPath, 'http://') || fn:startsWith(imgPath, 'https://'))}">
                                        <c:set var="finalSrc" value="${imgPath}"/>
                                    </c:when>
                                    <c:when test="${not empty imgPath && fn:startsWith(imgPath, '/')}">
                                        <c:set var="finalSrc" value="${pageContext.request.contextPath}${imgPath}"/>
                                    </c:when>
                                    <c:when test="${not empty imgPath}">
                                        <c:set var="finalSrc" value="${pageContext.request.contextPath}/${imgPath}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="finalSrc" value="${pageContext.request.contextPath}/assets/customer/img/property-1.jpg"/>
                                    </c:otherwise>
                                </c:choose>
                                <img src="${finalSrc}" alt="${p.title}" width="55" height="55" class="rounded shadow-sm"
                                     style="object-fit: cover;"
                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/customer/img/property-1.jpg';"/>
                            </td>
                            <td class="fw-bold text-dark text-start">${p.title}</td>
                            <td class="text-start text-dark fw-medium">${p.address}</td>
                            <td class="text-center">
                                <span class="badge bg-info text-dark px-2 py-1">${p.propertyType}</span>
                            </td>
                            <td class="fw-bold text-center">${p.area} m²</td>
                            <td class="text-danger fw-bold text-center">
                                <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                            </td>

                            <!-- CỘT TRẠNG THÁI -->
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${p.status == 'AVAILABLE'}">
                                        <span class="badge bg-success px-2 py-1">Đang mở bán</span>
                                    </c:when>
                                    <c:when test="${p.status == 'DEPOSITED'}">
                                        <span class="badge bg-warning text-dark px-2 py-1">Đã nhận cọc</span>
                                    </c:when>
                                    <c:when test="${p.status == 'SOLD'}">
                                        <span class="badge bg-danger px-2 py-1">Đã bán</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary px-2 py-1">Đã xóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <!-- CỘT HÀNH ĐỘNG -->
                            <td class="text-center">
                                <div class="d-flex align-items-center justify-content-center gap-1">
                                    <c:choose>
                                        <%-- 1. Khi BĐS đang AVAILABLE: Cho phép Sửa và Xóa --%>
                                        <c:when test="${p.status == 'AVAILABLE'}">
                                            <button type="button" class="btn btn-warning btn-sm text-white shadow-sm"
                                                    onclick="openEditModal(${p.id})" title="Chỉnh sửa & Xem toàn bộ ảnh"
                                                    style="border-radius: 6px; padding: 4px 8px;">
                                                <i class="fa-solid fa-pen-to-square"></i>
                                            </button>

                                            <form action="${pageContext.request.contextPath}/staff/bds" method="post"
                                                  class="d-inline m-0"
                                                  onsubmit="return confirm('Bạn có chắc chắn muốn xóa BĐS này không?\nHệ thống sẽ chuyển BĐS vào trạng thái Đã xóa (Xóa mềm).');">
                                                <input type="hidden" name="action" value="delete"/>
                                                <input type="hidden" name="id" value="${p.id}"/>
                                                <button type="submit" class="btn btn-danger btn-sm shadow-sm"
                                                        title="Xóa mềm"
                                                        style="border-radius: 6px; padding: 4px 8px;">
                                                    <i class="fa-solid fa-trash"></i>
                                                </button>
                                            </form>
                                        </c:when>

                                        <%-- 2. Khi BĐS đang DEPOSITED: Cho phép Hủy cọc / Mở bán lại --%>
                                        <c:when test="${p.status == 'DEPOSITED'}">
                                            <form action="${pageContext.request.contextPath}/staff/bds" method="post"
                                                  class="d-inline m-0"
                                                  onsubmit="return confirm('Xác nhận: Khách hàng đã hủy kèo (chấp nhận mất cọc)?\nBĐS này sẽ được đưa về trạng thái Đang Mở Bán để tiếp tục giao dịch.');">
                                                <input type="hidden" name="action" value="reopen"/>
                                                <input type="hidden" name="id" value="${p.id}"/>
                                                <button type="submit"
                                                        class="btn btn-info btn-sm text-white shadow-sm fw-bold"
                                                        title="Hủy cọc -> Mở bán lại"
                                                        style="border-radius: 6px; padding: 4px 8px; font-size: 12px;">
                                                    <i class="fa-solid fa-rotate-left me-1"></i> Mở lại
                                                </button>
                                            </form>
                                        </c:when>

                                        <%-- 3. Khi BĐS đã bị DELETED: Cho phép Khôi phục --%>
                                        <c:when test="${p.status == 'DELETED'}">
                                            <form action="${pageContext.request.contextPath}/staff/bds" method="post" class="d-inline m-0"
                                                  onsubmit="return confirm('Bạn có chắc chắn muốn khôi phục và mở bán lại BĐS này không?');">
                                                <input type="hidden" name="action" value="restore"/>
                                                <input type="hidden" name="id" value="${p.id}"/>
                                                <button type="submit" class="btn btn-success btn-sm shadow-sm" title="Khôi phục BĐS"
                                                        style="border-radius: 6px; padding: 4px 8px;">
                                                    <i class="fa-solid fa-trash-arrow-up"></i>
                                                </button>
                                            </form>
                                        </c:when>

                                        <%-- 4. Các trường hợp còn lại (SOLD): Khóa thao tác --%>
                                        <c:otherwise>
                                            <span class="text-muted small" title="Đã khóa thao tác"><i class="fa-solid fa-lock"></i> Đã khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productList}">
                        <tr>
                            <td colspan="9" class="text-center text-muted py-4">Không tìm thấy Bất Động Sản nào phù hợp.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- PHÂN TRANG -->
    <c:set var="maxPage" value="${totalPage > 0 ? totalPage : 1}"/>
    <c:set var="kwParam"
           value="${not empty keyword ? '&keyword='.concat(keyword) : ''}${not empty propertyType ? '&propertyType='.concat(propertyType) : ''}${not empty priceRange ? '&priceRange='.concat(priceRange) : ''}${not empty currentStatus ? '&status='.concat(currentStatus) : ''}"/>
    <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-end mb-0">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/staff/bds?page=${currentPage - 1}${kwParam}">Trước</a>
            </li>
            <c:forEach begin="1" end="${maxPage}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/staff/bds?page=${i}${kwParam}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage >= maxPage ? 'disabled' : ''}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/staff/bds?page=${currentPage + 1}${kwParam}">Sau</a>
            </li>
        </ul>
    </nav>
</div>

<!-- MODAL THÊM / SỬA / XEM TOÀN BỘ ẢNH -->
<div class="modal fade" id="bdsModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content border-0 shadow-lg" style="border-radius: 16px; overflow: hidden;">
            <div id="modalHeader" class="modal-header text-white px-4 py-3 bg-success">
                <h5 class="modal-title fw-bold" id="modalTitle"><i class="fa-solid fa-house-medical me-2"></i>Thêm Bất Động Sản Mới</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form id="bdsForm" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" id="formAction" value="create"/>
                <input type="hidden" name="id" id="bdsId" value=""/>

                <div class="modal-body px-4 pt-4">
                    <div id="modalAlert" class="alert d-none py-2 px-3 mb-3 alert-danger"></div>
                    <div class="row g-3">
                        <div class="col-md-12">
                            <label class="form-label fw-bold">Tiêu đề BĐS <span class="text-danger">*</span></label>
                            <input type="text" name="title" id="bdsTitleInput" class="form-control"
                                   placeholder="Nhập tiêu đề BĐS..." required>
                        </div>
                        <div class="col-md-12">
                            <label class="form-label fw-bold">Địa chỉ chi tiết <span class="text-danger">*</span></label>
                            <input type="text" name="address" id="bdsAddressInput" class="form-control"
                                   placeholder="Nhập số nhà, đường, phường/xã, quận/huyện..." required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Loại hình BĐS <span class="text-danger">*</span></label>
                            <select name="propertyType" id="bdsTypeInput" class="form-select" required>
                                <option value="APARTMENT">Căn hộ (APARTMENT)</option>
                                <option value="HOUSE">Nhà riêng (HOUSE)</option>
                                <option value="LAND">Đất nền (LAND)</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Diện tích (m²) <span class="text-danger">*</span></label>
                            <input type="number" step="0.01" name="area" id="bdsAreaInput" class="form-control"
                                   placeholder="Ví dụ: 85.5" min="0.1" required>
                        </div>

                        <!-- Ô GIÁ BÁN -->
                        <div class="col-md-4">
                            <label class="form-label fw-bold">Giá bán (VNĐ) <span class="text-danger">*</span></label>
                            <input type="hidden" name="price" id="bdsPriceInputReal">
                            <input type="text" id="bdsPriceInputDisplay" class="form-control"
                                   placeholder="Ví dụ: 2.500.000.000" required oninput="handlePriceInput(this)">
                            <small id="priceTextPreview" class="fw-bold text-success d-block mt-1" style="min-height: 20px;"></small>
                        </div>

                        <div class="col-md-12">
                            <label class="form-label fw-bold">Mô tả chi tiết</label>
                            <textarea name="description" id="bdsDescInput" class="form-control" rows="3"
                                      placeholder="Nhập thông tin pháp lý, tiện ích nội ngoại khu..."></textarea>
                        </div>

                        <!-- XEM TOÀN BỘ ẢNH KÈM NÚT XÓA TỪNG ẢNH -->
                        <div class="col-md-12 d-none" id="currentImagesArea">
                            <label class="form-label fw-bold">Ảnh hiện tại của BĐS (Xem toàn bộ):</label>
                            <div class="d-flex flex-wrap gap-2 p-2 border rounded bg-light" id="currentImagesGallery"></div>
                        </div>

                        <!-- UPLOAD ẢNH (TỐI ĐA 10 ẢNH: 1 CHÍNH + 9 PHỤ) -->
                        <div class="col-md-12">
                            <label class="form-label fw-bold" id="imageLabel">Tải lên ảnh minh họa (Tối đa 10 ảnh: 1 ảnh chính + 9 ảnh phụ) <span class="text-danger" id="imageRequiredNote">*</span></label>
                            <input type="file" name="images" id="bdsImagesInput" class="form-control" multiple
                                   accept="image/*" onchange="handleImageSelection(this)">
                            <small class="text-muted d-block mt-1" id="imageHelpText">💡 Ảnh đầu tiên bạn chọn sẽ là <strong>Ảnh đại diện chính</strong>, các ảnh còn lại là <strong>Ảnh phụ</strong> (Tối đa 10 ảnh).</small>

                            <!-- KHUNG XEM TRƯỚC ẢNH SẮP TẢI LÊN (KÈM NÚT XÓA TỪNG ẢNH TRƯỚC KHI LƯU) -->
                            <div id="newImagesPreviewArea" class="d-none mt-2 p-2 border rounded bg-white">
                                <small class="fw-bold text-primary d-block mb-1">Ảnh đã chọn tải lên (Bấm dấu x để bỏ ảnh chọn nhầm):</small>
                                <div class="d-flex flex-wrap gap-2" id="newImagesPreviewList"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0 px-4 pb-4">
                    <button type="button" class="btn btn-light border" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="submit" id="btnSubmitForm" class="btn btn-success fw-bold px-4">Lưu BĐS</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    var contextPath = "${pageContext.request.contextPath}";
    var selectedFilesDT = new DataTransfer();

    function handleImageSelection(input) {
        if (!input.files || input.files.length === 0) return;

        // ĐÃ NÂNG CẤP: Chặn khi chọn quá 10 ảnh
        if (input.files.length > 10) {
            alert('⚠️ Bạn chỉ được chọn tối đa 10 ảnh (1 ảnh chính và tối đa 9 ảnh phụ)! Vui lòng chọn lại.');
            input.value = '';
            selectedFilesDT = new DataTransfer();
            renderNewImagesPreview();
            return;
        }

        selectedFilesDT = new DataTransfer();
        Array.from(input.files).forEach(function (file) {
            selectedFilesDT.items.add(file);
        });

        renderNewImagesPreview();
    }

    function renderNewImagesPreview() {
        var input = document.getElementById('bdsImagesInput');
        input.files = selectedFilesDT.files;

        var previewList = $('#newImagesPreviewList');
        var previewArea = $('#newImagesPreviewArea');
        previewList.html('');

        if (selectedFilesDT.files.length === 0) {
            previewArea.addClass('d-none');
            return;
        }

        previewArea.removeClass('d-none');

        Array.from(selectedFilesDT.files).forEach(function (file, index) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var isMain = (index === 0);
                var badgeHtml = isMain
                    ? '<span class="badge bg-primary position-absolute top-0 start-0 m-1" style="font-size: 10px;">Ảnh chính</span>'
                    : '<span class="badge bg-secondary position-absolute top-0 start-0 m-1" style="font-size: 10px;">Ảnh phụ ' + index + '</span>';

                var borderClass = isMain ? 'border-primary border-2' : 'border';

                var html = '<div class="position-relative d-inline-block m-1" id="preview-box-' + index + '">' +
                    '  <img src="' + e.target.result + '" class="rounded ' + borderClass + ' shadow-sm" style="width: 80px; height: 80px; object-fit: cover;"/>' +
                    badgeHtml +
                    '  <button type="button" class="btn btn-danger btn-sm rounded-circle position-absolute top-0 end-0 p-0 d-flex align-items-center justify-content-center shadow" ' +
                    '          style="width: 22px; height: 22px; transform: translate(30%, -30%); cursor: pointer;" ' +
                    '          onclick="removeSelectedFile(' + index + ')" title="Bỏ ảnh này">' +
                    '    <i class="fa-solid fa-xmark" style="font-size: 11px;"></i>' +
                    '  </button>' +
                    '</div>';
                previewList.append(html);
            };
            reader.readAsDataURL(file);
        });
    }

    function removeSelectedFile(index) {
        var newDT = new DataTransfer();
        Array.from(selectedFilesDT.files).forEach(function (file, idx) {
            if (idx !== index) {
                newDT.items.add(file);
            }
        });
        selectedFilesDT = newDT;
        renderNewImagesPreview();
    }

    function handlePriceInput(inputElem) {
        var rawValue = inputElem.value.replace(/\D/g, '');
        if (!rawValue) {
            inputElem.value = '';
            document.getElementById('bdsPriceInputReal').value = '';
            document.getElementById('priceTextPreview').innerText = '';
            return;
        }
        document.getElementById('bdsPriceInputReal').value = rawValue;
        inputElem.value = Number(rawValue).toLocaleString('vi-VN');
        formatPriceText(rawValue);
    }

    function formatPriceText(val) {
        var preview = document.getElementById('priceTextPreview');
        if (!val || isNaN(val) || val <= 0) {
            preview.innerText = '';
            return;
        }
        var num = parseFloat(val);
        var ty = Math.floor(num / 1000000000);
        var meo = Math.floor((num % 1000000000) / 1000000);
        var str = '👉 Bằng chữ: ';
        if (ty > 0) str += ty + ' tỷ ';
        if (meo > 0) str += meo + ' triệu ';
        if (ty === 0 && meo === 0) str += Number(num).toLocaleString('vi-VN') + ' VNĐ';
        else str += 'VNĐ';
        preview.innerText = str;
    }

    function openAddModal() {
        $('#formAction').val('create');
        $('#bdsId').val('');
        $('#bdsForm')[0].reset();

        selectedFilesDT = new DataTransfer();
        renderNewImagesPreview();

        document.getElementById('bdsPriceInputReal').value = '';
        document.getElementById('bdsPriceInputDisplay').value = '';
        document.getElementById('priceTextPreview').innerText = '';

        $('#modalHeader').removeClass('bg-warning').addClass('bg-success');
        $('#modalTitle').html('<i class="fa-solid fa-house-medical me-2"></i>Thêm Bất Động Sản Mới');
        $('#btnSubmitForm').removeClass('btn-warning text-white').addClass('btn-success').text('Lưu BĐS');
        $('#currentImagesArea').addClass('d-none');
        $('#currentImagesGallery').html('');

        $('#imageRequiredNote').show();
        $('#bdsImagesInput').prop('required', true);
        $('#imageHelpText').html('💡 Ảnh đầu tiên bạn chọn sẽ là <strong>Ảnh đại diện chính</strong>, các ảnh còn lại là <strong>Ảnh phụ</strong> (Tối đa 10 ảnh).');
        $('#modalAlert').addClass('d-none').html('');

        var modalElement = document.getElementById('bdsModal');
        var modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modalInstance.show();
    }

    function openEditModal(id) {
        $.ajax({
            url: contextPath + '/staff/bds',
            type: 'GET',
            data: {action: 'get-detail', id: id},
            dataType: 'json',
            success: function (data) {
                $('#formAction').val('update');
                $('#bdsId').val(data.id);
                $('#bdsTitleInput').val(data.title);
                $('#bdsAddressInput').val(data.address);
                $('#bdsTypeInput').val(data.propertyType);
                $('#bdsAreaInput').val(data.area);
                $('#bdsDescInput').val(data.description);
                $('#bdsImagesInput').val('');

                selectedFilesDT = new DataTransfer();
                renderNewImagesPreview();

                if (data.price) {
                    document.getElementById('bdsPriceInputReal').value = data.price;
                    document.getElementById('bdsPriceInputDisplay').value = Number(data.price).toLocaleString('vi-VN');
                    formatPriceText(data.price);
                } else {
                    document.getElementById('bdsPriceInputReal').value = '';
                    document.getElementById('bdsPriceInputDisplay').value = '';
                    document.getElementById('priceTextPreview').innerText = '';
                }

                if (data.images && data.images.length > 0) {
                    var galleryHtml = '';
                    for (var i = 0; i < data.images.length; i++) {
                        var imgObj = data.images[i];
                        var imgSrc = typeof imgObj === 'object' ? imgObj.path : imgObj;
                        var imgId = typeof imgObj === 'object' ? imgObj.id : null;

                        if (imgSrc && !imgSrc.startsWith('http://') && !imgSrc.startsWith('https://')) {
                            imgSrc = contextPath + '/' + (imgSrc.startsWith('/') ? imgSrc.substring(1) : imgSrc);
                        }

                        galleryHtml += '<div class="position-relative d-inline-block m-1" id="img-box-' + imgId + '">' +
                            '  <img src="' + imgSrc + '" class="rounded border shadow-sm" style="width: 80px; height: 80px; object-fit: cover;" onerror="this.src=\'' + contextPath + '/assets/customer/img/property-1.jpg\'"/>';

                        if (imgId) {
                            galleryHtml += '  <button type="button" class="btn btn-danger btn-sm rounded-circle position-absolute top-0 end-0 p-0 d-flex align-items-center justify-content-center shadow" ' +
                                '          style="width: 22px; height: 22px; transform: translate(30%, -30%); cursor: pointer;" ' +
                                '          onclick="deleteSingleImage(' + imgId + ')" title="Xóa ảnh này">' +
                                '    <i class="fa-solid fa-xmark" style="font-size: 11px;"></i>' +
                                '  </button>';
                        }
                        galleryHtml += '</div>';
                    }
                    $('#currentImagesGallery').html(galleryHtml);
                    $('#currentImagesArea').removeClass('d-none');
                } else {
                    $('#currentImagesArea').addClass('d-none');
                }

                $('#modalHeader').removeClass('bg-success').addClass('bg-warning');
                $('#modalTitle').html('<i class="fa-solid fa-pen-to-square me-2"></i>Chỉnh Sửa BĐS #' + data.id);
                $('#btnSubmitForm').removeClass('btn-success').addClass('btn-warning text-white').text('Lưu Thay Đổi');
                $('#imageRequiredNote').hide();
                $('#bdsImagesInput').prop('required', false);
                $('#imageHelpText').html('💡 Chọn tối đa 10 ảnh mới nếu muốn bổ sung. Để trống nếu muốn giữ nguyên ảnh cũ.');
                $('#modalAlert').addClass('d-none').html('');

                var modalElement = document.getElementById('bdsModal');
                var modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
                modalInstance.show();
            },
            error: function () {
                alert('Không thể lấy chi tiết BĐS #' + id + '. Vui lòng kiểm tra lại!');
            }
        });
    }

    function deleteSingleImage(imageId) {
        if (!confirm('Bạn có chắc chắn muốn xóa ảnh này không?')) return;
        $.ajax({
            url: contextPath + '/staff/bds',
            type: 'POST',
            data: {action: 'delete-image', imageId: imageId},
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    $('#img-box-' + imageId).fadeOut(300, function () {
                        $(this).remove();
                        if ($('#currentImagesGallery').children(':visible').length === 0) {
                            $('#currentImagesArea').addClass('d-none');
                        }
                    });
                } else {
                    alert('Không thể xóa ảnh. Vui lòng thử lại!');
                }
            },
            error: function () {
                alert('Lỗi kết nối máy chủ khi xóa ảnh!');
            }
        });
    }

    $(document).ready(function () {
        $('#bdsForm').on('submit', function (e) {
            e.preventDefault();
            var formData = new FormData(this);
            $('#btnSubmitForm').prop('disabled', true);

            $.ajax({
                url: contextPath + '/staff/bds',
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (res) {
                    $('#btnSubmitForm').prop('disabled', false);
                    if (res.success) {
                        location.reload();
                    } else {
                        var html = '<ul class="mb-0 text-start">';
                        for (var i = 0; i < res.errors.length; i++) {
                            html += '<li>' + res.errors[i] + '</li>';
                        }
                        html += '</ul>';
                        $('#modalAlert').removeClass('d-none').html(html);
                    }
                },
                error: function () {
                    $('#btnSubmitForm').prop('disabled', false);
                    alert('Lỗi kết nối máy chủ khi tải lên ảnh!');
                }
            });
        });
    });
</script>

<%@ include file="staffFooter.jsp" %>