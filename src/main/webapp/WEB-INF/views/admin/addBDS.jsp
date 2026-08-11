<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="${pageContext.request.contextPath}/admin/bds/add" method="post" enctype="multipart/form-data">
    <div class="modal-header bg-gradient-success text-white">
        <h5 class="modal-title">🏠 Thêm Bất Động Sản Mới</h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <!-- Mã BĐS -->
        <div class="form-group">
            <label for="mabds" class="font-weight-bold">🔢 Mã BĐS</label>
            <input type="text" id="mabds" name="mabds"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập mã BĐS (ví dụ: BDS001)..." required>
        </div>

        <!-- Tiêu đề BĐS -->
        <div class="form-group">
            <label for="name" class="font-weight-bold">🏷️ Tiêu đề BĐS</label>
            <input type="text" id="name" name="name"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập tiêu đề BĐS (ví dụ: Căn hộ Vinhomes 2PN)..." required>
        </div>

        <!-- Địa chỉ chi tiết (Yêu cầu Mục 2.A) -->
        <div class="form-group">
            <label for="address" class="font-weight-bold">📍 Địa chỉ</label>
            <input type="text" id="address" name="address"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập địa chỉ chi tiết (Số nhà, Đường, Quận/Huyện)..." required>
        </div>

        <!-- Loại hình BĐS -->
        <div class="form-group">
            <label for="category_id" class="font-weight-bold">📂 Loại hình BĐS</label>
            <select id="category_id" name="category_id" class="form-control shadow-sm rounded-pill px-4" required>
                <option value="">-- Chọn loại hình (Căn hộ, Nhà riêng, Đất nền) --</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.cid}">${category.cname}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Giá bán -->
        <div class="form-group">
            <label for="price" class="font-weight-bold">💲 Giá bán (VNĐ)</label>
            <input type="number" id="price" name="price" min="0" step="100000"
                   class="form-control shadow-sm rounded-pill px-4"
                   placeholder="Nhập giá bán (ví dụ: 2500000000)..." required>
        </div>

        <!-- Mô tả chi tiết -->
        <div class="form-group">
            <label for="description" class="font-weight-bold">📝 Mô tả chi tiết</label>
            <textarea id="description" name="description"
                      class="form-control shadow-sm rounded px-3"
                      placeholder="Nhập thông tin diện tích, pháp lý, số phòng..." rows="3"></textarea>
        </div>

        <!-- Upload nhiều file ảnh minh họa (Yêu cầu đặc biệt Mục 2.A) -->
        <div class="form-group">
            <label for="images" class="font-weight-bold">🖼️ Upload nhiều ảnh minh họa</label>
            <input type="file" id="images" name="images"
                   class="form-control-file border p-2 rounded" multiple accept="image/*" required>
            <small class="form-text text-muted">Nhấn giữ Ctrl (hoặc Shift) để chọn nhiều ảnh upload cùng lúc.</small>
        </div>

        <!-- Nút thao tác -->
        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-success rounded-pill px-4">
                <i class="fas fa-plus-circle"></i> Thêm BĐS
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
    </div>
</form>