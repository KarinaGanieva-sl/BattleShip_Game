package sample;

import battleship.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ControllerStartSceneClient {
    @FXML
    private GridPane fieldPane;

    @FXML
    private TextField serverPortNumber;

    private static final int RECTANGLE_WIDTH = 25;

    @FXML
    private TextField serverHostName;

    @FXML
    private Button allocateButton;

    @FXML
    private Button playButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button allocateAutomaticallyButton;

    @FXML
    private HBox ship4;

    @FXML
    private HBox ship3;

    @FXML
    private HBox ship2;

    @FXML
    private HBox ship1;

    @FXML
    private TextField ship4_count;

    @FXML
    private TextField ship3_count;

    @FXML
    private TextField ship2_count;

    @FXML
    private TextField ship1_count;

    private ControllerToGame controllerToGame = new ControllerToGame();

    int length;


    /**
     * This method is used to initialize game and scene.
     */
    public void initialize()
    {
        controllerToGame.initField(fieldPane, true);
        ship4.setOnMousePressed( pressedShip4);
        ship3.setOnMousePressed( pressedShip3);
        ship2.setOnMousePressed( pressedShip2);
        ship1.setOnMousePressed( pressedShip1);
        initTable();
        initMethod();
    }

    void initTable() {
        for(int i = 0; i < 4; ++i) {
            Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_WIDTH);
            r.setFill(Color.BROWN);
            r.setStroke(Color.BLACK);
            ship4.getChildren().add(r);
        }
        for(int i = 0; i < 3; ++i) {
            Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_WIDTH);
            r.setFill(Color.BROWN);
            r.setStroke(Color.BLACK);
            ship3.getChildren().add(r);
        }
        for(int i = 0; i < 2; ++i) {
            Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_WIDTH);
            r.setFill(Color.BROWN);
            r.setStroke(Color.BLACK);
            ship2.getChildren().add(r);
        }
        for(int i = 0; i < 1; ++i) {
            Rectangle r = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_WIDTH);
            r.setFill(Color.BROWN);
            r.setStroke(Color.BLACK);
            ship1.getChildren().add(r);
        }
    }
    EventHandler< MouseEvent> pressedShip4 = e -> {
        length = 4;
    };
    EventHandler< MouseEvent> pressedShip3 = e -> {
        length = 3;
    };
    EventHandler< MouseEvent> pressedShip2 = e -> {
        length = 2;
    };
    EventHandler< MouseEvent> pressedShip1 = e -> {
        length = 1;
    };


    boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        if ((horizontal ? column : row) + length - 1 > 9)
            return false;
        for (int i = Math.max(0,row-1); i <= Math.min(horizontal ? row+1 : row+length, 9); i++) {
            for (int j = Math.max(0, column - 1); j <= Math.min(horizontal ? column + length : column + 1, 9); j++) {
                if (ocean.isOccupied(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    void initMethod() {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                controllerToGame.field_me[i][j].setOnMouseClicked(this::rectangleEventHandler);
    }
    void rectangleEventHandler (MouseEvent e) {
        int row = fieldPane.getRowIndex((Node)e.getTarget());
        int column = fieldPane.getColumnIndex((Node)e.getTarget());
        Ship ship = null;
        switch (length){
            case 1: ship = new Submarine(); break;
            case 2: ship = new Destroyer(); break;
            case 3: ship = new Cruiser(); break;
            case 4: ship = new Battleship(); break;
        }
        if(okToPlaceShipAt(row, column, true, controllerToGame.ocean_me))
        {
            ship.placeShipAt(row, column, true, controllerToGame.ocean_me);
            for(int i = 0; i < length; ++i)
            {
                controllerToGame.field_me[row][column+i].setFill(Color.BROWN);
            }

        }
    }

    @FXML
    void allocate() {

    }

    @FXML
    void allocateAutomatically() {
        controllerToGame.allocateAutomatically();
        playButton.setDisable(false);
    }

    @FXML
    void connect() {
        int port;
        try {
            port = Integer.parseInt(serverPortNumber.getText());
        }
        catch(NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage() + "must be an interger > 0");
            alert.showAndWait();
            return;
        }
        if(controllerToGame.createConnection(serverHostName.getText(), port)) {
            connectButton.setDisable(true);
            allocateButton.setDisable(false);
            allocateAutomaticallyButton.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Сonnection established!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is busy. Try again later!");
            alert.showAndWait();
        }
    }

    @FXML
    void play() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Stage primStage = (Stage) allocateButton.getScene().getWindow();
        primStage.setScene(new Scene(loader.load(), 1024, 551));
        Controller controller = loader.getController();
        controller.initData(controllerToGame);
        primStage.show();
    }
}
