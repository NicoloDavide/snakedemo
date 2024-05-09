module com.example.snakedemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.snakedemo to javafx.fxml;
    exports com.example.snakedemo;
}