<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="adminHeader.jsp" %>

<h1 class="h3 mb-4 text-gray-800">📜 Quản lý giao dịch Bất Động Sản</h1>

<!-- Form tìm kiếm giao dịch (Mục 2.D: Tìm theo thời gian, thông tin KH, tình trạng) -->
<form action="admin-transaction" method="get" class="form-inline mb-3">
    <input type="text" name="customer" class="form-control rounded-pill mr-2 shadow-sm"
           placeholder="Tên / Email khách hàng..." value="${param.customer}">
    <input type="date" name="transactionDate" class="form-control rounded-pill mr-2 shadow-sm"
           value="${param.transactionDate}">
    <select name="status" class="form-control rounded-pill mr-2 shadow-sm">
        <option value="">Tất cả trạng thái</option>
        <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Pending (Chờ duyệt)</option>
        <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Completed (Thành công)</option>
        <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>Cancelled (Đã hủy)</option>
    </select>
    <button type="submit" class="btn btn-primary rounded-pill px-4"><i class="fas fa-search"></i> Tìm kiếm</button>
    <button type="button" class="btn btn-success ml-2 rounded-pill px-4" onclick="openCreateOrderModal()"><i
            class="fas fa-plus"></i> Tạo giao dịch mới
    </button>
</form>

<!-- Danh sách giao dịch -->
<div class="table-responsive">
    <table class="table table-bordered shadow-sm bg-white">
        <thead class="thead-light">
        <tr>
            <th>STT</th>
            <th>Mã GD</th>
            <th>Khách hàng</th>
            <th>Bất động sản</th>
            <th>Loại GD</th>
            <th>Giá trị / Đặt cọc</th>
            <th>Ngày tạo</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}"/>
        <c:forEach var="o" items="${orderList}" varStatus="loop">
            <tr>
                <td>${startIndex + loop.index + 1}</td>
                <td><strong>#${o.id}</strong></td>
                <td>${o.customer != null ? o.customer.fullName : o.recipientName}</td>
                <td><span class="text-primary font-weight-bold">${o.propertyTitle}</span></td>
                <td><span class="badge badge-info px-2 py-1">${o.type}</span></td>
                <td class="text-danger font-weight-bold">
                    <fmt:formatNumber value="${o.total}" type="number" maxFractionDigits="0"/> VNĐ
                </td>
                <td>${o.orderDate}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/updateTransactionStatus" method="post"
                          class="form-inline">
                        <input type="hidden" name="transactionId" value="${o.id}"/>
                        <select name="status" class="form-control form-control-sm mr-2 rounded-pill font-weight-bold"
                                onchange="toggleReason(this, ${o.id})">
                            <option value="Pending" ${o.status == 'Pending' ? 'selected' : ''}>Pending</option>
                            <option value="Completed" ${o.status == 'Completed' ? 'selected' : ''}>Completed</option>
                            <option value="Cancelled" ${o.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                        </select>

                        <input type="text" name="cancelReason" id="cancelReason-${o.id}"
                               class="form-control form-control-sm mr-2 rounded-pill"
                               placeholder="Lý do hủy..."
                               value="${o.cancelReason}"
                               style="display: ${o.status == 'Cancelled' && empty o.cancelReason ? 'inline-block' : 'none'};"/>

                        <button type="submit" class="btn btn-sm btn-primary rounded-pill"
                                title="Cập nhật & Tự động gửi Email thông báo">
                            <i class="fas fa-sync-alt"></i> Cập nhật
                        </button>
                    </form>
                </td>
                <td>
                    <button type="button" class="btn btn-sm btn-info mb-1 rounded-pill"
                            onclick="openOrderDetail(${o.id})">
                        <i class="fas fa-eye"></i> Chi tiết
                    </button>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty orderList}">
            <tr>
                <td colspan="9" class="text-center text-muted py-4">Chưa có giao dịch BĐS nào được tạo.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<!-- Modal xem chi tiết giao dịch -->
<div class="modal fade" id="orderDetailModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content" id="orderDetailContent">
            <!-- Nội dung AJAX sẽ load vào đây -->
        </div>
    </div>
</div>

<!-- Phân trang -->
<c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}"/>
<c:set var="end" value="${currentPage + 2 > totalPage ? totalPage : currentPage + 2}"/>

<c:if test="${totalPage > 1}">
    <nav class="mt-3">
        <ul class="pagination justify-content-center">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="admin-transaction?page=${currentPage - 1}&customer=${param.customer}&transactionDate=${param.transactionDate}&status=${param.status}">«</a>
            </li>

            <c:forEach begin="${start}" end="${end}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link"
                       href="admin-transaction?page=${i}&customer=${param.customer}&transactionDate=${param.transactionDate}&status=${param.status}">${i}</a>
                </li>
            </c:forEach>

            <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
                <a class="page-link"
                   href="admin-transaction?page=${currentPage + 1}&customer=${param.customer}&transactionDate=${param.transactionDate}&status=${param.status}">»</a>
            </li>
        </ul>
    </nav>
</c:if>

<!-- Modal tạo giao dịch -->
<div class="modal fade" id="createOrderModal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content" id="createOrderContent">
            <!-- Nội dung AJAX sẽ load vào đây -->
        </div>
    </div>
</div>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

<script>
    function toggleReason(selectElem, orderId) {
        const input = document.getElementById('cancelReason-' + orderId);
        if (!input) return;
        if (selectElem.value === 'Cancelled') {
            input.style.display = 'inline-block';
            input.focus();
        } else {
            input.style.display = 'none';
            input.value = '';
        }
    }

    function openOrderDetail(orderId) {
        $.ajax({
            url: 'transaction-detail',
            type: 'GET',
            data: {id: orderId},
            success: function (data) {
                $('#orderDetailContent').html(data);
                $('#orderDetailModal').modal('show');
            },
            error: function () {
                alert('Không thể tải chi tiết giao dịch.');
            }
        });
    }

    function openCreateOrderModal(page = 1) {
        $.ajax({
            url: 'create-transaction?page=' + page,
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            success: function (data) {
                $('#createOrderContent').html(data);
                $('#createOrderModal').modal('show');
                attachCreateOrderEvents();
            },
            error: function () {
                alert('Không thể tải form tạo giao dịch.');
            }
        });
    }

    function loadModalPage(url) {
        $.ajax({
            url: url,
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            success: function (data) {
                $('#createOrderContent').html(data);
                attachCreateOrderEvents();
            },
            error: function () {
                alert('Không thể tải trang BĐS.');
            }
        });
    }

    function attachCreateOrderEvents() {
        $('#createOrderContent').find('.load-modal-page').off('click').on('click', function (e) {
            e.preventDefault();
            const url = $(this).attr('href');
            loadModalPage(url);
        });

        $('#createOrderContent').find('#searchProduct').off('keyup').on('keyup', function () {
            const keyword = $(this).val().toLowerCase();
            $('#createOrderContent').find('#productTable tbody tr').each(function () {
                const name = $(this).find('.product-name').text().toLowerCase();
                $(this).toggle(name.includes(keyword));
            });
        });

        $('#createOrderContent').find('.goto-button').off('click').on('click', function () {
            const page = $('#createOrderContent').find('#gotoPage').val();
            const max = parseInt($('#createOrderContent').find('#gotoPage').attr('max'));
            if (page >= 1 && page <= max) {
                loadModalPage('create-transaction?page=' + page);
            } else {
                alert("Vui lòng nhập số từ 1 đến " + max);
            }
        });
    }
</script>
<%@ include file="adminFooter.jsp" %>
