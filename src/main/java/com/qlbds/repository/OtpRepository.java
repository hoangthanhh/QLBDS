package com.qlbds.repository;

import com.qlbds.entity.OtpCode;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class OtpRepository {

    public OtpCode findLatestActiveOtp(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM OtpCode o WHERE o.user.id = :userId AND o.isUsed = false ORDER BY o.createdAt DESC";
            Query<OtpCode> query = session.createQuery(hql, OtpCode.class);
            query.setParameter("userId", userId);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long countOtpGeneratedToday(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(o) FROM OtpCode o WHERE o.user.id = :userId AND DATE(o.createdAt) = CURRENT_DATE";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            Long count = query.uniqueResult();
            return count != null ? count : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 999;
        }
    }

    public boolean updateOtp(OtpCode otpCode) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(otpCode);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveOtp(OtpCode otpCode) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(otpCode);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}