package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    private Canvas canvas;


    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane page = FXMLLoader.load(Main.class.getResource("sample.fxml"));
        Scene scene = new Scene(page, 420, 420);
        primaryStage.setTitle("Particles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
