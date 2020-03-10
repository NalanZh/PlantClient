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
    Image leftPaddle;
    Image rightPaddle;
    Circle bullet;
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
        leftPaddle = new Image("D:\\COMPUTER SCIENCE\\CMSC250\\Final plants\\PlantServer\\src\\main\\resources\\shooter1.jpg");
        //leftPaddle.setFill(Color.RED);
        shapes.add(leftPaddle);
        rightPaddle = new Image("D:\\COMPUTER SCIENCE\\CMSC250\\Final plants\\PlantServer\\src\\main\\resources\\shooter2.jpg");
        //rightPaddle.setFill(Color.BLUE);
        shapes.add(rightPaddle);
        bullet = new Circle(WIDTH/2,HEIGHT/4,MARGIN/4);
        bullet.setFill(Color.RED);
        shapes.add(bullet);
       
       
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
        String parts[] = state.split(" ");
        bullet.setCenterX(Double.parseDouble(parts[0]));
        bullet.setCenterY(Double.parseDouble(parts[1]));
        leftPaddle.setY(Double.parseDouble(parts[2]));
        rightPaddle.setY(Double.parseDouble(parts[3]));
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

