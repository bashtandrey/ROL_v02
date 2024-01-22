module com.bashtan.rol {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires lombok;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    exports org.bashtan.library.hibernate;
    opens org.bashtan.library.hibernate;

    exports org.bashtan.library.table;
    opens org.bashtan.library.table;

    exports org.bashtan.library.constructor;
    opens org.bashtan.library.constructor;

    exports org.bashtan.library.constants;
    opens org.bashtan.library.constants;

    exports org.bashtan.library.controller;
    opens org.bashtan.library.controller to javafx.fxml;

    exports org.bashtan.library.application;
    opens org.bashtan.library.application;

}