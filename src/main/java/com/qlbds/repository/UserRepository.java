package com.qlbds.repository;

import com.qlbds.entity.User;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepository {

    // ==========================================
    // 1. NHÓM HÀM XÁC THỰC & ĐĂNG KÝ
    // ==========================================

    public User findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.email = :email";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 2. NHÓM HÀM QUẢN TRỊ ADMIN (CRUD & PHÂN TRANG)
    // ==========================================

    // Lấy danh sách tài khoản có phân trang (Sắp xếp mới nhất lên đầu)
    public List<User> findAllWithPagination(int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u ORDER BY u.id DESC";
            Query<User> query = session.createQuery(hql, User.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Đếm tổng số lượng tài khoản trong Database
    public long countAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT count(u) FROM User u";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Tìm tài khoản theo ID
    public User findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    // Cập nhật thông tin tài khoản (Đổi quyền, Đổi trạng thái, Đổi mật khẩu)
    public boolean update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tài khoản
    public boolean delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}