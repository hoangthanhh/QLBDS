package com.qlbds.repository;

import com.qlbds.constant.TransactionStatusEnum;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class StatisticRepository {

    public int countTotalAccounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(u) FROM User u";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int countTotalProperties() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(p) FROM Property p";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int countTotalCompletedTransactions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.status = :status";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("status", TransactionStatusEnum.COMPLETED); // Đảm bảo khớp Enum của bạn
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double sumTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.amount) FROM Transaction t WHERE t.status = :status";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("status", TransactionStatusEnum.COMPLETED);
            Double sum = query.uniqueResult();
            return sum != null ? sum : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public List<Object[]> getMonthlyTransactionData(int year) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT MONTH(t.createdAt), COUNT(t) FROM Transaction t " +
                    "WHERE YEAR(t.createdAt) = :year AND t.status = :status " +
                    "GROUP BY MONTH(t.createdAt)";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("year", year);
            query.setParameter("status", TransactionStatusEnum.COMPLETED);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public double sumRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.amount) FROM Transaction t " +
                    "WHERE t.status = :status AND t.createdAt BETWEEN :start AND :end";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("status", TransactionStatusEnum.COMPLETED);
            query.setParameter("start", start);
            query.setParameter("end", end);
            Double sum = query.uniqueResult();
            return sum != null ? sum : 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}