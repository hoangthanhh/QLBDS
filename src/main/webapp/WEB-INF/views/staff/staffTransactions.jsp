<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="staffHeader.jsp" %>

<style>
    .table-custom th {
        background-color: #f8f9fc;
        color: #4e73df;
        border-bottom: 2px solid #e3e6f0;
        text-transform: uppercase;
        font-size: 0.85rem;
    }

    .table-custom td {
        vertical-align: middle;
        color: #5a5c69;
        font-size: 0.95rem;
    }

    .badge-custom {
        padding: 0.4em 0.7em;
        font-weight: 600;
        border-radius: 6px;
    }

    .action-btn {
        width: 32px;
        height: 32px;
        padding: 0;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 6px;
    }
</style>

<div class="d-sm-flex align-items-center justify-content-between mb-3 mt-3">
    <h1 class="h3 mb-0 text-gray-800 fw-bold">
        <i class="fa-solid fa-file-signature me-2 text-primary"></i> Quản lý & Xử lý Giao dịch (Staff)
    </h1>
</div>

<!-- THÔNG BÁO -->
<c:if test="${not empty sessionScope.msgSuccess}">
    <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm rounded-3">
        <i class="fa-solid fa-check-circle me-2"></i><strong>Thành công!</strong> ${sessionScope.msgSuccess}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="msgSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.msgError}">
    <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm rounded-3">
        <i class="fa-solid fa-triangle-exclamation me-2"></i><strong>Lỗi!</strong> ${sessionScope.msgError}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="msgError" scope="session"/>
</c:if>

<!-- KHUNG TÌM KIẾM ĐA ĐIỀU KIỆN (Bắt buộc theo yêu cầu) -->
<div class="card shadow-sm border-0 mb-4 rounded-4">
    <div class="card-body p-3">
        <form action="${pageContext.request.contextPath}/staff/transactions" method="GET"
              class="row g-2 align-items-center">
            <div class="col-md-3">
                <input type="text" name="keyword" class="form-control rounded-pill px-3 shadow-none"
                       placeholder="Họ tên, SĐT, Email, Mã GD..." value="${keyword}">
            </div>
            <div class="col-md-2">
                <input type="date" name="startDate" class="form-control rounded-pill px-3 shadow-none"
                       value="${startDate}" title="Từ ngày">
            </div>
            <div class="col-md-2">
                <input type="date" name="endDate" class="form-control rounded-pill px-3 shadow-none" value="${endDate}"
                       title="Đến ngày">
            </div>
            <div class="col-md-2">
                <select name="status" class="form-select rounded-pill px-3 shadow-none">
                    <option value="ALL" ${currentStatus == 'ALL' ? 'selected' : ''}>-- Tất cả trạng thái --</option>
                    <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>🟡 Chờ duyệt</option>
                    <option value="COMPLETED" ${currentStatus == 'COMPLETED' ? 'selected' : ''}>🟢 Hoàn thành</option>
                    <option value="CANCELLED" ${currentStatus == 'CANCELLED' ? 'selected' : ''}>⚪ Khách hủy</option>
                    <option value="REJECTED" ${currentStatus == 'REJECTED' ? 'selected' : ''}>🔴 Bị từ chối</option>
                </select>
            </div>
            <div class="col-md-3 text-end">
                <button type="submit" class="btn btn-primary rounded-pill px-4 shadow-sm">
                    <i class="fa-solid fa-magnifying-glass me-1"></i> Tìm kiếm
                </button>
                <a href="${pageContext.request.contextPath}/staff/transactions"
                   class="btn btn-outline-secondary rounded-pill px-3 ms-1 shadow-sm" title="Làm mới">
                    <i class="fa-solid fa-rotate-left"></i>
                </a>
            </div>
        </form>
    </div>
</div>

