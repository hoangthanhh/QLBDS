<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form action="updateTransactionStatus" method="post">
    <div class="modal-header bg-gradient-info text-white">
        <h5 class="modal-title">📑 Chi Tiết Giao Dịch #${trans.id}</h5>
        <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>

    <div class="modal-body px-4 pt-3 pb-1">
        <input type="hidden" name="transactionId" value="${trans.id}"/>

        <div class="row">
            <!-- Cột trái: Thông tin Khách hàng & BĐS -->
            <div class="col-md-7 border-right">
                <h6 class="font-weight-bold text-primary mb-3"><i class="fas fa-user-tag me-1"></i> Thông Tin Khách Hàng
                </h6>
                <p class="mb-1"><strong>Họ tên:</strong> ${trans.customerName}</p>
                <p class="mb-1"><strong>Email:</strong> ${trans.customerEmail}</p>
                <p class="mb-3"><strong>Số điện thoại:</strong> ${trans.customerPhone}</p>

                <hr>

                <h6 class="font-weight-bold text-primary mb-3"><i class="fas fa-building me-1"></i> Bất Động Sản Giao
                    Dịch</h6>
                <p class="mb-1"><strong>Tiêu đề:</strong> <span
                        class="text-dark font-weight-bold">${trans.propertyTitle}</span></p>
                <p class="mb-1"><strong>Địa chỉ:</strong> ${trans.propertyAddress}</p>
                <p class="mb-1"><strong>Giá niêm yết:</strong>
                    <span class="text-danger font-weight-bold">
                        <fmt:formatNumber value="${trans.propertyPrice}" type="number" maxFractionDigits="0"/> VNĐ
                    </span>
                </p>
            </div>

            <!-- Cột phải: Thông tin & Duyệt trạng thái giao dịch -->
            <div class="col-md-5">
                <h6 class="font-weight-bold text-success mb-3"><i class="fas fa-file-invoice-dollar me-1"></i> Chi Tiết
                    Thanh Toán</h6>
                <p class="mb-1"><strong>Loại giao dịch:</strong> <span
                        class="badge badge-info px-2 py-1">${trans.type}</span></p>
                <p class="mb-1"><strong>Số tiền đặt cọc/thanh toán:</strong></p>
                <h5 class="text-danger font-weight-bold mb-3">
                    <fmt:formatNumber value="${trans.amount}" type="number" maxFractionDigits="0"/> VNĐ
                </h5>
                <p class="mb-3 text-muted small"><strong>Ngày tạo GD:</strong> ${trans.createdDate}</p>

                <hr>

                <!-- Cập nhật trạng thái -->
                <div class="form-group mb-2">
                    <label for="modalStatus" class="font-weight-bold text-dark">Cập nhật trạng thái:</label>
                    <select id="modalStatus" name="status" class="form-control font-weight-bold rounded-pill"
                            onchange="toggleModalCancelReason(this)">
                        <option value="Pending" ${trans.status == 'Pending' ? 'selected' : ''}>Pending (Chờ duyệt)
                        </option>
                        <option value="Completed" ${trans.status == 'Completed' ? 'selected' : ''}>Completed (Thành
                            công)
                        </option>
                        <option value="Cancelled" ${trans.status == 'Cancelled' ? 'selected' : ''}>Cancelled (Đã hủy)
                        </option>
                    </select>
                </div>

                <!-- Lý do hủy -->
                <div class="form-group" id="modalCancelReasonGroup"
                     style="display: ${trans.status == 'Cancelled' ? 'block' : 'none'};">
                    <label for="cancelReason" class="font-weight-bold text-danger">Lý do hủy giao dịch:</label>
                    <textarea name="cancelReason" id="cancelReason" class="form-control rounded" rows="2"
                              placeholder="Nhập lý do hủy...">${trans.cancelReason}</textarea>
                </div>

                <div class="alert alert-warning p-2 small mt-2">
                    <i class="fas fa-info-circle"></i> Khi đổi sang <strong>Completed</strong>, hệ thống sẽ tự động gửi
                    Email xác nhận bằng JavaMail API.
                </div>
            </div>
        </div>
    </div>

    <!-- Nút thao tác -->
    <div class="modal-footer border-0 px-4 pb-3">
        <button type="submit" class="btn btn-success rounded-pill px-4 font-weight-bold">
            <i class="fas fa-save me-1"></i> Lưu & Cập nhật
        </button>
        <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-dismiss="modal">
            <i class="fas fa-times-circle"></i> Đóng
        </button>
    </div>
</form>

<script>
    function toggleModalCancelReason(selectElem) {
        const reasonGroup = document.getElementById('modalCancelReasonGroup');
        if (selectElem.value === 'Cancelled') {
            reasonGroup.style.display = 'block';
        } else {
            reasonGroup.style.display = 'none';
        }
    }
</script>