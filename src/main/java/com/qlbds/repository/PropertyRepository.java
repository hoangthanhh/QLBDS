package com.qlbds.repository;

import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.entity.Property;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class PropertyRepository {

    // 1. Hàm đếm tổng số BĐS CÓ LỌC (Để tính số trang chính xác khi tìm kiếm)
    public long countAvailableProperties(String address, String priceRange, String propertyType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(p) FROM Property p WHERE p.isDeleted = false AND p.status = 'AVAILABLE'");
            buildFilterCondition(hql, address, priceRange, propertyType);

            Query<Long> query = session.createQuery(hql.toString(), Long.class);
            setParameters(query, address, propertyType);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 2. Hàm lấy danh sách BĐS CÓ LỌC và PHÂN TRANG (Đã fix lỗi Memory Pagination)
    public List<Property> findAllAvailableByPage(int page, int pageSize, String address, String priceRange, String propertyType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // BƯỚC 1: Chỉ lấy danh sách ID của các Bất động sản thỏa mãn điều kiện và phân trang
            StringBuilder idHql = new StringBuilder("SELECT p.id FROM Property p WHERE p.isDeleted = false AND p.status = 'AVAILABLE'");
            buildFilterCondition(idHql, address, priceRange, propertyType);
            idHql.append(" ORDER BY p.createdAt DESC");

            Query<Integer> idQuery = session.createQuery(idHql.toString(), Integer.class);
            setParameters(idQuery, address, propertyType);

            int offset = (page - 1) * pageSize;
            idQuery.setFirstResult(offset);
            idQuery.setMaxResults(pageSize);

            List<Integer> propertyIds = idQuery.getResultList();

            // Nếu không có BĐS nào thỏa mãn điều kiện, trả về list rỗng luôn
            if (propertyIds.isEmpty()) {
                return Collections.emptyList();
            }

            // BƯỚC 2: Truy vấn lấy Property và JOIN FETCH Images dựa trên danh sách ID vừa lấy
            String hql = "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.images WHERE p.id IN (:ids) ORDER BY p.createdAt DESC";
            Query<Property> query = session.createQuery(hql, Property.class);
            query.setParameterList("ids", propertyIds);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- HÀM PHỤ TRỢ NỐI CHUỖI HQL ---
    private void buildFilterCondition(StringBuilder hql, String address, String priceRange, String propertyType) {
        if (address != null && !address.trim().isEmpty()) {
            hql.append(" AND (p.address LIKE :address OR p.title LIKE :address)");
        }
        if (propertyType != null && !propertyType.trim().isEmpty()) {
            hql.append(" AND p.propertyType = :propertyType");
        }
        if (priceRange != null && !priceRange.trim().isEmpty()) {
            switch (priceRange) {
                case "UNDER_1B": hql.append(" AND p.price < 1000000000"); break;
                case "1B_3B": hql.append(" AND p.price >= 1000000000 AND p.price <= 3000000000"); break;
                case "3B_7B": hql.append(" AND p.price >= 3000000000 AND p.price <= 7000000000"); break;
                case "OVER_7B": hql.append(" AND p.price > 7000000000"); break;
            }
        }
    }

    // --- HÀM PHỤ TRỢ SET THAM SỐ CHO QUERY ---
    private void setParameters(Query<?> query, String address, String propertyType) {
        if (address != null && !address.trim().isEmpty()) {
            query.setParameter("address", "%" + address.trim() + "%");
        }
        if (propertyType != null && !propertyType.trim().isEmpty()) {
            query.setParameter("propertyType", PropertyTypeEnum.valueOf(propertyType));
        }
    }

    // Thêm hàm lấy BĐS theo ID
    public Property findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.images WHERE p.id = :id AND p.isDeleted = false";
            Query<Property> query = session.createQuery(hql, Property.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Thêm hàm cập nhật BĐS
    public boolean update(Property property) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(property);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}