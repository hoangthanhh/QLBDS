<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="change-password" method="post" onsubmit="return validatePasswordMatch()">
    <div class="modal-header bg-gradient-info text-white">
        <h5 class="modal-title"><i class="fas fa-key me-1"></i> Đổi mật khẩu cho: <strong>${acc.fullName}</strong></h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span>&times;</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${acc.id}" />

        <!-- Mật khẩu mới -->
        <div class="form-group">
            <label for="newPassword" class="font-weight-bold text-dark">🔒 Mật khẩu mới</label>
            <input type="password" name="newPassword" id="newPassword"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập mật khẩu mới..." required>
        </div>

        <!-- Xác nhận mật khẩu -->
        <div class="form-group">
            <label for="confirmPassword" class="font-weight-bold text-dark">🔒 Xác nhận mật khẩu</label>
            <input type="password" name="confirmPassword" id="confirmPassword"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập lại mật khẩu..." required>
            <small id="pwdError" class="text-danger font-weight-bold mt-1 d-block" style="display:none;"></small>
        </div>
    </div>

    <div class="modal-footer border-0 px-4 pb-3">
        <button type="submit" class="btn btn-info rounded-pill px-4 font-weight-bold text-white">
            <i class="fas fa-check-circle"></i> Cập nhật
        </button>
        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
            <i class="fas fa-times-circle"></i> Hủy
        </button>
    </div>
</form>

<script>
    function validatePasswordMatch() {
        const pass = document.getElementById('newPassword').value;
        const confirmPass = document.getElementById('confirmPassword').value;
        const errorElem = document.getElementById('pwdError');

        if (pass !== confirmPass) {
            errorElem.innerText = '⚠️ Mật khẩu xác nhận không trùng khớp!';
            errorElem.style.display = 'block';
            return false;
        }
        errorElem.style.display = 'none';
        return true;
    }
</script>