package com.kasimxo.api.cliente.views;

import java.io.File;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RenombrarArchivoWindow {
	
	public static Stage stage;

	public static void abrir(String oldname) throws Exception {
		stage = new Stage();
		stage.setTitle("Renombrar archivo");
		System.out.println("Ejecutando inicialización ventana renombrar archivo");
		
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/renombrararchivowindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            
            Label nombreactual = (Label) scene.lookup("#nombreactual");

            nombreactual.setText(oldname);
            
            stage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 
	}
}
