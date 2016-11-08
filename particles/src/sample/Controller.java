package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    private Button button;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField textField;

    private GraphicsContext gc;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert button != null : "fx:id=\"button\" was not injected: check your FXML file 'sample.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'sample.fxml'.";
        gc = canvas.getGraphicsContext2D();

        anchorPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                canvas.setWidth(newSceneWidth.doubleValue() - 40);
                clear();
            }
        });
        anchorPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                canvas.setHeight(newSceneHeight.doubleValue() - 60);
                clear();
            }
        });

        EventHandler<ActionEvent> drawer = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setResizable(false);
                clear();

                gc.setFill(Color.BLACK);
                Integer num = Integer.parseInt(textField.getCharacters().toString());
                gc.fillText("" + num, 20, 20);

                stage.setResizable(true);
            }
        };

        button.setOnAction(drawer);

    }

    public void clear() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
