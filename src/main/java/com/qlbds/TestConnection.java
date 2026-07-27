package com.qlbds;

import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;

public class TestConnection {
    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("====== KẾT NỐI DATABASE HIBERNATE THÀNH CÔNG! ======");
        } catch (Exception e) {
            System.err.println("====== LỖI KẾT NỐI DATABASE: ======");
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}