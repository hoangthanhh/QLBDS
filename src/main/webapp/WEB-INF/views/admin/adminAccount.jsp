<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="adminHeader.jsp" %>

<style>
    .custom-modal-content {
        border-radius: 16px;
        overflow: hidden;
    }

    .modal-header-custom {
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-bottom: 2px solid #dee2e6;
    }

    .form-floating > label {
        color: #6c757d;
        font-weight: 500;
    }

    .form-floating > .form-control:focus ~ label {
        color: #0d6efd;
    }

    .form-floating > .form-control {
        border-radius: 10px;
        border: 1px solid #ced4da;
    }

    .form-floating > .form-control:focus {
        box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.15);
        border-color: #86b7fe;
    }

    .btn-custom {
        border-radius: 8px;
        font-weight: 600;
        padding: 0.6rem 1.5rem;
        transition: all 0.3s ease;
    }

    .btn-custom:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
    }
</style>

<div class="container-fluid pt-3">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1 class="h3 mb-0 text-dark fw-bold">👤 Quản lý tài khoản</h1>
        <button type="button" class="btn btn-primary fw-bold px-3 shadow-sm" data-bs-toggle="modal"
                data-bs-target="#addAccountModal">
            <i class="fa-solid fa-plus me-1"></i> Thêm tài khoản
        </button>
    </div>

    <!-- KHU VỰC TÌM KIẾM & LÀM MỚI -->
    <div class="card shadow-sm border-0 mb-3" style="border-radius: 12px;">
        <div class="card-body p-3">
            <form action="${pageContext.request.contextPath}/admin/user" method="get"
                  class="row g-2 align-items-center">
                <div class="col-md-5 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0 text-muted"><i
                                class="fa-solid fa-magnifying-glass"></i></span>
                        <input type="text" name="keyword" class="form-control border-start-0 shadow-none"
                               placeholder="Tìm kiếm theo họ tên, email, SĐT..." value="${param.keyword}">
                    </div>
                </div>
                <div class="col-md-7 col-sm-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary fw-bold px-3 shadow-sm">
                        <i class="fa-solid fa-search me-1"></i> Tìm kiếm
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/user"
                       class="btn btn-outline-secondary fw-bold px-3 shadow-sm" title="Làm mới danh sách">
                        <i class="fa-solid fa-rotate me-1"></i> Refresh
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Thông báo Alert ngoài màn hình -->
    <c:if test="${not empty sessionScope.msg}">
        <div class="alert alert-dismissible fade show shadow-sm border-0 mb-3 py-2 px-3" role="alert"
             style="border-radius: 8px; background-color: ${sessionScope.msgType == 'success' ? '#d1e7dd' : '#f8d7da'}; color: ${sessionScope.msgType == 'success' ? '#0f5132' : '#842029'};">
            <div>
                <strong>${sessionScope.msgType == 'success' ? '✅ Thành công:' : '⚠️ Thông báo:'}</strong> ${sessionScope.msg}
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="msg" scope="session"/>
        <c:remove var="msgType" scope="session"/>
    </c:if>

    <!-- BẢNG DANH SÁCH -->
    <div class="card shadow-sm border-0 mb-3" style="border-radius: 12px; overflow: hidden;">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0 align-middle">
                    <thead class="table-light">
                    <tr>
                        <th style="width: 5%;" class="text-center">STT</th>
                        <th style="width: 15%;">Họ tên</th>
                        <th style="width: 20%;">Tên đăng nhập</th>
                        <th style="width: 13%;">Số điện thoại</th>
                        <th style="width: 10%;">Địa chỉ</th>
                        <th style="width: 13%;">Vai trò</th>
                        <th style="width: 10%;">Trạng thái</th>
                        <th style="width: 14%;">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="startIndex" value="${(currentPage - 1) * 5}"/>
                    <c:forEach var="acc" items="${userList}" varStatus="loop">
                        <tr>
                            <td class="text-center fw-bold text-muted">${startIndex + loop.index + 1}</td>
                            <td class="fw-bold text-dark">${acc.fullName}</td>
                            <td class="text-break">${acc.email}</td>
                            <td>${not empty acc.phone ? acc.phone : 'N/A'}</td>
                            <td>Việt Nam</td>

                            <td>
                                <form action="${pageContext.request.contextPath}/admin/user" method="post"
                                      class="d-flex align-items-center gap-1 m-0">
                                    <input type="hidden" name="action" value="change-role"/>
                                    <input type="hidden" name="id" value="${acc.id}"/>
                                    <select name="role" class="form-select form-select-sm fw-bold shadow-none"
                                            style="width: 85px; min-height: 28px; padding: 2px 20px 2px 8px; font-size: 11px;"
                                        ${acc.role == 'ADMIN' ? 'disabled' : ''}>
                                        <option value="CUSTOMER" ${acc.role == 'CUSTOMER' ? 'selected' : ''}>User
                                        </option>
                                        <option value="STAFF" ${acc.role == 'STAFF' ? 'selected' : ''}>Staff</option>
                                        <option value="ADMIN" ${acc.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                    </select>
                                    <c:if test="${acc.role != 'ADMIN'}">
                                        <button type="submit" class="btn btn-primary btn-sm"
                                                style="padding: 2px 6px; font-size: 10px;">Lưu
                                        </button>
                                    </c:if>
                                </form>
                            </td>

                            <td>
                                <span class="badge ${acc.status == 'ACTIVE' ? 'bg-success' : 'bg-danger'}"
                                      style="padding: 5px 8px;">
                                        ${acc.status == 'ACTIVE' ? 'Hoạt động' : 'Đã khóa'}
                                </span>
                            </td>

                            <td>
                                <div class="d-flex align-items-center justify-content-center flex-nowrap text-nowrap">
                                    <button type="button"
                                            class="btn btn-warning btn-sm text-white d-flex align-items-center gap-1 me-1 shadow-sm"
                                            onclick="openEditModal('${acc.id}', '${acc.fullName}', '${acc.email}', '${acc.phone}')"
                                            title="Sửa" style="font-size: 10px; padding: 4px 8px; border-radius: 6px;">
                                        <i class="fa-solid fa-pen-to-square"></i> Sửa
                                    </button>

                                    <button type="button"
                                            class="btn btn-info btn-sm text-white d-flex align-items-center gap-1 me-1 shadow-sm"
                                            onclick="openPasswordModal('${acc.id}')"
                                            title="Đổi mật khẩu"
                                            style="font-size: 10px; padding: 4px 8px; border-radius: 6px;">
                                        <i class="fa-solid fa-key"></i> Đổi MK
                                    </button>

                                    <c:choose>
                                        <c:when test="${acc.role == 'ADMIN'}">
                                            <span class="badge bg-secondary text-light ms-1"
                                                  style="font-size: 10px; padding: 5px 8px;">
                                                <i class="fa-solid fa-shield-halved me-1"></i> Admin Bảo Vệ
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="${pageContext.request.contextPath}/admin/user" method="post"
                                                  class="d-inline-block m-0"
                                                  onsubmit="return confirm('Bạn có chắc chắn muốn ${acc.status == 'ACTIVE' ? 'khóa' : 'mở khóa'} tài khoản ${acc.fullName} không?');">
                                                <input type="hidden" name="action" value="toggle-status"/>
                                                <input type="hidden" name="id" value="${acc.id}"/>
                                                <button type="submit"
                                                        class="btn ${acc.status == 'ACTIVE' ? 'btn-danger' : 'btn-success'} btn-sm shadow-sm"
                                                        title="${acc.status == 'ACTIVE' ? 'Khóa tài khoản' : 'Mở khóa tài khoản'}"
                                                        style="font-size: 10px; padding: 4px 8px; border-radius: 6px;">
                                                    <i class="fa-solid ${acc.status == 'ACTIVE' ? 'fa-lock' : 'fa-lock-open'}"></i> ${acc.status == 'ACTIVE' ? 'Khóa' : 'Mở'}
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty userList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">
                                <i class="fa-solid fa-folder-open me-1"></i> Không tìm thấy dữ liệu tài khoản phù hợp.
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- NÚT PHÂN TRANG (CỐ ĐỊNH HIỂN THỊ DÙ CHỈ CÓ 1 TRANG) -->
    <c:set var="maxPage" value="${totalPage > 0 ? totalPage : 1}"/>
    <c:set var="start" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}"/>
    <c:set var="end" value="${currentPage + 2 > maxPage ? maxPage : currentPage + 2}"/>
    <c:set var="kwParam" value="${not empty param.keyword ? '&keyword='.concat(param.keyword) : ''}"/>

    <nav class="mt-3">
        <ul class="pagination pagination-sm justify-content-end mb-0">
            <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/admin/user?page=${currentPage - 1}${kwParam}">Trước</a>
            </li>
            <c:forEach begin="${start}" end="${end}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/admin/user?page=${i}${kwParam}">${i}</a>
                </li>
            </c:forEach>
            <li class="page-item ${currentPage >= maxPage ? 'disabled' : ''}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/admin/user?page=${currentPage + 1}${kwParam}">Sau</a>
            </li>
        </ul>
    </nav>
