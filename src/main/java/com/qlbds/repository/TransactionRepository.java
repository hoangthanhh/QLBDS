package com.qlbds.repository;

import com.qlbds.entity.Transaction;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;
// XÓA dòng import org.hibernate.Transaction as HbTransaction; đi nhé!

public class TransactionRepository {

    public boolean save(Transaction transaction) {
        // Dùng đường dẫn tuyệt đối của package để khai báo Transaction của Hibernate
        org.hibernate.Transaction hbTx = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            hbTx = session.beginTransaction();
            session.save(transaction);
            hbTx.commit();
            return true;
        } catch (Exception e) {
            if (hbTx != null) {
                hbTx.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}