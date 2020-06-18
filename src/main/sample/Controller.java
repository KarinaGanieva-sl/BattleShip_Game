package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import battleship.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

public class Controller
{
    private Rectangle[][] field = new Rectangle[10][10];
    private Ocean ocean;
    private ControllerToGame controllerToGame;

    @FXML
    private GridPane field_partner;

    @FXML
    private GridPane field_me;

    @FXML
    private TextArea logging;

    @FXML
    private TextField row;

    @FXML
    private TextField column;

    /**
     * This method is used to initialize game and scene.
     */
    void initData(ControllerToGame c) {
        controllerToGame = c;
        controllerToGame.setLogging(logging);
        controllerToGame.setController(this);
        controllerToGame.addShipsToField(field_me);
        controllerToGame.initField(field_partner, false);
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                controllerToGame.getPartnerField()[i][j].setOnMouseClicked(this::rectangleEventHandler);
        field_me.setDisable(true);
        field_partner.setDisable(!controllerToGame.isServer);
        logging.setText("Welcome to the game!");
    }

    /**
     * This method is used to start new game.
     * @param event MouseEvent on button
     */
    @FXML
    void startNewGame(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING,"The new game will be started!", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("New game warning");
        Optional<ButtonType> result = alert.showAndWait();
    }

    /**
     * This method is used to get coordinates entered by user.
     * @param event MouseEvent on button
     */
    @FXML
    void getCoordinates(MouseEvent event) {
        endGameLost(4,4);
        int row_, column_;
        try {
            row_ = Integer.parseInt(row.getText());
            if (row_ < 0 || row_ > 9) {
                throw new NumberFormatException();
            }
            column_ = Integer.parseInt(column.getText());
            if (column_ < 0 || column_ > 9) {
                throw new NumberFormatException();
            }
        } catch(Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage() + "must be a number from 0 to 9!");
            alert.showAndWait();
            return;
        }
        try {
            controllerToGame.sendShot(row_, column_);
        }
        catch(Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to make a shot!");
            alert.showAndWait();
        }
    }

    /**
     * This method is used to shoot at chosen rectangle.
     * @param e MouseEvent on button
     */
    void rectangleEventHandler (MouseEvent e) {
        int row = GridPane.getRowIndex((Node)e.getTarget());
        int column = GridPane.getColumnIndex((Node)e.getTarget());
        try {
            controllerToGame.sendShot(row, column);
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to make a shot!");
            alert.showAndWait();
        }
    }

    void endGameWin(int shotsCountMe, int shotsCountPartner) {
        logging.appendText("\nthe game is over" +
                "\nYou won!\n" +
                "Winner: me\n" + "My shots count: " + shotsCountMe +
                "\nMy partner count: " + shotsCountPartner);
        closeConnection();
    }


    void endGameLost(int shotsCountMe, int shotsCountPartner) {
        logging.appendText("\nthe game is over" +
                "\nYou lost!\n" +
                "Winner: partner\n" + "My shots count: " + shotsCountMe +
                "\nMy partner count: " + shotsCountPartner);
        closeConnection();
    }

    void closeConnection() {
        try {
            controllerToGame.dataSocket.close();
        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to close connection");
            alert.showAndWait();
        }
    }

}
