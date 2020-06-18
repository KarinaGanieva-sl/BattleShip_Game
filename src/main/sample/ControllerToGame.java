package sample;

import battleship.Ocean;
import battleship.Ship;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.scene.control.*;
import java.net.*;

class ControllerToGame
{
    private ServerSocket connectionSocket;
    Socket dataSocket;
    boolean isServer;
    private ConnectionThread thread;
    private static final int RECTANGLE_WIDTH = 25;
    Rectangle[][] field_me = new Rectangle[10][10];
    Ocean ocean_me;
    private Ocean ocean_partner;
    private Rectangle[][] field_partner = new Rectangle[10][10];
    private static final String[] STATUS = {"miss", "hit", "sunk", "win"};
    boolean isGameOver;
    int shipsSunkCount;
    private TextArea logging;
    private int shotsCountPartner;
    private int shotsCountMe;
    private Controller controller;

    ControllerToGame() {
        ocean_me = new Ocean();
        ocean_partner = new Ocean();
    }

    void setController(Controller c)
    {
        controller = c;
    }

    boolean createConnection(int port) {
        try {
            connectionSocket = new ServerSocket(port);
            dataSocket = connectionSocket.accept();
            isServer = true;
            thread = new ConnectionThread();
            thread.start();
        }
        catch(IOException ex)
        {
            return false;
        }
        return true;
    }

    boolean createConnection(String host, int port) {
        try {
            InetAddress serverHost = InetAddress.getByName(host);
            SocketAddress socketAddress = new InetSocketAddress(serverHost, port);
            dataSocket = new Socket();
            dataSocket.connect(socketAddress);
            thread = new ConnectionThread();
            thread.start();
        }
        catch(IOException ex)
        {
            return false;
        }
        return true;
    }
    /**
     * This method is used to create a field for game.
     */
    void initField(GridPane fieldPane, boolean me) {
        Rectangle[][] field = me ? field_me : field_partner;
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
                field[i][j] = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_WIDTH);
                field[i][j].setFill(Color.BURLYWOOD);
                field[i][j].setStroke(Color.BLACK);
                fieldPane.add(field[i][j], j, i);
            }
    }



    Rectangle[][] getPartnerField() {
        return field_partner;
    }

    void allocateAutomatically() {
        ocean_me.fillShips();
        ocean_me.placeAllShipsRandomly();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                field_me[i][j].setFill(Color.BURLYWOOD);
        Ship[][] ships = ocean_me.getShipArray();
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
                if(ships[i][j].getShipType().equals(""))
                    continue;
                field_me[i][j].setFill(Color.BROWN);
            }
    }

    void addShipsToField(GridPane field) {
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                field.add(field_me[i][j], j, i);
    }


    private class ConnectionThread extends Thread {
        private ObjectOutputStream out;
        ObjectInputStream in;
        @Override
        public void run() {
            try(ObjectOutputStream out = new ObjectOutputStream(dataSocket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(dataSocket.getInputStream()))
            {
                dataSocket.setTcpNoDelay(true);
                this.out = out;
                this.in = in;
                while (!ocean_me.isGameOver()) {
                    if(isServer)
                    {
                        getShotResult((Serializable)thread.in.readObject());
                        if(isGameOver)
                            return;
                        Serializable data = (Serializable) in.readObject();
                        getShot(data);
                        if(isGameOver)
                            return;
                    }
                    else
                    {
                        Serializable data = (Serializable) in.readObject();
                        getShot(data);
                        if(isGameOver)
                            return;
                        getShotResult((Serializable)thread.in.readObject());
                        if(isGameOver)
                            return;
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    void sendShot(int row, int column) throws IOException
    {
        thread.out.writeObject(row + " " + column); //отправляем выстрел
        field_partner[0][0].getParent().setDisable(true);
        shotsCountMe++;
    }

    void getShotResult(Serializable data)
    {
        int row = Integer.parseInt((data.toString()).split(" ")[0]);
        int column = Integer.parseInt((data.toString()).split(" ")[1]);
        String status = (data.toString()).split(" ")[2];
        String string_for_logging = "\nme: " + row + "," + column + " = " + status;
        logging.appendText(string_for_logging);
        if(status.equals(STATUS[0]))
            field_partner[row][column].setFill(Color.GREY);
        if(status.equals(STATUS[1]))
            field_partner[row][column].setFill(Color.RED);
        if(status.equals(STATUS[2]))
        {
            shipsSunkCount++;
            String[] result = (data.toString()).split(" ");
            int bowRow = Integer.parseInt(result[3]);
            int bowColumn = Integer.parseInt(result[4]);
            int length = Integer.parseInt(result[5]);
            boolean isHorizontal = Integer.parseInt(result[6]) == 1;
            if (isHorizontal)
                for (int i = 0; i < length; i++)
                    field_partner[bowRow][bowColumn + i].setFill(Color.BLACK);
            else
                for (int i = 0; i < length; i++)
                    field_partner[bowRow + i][bowColumn].setFill(Color.BLACK);
            if(shipsSunkCount == 10)
            {
                isGameOver = true;
                controller.endGameWin(shotsCountMe, shotsCountPartner);
                thread.interrupt();
            }
        }
    }

    //ответ на выстрел
    void getShot(Serializable data) throws IOException {
        shotsCountPartner++;
        int row = Integer.parseInt((data.toString()).split(" ")[0]);
        int column = Integer.parseInt((data.toString()).split(" ")[1]);
        field_partner[0][0].getParent().setDisable(false);
        String string_for_logging = "\npartner: " + row + "," + column + " = ";
        if(!ocean_me.shootAt(row, column)) {
            thread.out.writeObject(data + " " +STATUS[0]); //отвечаем на выстрел
            field_me[row][column].setFill(Color.GREY);
            string_for_logging += STATUS[0];
            logging.appendText(string_for_logging);
            return;
        }
        if(!ocean_me.getShipArray()[row][column].isSunk()) {
            thread.out.writeObject(data + " " +STATUS[1]); //отвечаем на выстрел
            field_me[row][column].setFill(Color.RED);
            string_for_logging += STATUS[1];
            logging.appendText(string_for_logging);
            return;
        }
        String result = data + " " + STATUS[2] + " ";
        result += ocean_me.getShipArray()[row][column].getBowRow() + " " +
                ocean_me.getShipArray()[row][column].getBowColumn() + " " +
                ocean_me.getShipArray()[row][column].getLength() + " " +
                (ocean_me.getShipArray()[row][column].isHorizontal() ? "1" : "0");
        string_for_logging += STATUS[2];
        logging.appendText(string_for_logging);
        Ship ship = ocean_me.getShipArray()[row][column];
        if (ship.isHorizontal())
            for (int i = 0; i < ship.getLength(); i++)
                field_me[ship.getBowRow()][ship.getBowColumn() + i].setFill(Color.BLACK);
        else
            for (int i = 0; i < ship.getLength(); i++)
                field_me[ship.getBowRow() + i][ship.getBowColumn()].setFill(Color.BLACK);
        thread.out.writeObject(result); //отвечаем на выстрел
        if (ocean_me.isGameOver()) {
            controller.endGameLost(shotsCountMe, shotsCountPartner);
            thread.interrupt();
            isGameOver = true;
        }
    }

    void setLogging(TextArea log)
    {
        logging = log;
    }
}
