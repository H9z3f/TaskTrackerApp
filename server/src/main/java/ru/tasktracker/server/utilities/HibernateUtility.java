package ru.tasktracker.server.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtility {
    private static SessionFactory sessionFactory;

    public static void setSessionFactory() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            setSessionFactory();
        }

        return sessionFactory;
    }
}
