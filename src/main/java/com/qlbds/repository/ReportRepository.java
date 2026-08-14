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

public class ReportRepository {

    // 1. Đếm tổng số tài khoản
    // 1. Đếm BĐS đã Đặt cọc (DEPOSITED)
    public long countDepositedBDS() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM Property b WHERE b.status = 'DEPOSITED'";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // 2. Đếm BĐS đã Bán thành công (SOLD)
    public long countSoldBDS() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(b) FROM Property b WHERE b.status = 'SOLD'";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // 3. Đếm tổng giao dịch THÀNH CÔNG
    public long countTotalSuccessfulTransactions() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.status = 'COMPLETED'";
            Long count = (Long) session.createQuery(hql).uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // 4. Tính tổng doanh thu toàn thời gian
    public BigDecimal getTotalRevenue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.amount) FROM Transaction t WHERE t.status = 'COMPLETED'";
            Object result = session.createQuery(hql).uniqueResult();
            return result != null ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    // 5. ĐÃ SỬA: Bổ sung thêm điều kiện chỉ đếm các giao dịch 'COMPLETED' cho biểu đồ tháng
    public List<Long> getMonthlyTransactionCounts(int year) {
        List<Long> monthlyCounts = new ArrayList<>(Collections.nCopies(12, 0L));
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT MONTH(t.createdAt), COUNT(t.id) " +
                    "FROM Transaction t " +
                    "WHERE YEAR(t.createdAt) = :year AND t.status = 'COMPLETED' " +
                    "GROUP BY MONTH(t.createdAt)";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("year", year);

            List<Object[]> results = query.list();
            for (Object[] row : results) {
                int month = (Integer) row[0]; // 1 -> 12
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

    // 6. ĐÃ SỬA: Tính tổng doanh thu lọc theo khoảng thời gian ('SUCCESS' -> 'COMPLETED')
    public BigDecimal getFilteredRevenue(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59.999999999

            String hql = "SELECT SUM(t.amount) FROM Transaction t " +
                    "WHERE t.status = 'COMPLETED' AND t.createdAt BETWEEN :startDateTime AND :endDateTime";

            Query<Object> query = session.createQuery(hql, Object.class);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);

            Object result = query.uniqueResult();
            return result != null ? new BigDecimal(result.toString()) : BigDecimal.ZERO;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}