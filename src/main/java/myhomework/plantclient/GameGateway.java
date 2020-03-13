package myhomework.plantclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

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
    Image l, r;
    int end = 0;
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
        l = new Image(getClass().getResourceAsStream("/images/shooter1.jpg"));
        r = new Image(getClass().getResourceAsStream("/images/shooter2.jpg"));
        leftPaddle = new Rectangle(MARGIN, MARGIN, 3 * THICKNESS, 1.5 * LENGTH);
        //leftPaddle.setFill(new ImagePattern(l, MARGIN,MARGIN,THICKNESS,LENGTH, true));
        leftPaddle.setFill(new ImagePattern(l));
        shapes.add(leftPaddle);
        rightPaddle = new Rectangle(WIDTH - MARGIN - 3 * THICKNESS, MARGIN, 3 * THICKNESS, 1.5 * LENGTH);
        //rightPaddle.setFill(new ImagePattern(r, MARGIN,MARGIN,THICKNESS,LENGTH, true));
        rightPaddle.setFill(new ImagePattern(r));
        shapes.add(rightPaddle);

        b1 = new Circle(MARGIN, MARGIN, MARGIN / 2);
        b1.setFill(Color.GREEN);
        shapes.add(b1);
        b2 = new Circle(WIDTH - MARGIN - THICKNESS, MARGIN, MARGIN / 2);
        Color c = Color.web("#25a3d3");
        b2.setFill(c);
        shapes.add(b2);

    }

    public List<Shape> getShapes() {
        return shapes;
    }

    // Move the player's paddle
    public synchronized void movePaddle(boolean up) {
        if (up) {
            outputToServer.println(MOVE_UP);
        } else {
            outputToServer.println(MOVE_DOWN);
        }
        outputToServer.flush();
    }

    public synchronized void Fire(boolean fire) {
        if (fire) {
            outputToServer.println(FIRE);
            outputToServer.flush();
        }
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

        if (state.equals("One wins")) {
            end =1;
        }///////////////////////////////////////////Toan do this game over sign! 
        else if (state.equals("Two wins")) {
            end =1;
        } else {
            String parts[] = state.split(" ");

            if (parts[0].equals("-1")) {
            } else {
                System.out.println("Bullet 1 x = " + parts[0]);
                b1.setCenterX(Double.parseDouble(parts[0]));
                b1.setCenterY(Double.parseDouble(parts[1]));
            }

            if (parts[2].equals("-1")) {
            } else {
                System.out.println("Bullet 2 x = " + parts[2]);
                b2.setCenterX(Double.parseDouble(parts[2]));
                b2.setCenterY(Double.parseDouble(parts[3]));
            }

            leftPaddle.setY(Double.parseDouble(parts[4]));
            rightPaddle.setY(Double.parseDouble(parts[5]));
        }
    }

    public void close() {
        try {
            outputToServer.close();
            inputFromServer.close();
        } catch (Exception ex) {

        }
        isOpen = false;
    }

    public boolean open() {
        return isOpen;
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
}
