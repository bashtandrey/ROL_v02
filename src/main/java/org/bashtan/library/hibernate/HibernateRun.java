package org.bashtan.library.hibernate;

import org.bashtan.library.constructor.SettingProperties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HibernateRun {
    public SessionFactory sessionFactory;
    public HibernateRun(SettingProperties settingProperties, Class<?> ... classes) {
        setUp(settingProperties,classes);
    }
    private void setUp(SettingProperties settingProperties, Class<?>... classes){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.url", settingProperties.getUrl())
                .applySetting("hibernate.connection.username", settingProperties.getUsername())
                .applySetting("hibernate.connection.password", settingProperties.getPassword())
                .applySetting("hibernate.show_sql",true)
                .applySetting("hibernate.format_sql",true)
                .applySetting("hibernate.highlight_sql",true)
                .applySetting("hibernate.hbm2ddl.auto","update")
                .build();
        try {
            MetadataSources metadataSources = new MetadataSources(registry);
            for (Class<?> tclass : classes) {
                metadataSources.addAnnotatedClass(tclass);
            }
           sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    public void persist(Object object){
        sessionFactory.inTransaction(session -> session.persist(object));
    }
    public <T> List<T> load(Class<T> tClass){
        List<T> list = new ArrayList<>();
        sessionFactory.inTransaction(session -> {
            list.addAll(session.createSelectionQuery("from " + tClass.getSimpleName(),tClass)
                    .getResultList());
        });

        return list;
    }
    public <T> T find(Class<T> tClass,long id){
        return sessionFactory.openSession().find(tClass,id);
    }
    public void remove(Object object){sessionFactory.inTransaction(session -> session.remove(object));}
    public void merge(Object object){sessionFactory.inTransaction(session -> session.merge(object));}
}