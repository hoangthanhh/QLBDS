package com.qlbds.service;

import com.qlbds.constant.TransactionStatusEnum;
import com.qlbds.constant.TransactionTypeEnum;
import com.qlbds.entity.Property;
import com.qlbds.entity.Transaction;
import com.qlbds.entity.User;
import com.qlbds.repository.PropertyRepository;
import com.qlbds.repository.TransactionRepository;
import com.qlbds.util.EmailUtil;

public class TransactionService {

    private PropertyRepository propertyRepository = new PropertyRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public String processTransaction(User user, Integer propertyId, String type) {
        Property property = propertyRepository.findById(propertyId);
        if (property == null) {
            return "Bất động sản không tồn tại hoặc đã bị xóa!";
        }

        // Tính toán số tiền và ép kiểu về Long cho khớp với thuộc tính 'amount' trong Entity
        double calculatedAmount = "DEPOSIT".equals(type) ? (property.getPrice() * 0.1) : property.getPrice();
        Long finalAmount = (long) calculatedAmount;

        Transaction tx = new Transaction();

        // 1. Tự động tạo mã giao dịch ngẫu nhiên (Ví dụ: TX-171829381293)
        tx.setTransactionCode("TX-" + System.currentTimeMillis());

        // 2. Gán các thông tin Enum
        tx.setTransactionType(TransactionTypeEnum.valueOf(type));
        tx.setStatus(TransactionStatusEnum.PENDING);
        tx.setAmount(finalAmount);

        // 3. Sửa 'setUser' thành 'setCustomer' cho khớp với Entity
        tx.setCustomer(user);
        tx.setProperty(property);

        // LƯU Ý: Đã bỏ dòng tx.setCreatedAt() vì CSDL sẽ tự động tạo do (insertable = false)

        boolean saved = transactionRepository.save(tx);
        if (!saved) {
            return "Lỗi hệ thống khi khởi tạo giao dịch!";
        }

        // Gửi email bất đồng bộ để không làm chậm trang web
        new Thread(() -> {
            EmailUtil.sendTransactionEmail(user.getEmail(), user.getFullName(), property.getTitle(), type, calculatedAmount);
        }).start();

        return "SUCCESS";
    }
}