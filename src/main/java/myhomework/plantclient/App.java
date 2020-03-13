package myhomework.plantclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        GameGateway gateway = new GameGateway();
        GamePane pane = new GamePane(gateway);
        var scene = new Scene(pane, 640, 480);
        pane.requestFocus();
        stage.setScene(scene);
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/background.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(myBI);
        stage.setOnCloseRequest(e -> gateway.close());
        stage.setTitle("PvP");
        pane.setBackground(background);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
