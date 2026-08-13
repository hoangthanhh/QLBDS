package com.qlbds.repository;

import com.qlbds.entity.ViewLog;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ViewLogRepository {

    // Xóa log cũ (Nếu khách hàng xem lại BĐS đã từng xem)
    public void deleteOldLog(Integer userId, Integer propertyId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "DELETE FROM ViewLog v WHERE v.user.id = :userId AND v.property.id = :propertyId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            query.setParameter("propertyId", propertyId);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Lưu lịch sử xem mới
    public boolean saveLog(ViewLog log) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(log);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách BĐS đã xem (Dùng JOIN FETCH để lấy luôn ảnh, tránh lỗi N+1)
    public List<ViewLog> findLogsByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT v FROM ViewLog v " +
                    "JOIN FETCH v.property p " +
                    "LEFT JOIN FETCH p.images " +
                    "WHERE v.user.id = :userId " +
                    "ORDER BY v.id DESC"; // Mới nhất lên đầu
            Query<ViewLog> query = session.createQuery(hql, ViewLog.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}