package com.qlbds.repository;

import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.nCopies;

public class ReportRepository {

    // 1. Đếm BĐS đang bán (AVAILABLE)
    public long countAvailableBDS() {
        try (Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM Property b WHERE b.status = 'AVAILABLE' AND b.isDeleted = false";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // Tính tổng Tiền cọc đã nhận (Bao gồm đang cọc và đã thu hồi do bể kèo)
    public BigDecimal getTotalDepositAmount() {
        try (Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionType = 'DEPOSIT' AND t.status IN ('COMPLETED', 'FORFEITED')";
            Object result = session.createQuery(hql).uniqueResult();
            return result != null ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    // 3. Đếm BĐS đã bán thành công (SOLD)
    public long countSoldBDS() {
        try (Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM Property b WHERE b.status = 'SOLD'";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // 4. Tính tổng doanh thu toàn thời gian (Bao gồm Giao dịch hoàn thành + Tiền thu cọc)
    public java.math.BigDecimal getTotalRevenue() {
        try (Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.amount) FROM Transaction t WHERE t.status IN ('COMPLETED', 'FORFEITED')";
            Object result = session.createQuery(hql).uniqueResult();
            return result != null ? new java.math.BigDecimal(result.toString()) : java.math.BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            return java.math.BigDecimal.ZERO;
        }
    }

    // 5. ĐÃ SỬA: Thống kê số lượng TẤT CẢ giao dịch theo 12 tháng (Bỏ điều kiện lọc Status)
    public List<Long> getMonthlyTransactionCounts(int year) {
        List<Long> monthlyCounts = new ArrayList<>(nCopies(12, 0L));
        try (org.hibernate.Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT MONTH(t.createdAt), COUNT(t.id) " +
                    "FROM Transaction t " +
                    "WHERE YEAR(t.createdAt) = :year " +
                    "GROUP BY MONTH(t.createdAt)";

            org.hibernate.query.Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("year", year);

            java.util.List<Object[]> results = query.list();
            for (Object[] row : results) {
                int month = (Integer) row[0];
                long count = (Long) row[1];
                if (month >= 1 && month <= 12) {
                    monthlyCounts.set(month - 1, count);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthlyCounts;
    }

    // 6. Tính tổng doanh thu lọc theo khoảng thời gian tùy chọn
    public java.math.BigDecimal getFilteredRevenue(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        try (org.hibernate.Session session = com.qlbds.util.HibernateUtil.getSessionFactory().openSession()) {
            java.time.LocalDateTime startDateTime = startDate.atStartOfDay();
            java.time.LocalDateTime endDateTime = endDate.atTime(java.time.LocalTime.MAX);

            String hql = "SELECT SUM(t.amount) FROM Transaction t " +
                    "WHERE t.status IN ('COMPLETED', 'FORFEITED') AND t.createdAt BETWEEN :startDateTime AND :endDateTime";

            org.hibernate.query.Query<Object> query = session.createQuery(hql, Object.class);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);

            Object result = query.uniqueResult();
            return result != null ? new java.math.BigDecimal(result.toString()) : java.math.BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            return java.math.BigDecimal.ZERO;
        }
    }
}