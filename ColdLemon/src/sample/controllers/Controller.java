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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import sample.Main;
import sample.constantes.Configs;


import javax.swing.*;
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
    TreeView <String> treeview,treevinspector;

    @FXML TabPane tapane;

    @FXML
    Pane panesote;
    @FXML
    AnchorPane anchor;
    @FXML
    TextArea txtConsola;

    TreeItem<String> Main;


    CodeArea codeArea = new CodeArea();



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ///Para el treeview de las entidades
        Main = new TreeItem<>("Nivel 1");

        TreeItem<String> Entidades = new TreeItem<>("Entidades");
        TreeItem<String> Escenarios = new TreeItem<>("Escenarios");
        //Entidades.getChildren().addAll(Collider,Sprite,Control);
        //Escenarios.getChildren().addAll(Collider,Sprite,Control);
        Main.getChildren().addAll(Entidades,Escenarios);
        treeview.setRoot(Main);


        ///Para el TreeView Del inspector xdxdxdxdxdxd si o no raza?

        TreeItem<String> mainIns = new TreeItem<>("Proyecto");
        String dir = System.getProperty("user.dir").trim();
        String[] direcctorio = dir.split("\\/");

        for(int x = 1; x < direcctorio.length;x++){


            TreeItem<String> item = new TreeItem<>(direcctorio[x]);

            mainIns.getChildren().add(item);
        }
        treevinspector.setRoot(mainIns);







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


        });///final del evento

        anchor.getChildren().add(codeArea);
        codeArea.replaceText(0,0,0,0," ");


    }
    ////crear un folder del proyecto
    public void crearFolders(){
        //Para agarrar el directorio donde lo queremos guardar
        DirectoryChooser path = new DirectoryChooser();
        path.setTitle("Guardar Proyecto");
        path.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = (Stage) treeview.getScene().getWindow();
        File file = path.showDialog(stage);
      if(file != null){

          System.out.println(file.getAbsolutePath());
          File folder = new File(file,"Proyecto");
          folder.mkdir();
          File folImagenes = new File(folder.getAbsolutePath(),"Imagenes");
          folImagenes.mkdir();
      }
       /* File script = new File("script.clemon");
        File folder = new File("Proyecto",file.getAbsolutePath());
        try{
            script.createNewFile();
            folder.mkdir();
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println(folder.getPath());*/




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
        public void evtNuevo(ActionEvent event){
            crearFolders();
        }
        public void evtPlay(ActionEvent evt){

            compilar();
        }
        public void evtAddEntidad(ActionEvent evt){


        addEntidad();


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
                Parent editorSp = FXMLLoader.load(getClass().getResource("../vistas/editorSprite.fxml"));
                Scene edSp = new Scene(editorSp,800,600);
                Stage stage = (Stage) treeview.getScene().getWindow();
                stage.setScene(edSp);
                stage.show();




            } catch (IOException e){
                e.printStackTrace();
            }



        }




        public void addEntidad(){
            TreeItem<String> Collider = new TreeItem<>("Collider");
            TreeItem<String> Sprite = new TreeItem<>("Sprite");
            TreeItem<String> Control = new TreeItem<>("Control");



            if(treeview.getSelectionModel().getSelectedItem().getValue().equals("Entidades")){
                TreeItem<String> seleccion = treeview.getSelectionModel().getSelectedItem();
                seleccion.getChildren().add(Collider);
                seleccion.setExpanded(true);
            }


            Tab tabsito = new Tab();
            AnchorPane anchorPane = new AnchorPane();
            Pane panesito = new Pane();
            CodeArea codeAreas = new CodeArea();

            ///empieza el codigo del codeArea
            codeAreas.setParagraphGraphicFactory(LineNumberFactory.get(codeAreas));
            codeAreas.replaceText(0, 0, sampleCode);
            Subscription cleanupWhenNoLongerNeedIt = codeAreas
                    .multiPlainChanges()
                    .successionEnds(Duration.ofMillis(500))
                    .subscribe(ignore -> codeAreas.setStyleSpans(0, computeHighlighting(codeAreas.getText())));
            codeAreas.setPrefSize(anchor.getPrefWidth() + 470,400);
            codeAreas.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode() == KeyCode.ENTER){
                        int linea = codeAreas.getCurrentParagraph();
                        String texto = codeAreas.getText(linea - 1).trim();
                        if(texto.endsWith(":")|texto.startsWith(">")){
                            codeAreas.replaceText(linea,0,linea,0,"\t" + ">" + " ");
                        } else {
                            System.out.println("no se tabula morro");
                        }


                    }
                }


            });///final del evento

            panesito.getChildren().add(codeAreas);
            anchorPane.getChildren().add(panesito);
            tabsito.setContent(anchorPane);
            tabsito.setText("Entidad");
            tapane.getTabs().add(tabsito);
            codeAreas.replaceText(0,0,0,0," ");


        }



}



