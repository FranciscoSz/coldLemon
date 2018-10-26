package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;

public class Entidades {

    @FXML
    TextField txtNombre;

    Controller c = new Controller();

        public void evtAceptar(ActionEvent event){


            TreeItem<String> item = new TreeItem<>(txtNombre.getText());
            Controller.addEntidad(item);
        }

}
