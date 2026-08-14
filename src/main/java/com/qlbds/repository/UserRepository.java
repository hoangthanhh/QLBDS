package com.qlbds.repository;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.entity.User;
import com.qlbds.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    // =========================================================================
    // KHU VỰC 1: DÙNG CHUNG / TẦNG CUSTOMER (Đăng nhập, Đăng ký, Profile, OTP)
    // =========================================================================

    public User findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public User findByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.phone = :phone";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("phone", phone);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertUser(User user) {
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

    public boolean updateUser(User user) {
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

    // =========================================================================
    // KHU VỰC 2: TẦNG QUẢN TRỊ ADMIN (Danh sách, Phân trang, Tìm kiếm, Đổi Role, Khóa tài khoản)
    // =========================================================================

    // 1. Lấy danh sách tài khoản theo từ khóa tìm kiếm (Họ tên, Email, SĐT) và Phân trang
    public List<User> searchUsers(String keyword, int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM User u WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                hql.append(" AND (LOWER(u.fullName) LIKE :kw OR LOWER(u.email) LIKE :kw OR u.phone LIKE :kw)");
            }
            hql.append(" ORDER BY u.id DESC");

            Query<User> query = session.createQuery(hql.toString(), User.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 2. Đếm tổng số tài khoản khớp với từ khóa tìm kiếm để tính tổng số trang chính xác
    public int countSearchUsers(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");

            if (keyword != null && !keyword.trim().isEmpty()) {
                hql.append(" AND (LOWER(u.fullName) LIKE :kw OR LOWER(u.email) LIKE :kw OR u.phone LIKE :kw)");
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("kw", "%" + keyword.trim().toLowerCase() + "%");
            }
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 3. Lấy toàn bộ danh sách tài khoản mặc định (khi không nhập từ khóa)
    public List<User> findAllUsers(int offset, int limit) {
        return searchUsers(null, offset, limit);
    }

    // 4. Đếm tổng số tài khoản toàn hệ thống
    public int countTotalUsers() {
        return countSearchUsers(null);
    }

    // 5. Cập nhật Vai trò (Customer / Staff / Admin)
    public boolean updateRole(int id, RoleTypeEnum role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                user.setRole(role);
                session.update(user);
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

    // 6. Xóa mềm: Chuyển đổi trạng thái tài khoản giữa ACTIVE (Hoạt động) <-> INACTIVE (Khóa)
    public boolean toggleStatus(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                UserStatusEnum newStatus = (user.getStatus() == UserStatusEnum.ACTIVE)
                        ? UserStatusEnum.INACTIVE
                        : UserStatusEnum.ACTIVE;
                user.setStatus(newStatus);
                session.update(user);
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