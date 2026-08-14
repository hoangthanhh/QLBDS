package com.qlbds.repository;

import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.entity.Property;
import com.qlbds.entity.PropertyImage;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PropertyRepository {

    // =========================================================================
    // KHU VỰC 1: DÙNG CHUNG / TẦNG CUSTOMER (Trang chủ, Lọc BĐS, Chi tiết BĐS)
    // =========================================================================

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
                return java.util.Collections.emptyList();
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

    // 3. Hàm lấy BĐS theo ID
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
                case "UNDER_1B":
                    hql.append(" AND p.price < 1000000000");
                    break;
                case "1B_3B":
                    hql.append(" AND p.price >= 1000000000 AND p.price <= 3000000000");
                    break;
                case "3B_7B":
                    hql.append(" AND p.price >= 3000000000 AND p.price <= 7000000000");
                    break;
                case "OVER_7B":
                    hql.append(" AND p.price > 7000000000");
                    break;
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

    // =========================================================================
    // KHU VỰC 2: TẦNG QUẢN TRỊ ADMIN (Thêm mới, Cập nhật, Xóa kiểm tra ràng buộc)
    // =========================================================================

    // 1. Thêm mới BĐS kèm danh sách ảnh upload
    public boolean saveProperty(Property property, List<PropertyImage> images) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(property);
            if (images != null) {
                for (PropertyImage img : images) {
                    img.setProperty(property);
                    session.save(img);
                }
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 2 Cập nhật BĐS - Đảm bảo chỉ cập nhật đúng 1 bản ghi có ID tương ứng
    public boolean updateProperty(Property property, List<PropertyImage> newImages) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if (property != null && property.getId() != null) {
                // 1. Cập nhật thông tin BĐS theo đúng ID
                session.update(property);

                // 2. Nếu người dùng chọn upload ảnh mới cho BĐS này
                if (newImages != null && !newImages.isEmpty()) {
                    for (PropertyImage img : newImages) {
                        img.setProperty(property); // Khóa chặt quan hệ với BĐS đang sửa
                        session.save(img);
                    }
                }
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 3. RÀNG BUỘC NGHIỆP VỤ: Kiểm tra BĐS đã từng phát sinh giao dịch (Đặt cọc / Mua) chưa
    public boolean hasTransactions(Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.property.id = :pid";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("pid", propertyId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để bảo vệ dữ liệu nếu gặp lỗi DB
        }
    }

    // 4. Xóa mềm BĐS (Đánh dấu isDeleted = true để giữ nguyên toàn bộ lịch sử)
    public boolean deleteProperty(Integer propertyId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Property p = session.get(Property.class, propertyId);
            if (p != null) {
                p.setIsDeleted(true); // Xóa mềm an toàn
                session.update(p);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}