</div>

<!-- MODAL THÊM MỚI TÀI KHOẢN -->
<div class="modal fade" id="addAccountModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg custom-modal-content">
            <div class="modal-header modal-header-custom px-4 py-3">
                <h5 class="modal-title fw-bold text-primary"><i class="fa-solid fa-user-plus me-2"></i>Thêm tài khoản
                    mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form id="addAccountForm" method="post">
                <input type="hidden" name="action" value="add-user">

                <div class="modal-body px-4 pt-4 pb-2">
                    <div id="addModalAlert" class="alert d-none py-2 px-3 mb-4 rounded-3 shadow-sm" role="alert"
                         style="background-color: #f8d7da; border: 1px solid #f5c2c7;"></div>

                    <div class="row g-3">
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="addFullName" name="fullName"
                                       placeholder="Họ và tên">
                                <label for="addFullName"><i class="fa-regular fa-id-badge text-primary me-2"></i>Họ và
                                    tên</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="addEmail" name="email"
                                       placeholder="name@example.com">
                                <label for="addEmail"><i class="fa-regular fa-envelope text-primary me-2"></i>Địa chỉ
                                    Email</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="addPhone" name="phone"
                                       placeholder="Số điện thoại">
                                <label for="addPhone"><i class="fa-solid fa-phone text-primary me-2"></i>Số điện
                                    thoại</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="password" class="form-control" id="addPassword" name="password"
                                       placeholder="Mật khẩu">
                                <label for="addPassword"><i class="fa-solid fa-lock text-primary me-2"></i>Mật
                                    khẩu</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-group">
                                <label class="fw-bold mb-1 text-dark small"><i
                                        class="fa-solid fa-shield-halved text-primary me-2"></i>Vai trò hệ thống</label>
                                <select name="role" class="form-select shadow-none"
                                        style="border-radius: 10px; height: 48px;">
                                    <option value="STAFF" selected>Staff (Nhân viên)</option>
                                    <option value="ADMIN">Admin (Quản trị viên)</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-0 px-4 pb-4 pt-2 justify-content-end">
                    <button type="button" class="btn btn-light btn-custom text-secondary border shadow-sm"
                            data-bs-dismiss="modal">Hủy bỏ
                    </button>
                    <button type="submit" id="btnAddSubmit" class="btn btn-primary btn-custom shadow-sm">
                        <i class="fas fa-save me-1"></i> <span id="btnAddText">Lưu tài khoản</span>
                        <span id="btnAddSpinner" class="spinner-border spinner-border-sm ms-2 d-none"
                              role="status"></span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- MODAL SỬA -->
