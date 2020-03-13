package myhomework.plantclient;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author samsung1
 */
public class GamePane extends Pane {

    private GameGateway gateway;

    public GamePane(GameGateway gateway) {
        this.gateway = gateway;
        this.getChildren().addAll(gateway.getShapes());
        this.setOnKeyPressed(e -> handleKey(e));
        this.setMinWidth(USE_PREF_SIZE);//the same size for window
        this.setMaxWidth(USE_PREF_SIZE);
        this.setPrefWidth(PlantConstants.WIDTH);
        this.setMinHeight(USE_PREF_SIZE);
        this.setMaxHeight(USE_PREF_SIZE);
        this.setPrefHeight(PlantConstants.HEIGHT);
        new Thread(new UpdateGameState(gateway)).start();
    }

    private void handleKey(KeyEvent evt) {
        KeyCode code = evt.getCode();
        {
            if (code == KeyCode.UP) {
                gateway.movePaddle(true);
            } else if (code == KeyCode.DOWN) {
                gateway.movePaddle(false);
            }
        }
        if (code == KeyCode.SPACE) {
            gateway.Fire(true);
        }
    }

    private void fire(KeyEvent evt) {

    }

    @Override
    public boolean isResizable() {
        return false;
    }

}

class UpdateGameState implements Runnable {

    private GameGateway gateway;

    public UpdateGameState(GameGateway gateway) {
        this.gateway = gateway;
    }

    public void start() {
        Stage stage = new Stage();
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/maxresdefault.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(myBI);
        stage.setTitle("Game Over");
        root.setBackground(background);
        stage.show();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(250);
                if (gateway.end == 1) {
                    start();
                } else if (gateway.open()) {
                    gateway.refresh();
                } else {
                    break;
                }
            } catch (Exception ex) {

            }
        }
    }
}
