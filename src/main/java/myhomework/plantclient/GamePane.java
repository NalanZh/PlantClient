package myhomework.plantclient;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author samsung1
 */
public class GamePane extends Pane {
    private GameGateway gateway;
    
    public GamePane(GameGateway gateway) {
        this.gateway = gateway;
        this.getChildren().addAll(gateway.getShapes());
        this.setOnKeyPressed(e->handleKey(e));
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
        if(code == KeyCode.UP)
            gateway.movePaddle(true);
        else if(code == KeyCode.DOWN)
            gateway.movePaddle(false);
    }

private void fire(KeyEvent evt) {
        KeyCode code = evt.getCode();
        if(code == KeyCode.SPACE)
            gateway.Fire(true);
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
    
    public void run() {
        while(true) {
            try {
                Thread.sleep(250);
                if(gateway.open())
                    gateway.refresh();
                else
                    break;
            } catch(Exception ex) {
                
            }
        }
    }
}
