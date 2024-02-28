package com.kasimxo.api.cliente.views;

import java.io.File;

import com.kasimxo.api.cliente.utils.Configuracion;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class ModificarIpWindow {

	public static Stage stage;

	public static void abrir() throws Exception {
		stage = new Stage();
		stage.setTitle("Nueva dirección");
		System.out.println("Ejecutando inicialización ventana modificar dirección");
		
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/modificaripwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            
            TextField ipInput = (TextField) scene.lookup("#ipinput");
            ipInput.setPromptText(Configuracion.ip);
            
            TextField puertoInput = (TextField) scene.lookup("#puertoinput");
            puertoInput.setPromptText(Configuracion.puerto);
            
            stage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 
	}
	
	
}
