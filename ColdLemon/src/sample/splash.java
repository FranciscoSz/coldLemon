package sample;

import com.sun.prism.paint.Color;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOError;
import java.io.IOException;

public class splash extends Preloader {


    private Label lb_progress;
    private ProgressBar barra;
    private Stage stage;
    private Scene escena;
    private AnchorPane pane;
    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        switch (type){

            case BEFORE_START:{
                stage.hide();



                break;
            }

        }
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if(info instanceof ProgressNotification){

            lb_progress.setText(((ProgressNotification) info).getProgress() + "%");
            barra.setProgress(((ProgressNotification) info).getProgress()/100);

        }
    }

    @Override
    public void init() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Parent root2;
                try{

                    root2 = FXMLLoader.load(getClass().getResource("splash.fxml"));
                    escena = new Scene(root2,600,600);



                }catch(IOException e){
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        this.stage = primaryStage;
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setScene(escena);
        escena.setFill(javafx.scene.paint.Color.TRANSPARENT);
        this.stage.show();
        lb_progress = (Label) escena.lookup("#lbpro");
        barra = (ProgressBar) escena.lookup("#pro");
        pane = (AnchorPane) escena.lookup("#anchor");
        pane.setBackground(Background.EMPTY);


    }
}
