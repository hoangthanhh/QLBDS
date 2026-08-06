<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="edit-account" method="post">
    <div class="modal-header bg-gradient-primary text-white">
        <h5 class="modal-title">✏️ Chỉnh sửa thông tin tài khoản</h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${acc.id}"/>

        <!-- Họ tên -->
        <div class="form-group">
            <label for="fullName" class="font-weight-bold">👤 Họ tên</label>
            <input type="text" id="fullName" name="fullName"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.fullName}" placeholder="Nhập họ tên..." required/>
        </div>

        <!-- Email -->
        <div class="form-group">
            <label for="email" class="font-weight-bold">📧 Email</label>
            <input type="email" id="email" name="email"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.email}" placeholder="Nhập email..." required/>
        </div>

        <!-- Số điện thoại -->
        <div class="form-group">
            <label for="phone" class="font-weight-bold">📱 Số điện thoại</label>
            <input type="text" id="phone" name="phone"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${acc.phone}" placeholder="Nhập số điện thoại..."/>
        </div>

        <!-- Vai trò / Phân quyền (Yêu cầu Mục 2.B) -->
        <div class="form-group">
            <label for="role" class="font-weight-bold">🔑 Vai trò (Phân quyền)</label>
            <select id="role" name="role" class="form-control shadow-sm rounded-pill px-4 font-weight-bold" required>
                <option value="Customer" ${acc.role == 'Customer' ? 'selected' : ''}>Customer (Khách hàng)</option>
                <option value="Staff" ${acc.role == 'Staff' ? 'selected' : ''}>Staff (Nhân viên)</option>
                <option value="Admin" ${acc.role == 'Admin' ? 'selected' : ''}>Admin (Quản trị viên)</option>
            </select>
        </div>

        <!-- Trạng thái xác thực OTP (Yêu cầu Mục 2.B) -->
        <div class="form-group">
            <label for="isVerified" class="font-weight-bold">✅ Trạng thái xác thực OTP Email</label>
            <select id="isVerified" name="isVerified" class="form-control shadow-sm rounded-pill px-4" required>
                <option value="1" ${acc.isVerified == 1 ? 'selected' : ''}>Đã xác thực OTP</option>
                <option value="0" ${acc.isVerified == 0 ? 'selected' : ''}>Chưa xác thực</option>
            </select>
        </div>

        <!-- Địa chỉ -->
        <div class="form-group">
            <label for="address" class="font-weight-bold">🏠 Địa chỉ</label>
            <textarea id="address" name="address"
                      class="form-control shadow-sm rounded px-3"
                      placeholder="Nhập địa chỉ..." rows="2">${acc.address}</textarea>
        </div>
    </div>

    <!-- Nút thao tác -->
    <div class="modal-footer border-0 px-4 pb-3">
        <button type="submit" class="btn btn-primary rounded-pill px-4 font-weight-bold">
            <i class="fas fa-save"></i> Cập nhật
        </button>
        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
            <i class="fas fa-times-circle"></i> Hủy
        </button>
    </div>
</form>