package myhomework.plantclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
        stage.setOnCloseRequest(e->gateway.close());
        stage.setTitle("PvP");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}