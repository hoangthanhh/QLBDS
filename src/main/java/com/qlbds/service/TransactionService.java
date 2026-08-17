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

    // Customer

    // Khởi tạo yêu cầu Đặt cọc (10%) hoặc Mua BĐS (Pending)
    public String processTransaction(Integer userId, Integer propertyId, String type) {
        Property property = propertyRepository.findById(propertyId);
        if (property == null) return "Bất động sản không tồn tại hoặc đã bị xóa!";

        // Kiểm tra nếu khách đang có giao dịch Pending cho BĐS này
        if (transactionRepository.hasPendingTransaction(userId, propertyId)) {
            return "Bạn đang có một yêu cầu chờ xác nhận cho Bất động sản này. Vui lòng chờ phản hồi hoặc hủy yêu cầu cũ trước khi thực hiện giao dịch mới!";
        }

        // Kiểm tra trạng thái BĐS đã bán hoặc đã cọc
        if (property.getStatus() == PropertyStatusEnum.SOLD) {
            return "Rất tiếc, Bất động sản này đã được bán thành công cho khách hàng khác!";
        }

        if (property.getStatus() == PropertyStatusEnum.DEPOSITED) {
            if ("DEPOSIT".equals(type)) {
                return "Bất động sản này đã được đặt cọc, bạn không thể cọc thêm!";
            }
            if ("BUY".equals(type)) {
                if (!transactionRepository.hasCompletedDeposit(userId, propertyId)) {
                    return "Bất động sản này đang được đặt cọc bởi khách hàng khác, bạn không thể mua!";
                }
            }
        }

        double calculatedAmount = "DEPOSIT".equals(type) ? (property.getPrice() * 0.1) : property.getPrice();
        Long finalAmount = (long) calculatedAmount;

        User userMapping = new User();
        userMapping.setId(userId);

        Transaction tx = new Transaction();
        tx.setTransactionCode("TX-" + System.currentTimeMillis());
        tx.setTransactionType(TransactionTypeEnum.valueOf(type));
        tx.setStatus(TransactionStatusEnum.PENDING);
        tx.setAmount(finalAmount);
        tx.setCustomer(userMapping);
        tx.setProperty(property);

        return transactionRepository.save(tx) ? "SUCCESS" : "Lỗi hệ thống khi khởi tạo giao dịch!";
    }

    // Khách hàng hủy giao dịch đang Pending
    public boolean cancelTransaction(Integer userId, Integer propertyId) {
        return transactionRepository.cancelPendingTransaction(userId, propertyId);
    }

    // Lấy danh sách lịch sử giao dịch đóng gói vào DTO
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

    // Tính tổng số trang lịch sử giao dịch của khách hàng
    public int getTotalTransactionPages(Integer userId, int pageSize) {
        long totalRecords = transactionRepository.countTransactionsByUserId(userId);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // Staff & Admin

    // Lấy danh sách giao dịch quản lý đóng gói vào AdminTransactionDTO (có lọc đa điều kiện)
    public List<AdminTransactionDTO> getManagementTransactions(String keyword, String startDate, String endDate, String statusFilter, int page, int pageSize) {
        List<Transaction> entities = transactionRepository.findTransactionsWithFilter(keyword, startDate, endDate, statusFilter, page, pageSize);
        List<AdminTransactionDTO> dtos = new ArrayList<>();
        for (Transaction tx : entities) {
            AdminTransactionDTO dto = new AdminTransactionDTO();
            dto.setId(tx.getId());
            dto.setTransactionCode(tx.getTransactionCode());
            dto.setAmount(tx.getAmount());
            dto.setType(tx.getTransactionType());
            dto.setStatus(tx.getStatus());
            dto.setCreatedAt(tx.getCreatedAt());
            dto.setRejectReason(tx.getRejectReason());

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

    // Tính tổng số trang quản lý giao dịch theo bộ lọc
    public int getTotalManagementPages(String keyword, String startDate, String endDate, String statusFilter, int pageSize) {
        long total = transactionRepository.countTransactionsWithFilter(keyword, startDate, endDate, statusFilter);
        return (int) Math.ceil((double) total / pageSize);
    }

    // Duyệt giao dịch: Cập nhật trạng thái BĐS, gửi mail thành công và tự động từ chối các đơn sau
    public String approveTransaction(Integer txId) {
        Transaction tx = transactionRepository.findById(txId);
        if (tx == null || tx.getStatus() != TransactionStatusEnum.PENDING) {
            return "Giao dịch không tồn tại hoặc đã được xử lý!";
        }

        // Kiểm tra luật ưu tiên First-Come First-Served
        if (transactionRepository.hasOlderPendingTransaction(tx.getProperty().getId(), tx.getCreatedAt())) {
            return "Cảnh báo: Có khách hàng khác đã gửi yêu cầu cho BĐS này TRƯỚC. Vui lòng kéo xuống dưới để ưu tiên duyệt cho giao dịch cũ hơn!";
        }

        tx.setStatus(TransactionStatusEnum.COMPLETED);
        Property p = tx.getProperty();
        if (tx.getTransactionType() == TransactionTypeEnum.DEPOSIT) {
            p.setStatus(PropertyStatusEnum.DEPOSITED);
        } else if (tx.getTransactionType() == TransactionTypeEnum.BUY) {
            p.setStatus(PropertyStatusEnum.SOLD);
        }

        propertyRepository.update(p);
        if (!transactionRepository.update(tx)) return "Lỗi cập nhật CSDL!";

        // Gửi email xác nhận thành công
        new Thread(() -> {
            EmailUtil.sendTransactionEmail(
                    tx.getCustomer().getEmail(),
                    tx.getCustomer().getFullName(),
                    p.getTitle(),
                    tx.getTransactionType().name(),
                    tx.getAmount().doubleValue()
            );
        }).start();

        // Tự động từ chối những người nộp đơn sau
        List<Transaction> loserTransactions = transactionRepository.findOtherPendingTransactions(p.getId(), tx.getId());
        String autoRejectReason = "Rất tiếc, Bất động sản này vừa được chốt giao dịch với một khách hàng đã tạo yêu cầu trước bạn. Mong bạn thông cảm!";

        for (Transaction loserTx : loserTransactions) {
            loserTx.setStatus(TransactionStatusEnum.REJECTED);
            loserTx.setRejectReason(autoRejectReason);
            transactionRepository.update(loserTx);

            new Thread(() -> {
                EmailUtil.sendRejectEmail(loserTx.getCustomer().getEmail(), loserTx.getCustomer().getFullName(), p.getTitle(), autoRejectReason);
            }).start();
        }

        return "SUCCESS";
    }

    // Từ chối giao dịch kèm lý do và gửi mail thông báo
    public String rejectTransaction(Integer txId, String reason) {
        Transaction tx = transactionRepository.findById(txId);
        if (tx == null || tx.getStatus() != TransactionStatusEnum.PENDING) {
            return "Giao dịch không tồn tại hoặc đã được xử lý!";
        }

        tx.setStatus(TransactionStatusEnum.REJECTED);
        tx.setRejectReason(reason);

        if (!transactionRepository.update(tx)) return "Lỗi cập nhật CSDL!";

        new Thread(() -> {
            EmailUtil.sendRejectEmail(tx.getCustomer().getEmail(), tx.getCustomer().getFullName(), tx.getProperty().getTitle(), reason);
        }).start();

        return "SUCCESS";
    }
}