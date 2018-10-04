package sample;

import com.sun.org.omg.CORBA.Initializer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {




    @FXML
    TreeView <String> treeview;





    @Override
    public void initialize(URL location, ResourceBundle resources) {




        TreeItem<String> Main = new TreeItem<>("Nivel 1");



        TreeItem<String> Player = new TreeItem<>("Entidades");
        TreeItem<String> Enemigo = new TreeItem<>("Escenaios");
        TreeItem<String> Collider = new TreeItem<>("Collider");
        TreeItem<String> Sprite = new TreeItem<>("Sprite");
        TreeItem<String> Control = new TreeItem<>("Control");


        Player.getChildren().addAll(Collider,Sprite,Control);
        Enemigo.getChildren().addAll(Collider,Sprite,Control);



        Main.getChildren().addAll(Player,Enemigo);
        treeview.setRoot(Main);
    }

        public void evtAbrir(ActionEvent event){
            FileChooser of = new FileChooser();
            of.setTitle("Abrir proyecto");
            FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter("Archivos.clemon", "*.clemon");
            of.getExtensionFilters().add(filtro);
            Stage stage = (Stage) treeview.getScene().getWindow();
            File file = of.showOpenDialog(stage);

        }


        public void editarSprite(ActionEvent event){
            try {
                Parent editorSp = FXMLLoader.load(getClass().getResource("editorSprite.fxml"));
                Scene edSp = new Scene(editorSp,800,600);
                Stage stage = (Stage) treeview.getScene().getWindow();
                stage.setScene(edSp);
                stage.show();




            } catch (IOException e){
                e.printStackTrace();
            }



        }



}
