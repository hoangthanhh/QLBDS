package com.qlbds.service;

import com.qlbds.constant.PropertyStatusEnum;
import com.qlbds.constant.TransactionStatusEnum;
import com.qlbds.constant.TransactionTypeEnum;
import com.qlbds.dto.admin.AdminTransactionDTO;
import com.qlbds.dto.user.TransactionHistoryDTO;
import com.qlbds.entity.Property;
import com.qlbds.entity.Transaction;
import com.qlbds.entity.User;
import com.qlbds.repository.PropertyRepository;
import com.qlbds.repository.TransactionRepository;
import com.qlbds.util.EmailUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private PropertyRepository propertyRepository = new PropertyRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public String processTransaction(Integer userId, Integer propertyId, String type) {
        Property property = propertyRepository.findById(propertyId);
        if (property == null) return "Bất động sản không tồn tại hoặc đã bị xóa!";

        // QUY TẮC 1: Nếu khách đã bấm cọc/mua và đang chờ (PENDING) -> Chặn không cho bấm tiếp
        if (transactionRepository.hasPendingTransaction(userId, propertyId)) {
            return "Bạn đang có một yêu cầu chờ xác nhận cho Bất động sản này. Vui lòng chờ phản hồi hoặc hủy yêu cầu cũ trước khi thực hiện giao dịch mới!";
        }

        // QUY TẮC 2 & 3: Kiểm tra dựa trên trạng thái Bất động sản
        if (property.getStatus() == PropertyStatusEnum.SOLD) {
            return "Rất tiếc, Bất động sản này đã được bán thành công cho khách hàng khác!";
        }

        if (property.getStatus() == PropertyStatusEnum.DEPOSITED) {
            if ("DEPOSIT".equals(type)) {
                return "Bất động sản này đã được đặt cọc, bạn không thể cọc thêm!";
            }
            if ("BUY".equals(type)) {
                // Chỉ cho phép MUA nếu chính user này là người đã cọc thành công
                if (!transactionRepository.hasCompletedDeposit(userId, propertyId)) {
                    return "Bất động sản này đang được đặt cọc bởi khách hàng khác, bạn không thể mua!";
                }
            }
        }

        double calculatedAmount = "DEPOSIT".equals(type) ? (property.getPrice() * 0.1) : property.getPrice();
        Long finalAmount = (long) calculatedAmount;

        // Dùng Entity Mapping để gán khóa ngoại nhanh, không cần query lại bảng User
        User userMapping = new User();
        userMapping.setId(userId);

        Transaction tx = new Transaction();
        tx.setTransactionCode("TX-" + System.currentTimeMillis());
        tx.setTransactionType(TransactionTypeEnum.valueOf(type));
        tx.setStatus(TransactionStatusEnum.PENDING);
        tx.setAmount(finalAmount);
        tx.setCustomer(userMapping);
        tx.setProperty(property);

        // ĐÃ XÓA LOGIC GỬI EMAIL Ở ĐÂY. Sẽ chuyển sang luồng của Admin khi duyệt.

        return transactionRepository.save(tx) ? "SUCCESS" : "Lỗi hệ thống khi khởi tạo giao dịch!";
    }

    // Thêm vào TransactionService.java
    public boolean cancelTransaction(Integer userId, Integer propertyId) {
        return transactionRepository.cancelPendingTransaction(userId, propertyId);
    }

    public List<TransactionHistoryDTO> getTransactionHistory(Integer userId, int page, int pageSize) {
        List<Transaction> entities = transactionRepository.findTransactionsByUserId(userId, page, pageSize);
        List<TransactionHistoryDTO> dtos = new ArrayList<>();

        for (Transaction tx : entities) {
            TransactionHistoryDTO dto = new TransactionHistoryDTO();
            dto.setTransactionCode(tx.getTransactionCode());
            dto.setAmount(tx.getAmount());
            dto.setType(tx.getTransactionType());
            dto.setStatus(tx.getStatus());
            dto.setCreatedAt(tx.getCreatedAt());

            Property p = tx.getProperty();
            if (p != null) {
                dto.setPropertyId(p.getId());
                dto.setPropertyTitle(p.getTitle());
                if (p.getImages() != null && !p.getImages().isEmpty()) {
                    dto.setThumbnail(p.getImages().get(0).getImagePath());
                } else {
                    dto.setThumbnail("assets/customer/img/property-1.jpg");
                }
            }
            dtos.add(dto);
        }
        return dtos;
    }

    public int getTotalTransactionPages(Integer userId, int pageSize) {
        long totalRecords = transactionRepository.countTransactionsByUserId(userId);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // --- CÁC HÀM DÀNH CHO ADMIN ---

    // 1. Lấy danh sách hiển thị
    public List<AdminTransactionDTO> getTransactionsForAdmin(String statusFilter, int page, int pageSize) {
        List<Transaction> entities = transactionRepository.findAllForAdmin(statusFilter, page, pageSize);
        List<AdminTransactionDTO> dtos = new ArrayList<>();
        for (Transaction tx : entities) {
            AdminTransactionDTO dto = new AdminTransactionDTO();
            dto.setId(tx.getId());
            dto.setTransactionCode(tx.getTransactionCode());
            dto.setAmount(tx.getAmount());
            dto.setType(tx.getTransactionType());
            dto.setStatus(tx.getStatus());
            dto.setCreatedAt(tx.getCreatedAt());

            if (tx.getCustomer() != null) {
                dto.setCustomerName(tx.getCustomer().getFullName());
                dto.setCustomerEmail(tx.getCustomer().getEmail());
                dto.setCustomerPhone(tx.getCustomer().getPhone());
            }
            if (tx.getProperty() != null) {
                dto.setPropertyId(tx.getProperty().getId());
                dto.setPropertyTitle(tx.getProperty().getTitle());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    public int getTotalPagesForAdmin(String statusFilter, int pageSize) {
        long total = transactionRepository.countAll(statusFilter);
        return (int) Math.ceil((double) total / pageSize);
    }

    // 2. Logic Duyệt Giao Dịch
    public String approveTransaction(Integer txId) {
        Transaction tx = transactionRepository.findById(txId);
        if (tx == null || tx.getStatus() != com.qlbds.constant.TransactionStatusEnum.PENDING) {
            return "Giao dịch không tồn tại hoặc đã được xử lý!";
        }

        // --- BƯỚC CHẶN: KIỂM TRA LUẬT FIRST COME FIRST SERVE ---
        if (transactionRepository.hasOlderPendingTransaction(tx.getProperty().getId(), tx.getCreatedAt())) {
            return "Cảnh báo: Có khách hàng khác đã gửi yêu cầu cho BĐS này TRƯỚC. Vui lòng kéo xuống dưới để ưu tiên duyệt cho giao dịch cũ hơn!";
        }
        // --------------------------------------------------------

        // 1. Nếu không có ai đặt trước -> Tiến hành cập nhật trạng thái
        tx.setStatus(com.qlbds.constant.TransactionStatusEnum.COMPLETED);
        Property p = tx.getProperty();
        if (tx.getTransactionType() == com.qlbds.constant.TransactionTypeEnum.DEPOSIT) {
            p.setStatus(com.qlbds.constant.PropertyStatusEnum.DEPOSITED);
        } else if (tx.getTransactionType() == com.qlbds.constant.TransactionTypeEnum.BUY) {
            p.setStatus(com.qlbds.constant.PropertyStatusEnum.SOLD);
        }

        propertyRepository.update(p);
        if (!transactionRepository.update(tx)) return "Lỗi cập nhật CSDL!";

        // Gửi Email Thành công cho người Thắng
        new Thread(() -> {
            com.qlbds.util.EmailUtil.sendTransactionEmail(
                    tx.getCustomer().getEmail(),
                    tx.getCustomer().getFullName(),
                    p.getTitle(),
                    tx.getTransactionType().name(),
                    tx.getAmount().doubleValue()
            );
        }).start();

        // 2. TỰ ĐỘNG XỬ LÝ NHỮNG NGƯỜI ĐẾN SAU (TỪ CHỐI & GỬI EMAIL)
        List<Transaction> loserTransactions = transactionRepository.findOtherPendingTransactions(p.getId(), tx.getId());
        String autoRejectReason = "Rất tiếc, Bất động sản này vừa được chốt giao dịch với một khách hàng đã tạo yêu cầu trước bạn. Mong bạn thông cảm!";

        for (Transaction loserTx : loserTransactions) {
            loserTx.setStatus(com.qlbds.constant.TransactionStatusEnum.REJECTED);
            loserTx.setRejectReason(autoRejectReason);
            transactionRepository.update(loserTx);

            new Thread(() -> {
                 EmailUtil.sendRejectEmail(loserTx.getCustomer().getEmail(), loserTx.getCustomer().getFullName(), p.getTitle(), autoRejectReason);
            }).start();
        }

        return "SUCCESS";
    }

    // 3. Logic Từ Chối Giao Dịch
    public String rejectTransaction(Integer txId, String reason) {
        Transaction tx = transactionRepository.findById(txId);
        if (tx == null || tx.getStatus() != TransactionStatusEnum.PENDING) {
            return "Giao dịch không tồn tại hoặc đã được xử lý!";
        }

        tx.setStatus(TransactionStatusEnum.REJECTED);

        if (!transactionRepository.update(tx)) return "Lỗi cập nhật CSDL!";

        // Gửi Email Từ chối bất đồng bộ (Cần thêm hàm sendRejectEmail vào EmailUtil)
        new Thread(() -> {
            EmailUtil.sendRejectEmail(tx.getCustomer().getEmail(), tx.getCustomer().getFullName(), tx.getProperty().getTitle(), reason);
        }).start();

        return "SUCCESS";
    }
}