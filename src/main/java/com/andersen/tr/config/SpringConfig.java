package com.andersen.tr.config;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.andersen.tr.dao", "com.andersen.tr.model", "com.andersen.tr.service", "com.andersen.tr"})
@PropertySource("classpath:db.properties")
public class SpringConfig {
    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${db.driver}")
    private String connectionDriverClass;

    @Value("${db.url}")
    private String connectionUrl;

    @Value("${db.user}")
    private String connectionUsername;

    @Value("${db.password}")
    private String connectionPassword;

    @Value("${db.poolsize}")
    private int poolSize;

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration.setProperty("hibernate.connection.driver_class", connectionDriverClass);
        configuration.setProperty("hibernate.connection.url", connectionUrl);
        configuration.setProperty("hibernate.connection.username", connectionUsername);
        configuration.setProperty("hibernate.connection.password", connectionPassword);
        configuration.setProperty("hibernate.connection.pool_size", String.valueOf(poolSize));
        configuration.setProperty("hibernate.dialect", hibernateDialect);
        configuration.setProperty("hibernate.show_sql", hibernateShowSql);

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Ticket.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }
}
