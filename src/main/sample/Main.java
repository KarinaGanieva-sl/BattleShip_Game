package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        String mode = getParameters().getRaw().get(0);
        Parent root = null;
        if(mode.equals("Client"))
            root = FXMLLoader.load(getClass().getResource("start_scene.fxml"));
        else if(mode.equals("Server"))
            root = FXMLLoader.load(getClass().getResource("start_scene_server.fxml"));
        else
            Platform.exit();
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(new Scene(root, 1024, 550));
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
            launch(args);
    }
}
