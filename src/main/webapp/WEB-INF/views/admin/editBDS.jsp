<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form method="post" action="edit-bds" enctype="multipart/form-data">
    <div class="modal-header bg-gradient-warning text-white">
        <h5 class="modal-title">✏️ Chỉnh sửa Bất Động Sản</h5>
        <button type="button" class="close text-white" data-dismiss="modal">×</button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="id" value="${bds.id}" />
        <input type="hidden" name="oldImage" value="${bds.image}" />

        <!-- Mã BĐS -->
        <div class="form-group">
            <label for="mabds" class="font-weight-bold">🔢 Mã BĐS</label>
            <input type="text" id="mabds" name="mabds"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${bds.masp}" required />
        </div>

        <!-- Tiêu đề BĐS -->
        <div class="form-group">
            <label for="name" class="font-weight-bold">🏷️ Tiêu đề BĐS</label>
            <input type="text" id="name" name="name"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${bds.name}" required />
        </div>

        <!-- Địa chỉ chi tiết (Mục 2.A) -->
        <div class="form-group">
            <label for="address" class="font-weight-bold">📍 Địa chỉ</label>
            <input type="text" id="address" name="address"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${bds.address}" required />
        </div>

        <!-- Loại hình BĐS -->
        <div class="form-group">
            <label for="category_id" class="font-weight-bold">📂 Loại hình BĐS</label>
            <select id="category_id" name="category_id" class="form-control shadow-sm rounded-pill px-4" required>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.cid}" ${category.cid == bds.categoryId ? 'selected' : ''}>
                            ${category.cname}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Giá bán -->
        <div class="form-group">
            <label for="price" class="font-weight-bold">💲 Giá bán (VNĐ)</label>
            <input type="number" id="price" name="price" min="0" step="100000"
                   class="form-control shadow-sm rounded-pill px-4"
                   value="${bds.price}" required />
        </div>

        <!-- Mô tả -->
        <div class="form-group">
            <label for="description" class="font-weight-bold">📝 Mô tả chi tiết</label>
            <textarea id="description" name="description"
                      class="form-control shadow-sm rounded px-3"
                      rows="3" placeholder="Mô tả thông tin diện tích, số phòng, pháp lý...">${bds.description}</textarea>
        </div>

        <!-- Ảnh hiện tại -->
        <div class="form-group">
            <label class="font-weight-bold">🖼️ Ảnh hiện tại</label><br>
            <c:choose>
                <c:when test="${not empty bds.image}">
                    <img src="${pageContext.request.contextPath}/image?file=${fn:substringAfter(bds.image, 'image\\')}"
                         alt="${bds.name}" class="img-thumbnail mb-2" style="width: 200px; height: auto;">
                </c:when>
                <c:otherwise>
                    <p><i>Không có ảnh</i></p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Upload ảnh mới/bổ sung -->
        <div class="form-group">
            <label for="images" class="font-weight-bold">📤 Chọn ảnh mới (nếu muốn thay thế/thêm mới)</label>
            <input type="file" id="images" name="images" multiple
                   class="form-control-file border p-2 rounded" accept="image/*" />
            <small class="form-text text-muted">Bỏ trống nếu giữ nguyên ảnh cũ. Nhấn Ctrl để chọn nhiều ảnh.</small>
        </div>

        <!-- Nút thao tác -->
        <div class="modal-footer border-0 px-0 pt-3">
            <button type="submit" class="btn btn-warning rounded-pill px-4 text-white">
                <i class="fas fa-save"></i> Lưu thay đổi
            </button>
            <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
                <i class="fas fa-times-circle"></i> Hủy
            </button>
        </div>
    </div>
</form>