<!-- BẢNG DANH SÁCH GIAO DỊCH -->
<div class="card shadow-sm border-0 mb-4 rounded-4">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover table-custom text-center mb-0">
                <thead>
                <tr>
                    <th style="width: 15%;">Mã GD / Ngày</th>
                    <th style="width: 20%;">Khách hàng</th>
                    <th style="width: 25%;">Bất động sản</th>
                    <th style="width: 10%;">Loại</th>
                    <th style="width: 12%;">Số tiền</th>
                    <th style="width: 10%;">Trạng thái</th>
                    <th style="width: 8%;">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty txList}">
                        <tr>
                            <td colspan="7" class="text-center py-5 text-muted">
                                <i class="fa-solid fa-inbox fa-3x mb-3 text-gray-300"></i>
                                <p class="mb-0">Không tìm thấy giao dịch nào phù hợp.</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="tx" items="${txList}">
                            <tr>
                                <td>
                                    <span class="fw-bold text-dark">${tx.transactionCode}</span><br>
                                    <small class="text-muted"><i
                                            class="fa-regular fa-clock me-1"></i>${tx.formattedDate}</small>
                                </td>
                                <td class="text-start">
                                    <div class="fw-bold text-dark">${tx.customerName}</div>
                                    <small class="text-muted d-block"><i
                                            class="fa-solid fa-phone me-1"></i>${tx.customerPhone}</small>
                                    <small class="text-muted d-block"><i
                                            class="fa-solid fa-envelope me-1"></i>${tx.customerEmail}</small>
                                </td>
                                <td class="text-start">
                                    <a href="${pageContext.request.contextPath}/property/detail?id=${tx.propertyId}"
                                       target="_blank" class="fw-bold text-primary text-decoration-none">
                                            ${tx.propertyTitle}
                                    </a>
                                </td>
                                <td>
                                        <span class="badge ${tx.type == 'DEPOSIT' ? 'bg-warning text-dark' : 'bg-primary'} badge-custom">
                                                ${tx.formattedType}
                                        </span>
                                </td>
                                <td class="text-success fw-bold text-end pe-4">
                                    <fmt:formatNumber value="${tx.amount}" pattern="#,###"/> đ
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tx.status == 'PENDING'}"><span
                                                class="badge bg-warning text-dark badge-custom">Chờ duyệt</span></c:when>
                                        <c:when test="${tx.status == 'COMPLETED'}"><span
                                                class="badge bg-success badge-custom">Hoàn thành</span></c:when>
                                        <c:when test="${tx.status == 'REJECTED'}">
                                            <span class="badge bg-danger badge-custom">Từ chối</span><br>
                                            <small class="text-muted d-block mt-1" style="font-size: 11px;"
                                                   title="${tx.rejectReason}">Lý do đính kèm</small>
                                        </c:when>
                                        <c:otherwise><span
                                                class="badge bg-secondary badge-custom">Khách hủy</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:if test="${tx.status == 'PENDING'}">
                                        <div class="d-flex justify-content-center gap-2">
                                            <form action="${pageContext.request.contextPath}/staff/transactions"
                                                  method="post" class="m-0">
                                                <input type="hidden" name="action" value="APPROVE">
                                                <input type="hidden" name="id" value="${tx.id}">
                                                <button type="submit" class="btn btn-success action-btn shadow-sm"
                                                        title="Duyệt giao dịch"
                                                        onclick="return confirm('Xác nhận duyệt giao dịch này? BĐS sẽ được cập nhật trạng thái tương ứng.');">
                                                    <i class="fa-solid fa-check"></i>
                                                </button>
                                            </form>

                                            <button type="button" class="btn btn-danger action-btn shadow-sm"
                                                    title="Từ chối" data-bs-toggle="modal"
                                                    data-bs-target="#rejectModal${tx.id}">
                                                <i class="fa-solid fa-xmark"></i>
                                            </button>
                                        </div>

                                        <!-- Modal Từ chối (Lý do) -->
                                        <div class="modal fade" id="rejectModal${tx.id}" tabindex="-1"
                                             aria-hidden="true">
                                            <div class="modal-dialog modal-dialog-centered">
                                                <div class="modal-content text-start border-0 shadow">
                                                    <div class="modal-header bg-danger text-white border-0">
                                                        <h5 class="modal-title fw-bold"><i
                                                                class="fa-solid fa-triangle-exclamation me-2"></i>Từ
                                                            chối giao dịch</h5>
                                                        <button type="button" class="btn-close btn-close-white"
                                                                data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <form action="${pageContext.request.contextPath}/staff/transactions"
                                                          method="post">
                                                        <div class="modal-body p-4">
                                                            <input type="hidden" name="action" value="REJECT">
                                                            <input type="hidden" name="id" value="${tx.id}">
                                                            <p class="mb-3 text-dark">Từ chối giao dịch
                                                                <strong>#${tx.transactionCode}</strong> của
                                                                <strong>${tx.customerName}</strong>:</p>
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold text-gray-800">Lý do từ
                                                                    chối <span class="text-danger">*</span></label>
                                                                <textarea class="form-control" name="rejectReason"
                                                                          rows="3" required
                                                                          placeholder="Nhập lý do gửi email cho khách..."></textarea>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer border-0">
                                                            <button type="button" class="btn btn-secondary px-3"
                                                                    data-bs-dismiss="modal">Đóng
                                                            </button>
                                                            <button type="submit" class="btn btn-danger px-3 fw-bold">
                                                                Xác nhận Từ chối
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>

    <!-- PHÂN TRANG -->
    <c:set var="maxPage" value="${totalPages > 0 ? totalPages : 1}"/>
    <c:set var="filterParams"
           value="${not empty keyword ? '&keyword='.concat(keyword) : ''}${not empty startDate ? '&startDate='.concat(startDate) : ''}${not empty endDate ? '&endDate='.concat(endDate) : ''}${not empty currentStatus ? '&status='.concat(currentStatus) : ''}"/>
    <c:if test="${maxPage > 1}">
        <div class="card-footer bg-white border-top-0 py-3">
            <nav>
                <ul class="pagination justify-content-end mb-0">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link shadow-none"
                           href="${pageContext.request.contextPath}/staff/transactions?page=${currentPage - 1}${filterParams}">Trước</a>
                    </li>
                    <c:forEach begin="1" end="${maxPage}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link shadow-none"
                               href="${pageContext.request.contextPath}/staff/transactions?page=${i}${filterParams}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == maxPage ? 'disabled' : ''}">
                        <a class="page-link shadow-none"
                           href="${pageContext.request.contextPath}/staff/transactions?page=${currentPage + 1}${filterParams}">Sau</a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>
</div>

<%@ include file="staffFooter.jsp" %>