package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private static int PARTICLES_AMOUNT = 10;

    private Parent page;

    @FXML
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        page = loader.load();
        Controller controller = loader.getController();
        primaryStage.setResizable(false);
        Scene scene = new Scene(page, 420, 420);
        primaryStage.setTitle("Particles");
        primaryStage.setScene(scene);
        primaryStage.show();
        controller.setStage(primaryStage);

        primaryStage.setResizable(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
