module com.example.almanac {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.almanac to javafx.fxml;
    exports com.example.almanac;
    exports com.example.almanac.controller;
    opens com.example.almanac.controller to javafx.fxml;
}