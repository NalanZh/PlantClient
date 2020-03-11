package myhomework.plantclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
/**
 *
 * @author samsung1
 */

public class GameGateway implements PlantConstants {
    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private List<Shape> shapes;
    Rectangle leftPaddle;
    Rectangle rightPaddle;
    Circle b1;
    Circle b2;
    boolean isOpen = true;
    
    public GameGateway() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream to send data to the server
            outputToServer = new PrintWriter(socket.getOutputStream());

            // Create an input stream to read data from the server
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            System.out.println("Exception in GameGateway.");
            ex.printStackTrace();
        }
        
        // Make the shapes
        shapes = new ArrayList<Shape>();
        Image l = new Image(getClass().getResourceAsStream("/myhomework.images/shooter1.jpg"));
        Image r = new Image(getClass().getResourceAsStream("/myhomework.images/shooter2.jpg"));
        leftPaddle = new Rectangle(MARGIN,MARGIN,THICKNESS,LENGTH);
        //leftPaddle.setFill(new ImagePattern(l, MARGIN,MARGIN,THICKNESS,LENGTH, true));
        leftPaddle.setFill(new ImagePattern(l));
        shapes.add(leftPaddle);
        rightPaddle = new Rectangle(WIDTH-MARGIN-THICKNESS,MARGIN,THICKNESS,LENGTH);
        //rightPaddle.setFill(new ImagePattern(r, MARGIN,MARGIN,THICKNESS,LENGTH, true));
        rightPaddle.setFill(new ImagePattern(r));
        shapes.add(rightPaddle);
        
        b1 = new Circle(WIDTH/2,HEIGHT/4,MARGIN/4);
        b1.setFill(Color.GREEN);
        shapes.add(b1);
        b2 = new Circle(WIDTH/2,HEIGHT/4,MARGIN/4);
        b2.setFill(Color.GREEN);
        shapes.add(b2);
       
       
    }
    
    public List<Shape> getShapes() { return shapes; }
    
    // Move the player's paddle
    public synchronized void movePaddle(boolean up) {
        if(up)
            outputToServer.println(MOVE_UP);
        else
            outputToServer.println(MOVE_DOWN);
        outputToServer.flush();
    }
    
public synchronized void Fire(boolean fire) {
        if(fire)
        outputToServer.println(FIRE);
    }

    // Refresh the game state
    public synchronized void refresh() {
        outputToServer.println(GET_GAME_STATE);
        outputToServer.flush();
        String state = "";
        try {
            state = inputFromServer.readLine();
        } catch (IOException ex) {
            System.out.println("Exception in GameGateway.");
            ex.printStackTrace();
        }
        String parts[] = state.split(" ");/////????????????????//b1b2 might be null
        b1.setCenterX(Double.parseDouble(parts[0]));
        b1.setCenterY(Double.parseDouble(parts[1]));
        b2.setCenterX(Double.parseDouble(parts[2]));
        b2.setCenterY(Double.parseDouble(parts[3]));
        leftPaddle.setY(Double.parseDouble(parts[4]));
        rightPaddle.setY(Double.parseDouble(parts[5]));
    }
    
    public void close() {
        try {
        outputToServer.close();
        inputFromServer.close();
        } catch(Exception ex) {
            
        }
        isOpen = false;
    }
    
    public boolean open() { return isOpen; }
}

