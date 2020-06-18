package sample;

import battleship.Ocean;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ControllerStartSceneServer {

    @FXML
    private Button playButton;

    @FXML
    private Button allocateAutomaticallyButton;

    @FXML
    private GridPane fieldPane;

    @FXML
    private TextField serverPortNumber;

    @FXML
    private Button allocateButton;

    @FXML
    private Button startButton;

    private ControllerToGame controllerToGame = new ControllerToGame();

    /**
     * This method is used to initialize game and scene.
     */
    public void initialize()
    {
        controllerToGame.initField(fieldPane, true);
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
    void start() {
        int port;
        try {
            port = Integer.parseInt(serverPortNumber.getText());
            if(port < 0)
                throw new NumberFormatException();
        }
        catch(NumberFormatException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage() + "must be an interger > 0");
            alert.showAndWait();
            return;
        }
        if(controllerToGame.createConnection(port))
        {
            startButton.setDisable(true);
            allocateButton.setDisable(false);
            allocateAutomaticallyButton.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Сonnection established!");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't create connection!");
            alert.showAndWait();
        }
    }

    @FXML
    void play() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load(), 1024, 550));

        Controller controller = loader.getController();
        controller.initData(controllerToGame);

        stage.show();
    }


}
