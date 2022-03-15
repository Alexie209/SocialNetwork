module com.example.proiectextinssac {
    requires javafx.graphics;
    requires java.sql;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.proiectextinssac to javafx.fxml;
    exports com.example.proiectextinssac;
    opens com.example.proiectextinssac.domain to javafx.base;
    exports com.example.proiectextinssac.domain;

//    requires mysql.connector.java;

 //   opens com.example.proiectextinssac.domain.User;
}