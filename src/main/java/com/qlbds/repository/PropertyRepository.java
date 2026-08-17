package com.qlbds.repository;

import com.qlbds.constant.PropertyStatusEnum;
import com.qlbds.constant.PropertyTypeEnum;
import com.qlbds.entity.Property;
import com.qlbds.entity.PropertyImage;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyRepository {

    // Customer

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

    // 2. Hàm lấy danh sách BĐS CÓ LỌC và PHÂN TRANG (Chuẩn HQL 2 bước - Fix Memory Pagination)
    public List<Property> findAllAvailableByPage(int page, int pageSize, String address, String priceRange, String propertyType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // BƯỚC 1: Chỉ lấy danh sách ID của các BĐS thỏa mãn điều kiện và phân trang
            StringBuilder idHql = new StringBuilder("SELECT p.id FROM Property p WHERE p.isDeleted = false AND p.status = 'AVAILABLE'");
            buildFilterCondition(idHql, address, priceRange, propertyType);
            idHql.append(" ORDER BY p.createdAt DESC");

            Query<Integer> idQuery = session.createQuery(idHql.toString(), Integer.class);
            setParameters(idQuery, address, propertyType);

            int offset = (page - 1) * pageSize;
            idQuery.setFirstResult(offset);
            idQuery.setMaxResults(pageSize);

            List<Integer> propertyIds = idQuery.getResultList();

            // Nếu không có BĐS nào thỏa mãn điều kiện, trả về list rỗng
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

    // 3. Hàm lấy chi tiết BĐS kèm ảnh theo ID
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

    // Staff & Admin

    // 1. Admin thêm mới BĐS kèm danh sách ảnh upload
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

    // 2. Admin cập nhật BĐS đơn giản (Chỉ thông tin cơ bản)
    public boolean update(Property property) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(property);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // 3. Admin cập nhật BĐS nâng cao (Cập nhật thông tin + Bổ sung ảnh mới)
    public boolean updateProperty(Property property, List<PropertyImage> newImages) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            if (property != null && property.getId() != null) {
                session.update(property);

                if (newImages != null && !newImages.isEmpty()) {
                    for (PropertyImage img : newImages) {
                        img.setProperty(property);
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

    // 4. Kiểm tra ràng buộc nghiệp vụ: BĐS đã phát sinh giao dịch chưa
    public boolean hasTransactions(Integer propertyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(t) FROM Transaction t WHERE t.property.id = :pid";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("pid", propertyId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để bảo vệ an toàn dữ liệu nếu lỗi DB
        }
    }

    // 5. Admin xóa mềm BĐS (Đánh dấu isDeleted = true)
    public boolean deleteProperty(Integer propertyId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Property p = session.get(Property.class, propertyId);
            if (p != null) {
                p.setIsDeleted(true);
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

    // 6. Xóa một ảnh cụ thể của BĐS theo ID ảnh
    public boolean deleteImageById(Integer imageId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            PropertyImage img = session.get(PropertyImage.class, imageId);
            if (img != null) {
                session.delete(img);
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

    // 7. Lấy danh sách BĐS quản lý có phân trang và lọc theo trạng thái / xóa mềm (ĐÃ FIX LAZY LOAD)
    public List<Property> findPropertiesByPage(int page, int pageSize, String keyword, String priceRange, String propertyType, String statusFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // BƯỚC 1: Chỉ lấy danh sách ID của các BĐS thỏa mãn điều kiện và phân trang
            StringBuilder idHql = new StringBuilder("SELECT p.id FROM Property p WHERE 1=1");

            boolean isSpecificStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                    && !"ALL".equalsIgnoreCase(statusFilter)
                    && !"DELETED".equalsIgnoreCase(statusFilter)
                    && !"HIDDEN".equalsIgnoreCase(statusFilter);

            if ("DELETED".equalsIgnoreCase(statusFilter) || "HIDDEN".equalsIgnoreCase(statusFilter)) {
                idHql.append(" AND p.isDeleted = true");
            } else {
                idHql.append(" AND p.isDeleted = false");
                if (isSpecificStatus) {
                    idHql.append(" AND p.status = :status");
                }
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                idHql.append(" AND (LOWER(p.title) LIKE :kw OR LOWER(p.address) LIKE :kw)");
            }
            if (propertyType != null && !propertyType.trim().isEmpty()) {
                idHql.append(" AND p.propertyType = :ptype");
            }
            if ("UNDER_1B".equals(priceRange)) idHql.append(" AND p.price < 1000000000");
            else if ("1B_3B".equals(priceRange)) idHql.append(" AND p.price >= 1000000000 AND p.price <= 3000000000");
            else if ("3B_7B".equals(priceRange)) idHql.append(" AND p.price > 3000000000 AND p.price <= 7000000000");
            else if ("OVER_7B".equals(priceRange)) idHql.append(" AND p.price > 7000000000");

            idHql.append(" ORDER BY p.id DESC");

            Query<Integer> idQuery = session.createQuery(idHql.toString(), Integer.class);

            // Set tham số cho Bước 1
            if (keyword != null && !keyword.trim().isEmpty()) {
                idQuery.setParameter("kw", "%" + keyword.toLowerCase().trim() + "%");
            }
            if (propertyType != null && !propertyType.trim().isEmpty()) {
                idQuery.setParameter("ptype", PropertyTypeEnum.valueOf(propertyType));
            }
            if (isSpecificStatus) {
                idQuery.setParameter("status", PropertyStatusEnum.valueOf(statusFilter));
            }

            // Phân trang ở Bước 1 (Chỉ phân trang trên ID nên rất nhẹ)
            idQuery.setFirstResult((page - 1) * pageSize);
            idQuery.setMaxResults(pageSize);

            List<Integer> propertyIds = idQuery.getResultList();

            // Nếu không có BĐS nào thỏa mãn điều kiện, trả về list rỗng luôn để tránh lỗi
            if (propertyIds.isEmpty()) {
                return new ArrayList<>();
            }

            // BƯỚC 2: Dùng danh sách ID lấy được để truy vấn Property kèm theo Images (Dùng JOIN FETCH)
            String hql = "SELECT DISTINCT p FROM Property p LEFT JOIN FETCH p.images WHERE p.id IN (:ids) ORDER BY p.id DESC";
            Query<Property> query = session.createQuery(hql, Property.class);
            query.setParameterList("ids", propertyIds);

            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 8. Đếm tổng số BĐS quản lý để phân trang
    public long countTotalProperties(String keyword, String priceRange, String propertyType, String statusFilter) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(p) FROM Property p WHERE 1=1");

            boolean isSpecificStatus = statusFilter != null && !statusFilter.trim().isEmpty()
                    && !"ALL".equalsIgnoreCase(statusFilter)
                    && !"DELETED".equalsIgnoreCase(statusFilter)
                    && !"HIDDEN".equalsIgnoreCase(statusFilter);

            if ("DELETED".equalsIgnoreCase(statusFilter) || "HIDDEN".equalsIgnoreCase(statusFilter)) {
                hql.append(" AND p.isDeleted = true");
            } else {
                hql.append(" AND p.isDeleted = false");
                if (isSpecificStatus) {
                    hql.append(" AND p.status = :status");
                }
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                hql.append(" AND (LOWER(p.title) LIKE :kw OR LOWER(p.address) LIKE :kw)");
            }
            if (propertyType != null && !propertyType.trim().isEmpty()) {
                hql.append(" AND p.propertyType = :ptype");
            }
            if ("UNDER_1B".equals(priceRange)) hql.append(" AND p.price < 1000000000");
            else if ("1B_3B".equals(priceRange)) hql.append(" AND p.price >= 1000000000 AND p.price <= 3000000000");
            else if ("3B_7B".equals(priceRange)) hql.append(" AND p.price > 3000000000 AND p.price <= 7000000000");
            else if ("OVER_7B".equals(priceRange)) hql.append(" AND p.price > 7000000000");

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.setParameter("kw", "%" + keyword.toLowerCase().trim() + "%");
            }
            if (propertyType != null && !propertyType.trim().isEmpty()) {
                query.setParameter("ptype", PropertyTypeEnum.valueOf(propertyType));
            }
            if (isSpecificStatus) {
                query.setParameter("status", PropertyStatusEnum.valueOf(statusFilter));
            }

            Long count = query.uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    // Admin khôi phục BĐS đã xóa mềm
    public boolean restoreProperty(Integer propertyId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            // Dùng session.get để lấy được cả BĐS đã bị ẩn (isDeleted = true)
            Property p = session.get(Property.class, propertyId);
            if (p != null) {
                p.setIsDeleted(false);
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