<div class="modal fade" id="editAccountModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg custom-modal-content">
            <div class="modal-header modal-header-custom px-4 py-3">
                <h5 class="modal-title fw-bold text-warning"><i class="fa-solid fa-user-pen me-2"></i>Cập nhật thông tin
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form id="editAccountForm" method="post">
                <input type="hidden" name="action" value="edit-user">
                <input type="hidden" name="id" id="editId">

                <div class="modal-body px-4 pt-4 pb-2">
                    <div id="editModalAlert" class="alert d-none py-2 px-3 mb-4 rounded-3 shadow-sm" role="alert"
                         style="background-color: #f8d7da; border: 1px solid #f5c2c7;"></div>

                    <div class="row g-3">
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="editFullName" name="fullName"
                                       placeholder="Họ và tên">
                                <label for="editFullName"><i class="fa-regular fa-id-badge text-warning me-2"></i>Họ và
                                    tên</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="editEmail" name="email"
                                       placeholder="name@example.com">
                                <label for="editEmail"><i class="fa-regular fa-envelope text-warning me-2"></i>Địa chỉ
                                    Email</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="text" class="form-control" id="editPhone" name="phone"
                                       placeholder="Số điện thoại">
                                <label for="editPhone"><i class="fa-solid fa-phone text-warning me-2"></i>Số điện thoại</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-0 px-4 pb-4 pt-2 justify-content-end">
                    <button type="button" class="btn btn-light btn-custom text-secondary border shadow-sm"
                            data-bs-dismiss="modal">Hủy bỏ
                    </button>
                    <button type="submit" id="btnEditSubmit" class="btn btn-warning text-white btn-custom shadow-sm">
                        <i class="fas fa-check-circle me-1"></i> <span id="btnEditText">Lưu thay đổi</span>
                        <span id="btnEditSpinner" class="spinner-border spinner-border-sm ms-2 d-none"
                              role="status"></span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- MODAL ĐỔI MẬT KHẨU -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg custom-modal-content">
            <div class="modal-header modal-header-custom px-4 py-3">
                <h5 class="modal-title fw-bold text-info"><i class="fa-solid fa-shield-halved me-2"></i>Đổi mật khẩu
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form id="changePasswordForm" method="post">
                <input type="hidden" name="action" value="change-password">
                <input type="hidden" name="id" id="pwdId">

                <div class="modal-body px-4 pt-4 pb-2">
                    <div id="pwdModalAlert" class="alert d-none py-2 px-3 mb-4 rounded-3 shadow-sm" role="alert"
                         style="background-color: #f8d7da; border: 1px solid #f5c2c7;"></div>

                    <div class="row g-3">
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="password" class="form-control" id="newPassword" name="newPassword"
                                       placeholder="Mật khẩu mới">
                                <label for="newPassword"><i class="fa-solid fa-key text-info me-2"></i>Mật khẩu
                                    mới</label>
                            </div>
                        </div>
                        <div class="col-12">
                            <div class="form-floating">
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                       placeholder="Xác nhận mật khẩu">
                                <label for="confirmPassword"><i class="fa-solid fa-check-double text-info me-2"></i>Xác
                                    nhận mật khẩu mới</label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-0 px-4 pb-4 pt-2 justify-content-end">
                    <button type="button" class="btn btn-light btn-custom text-secondary border shadow-sm"
                            data-bs-dismiss="modal">Hủy bỏ
                    </button>
                    <button type="submit" id="btnPwdSubmit" class="btn btn-info text-white btn-custom shadow-sm">
                        <span id="btnPwdText">Cập nhật</span>
                        <span id="btnPwdSpinner" class="spinner-border spinner-border-sm ms-2 d-none"
                              role="status"></span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    function openEditModal(id, name, email, phone) {
        document.getElementById('editId').value = id;
        document.getElementById('editFullName').value = name;
        document.getElementById('editEmail').value = email;
        document.getElementById('editPhone').value = phone;
        var editModal = new bootstrap.Modal(document.getElementById('editAccountModal'));
        editModal.show();
    }

    function openPasswordModal(id) {
        document.getElementById('pwdId').value = id;
        document.getElementById('changePasswordForm').reset();
        var pwdModal = new bootstrap.Modal(document.getElementById('changePasswordModal'));
        pwdModal.show();
    }

    function renderErrorBox(errors) {
        if (!errors || errors.length === 0) return '';
        var html = '<div class="fw-bold mb-2 text-danger" style="font-size: 14px;">' +
            '<i class="fa-solid fa-triangle-exclamation me-2"></i>Vui lòng sửa các thông tin sau:</div>' +
            '<ul class="mb-0 text-start text-danger" style="list-style: none; padding-left: 0.2rem; font-size: 13px;">';
        for (var i = 0; i < errors.length; i++) {
            html += '<li class="mb-1"><i class="fa-solid fa-triangle-exclamation text-warning me-2" style="font-size: 12px;"></i>' + errors[i] + '</li>';
        }
        html += '</ul>';
        return html;
    }

    $(document).ready(function () {
        $('.modal').on('hidden.bs.modal', function () {
            $(this).find('form')[0].reset();
            $(this).find('.alert').addClass('d-none').removeClass('alert-success alert-danger').html('');
        });

        $('#addAccountForm').on('submit', function (e) {
            e.preventDefault();
            $('#btnAddSubmit').prop('disabled', true);
            $('#btnAddSpinner').removeClass('d-none');

            $.ajax({
                url: '${pageContext.request.contextPath}/admin/user',
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function (res) {
                    $('#btnAddSubmit').prop('disabled', false);
                    $('#btnAddSpinner').addClass('d-none');

                    if (res.success) {
                        $('#addModalAlert').removeClass('d-none alert-danger').addClass('alert-success')
                            .html('<div class="fw-bold"><i class="fa-solid fa-circle-check text-success me-2" style="font-size: 16px;"></i> Thêm tài khoản thành công!</div>');
                        setTimeout(function () {
                            location.reload();
                        }, 1200);
                    } else {
                        var errorHtml = renderErrorBox(res.errors);
                        $('#addModalAlert').removeClass('d-none alert-success').addClass('alert-danger').html(errorHtml);
                    }
                }
            });
        });

        $('#editAccountForm').on('submit', function (e) {
            e.preventDefault();
            $('#btnEditSubmit').prop('disabled', true);
            $('#btnEditSpinner').removeClass('d-none');

            $.ajax({
                url: '${pageContext.request.contextPath}/admin/user',
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function (res) {
                    $('#btnEditSubmit').prop('disabled', false);
                    $('#btnEditSpinner').addClass('d-none');

                    if (res.success) {
                        $('#editModalAlert').removeClass('d-none alert-danger').addClass('alert-success')
                            .html('<div class="fw-bold"><i class="fa-solid fa-circle-check text-success me-2" style="font-size: 16px;"></i> Cập nhật thành công!</div>');
                        setTimeout(function () {
                            location.reload();
                        }, 1200);
                    } else {
                        var errorHtml = renderErrorBox(res.errors);
                        $('#editModalAlert').removeClass('d-none alert-success').addClass('alert-danger').html(errorHtml);
                    }
                }
            });
        });

        $('#changePasswordForm').on('submit', function (e) {
            e.preventDefault();
            $('#btnPwdSubmit').prop('disabled', true);
            $('#btnPwdSpinner').removeClass('d-none');

            $.ajax({
                url: '${pageContext.request.contextPath}/admin/user',
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function (res) {
                    $('#btnPwdSubmit').prop('disabled', false);
                    $('#btnPwdSpinner').addClass('d-none');

                    if (res.success) {
                        $('#pwdModalAlert').removeClass('d-none alert-danger').addClass('alert-success')
                            .html('<div class="fw-bold"><i class="fa-solid fa-circle-check text-success me-2" style="font-size: 16px;"></i> Đổi mật khẩu thành công!</div>');
                        setTimeout(function () {
                            location.reload();
                        }, 1200);
                    } else {
                        var errorHtml = renderErrorBox(res.errors);
                        $('#pwdModalAlert').removeClass('d-none alert-success').addClass('alert-danger').html(errorHtml);
                    }
                }
            });
        });
    });
</script>

<%@ include file="adminFooter.jsp" %>