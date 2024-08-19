package com.andersen.tr.dao.connection;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionFactoryProvider {
    private static DBResourceManager dbResourceManager = DBResourceManager.getInstance();
    private static SessionFactory sessionFactory;

    private static final Class<?>[] ANNOTATED_CLASSES = {User.class, Ticket.class};

    public SessionFactoryProvider() {
    }

    public static SessionFactory getSessionFactory() {
        try {
                Configuration configuration = new Configuration();

                configuration.setProperty("hibernate.connection.driver_class", dbResourceManager.getValue(DBParameter.DB_DRIVER));
                configuration.setProperty("hibernate.connection.url", dbResourceManager.getValue(DBParameter.DB_URL));
                configuration.setProperty("hibernate.connection.username", dbResourceManager.getValue(DBParameter.DB_USER));
                configuration.setProperty("hibernate.connection.password", dbResourceManager.getValue(DBParameter.DB_PASSWORD));
                configuration.setProperty("hibernate.connection.pool_size", dbResourceManager.getValue(DBParameter.DB_POOL_SIZE));
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.show_sql", "false");

                for (Class<?> annotatedClass : ANNOTATED_CLASSES) {
                    configuration.addAnnotatedClass(annotatedClass);
                }

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
}
