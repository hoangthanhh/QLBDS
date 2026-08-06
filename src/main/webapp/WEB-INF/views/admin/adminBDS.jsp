<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="adminHeader.jsp" %>

<style>
    td.description-cell {
        max-width: 250px;
        max-height: 80px;
        overflow-y: auto;
        white-space: pre-line;
        word-break: break-word;
    }
</style>

<h1 class="h3 mb-4 text-gray-800">🏡 Quản lý Bất Động Sản</h1>

<!-- Thông báo -->
<c:if test="${not empty sessionScope.msg}">
    <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show" role="alert">
            ${sessionScope.msg}
        <button type="button" class="close" data-dismiss="alert">&times;</button>
    </div>
    <c:remove var="msg" scope="session"/>
    <c:remove var="msgType" scope="session"/>
</c:if>

<!-- TÌM KIẾM NÂNG CẠO (Mục 2.A: Địa chỉ, Khoảng giá, Loại hình) -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form action="admin-bds" method="get" class="row align-items-center">
            <!-- Từ khóa / Địa chỉ -->
            <div class="col-md-3 mb-2">
                <input type="text" name="keyword" class="form-control rounded-pill shadow-sm px-3"
                       placeholder="Tìm theo mã, tên, địa chỉ..." value="${keyword}">
            </div>

            <!-- Loại hình (Căn hộ, Nhà riêng, Đất nền) -->
            <div class="col-md-3 mb-2">
                <select name="categoryId" class="form-control rounded-pill shadow-sm px-3">
                    <option value="">-- Tất cả loại hình --</option>
                    <c:forEach var="cat" items="${categoryList}">
                        <option value="${cat.id}" ${param.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Lọc theo khoảng giá -->
            <div class="col-md-3 mb-2">
                <div class="input-group">
                    <input type="number" name="minPrice" class="form-control rounded-left shadow-sm" placeholder="Giá từ" value="${param.minPrice}">
                    <input type="number" name="maxPrice" class="form-control rounded-right shadow-sm" placeholder="Đến giá" value="${param.maxPrice}">
                </div>
            </div>

            <!-- Nút hành động -->
            <div class="col-md-3 mb-2 text-right">
                <button type="submit" class="btn btn-primary rounded-pill px-3"><i class="fas fa-search"></i> Tìm kiếm</button>
                <button type="button" class="btn btn-success rounded-pill px-3 ml-1" onclick="loadAddBDSForm()">
                    <i class="fas fa-plus"></i> Thêm BĐS
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Danh sách Bất Động Sản -->
<div class="table-responsive shadow-sm rounded">
    <table class="table table-bordered table-hover bg-white mb-0">
        <thead class="thead-light">
        <tr>
            <th>STT</th>
            <th>Mã BĐS</th>
            <th>Tiêu đề BĐS</th>
            <th>Địa chỉ</th>
            <th>Loại hình</th>
            <th>Giá bán</th>
            <th>Hình ảnh</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:forEach var="p" items="${productList}" varStatus="loop">
            <tr>
                <td>${startIndex + loop.index + 1}</td>
                <td><strong>${p.masp}</strong></td>
                <td>${p.name}</td>
                <td>${p.address}</td>
                <td><span class="badge badge-info px-2 py-1">${p.categoryName}</span></td>
                <td class="text-danger font-weight-bold">
                    <fmt:formatNumber value="${p.price}" type="number" maxFractionDigits="0"/> VNĐ
                </td>
                <td>
                    <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(p.image, 'image\\')}" alt="${p.name}" width="60" height="60" class="rounded" style="object-fit: cover;" />
                </td>
                <td>
                    <a href="javascript:void(0);" class="btn btn-sm btn-info mb-1" onclick="openEditBDSModal(${p.id})">
                        <i class="fas fa-edit"></i> Chi tiết / Sửa
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty productList}">
            <tr>
                <td colspan="8" class="text-center text-muted py-4">Chưa có dữ liệu Bất Động Sản nào.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<!-- Phân trang -->
<c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
<c:set var="end" value="${currentPage + 2 > totalPage ? totalPage : currentPage + 2}" />

<nav class="mt-4">
    <ul class="pagination justify-content-center">
        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
            <a class="page-link" href="admin-bds?page=${currentPage - 1}&keyword=${keyword}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&categoryId=${param.categoryId}&size=${pageSize}">«</a>
        </li>

        <c:if test="${start > 1}">
            <li class="page-item">
                <a class="page-link" href="admin-bds?page=1&keyword=${keyword}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&categoryId=${param.categoryId}&size=${pageSize}">1</a>
            </li>
            <li class="page-item disabled"><span class="page-link">...</span></li>
        </c:if>

        <c:forEach begin="${start}" end="${end}" var="i">
            <li class="page-item ${i == currentPage ? 'active' : ''}">
                <a class="page-link" href="admin-bds?page=${i}&keyword=${keyword}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&categoryId=${param.categoryId}&size=${pageSize}">${i}</a>
            </li>
        </c:forEach>

        <c:if test="${end < totalPage}">
            <li class="page-item disabled"><span class="page-link">...</span></li>
            <li class="page-item">
                <a class="page-link" href="admin-bds?page=${totalPage}&keyword=${keyword}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&categoryId=${param.categoryId}&size=${pageSize}">${totalPage}</a>
            </li>
        </c:if>

        <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
            <a class="page-link" href="admin-bds?page=${currentPage + 1}&keyword=${keyword}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&categoryId=${param.categoryId}&size=${pageSize}">»</a>
        </li>
    </ul>
</nav>

<!-- Chọn số bản ghi hiển thị -->
<form method="get" action="admin-bds" class="text-right mb-4">
    <input type="hidden" name="keyword" value="${param.keyword}" />
    <input type="hidden" name="minPrice" value="${param.minPrice}" />
    <input type="hidden" name="maxPrice" value="${param.maxPrice}" />
    <input type="hidden" name="categoryId" value="${param.categoryId}" />
    <select name="size" class="custom-select w-auto shadow-sm" onchange="this.form.submit()">
        <option value="5" ${pageSize == 5 ? 'selected' : ''}>5 BĐS/trang</option>
        <option value="10" ${pageSize == 10 ? 'selected' : ''}>10 BĐS/trang</option>
        <option value="20" ${pageSize == 20 ? 'selected' : ''}>20 BĐS/trang</option>
    </select>
</form>

<!-- Modal Thêm BĐS -->
<div class="modal fade" id="addBDSModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content" id="addBDSContent"></div>
    </div>
</div>

<!-- Modal Sửa BĐS -->
<div class="modal fade" id="editBDSModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content" id="editBDSContent"></div>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/admin/lib/js/bootstrap.bundle.min.js"></script>

<script>
    function loadAddBDSForm() {
        $.get("add-bds", function(data) {
            const formHtml = $('<div>').html(data).find('form').parent().html();
            $('#addBDSContent').html(formHtml);
            $('#addBDSModal').modal('show');
        });
    }

    function openEditBDSModal(bdsId) {
        $.get("edit-bds?id=" + bdsId, function(data) {
            const formHtml = $('<div>').html(data).find('form').parent().html();
            $('#editBDSContent').html(formHtml);
            $('#editBDSModal').modal('show');
        });
    }
</script>
<%@ include file="adminFooter.jsp" %>
