package sample.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditorSprite implements Initializable {



    @FXML
    ColorPicker piker;
    @FXML
    GridPane gpane;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int x = 0; x < 32; x++){

           for(int y = 0; y < 32; y++){
               Canvas cv = new Canvas();
               Label lb = new Label();
               lb.setText("Mecos");
               cv.setWidth(16);
               cv.setHeight(16);

               cv.setOnMousePressed(e-> {
                       cv.getGraphicsContext2D().setFill(piker.getValue());
                       cv.getGraphicsContext2D().fillRect(0, 0, cv.getWidth(), cv.getHeight());
               });
               gpane.add(cv,x,y,1,1);
           }
        }
    }



    public void save(ActionEvent evt){

        try {
            gpane.gridLinesVisibleProperty().set(false);
            Image snapshot = gpane.snapshot(null,null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
            gpane.gridLinesVisibleProperty().set(true);
        } catch(IOException e){
            e.printStackTrace();
        }


    }



}
