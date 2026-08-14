package com.qlbds.repository;

import com.qlbds.entity.Transaction;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

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

    // Kiểm tra xem khách hàng này có giao dịch nào đang CHỜ DUYỆT cho BĐS này không
    public boolean hasPendingTransaction(Integer userId, Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.customer.id = :userId AND t.property.id = :propertyId AND t.status = 'PENDING'";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để chặn an toàn nếu có lỗi DB
        }
    }

    // Kiểm tra xem khách hàng này ĐÃ CỌC THÀNH CÔNG BĐS này chưa (Để cho phép mua)
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

    // Thêm vào TransactionRepository.java
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

    // Lấy danh sách giao dịch của 1 User (Có phân trang)
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

    // Đếm tổng số giao dịch để tính trang
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

    // Lấy toàn bộ giao dịch cho Admin (Sắp xếp mới nhất lên đầu)
    // 1. Trả lại thứ tự MỚI NHẤT LÊN ĐẦU
    public List<Transaction> findAllForAdmin(String statusFilter, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT t FROM Transaction t JOIN FETCH t.customer JOIN FETCH t.property");

            if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("ALL")) {
                hql.append(" WHERE t.status = :status");
            }
            hql.append(" ORDER BY t.id DESC");

            org.hibernate.query.Query<Transaction> query = session.createQuery(hql.toString(), Transaction.class);
            if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("ALL")) {
                query.setParameter("status", com.qlbds.constant.TransactionStatusEnum.valueOf(statusFilter));
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 2. THÊM MỚI: Lấy danh sách các giao dịch PENDING của những người "chậm chân"
    public List<Transaction> findOtherPendingTransactions(Integer propertyId, Integer approvedTxId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy các giao dịch cùng Property, nhưng KHÁC ID với giao dịch vừa được duyệt, và đang PENDING
            String hql = "SELECT t FROM Transaction t JOIN FETCH t.customer JOIN FETCH t.property " +
                    "WHERE t.property.id = :propId AND t.id != :approvedId AND t.status = 'PENDING'";
            org.hibernate.query.Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("propId", propertyId);
            query.setParameter("approvedId", approvedTxId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 2. THÊM MỚI: Hàm kiểm tra xem có giao dịch nào CŨ HƠN đang chờ duyệt không
    public boolean hasOlderPendingTransaction(Integer propertyId, LocalDateTime currentTxTime) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Tìm giao dịch cùng BĐS, trạng thái PENDING, và thời gian tạo < thời gian của giao dịch đang xét
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.property.id = :propId AND t.status = 'PENDING' AND t.createdAt < :txTime";
            org.hibernate.query.Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("propId", propertyId);
            query.setParameter("txTime", currentTxTime);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để chặn an toàn nếu có lỗi DB
        }
    }

    // Đếm tổng giao dịch
    public long countAll(String statusFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(t) FROM Transaction t");
            if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("ALL")) {
                hql.append(" WHERE t.status = :status");
            }

            org.hibernate.query.Query<Long> query = session.createQuery(hql.toString(), Long.class);
            if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("ALL")) {
                query.setParameter("status", com.qlbds.constant.TransactionStatusEnum.valueOf(statusFilter));
            }
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Lấy giao dịch theo ID (Dùng khi duyệt)
    public Transaction findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy kèm Customer và Property để lát gửi Email
            String hql = "SELECT t FROM Transaction t JOIN FETCH t.customer JOIN FETCH t.property WHERE t.id = :id";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Cập nhật giao dịch (Duyệt/Từ chối)
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
}