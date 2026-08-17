package com.qlbds.repository;

import com.qlbds.constant.TransactionStatusEnum;
import com.qlbds.entity.Transaction;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    // Chung (CRUD)

    // Lưu mới giao dịch vào CSDL
    public boolean save(Transaction transaction) {
        org.hibernate.Transaction hbTx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hbTx = session.beginTransaction();
            session.save(transaction);
            hbTx.commit();
            return true;
        } catch (Exception e) {
            if (hbTx != null) hbTx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Tìm giao dịch theo ID kèm thông tin Khách hàng và BĐS
    public Transaction findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transaction t JOIN FETCH t.customer JOIN FETCH t.property WHERE t.id = :id";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Cập nhật trạng thái hoặc thông tin giao dịch
    public boolean update(Transaction transaction) {
        org.hibernate.Transaction hbTx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hbTx = session.beginTransaction();
            session.update(transaction);
            hbTx.commit();
            return true;
        } catch (Exception e) {
            if (hbTx != null) hbTx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Customer

    // Kiểm tra xem khách hàng có yêu cầu nào đang Chờ duyệt (PENDING) cho BĐS này không
    public boolean hasPendingTransaction(Integer userId, Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.customer.id = :userId AND t.property.id = :propertyId AND t.status = 'PENDING'";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // Kiểm tra xem khách hàng đã Đặt cọc thành công BĐS này chưa (để cho phép Mua)
    public boolean hasCompletedDeposit(Integer userId, Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.customer.id = :userId AND t.property.id = :propertyId AND t.transactionType = 'DEPOSIT' AND t.status = 'COMPLETED'";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Khách hàng tự hủy yêu cầu giao dịch đang ở trạng thái PENDING
    public boolean cancelPendingTransaction(Integer userId, Integer propertyId) {
        org.hibernate.Transaction hbTx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hbTx = session.beginTransaction();
            String hql = "UPDATE Transaction t SET t.status = 'CANCELLED' WHERE t.customer.id = :userId AND t.property.id = :propertyId AND t.status = 'PENDING'";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            int updatedCount = query.executeUpdate();
            hbTx.commit();
            return updatedCount > 0;
        } catch (Exception e) {
            if (hbTx != null) hbTx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách lịch sử giao dịch của 1 khách hàng (kèm ảnh và phân trang)
    public List<Transaction> findTransactionsByUserId(Integer userId, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transaction t JOIN FETCH t.property p LEFT JOIN FETCH p.images WHERE t.customer.id = :userId ORDER BY t.id DESC";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("userId", userId);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Đếm tổng số giao dịch của khách hàng để phân trang
    public long countTransactionsByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.customer.id = :userId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Staff & Admin

    // Lấy danh sách giao dịch có lọc nâng cao (Từ khóa, Khoảng ngày, Trạng thái) kèm phân trang
    public List<Transaction> findTransactionsWithFilter(String keyword, String startDate, String endDate, String statusFilter, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT t FROM Transaction t JOIN FETCH t.customer c JOIN FETCH t.property p WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                hql.append(" AND (LOWER(c.fullName) LIKE :kw OR LOWER(c.email) LIKE :kw OR c.phone LIKE :kw OR LOWER(t.transactionCode) LIKE :kw)");
            }
            if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                hql.append(" AND t.status = :status");
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                hql.append(" AND t.createdAt >= :startDate");
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                hql.append(" AND t.createdAt <= :endDate");
            }
            hql.append(" ORDER BY t.id DESC");

            Query<Transaction> query = session.createQuery(hql.toString(), Transaction.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                query.setParameter("status", TransactionStatusEnum.valueOf(statusFilter));
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                query.setParameter("startDate", LocalDate.parse(startDate.trim()).atStartOfDay());
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                query.setParameter("endDate", LocalDate.parse(endDate.trim()).atTime(LocalTime.MAX));
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Đếm tổng số giao dịch theo bộ lọc nâng cao để tính số trang
    public long countTransactionsWithFilter(String keyword, String startDate, String endDate, String statusFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(t) FROM Transaction t JOIN t.customer c WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                hql.append(" AND (LOWER(c.fullName) LIKE :kw OR LOWER(c.email) LIKE :kw OR c.phone LIKE :kw OR LOWER(t.transactionCode) LIKE :kw)");
            }
            if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                hql.append(" AND t.status = :status");
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                hql.append(" AND t.createdAt >= :startDate");
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                hql.append(" AND t.createdAt <= :endDate");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            if (statusFilter != null && !statusFilter.isEmpty() && !"ALL".equals(statusFilter)) {
                query.setParameter("status", TransactionStatusEnum.valueOf(statusFilter));
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                query.setParameter("startDate", LocalDate.parse(startDate.trim()).atStartOfDay());
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                query.setParameter("endDate", LocalDate.parse(endDate.trim()).atTime(LocalTime.MAX));
            }

            Long count = query.uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // Tìm các giao dịch PENDING khác trên cùng 1 BĐS (dùng để tự động từ chối những người đến sau)
    public List<Transaction> findOtherPendingTransactions(Integer propertyId, Integer approvedTxId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transaction t JOIN FETCH t.customer JOIN FETCH t.property " +
                    "WHERE t.property.id = :propId AND t.id != :approvedId AND t.status = 'PENDING'";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("propId", propertyId);
            query.setParameter("approvedId", approvedTxId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Kiểm tra luật ưu tiên: Có yêu cầu nào gửi trước (thời gian tạo nhỏ hơn) đang chờ duyệt không
    public boolean hasOlderPendingTransaction(Integer propertyId, LocalDateTime currentTxTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.property.id = :propId AND t.status = 'PENDING' AND t.createdAt < :txTime";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("propId", propertyId);
            query.setParameter("txTime", currentTxTime);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    // Kiểm tra BĐS có ĐANG BỊ RÀNG BUỘC bởi bất kỳ giao dịch PENDING nào không (Dùng cho Admin/Staff khi Sửa/Xóa BĐS)
    public boolean hasAnyPendingTransaction(Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.property.id = :propId AND t.status = 'PENDING'";
            org.hibernate.query.Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("propId", propertyId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để chặn an toàn (Fail-safe) nếu lỗi DB
        }
    }

    // Hủy giao dịch Đặt cọc thành công khi Admin chọn Mở bán lại (Bể kèo)
    // ĐÃ SỬA: Đổi trạng thái thành FORFEITED để giữ lại doanh thu cọc
    public boolean cancelCompletedDepositByProperty(Integer propertyId) {
        org.hibernate.Transaction hbTx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hbTx = session.beginTransaction();

            // Chuyển status thành FORFEITED và lưu lại lý do vào cột rejectReason
            String hql = "UPDATE Transaction t SET t.status = 'FORFEITED', t.rejectReason = 'Khách hàng hủy kèo, công ty thu hồi tiền cọc' " +
                    "WHERE t.property.id = :propertyId AND t.transactionType = 'DEPOSIT' AND t.status = 'COMPLETED'";

            Query query = session.createQuery(hql);
            query.setParameter("propertyId", propertyId);
            query.executeUpdate();
            hbTx.commit();
            return true;
        } catch (Exception e) {
            if (hbTx != null) hbTx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}