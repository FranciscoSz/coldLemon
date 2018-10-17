package sample.controllers;

import com.sun.org.omg.CORBA.Initializer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import sample.constantes.Configs;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sample.constantes.Configs.*;
public class Controller implements Initializable {

    @FXML
    TreeView <String> treeview;

    @FXML
    Pane panesote;
    @FXML
    AnchorPane anchor;
    @FXML
    TextArea txtConsola;



    CodeArea codeArea = new CodeArea();



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


        ////para las palabras reservadas
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.replaceText(0, 0, sampleCode);
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        codeArea.setPrefSize(anchor.getPrefWidth() + 470,400);
        codeArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    int linea = codeArea.getCurrentParagraph();
                    String texto = codeArea.getText(linea - 1).trim();
                    if(texto.endsWith(":")|texto.startsWith(">")){
                        codeArea.replaceText(linea,0,linea,0,"\t" + ">" + " ");
                    } else {
                        System.out.println("no se tabula morro");
                    }


                }
            }
        });
        anchor.getChildren().add(codeArea);


    }
 ////acciones de eventos
        public void evtAbrir(ActionEvent event){
            FileChooser of = new FileChooser();
            of.setTitle("Abrir proyecto");
            FileChooser.ExtensionFilter filtro = new FileChooser.ExtensionFilter("Archivos.clemon", "*.clemon");
            of.getExtensionFilters().add(filtro);
            Stage stage = (Stage) treeview.getScene().getWindow();
            File file = of.showOpenDialog(stage);

        }

        public void evtPlay(ActionEvent evt){

            compilar();


        }
        public void compilar(){
            txtConsola.setText("");
            Long tInicial = System.currentTimeMillis();

            String texto = codeArea.getText();
            String[] renglones = texto.split("\\n");

            for(int x = 0; x < renglones.length;x++) {
                boolean bandera = false;
                if (!renglones[x].trim().equals("")) {
                    for (int y = 0; y < Configs.EXPRECIONES.length && !bandera; y++) {
                        Pattern pattern = Pattern.compile(Configs.EXPRECIONES[y]);
                        Matcher matcher = pattern.matcher(renglones[x]);
                        if (matcher.matches()) {

                            bandera = true;

                        }
                    }
                    if (bandera == false) {
                        txtConsola.setText(txtConsola.getText() + " \n" + "Washa la linea " + (x + 1) + " tienes un error morro");
                    }

                }

            }
            Long tFinal = System.currentTimeMillis() - tInicial;
            txtConsola.setText(txtConsola.getText() + " \n" + "Compilado en: " + tFinal + "milisegundos");
        }


        public void limpiarConsola(ActionEvent evt){

            txtConsola.setText("");

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



