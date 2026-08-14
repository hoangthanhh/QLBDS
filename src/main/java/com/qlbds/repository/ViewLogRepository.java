package com.qlbds.repository;

import com.qlbds.constant.RoleTypeEnum;
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

    // ĐÃ SỬA: Thêm phân trang (page, pageSize) vào câu truy vấn
    public List<ViewLog> findLogsByUserId(Integer userId, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT v FROM ViewLog v " +
                    "JOIN FETCH v.property p " +
                    "LEFT JOIN FETCH p.images " +
                    "WHERE v.user.id = :userId " +
                    "ORDER BY v.id DESC";
            Query<ViewLog> query = session.createQuery(hql, ViewLog.class);
            query.setParameter("userId", userId);

            // Tính toán vị trí bắt đầu lấy dữ liệu
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // THÊM MỚI: Đếm tổng số log để tính tổng số trang
    public long countLogsByUserId(Integer userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(v) FROM ViewLog v WHERE v.user.id = :userId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Lấy toàn bộ lịch sử xem hệ thống (Phân trang dành cho Admin)
    public List<ViewLog> findAllLogsForAdmin(RoleTypeEnum role, int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT v FROM ViewLog v " +
                    "JOIN FETCH v.user u " +
                    "JOIN FETCH v.property p " +
                    "WHERE u.role = :role " + // Thêm điều kiện lọc Role
                    "ORDER BY v.id DESC";
            Query<ViewLog> query = session.createQuery(hql, ViewLog.class);
            query.setParameter("role", role);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ĐÃ SỬA: Đếm tổng số log theo Role
    public long countAllLogs(RoleTypeEnum role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(v) FROM ViewLog v WHERE v.user.role = :role";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("role", role);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}