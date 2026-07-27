<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Bảng điều khiển Nhân viên - REMS" scope="request"/>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4 bg-white p-4 rounded-4 shadow-sm border-start border-4 border-primary">
        <div>
            <h3 class="fw-bold mb-1 text-dark">Quản lý Kho Bất Động Sản</h3>
            <p class="text-muted mb-0 small">Thêm, sửa, hoặc gỡ bỏ thông tin hiển thị trên hệ thống.</p>
        </div>
        <button class="btn btn-rems-primary px-4 py-2.5 rounded-3 fw-bold shadow-sm" data-bs-toggle="modal" data-bs-target="#modalAddProperty">
            <i class="fa-solid fa-plus me-2"></i>Thêm tài sản mới
        </button>
    </div>

    <div class="card border-0 shadow-sm rounded-4 overflow-hidden bg-white">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0 text-dark">
                <thead class="bg-light fw-bold text-secondary">
                    <tr>
                        <th class="py-3 ps-4" style="width: 80px;">Mã</th>
                        <th class="py-3">Tên tài sản / Địa chỉ</th>
                        <th class="py-3">Loại hình</th>
                        <th class="py-3">Giá bán</th>
                        <th class="py-3">Trạng thái</th>
                        <th class="py-3 pe-4 text-end">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${properties}">
                        <tr>
                            <td class="ps-4 fw-medium text-secondary">#<c:out value="${item.id}"/></td>
                            <td>
                                <div class="fw-bold text-dark text-truncate" style="max-width: 300px;"><c:out value="${item.title}"/></div>
                                <span class="text-muted small text-truncate d-block" style="max-width: 300px;"><c:out value="${item.address}"/></span>
                            </td>
                            <td>
                                <span class="badge bg-light text-dark border"><c:out value="${item.propertyType}"/></span>
                            </td>
                            <td class="fw-bold text-danger">
                                <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                            </td>
                            <td>
                                <span class="badge rounded-pill px-3 py-1
                                    ${item.status == 'AVAILABLE' ? 'bg-success-subtle text-success' : item.status == 'DEPOSITED' ? 'bg-warning-subtle text-warning' : 'bg-secondary-subtle text-secondary'}">
                                    <c:out value="${item.status}"/>
                                </span>
                            </td>
                            <td class="pe-4 text-end">
                                <a href="#" class="btn btn-sm btn-outline-secondary me-1" title="Sửa"><i class="fa-solid fa-pen-to-square"></i></a>
                                <form action="${pageContext.request.contextPath}/staff/properties/delete" method="post" class="d-inline" onsubmit="return confirm('Bạn chắc chắn muốn xóa tài sản này?')">
                                    <input type="hidden" name="id" value="${item.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Xóa"><i class="fa-solid fa-trash-can"></i></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="modalAddProperty" data-bs-backdrop="static" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 rounded-4 shadow-lg text-dark">
            <div class="modal-header bg-rems-dark text-white p-4">
                <h5 class="modal-title fw-bold"><i class="fa-solid fa-house-medical me-2 text-info"></i>Đăng ký Bất động sản mới</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-toggle="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/staff/properties/add" method="post" enctype="multipart/form-data" class="p-4">
                <div class="modal-body p-0">
                    <div class="row g-3">
                        <div class="col-12">
                            <label class="form-label fw-semibold">Tiêu đề bài đăng</label>
                            <input type="text" name="title" class="form-control rounded-3" placeholder="Ví dụ: Căn hộ cao cấp Vinhomes 2PN ban công Đông Nam" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Loại hình sản phẩm</label>
                            <select name="propertyType" class="form-select rounded-3" required>
                                <option value="APARTMENT">Căn hộ (Apartment)</option>
                                <option value="HOUSE">Nhà riêng (House)</option>
                                <option value="LAND">Đất nền (Land)</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Giá bán (VNĐ)</label>
                            <input type="number" name="price" class="form-control rounded-3" placeholder="Nhập số tiền..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-semibold">Địa chỉ chính xác</label>
                            <input type="text" name="address" class="form-control rounded-3" placeholder="Số nhà, tên đường, quận/huyện, thành phố..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-semibold">Mô tả chi tiết</label>
                            <textarea name="description" class="form-control rounded-3" rows="4" placeholder="Thông tin chi tiết về tiện ích, pháp lý, hướng nhà..."></textarea>
                        </div>

                        <div class="col-12">
                            <label class="form-label fw-semibold text-primary"><i class="fa-solid fa-images me-2"></i>Tải lên album ảnh minh họa</label>
                            <input type="file" name="images" class="form-control rounded-3" multiple accept="image/*" required>
                            <div class="form-text small">Mẹo: Bạn có thể nhấn giữ phím Ctrl để chọn cùng lúc nhiều file ảnh.</div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0 px-0 pb-0 mt-4">
                    <button type="button" class="btn btn-light px-4 py-2 rounded-3 fw-bold" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="submit" class="btn btn-rems-primary px-4 py-2 rounded-3 fw-bold shadow-sm">Lưu dữ liệu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>