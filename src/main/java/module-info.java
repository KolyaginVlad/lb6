module com.example.lb6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lb6 to javafx.fxml;
    exports com.example.lb6;
}