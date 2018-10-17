package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.splash;
public class Main extends Application {

    public static int duracion = 10;
    public static int steps = 1;

    @Override
    public void init() throws Exception {
        for(int i = 0; i < duracion;i++){

            double progreso = (100*i)/duracion;
            LauncherImpl.notifyPreloader(this, new splash.ProgressNotification(progreso));

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("vistas/principal.fxml"));
        primaryStage.setTitle("ColdLemon");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {

        LauncherImpl.launchApplication(Main.class,splash.class,args);

        //launch(args);
    }
}
