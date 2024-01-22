package org.bashtan.library.application;

import org.bashtan.library.constructor.SettingProperties;
import org.bashtan.library.hibernate.HibernateRun;
import org.bashtan.library.table.Book;
import org.bashtan.library.table.People;
import org.bashtan.library.table.User;

import java.io.File;

public class RunLibrary {
    private static final File FILE_SECRET_KEY = new File("secret.key");
    private static final File FILE_HIBERNATE_PROPERTIES = new File("hibernate_properties.cfg");

    public static boolean flagFileHibernateProperties;
    public static boolean flagFileSecretKey;

    public static boolean flagSessionFactory;
    public static boolean flagUserEmpty;
    public HibernateRun hibernateRun;
    public SettingFile settingFile;


    public RunLibrary() {
        flagFileSecretKey = FILE_SECRET_KEY.exists();
        flagFileHibernateProperties = FILE_HIBERNATE_PROPERTIES.exists();
        settingFile = new SettingFile(FILE_SECRET_KEY, FILE_HIBERNATE_PROPERTIES);
          if (flagFileSecretKey && flagFileHibernateProperties) {
            hibernateRun();
        }
    }

    public void hibernateRun() {
        hibernateRun = new HibernateRun((SettingProperties) settingFile.readFileHibernateProperties(), User.class, Book.class, People.class);
        if (hibernateRun.sessionFactory == null) {
            flagSessionFactory = false;
        } else {
            flagSessionFactory = true;
            flagUserEmpty = hibernateRun.load(User.class).isEmpty();
        }
    }
}
