<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="adminHeader.jsp" %>

<h1 class="h3 mb-4 text-gray-800">👤 Quản lý tài khoản </h1>

<!-- Thông báo -->
<c:if test="${not empty sessionScope.msg}">
    <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show" role="alert">
            ${sessionScope.msg}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <c:remove var="msg" scope="session"/>
    <c:remove var="msgType" scope="session"/>
</c:if>

<!-- Bảng tài khoản -->
<div class="table-responsive">
    <table class="table table-bordered bg-white shadow-sm">
        <thead class="thead-light">
        <tr>
            <th>STT</th>
            <th>Họ tên</th>
            <th>Tên đăng nhập</th>
            <th>Email</th>
            <th>Xác thực OTP</th>
            <th>Vai trò (Phân quyền)</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:set var="startIndex" value="${(currentPage - 1) * pageSize}" />
        <c:forEach var="acc" items="${accountList}" varStatus="loop">
            <tr>
                <td>${startIndex + loop.index + 1}</td>
                <td><strong>${acc.fullName}</strong></td>
                <td>${acc.username}</td>
                <td class="email-cell">${acc.email}</td>
                <td>
                    <c:choose>
                        <c:when test="${acc.isVerified == 1}">
                            <span class="badge badge-success"><i class="fas fa-check-circle"></i> Đã xác thực</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-warning"><i class="fas fa-clock"></i> Chưa OTP</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <!-- Nếu là tài khoản Admin đang đăng nhập -->
                    <c:if test="${acc.username == sessionScope.acc.username}">
                        <span class="badge badge-danger px-2 py-1">${acc.role}</span>
                    </c:if>
                    <!-- Nếu là tài khoản khác: Cho phép phân quyền 3 cấp (Customer, Staff, Admin) theo Mục 2.B -->
                    <c:if test="${acc.username != sessionScope.acc.username}">
                        <form action="change-role" method="post" class="form-inline">
                            <input type="hidden" name="id" value="${acc.id}" />
                            <select name="role" class="form-control form-control-sm mr-2 font-weight-bold">
                                <option value="Customer" ${acc.role == 'Customer' ? 'selected' : ''}>Customer</option>
                                <option value="Staff" ${acc.role == 'Staff' ? 'selected' : ''}>Staff</option>
                                <option value="Admin" ${acc.role == 'Admin' ? 'selected' : ''}>Admin</option>
                            </select>
                            <button type="submit" class="btn btn-sm btn-primary">Lưu</button>
                        </form>
                    </c:if>
                </td>
                <td>
            <span class="badge badge-${acc.status == 1 ? 'success' : 'secondary'}">
                    ${acc.status == 1 ? 'Hoạt động' : 'Đã khóa'}
            </span>
                </td>
                <td>
                    <!-- Nếu là tài khoản đang đăng nhập -->
                    <c:if test="${acc.username == sessionScope.acc.username}">
                        <button class="btn btn-sm btn-warning mb-1" onclick="loadEditForm(${acc.id})">
                            <i class="fas fa-edit"></i> Sửa
                        </button>
                        <button type="button" class="btn btn-sm btn-info mb-1" onclick="loadPasswordForm(${acc.id})">
                            <i class="fas fa-key"></i> Đổi mật khẩu
                        </button>
                    </c:if>

                    <!-- Nếu là tài khoản khác -->
                    <c:if test="${acc.username != sessionScope.acc.username}">
                        <button class="btn btn-sm btn-warning mb-1" onclick="loadEditForm(${acc.id})">
                            <i class="fas fa-edit"></i> Sửa
                        </button>
                        <button type="button" class="btn btn-sm btn-info mb-1" onclick="loadPasswordForm(${acc.id})">
                            <i class="fas fa-key"></i> Đổi mật khẩu
                        </button>
                        <form action="toggle-status" method="get" style="display:inline;">
                            <input type="hidden" name="id" value="${acc.id}" />
                            <button type="submit" class="btn btn-sm btn-${acc.status == 1 ? 'danger' : 'success'} mb-1">
                                <i class="fas fa-lock"></i> ${acc.status == 1 ? 'Khóa' : 'Mở'}
                            </button>
                        </form>
                        <form action="delete-account" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc muốn xóa tài khoản này?')">
                            <input type="hidden" name="id" value="${acc.id}" />
                            <button type="submit" class="btn btn-sm btn-danger mb-1">
                                <i class="fas fa-trash"></i> Xóa
                            </button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty accountList}">
            <tr>
                <td colspan="8" class="text-center text-muted py-4">Chưa có dữ liệu tài khoản nào.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<!-- PHÂN TRANG -->
<c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
<c:set var="end" value="${currentPage + 2 > totalPage ? totalPage : currentPage + 2}" />

<c:if test="${totalPage > 1}">
    <nav class="mt-3">
        <ul class="pagination justify-content-center">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a class="page-link" href="admin-accounts?page=${currentPage - 1}">«</a>
            </li>

            <c:forEach begin="${start}" end="${end}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="admin-accounts?page=${i}">${i}</a>
                </li>
            </c:forEach>

            <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}">
                <a class="page-link" href="admin-accounts?page=${currentPage + 1}">»</a>
            </li>
        </ul>
    </nav>
</c:if>

<!-- Modal sửa tài khoản -->
<div class="modal fade" id="editAccountModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content" id="editAccountContent">
            <!-- Nội dung sẽ được load bằng JS -->
        </div>
    </div>
</div>

<!-- Modal đổi mật khẩu -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-md" role="document">
        <div class="modal-content" id="changePasswordContent">
            <!-- Nội dung sẽ được load bằng JS -->
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/admin/lib/js/bootstrap.bundle.min.js"></script>
<script>
    function loadEditForm(id) {
        $.get("edit-account?id=" + id, function(data) {
            const formHtml = $('<div>').html(data).find('form').parent().html();
            $('#editAccountContent').html(formHtml);
            $('#editAccountModal').modal('show');
        });
    }

    function loadPasswordForm(id) {
        $.get("change-password?id=" + id, function(data) {
            const formHtml = $('<div>').html(data).find('form').parent().html();
            $('#changePasswordContent').html(formHtml);
            $('#changePasswordModal').modal('show');
        });
    }
</script>

<!-- CSS -->
<style>
    td {
        vertical-align: middle;
        word-break: break-word;
        white-space: normal;
    }

    .email-cell {
        max-width: 180px;
        overflow-wrap: anywhere;
    }

    .address-cell {
        max-width: 250px;
        overflow-wrap: anywhere;
    }
</style>

<%@ include file="adminFooter.jsp" %>
