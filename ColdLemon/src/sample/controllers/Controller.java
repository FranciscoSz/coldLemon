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
import sun.reflect.generics.tree.Tree;


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
    public static TreeView<String> copia;
    public static TreeView<String> copiaInspector;
    @FXML
     TabPane tapane;
    public static TabPane copiaTab;

    @FXML
    Pane panesote;
    @FXML

    AnchorPane anchor;
    public static AnchorPane copiaAnchor;

    @FXML
    TextArea txtConsola;

    TreeItem<String> Main;


    CodeArea codeArea = new CodeArea();
    public static String PATHSCRIPTS,PATHIMAGENES,PATHPROYECTO,PATHAUDIOS = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        copia = treeview;
        copiaAnchor = anchor;
        copiaTab = tapane;
        copiaInspector = treevinspector;



        ///Para el treeview de las entidades
        Main = new TreeItem<>("Nivel 1");

        TreeItem<String> Entidades = new TreeItem<>("Entidades");
        TreeItem<String> Escenarios = new TreeItem<>("Escenarios");
        //Entidades.getChildren().addAll(Collider,Sprite,Control);
        //Escenarios.getChildren().addAll(Collider,Sprite,Control);
        Main.getChildren().addAll(Entidades,Escenarios);
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


        });///final del evento

        anchor.getChildren().add(codeArea);
        codeArea.replaceText(0,0,0,0," ");


    }

    public static void actualizarInspector(){

        File pathPro = new File(PATHPROYECTO);
        String[] archivos = pathPro.list();

        Node rootIcon =  new ImageView(new Image(Controller.class.getResourceAsStream("../../imagenes/folder.png")));
        TreeItem<String> mainIns = new TreeItem<>("Proyecto", rootIcon);


        for(int x = 0; x < archivos.length;x++){

            File filas = new File(pathPro.getAbsolutePath() + "/" + archivos[x]);
            if(filas.isDirectory()){
                Node subicono =  new ImageView(new Image(Controller.class.getResourceAsStream("../../imagenes/folder.png")));
                TreeItem<String> f = new TreeItem<>(archivos[x],subicono);

                String[] filitas = filas.list();
                for(int y = 0; y < filitas.length; y++){

                    TreeItem<String> Subarchivos = new TreeItem<>(filitas[y]);
                    f.getChildren().add(Subarchivos);
                    System.out.println(filitas[y]);


                }
                mainIns.getChildren().add(f);
            }


        }
        copiaInspector.setRoot(mainIns);
        mainIns.setExpanded(true);
        mainIns.getChildren().get(0).setExpanded(true);

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
            ////crear folder del proyecto
          File folder = new File(file,"Proyecto");
          folder.mkdir();
          PATHPROYECTO = folder.getAbsolutePath();
          System.out.println(PATHPROYECTO);
            ////crear folder de imagenes
          File folImagenes = new File(folder.getAbsolutePath(),"Imagenes");
          folImagenes.mkdir();
          PATHIMAGENES = folImagenes.getAbsolutePath();
            ////crear folder de scripts
          File folScripts = new File(folder.getAbsolutePath(), "Scripts");
          PATHSCRIPTS = folScripts.getAbsolutePath();
          folScripts.mkdir();
            ///cear folder de Audios
          File folAudio = new File(folder.getAbsolutePath(),"Audios");
          folAudio.mkdir();
          PATHAUDIOS = folAudio.getAbsolutePath();



          String[] archivos = folder.list();
          Node rootIcon =  new ImageView(new Image(getClass().getResourceAsStream("../../imagenes/folder.png")));
          TreeItem<String> mainIns = new TreeItem<>("Proyecto",rootIcon);




          for(int x = 0; x < archivos.length;x++){

              File filas = new File(folder.getAbsolutePath() + "/" + archivos[x]);
              if(filas.isDirectory()){

                  Node subicono =  new ImageView(new Image(getClass().getResourceAsStream("../../imagenes/folder.png")));
                  TreeItem<String> f = new TreeItem<>(archivos[x],subicono);

                  String[] filitas = filas.list();


                  for(int y = 0; y < filitas.length; y++){
                      String[] extencion = filitas[y].split("\\.");
                      if(extencion[1].equals("clemon")){
                          Node iconofila =  new ImageView(new Image(getClass().getResourceAsStream("../../imagenes/scripticon.png")));
                          TreeItem<String> Subarchivos = new TreeItem<>(filitas[y],iconofila);
                          f.getChildren().add(Subarchivos);
                      }else{
                          TreeItem<String> Subarchivos = new TreeItem<>(filitas[y]);
                          f.getChildren().add(Subarchivos);
                          System.out.println(filitas[y]);
                      }

                  }
                  mainIns.getChildren().add(f);
              }


          }
          treevinspector.setRoot(mainIns);

      }

        ///Para el TreeView Del inspector xdxdxdxdxdxd si o no raza?







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
            if(treeview.getSelectionModel().getSelectedItem().getValue().equals("Entidades")){
                Stage selecEntidad = new Stage();
                selecEntidad.initModality(Modality.WINDOW_MODAL);
                try{
                    Parent editorSp = FXMLLoader.load(getClass().getResource("../vistas/entidades.fxml"));
                    Scene edSp = new Scene(editorSp,600,400);
                    selecEntidad.setScene(edSp);
                }
                catch(IOException e){e.printStackTrace();}
                selecEntidad.initStyle(StageStyle.UNDECORATED);
                selecEntidad.show();
            }
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




        public static void addEntidad(TreeItem<String> item){


            TreeItem<String> seleccion = copia.getSelectionModel().getSelectedItem();
            seleccion.getChildren().add(item);
            seleccion.setExpanded(true);




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
            codeAreas.setPrefSize(copiaAnchor.getPrefWidth() + 470,400);
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
            tabsito.setText(item.getValue());
            copiaTab.getTabs().add(tabsito);
            codeAreas.replaceText(0,0,0,0," ");
            File script = new File(PATHSCRIPTS,"Script.clemon");
            try{script.createNewFile();}catch(IOException e){e.printStackTrace();}
            actualizarInspector();

        }



}



