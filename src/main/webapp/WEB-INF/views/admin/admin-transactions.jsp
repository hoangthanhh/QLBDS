<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Gọi Header (Đã bao gồm sẵn html, head, body, sidebar và mở sẵn container-fluid) -->
<%@ include file="adminHeader.jsp" %>

<!-- CSS tùy chỉnh bổ sung cho trang này -->
<style>
    .table-custom th {
        background-color: #f8f9fc;
        color: #4e73df;
        border-bottom: 2px solid #e3e6f0;
        text-transform: uppercase;
        font-size: 0.85rem;
        letter-spacing: 0.05rem;
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
    .property-link {
        color: #2c9faf;
        font-weight: 600;
        text-decoration: none;
    }
    .property-link:hover {
        text-decoration: underline;
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

<!-- TIÊU ĐỀ TRANG -->
<div class="d-sm-flex align-items-center justify-content-between mb-4 mt-4">
    <h1 class="h3 mb-0 text-gray-800 fw-bold">
        <i class="fa-solid fa-file-signature me-2 text-primary"></i> Quản lý Giao dịch
    </h1>
</div>

<!-- THÔNG BÁO HỆ THỐNG -->
<c:if test="${not empty sessionScope.msgSuccess}">
    <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm rounded-3">
        <i class="fa-solid fa-check-circle me-2"></i><strong>Thành công!</strong> ${sessionScope.msgSuccess}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="msgSuccess" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.msgError}">
    <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm rounded-3">
        <i class="fa-solid fa-exclamation-triangle me-2"></i><strong>Lỗi!</strong> ${sessionScope.msgError}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="msgError" scope="session"/>
</c:if>

<!-- KHỐI BẢNG DỮ LIỆU -->
<div class="card shadow-sm border-0 mb-4 rounded-4">
    <div class="card-header py-3 bg-white border-bottom d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold text-primary">Danh sách yêu cầu Đặt cọc / Mua Bất động sản</h6>

            <!-- Form Bộ lọc Trạng thái -->
            <form action="${pageContext.request.contextPath}/admin/transactions" method="GET" class="d-flex align-items-center mb-0">
                <label class="me-2 fw-bold text-secondary mb-0" style="font-size: 0.9rem;">Lọc trạng thái:</label>
                <select name="status" class="form-select form-select-sm shadow-none" onchange="this.form.submit()" style="width: auto; cursor: pointer;">
                    <option value="ALL" ${currentStatus == 'ALL' ? 'selected' : ''}>Tất cả giao dịch</option>
                    <option value="PENDING" ${currentStatus == 'PENDING' ? 'selected' : ''}>🟡 Chờ duyệt</option>
                    <option value="COMPLETED" ${currentStatus == 'COMPLETED' ? 'selected' : ''}>🟢 Hoàn thành</option>
                    <option value="CANCELLED" ${currentStatus == 'CANCELLED' ? 'selected' : ''}>⚪ Khách hủy</option>
                    <option value="REJECTED" ${currentStatus == 'REJECTED' ? 'selected' : ''}>🔴 Bị từ chối</option>
                    <option value="FORFEITED" ${currentStatus == 'FORFEITED' ? 'selected' : ''}>🟣 Đã thu cọc(Khách hủy cọc)</option>
                        </select>
                </select>
            </form>
        </div>
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
                                <td colspan="7" class="text-center py-5">
                                    <i class="fa-solid fa-inbox fa-3x text-gray-300 mb-3"></i>
                                    <p class="text-muted mb-0">Chưa có giao dịch nào trên hệ thống.</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="tx" items="${txList}">
                                <tr>
                                    <td>
                                        <span class="fw-bold text-dark">${tx.transactionCode}</span><br>
                                        <small class="text-muted"><i class="fa-regular fa-clock me-1"></i>${tx.formattedDate}</small>
                                    </td>
                                    <td class="text-start">
                                        <div class="fw-bold text-dark">${tx.customerName}</div>
                                        <small class="text-muted d-block"><i class="fa-solid fa-phone me-1"></i>${tx.customerPhone}</small>
                                        <small class="text-muted d-block"><i class="fa-solid fa-envelope me-1"></i>${tx.customerEmail}</small>
                                    </td>
                                    <td class="text-start">
                                        <a href="${pageContext.request.contextPath}/property/detail?id=${tx.propertyId}" target="_blank" class="property-link">
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
                                            <c:when test="${tx.status == 'PENDING'}"><span class="badge bg-warning text-dark badge-custom">Chờ duyệt</span></c:when>
                                            <c:when test="${tx.status == 'COMPLETED'}"><span class="badge bg-success badge-custom">Hoàn thành</span></c:when>
                                            <c:when test="${tx.status == 'REJECTED'}">
                                                <span class="badge bg-danger badge-custom">Từ chối</span><br>
                                                <small class="text-muted d-block mt-1" style="font-size: 11px;" title="${tx.rejectReason}">Lý do đính kèm</small>
                                            </c:when>
                                            <c:when test="${tx.status == 'FORFEITED'}">
                                                            <span class="badge px-2 py-1" style="background-color: #6f42c1; color: white;" title="Khách hủy kèo, công ty thu cọc">Đã thu cọc (Khách hủy cọc) </span>
                                                        </c:when>
                                            <c:otherwise><span class="badge bg-secondary badge-custom">Khách hủy</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${tx.status == 'PENDING'}">
                                            <div class="d-flex justify-content-center gap-2">
                                                <!-- Form Duyệt -->
                                                <form action="${pageContext.request.contextPath}/admin/transactions" method="post" class="m-0">
                                                    <input type="hidden" name="action" value="APPROVE">
                                                    <input type="hidden" name="id" value="${tx.id}">
                                                    <button type="submit" class="btn btn-success action-btn shadow-sm" title="Duyệt giao dịch" onclick="return confirm('Xác nhận duyệt giao dịch này? BĐS sẽ được cập nhật trạng thái tương ứng.');">
                                                        <i class="fa-solid fa-check"></i>
                                                    </button>
                                                </form>

                                                <!-- Nút mở Modal Từ chối -->
                                                <button type="button" class="btn btn-danger action-btn shadow-sm" title="Từ chối" data-bs-toggle="modal" data-bs-target="#rejectModal${tx.id}">
                                                    <i class="fa-solid fa-xmark"></i>
                                                </button>
                                            </div>

                                            <!-- Modal Từ Chối Nhập Lý Do -->
                                            <div class="modal fade" id="rejectModal${tx.id}" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered">
                                                    <div class="modal-content text-start border-0 shadow">
                                                        <div class="modal-header bg-danger text-white border-0">
                                                            <h5 class="modal-title fw-bold"><i class="fa-solid fa-triangle-exclamation me-2"></i>Từ chối giao dịch</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                                                        </div>
                                                        <form action="${pageContext.request.contextPath}/admin/transactions" method="post">
                                                            <div class="modal-body p-4">
                                                                <input type="hidden" name="action" value="REJECT">
                                                                <input type="hidden" name="id" value="${tx.id}">
                                                                <p class="mb-3 text-dark">Bạn đang từ chối giao dịch <strong>#${tx.transactionCode}</strong> của khách hàng <strong>${tx.customerName}</strong>.</p>
                                                                <div class="mb-3">
                                                                    <label class="form-label fw-bold text-gray-800">Lý do từ chối <span class="text-danger">*</span></label>
                                                                    <textarea class="form-control bg-light" name="rejectReason" rows="3" required placeholder="VD: Khách không chuyển khoản đúng hạn, giao dịch không hợp lệ..."></textarea>
                                                                    <div class="form-text text-danger mt-2"><i class="fa-solid fa-circle-info me-1"></i>Lý do này sẽ được đính kèm vào Email gửi cho khách hàng.</div>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer bg-light border-0">
                                                                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Đóng</button>
                                                                <button type="submit" class="btn btn-danger px-4 fw-bold">Xác nhận Từ chối</button>
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

    <!-- THANH PHÂN TRANG -->
    <c:if test="${totalPages > 1}">
        <div class="card-footer bg-white border-top-0 py-3">
            <nav aria-label="Page navigation">
                <ul class="pagination justify-content-end mb-0">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link shadow-none" href="?page=${currentPage - 1}" tabindex="-1">Trước</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link shadow-none" href="?page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link shadow-none" href="?page=${currentPage + 1}">Sau</a>
                    </li>
                </ul>
            </nav>
        </div>
    </c:if>
</div>

<!-- ĐÓNG CÁC THẺ DIV BỊ MỞ TỪ ADMINHEADER.JSP -->
    </div> <!-- End container-fluid -->
</div> <!-- End content -->
</div> <!-- End content-wrapper -->
</div> <!-- End wrapper -->

<!-- Khởi tạo thư viện JS Bootstrap (Bắt buộc để Modal/Alert hoạt động) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>