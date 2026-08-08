<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="adminHeader.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h3 text-gray-800">👤 Quản lý tài khoản </h1>
    <button type="button" class="btn btn-primary shadow-sm" data-toggle="modal" data-target="#addAccountModal">
        <i class="fas fa-user-plus fa-sm text-white-50"></i> Thêm tài khoản
    </button>
</div>

<!-- Thông báo -->
<c:if test="${not empty sessionScope.msg}">
    <div class="alert alert-${sessionScope.msgType} alert-dismissible fade show shadow-sm" role="alert">
            ${sessionScope.msg}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <c:remove var="msg" scope="session"/>
    <c:remove var="msgType" scope="session"/>
</c:if>

<!-- Bảng tài khoản -->
<div class="card shadow mb-4">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-bordered table-hover mb-0">
                <thead class="thead-light">
                <tr>
                    <th width="5%">STT</th>
                    <th width="15%">Họ tên</th>
                    <th width="15%">Email</th>
                    <th width="12%">SĐT</th>
                    <th width="10%">Xác thực</th>
                    <th width="15%">Vai trò</th>
                    <th width="10%">Trạng thái</th>
                    <th width="18%">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="startIndex" value="${(currentPage - 1) * pageSize}"/>
                <c:forEach var="acc" items="${accountList}" varStatus="loop">
                    <tr>
                        <td class="align-middle text-center">${startIndex + loop.index + 1}</td>
                        <td class="align-middle"><strong>${acc.fullName}</strong></td>
                        <td class="align-middle text-break">${acc.email}</td>
                        <td class="align-middle">${acc.phone}</td>
                        <td class="align-middle text-center">
                            <c:choose>
                                <c:when test="${acc.isVerified}">
                                    <span class="badge badge-success"><i
                                            class="fas fa-check-circle"></i> Đã xác thực</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-warning"><i class="fas fa-clock"></i> Chưa OTP</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="align-middle">
                            <c:if test="${acc.email == sessionScope.currentUser.email}">
                                <span class="badge badge-danger px-3 py-2 w-100">${acc.role}</span>
                            </c:if>
                            <c:if test="${acc.email != sessionScope.currentUser.email}">
                                <form action="change-role" method="post" class="d-flex">
                                    <input type="hidden" name="id" value="${acc.id}"/>
                                    <select name="role" class="form-control form-control-sm mr-1 font-weight-bold">
                                        <option value="CUSTOMER" ${acc.role == 'CUSTOMER' ? 'selected' : ''}>Customer
                                        </option>
                                        <option value="STAFF" ${acc.role == 'STAFF' ? 'selected' : ''}>Staff</option>
                                        <option value="ADMIN" ${acc.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                    </select>
                                    <button type="submit" class="btn btn-sm btn-primary"><i class="fas fa-save"></i>
                                    </button>
                                </form>
                            </c:if>
                        </td>
                        <td class="align-middle text-center">
                            <span class="badge badge-${acc.status == 'ACTIVE' ? 'success' : 'secondary'} px-2 py-1">
                                    ${acc.status == 'ACTIVE' ? 'Hoạt động' : 'Đã khóa'}
                            </span>
                        </td>
                        <td class="align-middle">
                            <button class="btn btn-sm btn-warning mb-1" onclick="loadEditForm(${acc.id})"><i
                                    class="fas fa-edit"></i></button>
                            <button type="button" class="btn btn-sm btn-info mb-1"
                                    onclick="loadPasswordForm(${acc.id})"><i class="fas fa-key"></i></button>

                            <c:if test="${acc.email != sessionScope.currentUser.email}">
                                <form action="toggle-status" method="get" style="display:inline;">
                                    <input type="hidden" name="id" value="${acc.id}"/>
                                    <button type="submit"
                                            class="btn btn-sm btn-${acc.status == 'ACTIVE' ? 'secondary' : 'success'} mb-1"
                                            title="Khóa/Mở">
                                        <i class="fas fa-${acc.status == 'ACTIVE' ? 'lock' : 'unlock'}"></i>
                                    </button>
                                </form>
                                <form action="delete-account" method="post" style="display:inline;"
                                      onsubmit="return confirm('Bạn có chắc chắn muốn xóa vĩnh viễn tài khoản này?')">
                                    <input type="hidden" name="id" value="${acc.id}"/>
                                    <button type="submit" class="btn btn-sm btn-danger mb-1"><i
                                            class="fas fa-trash"></i></button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty accountList}">
                    <tr>
                        <td colspan="8" class="text-center text-muted py-4">Chưa có dữ liệu tài khoản.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- PHÂN TRANG -->
<c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}"/>
<c:set var="end" value="${currentPage + 2 > totalPage ? totalPage : currentPage + 2}"/>
<c:if test="${totalPage > 1}">
    <nav class="mt-3">
        <ul class="pagination justify-content-end">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a class="page-link"
                                                                           href="admin-accounts?page=${currentPage - 1}">Trước</a>
            </li>
            <c:forEach begin="${start}" end="${end}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}"><a class="page-link"
                                                                             href="admin-accounts?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage == totalPage ? 'disabled' : ''}"><a class="page-link"
                                                                                   href="admin-accounts?page=${currentPage + 1}">Sau</a>
            </li>
        </ul>
    </nav>
</c:if>

<!-- Modal Thêm tài khoản mới -->
<div class="modal fade" id="addAccountModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title font-weight-bold">Thêm tài khoản mới</h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="add-account" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <label>Họ và tên <span class="text-danger">*</span></label>
                        <input type="text" name="fullName" class="form-control" required placeholder="Nhập họ tên">
                    </div>
                    <div class="form-group">
                        <label>Email <span class="text-danger">*</span></label>
                        <input type="email" name="email" class="form-control" required placeholder="example@domain.com">
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="text" name="phone" class="form-control" placeholder="09xxxxxxx">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu <span class="text-danger">*</span></label>
                        <input type="password" name="password" class="form-control" required
                               placeholder="Nhập mật khẩu">
                    </div>
                    <div class="form-group">
                        <label>Vai trò <span class="text-danger">*</span></label>
                        <select name="role" class="form-control">
                            <option value="CUSTOMER">Customer (Khách hàng)</option>
                            <option value="STAFF">Staff (Nhân viên)</option>
                            <option value="ADMIN">Admin (Quản trị viên)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Tạo tài khoản</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal Sửa & Đổi mật khẩu (Load bằng JS) -->
<div class="modal fade" id="editAccountModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" id="editAccountContent"></div>
    </div>
</div>
<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content" id="changePasswordContent"></div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/admin/lib/js/bootstrap.bundle.min.js"></script>
<script>
    function loadEditForm(id) {
        $.get("edit-account?id=" + id, function (data) {
            $('#editAccountContent').html($(data).find('form').parent().html());
            $('#editAccountModal').modal('show');
        });
    }

    function loadPasswordForm(id) {
        $.get("change-password?id=" + id, function (data) {
            $('#changePasswordContent').html($(data).find('form').parent().html());
            $('#changePasswordModal').modal('show');
        });
    }
</script>

<%@ include file="adminFooter.jsp" %>