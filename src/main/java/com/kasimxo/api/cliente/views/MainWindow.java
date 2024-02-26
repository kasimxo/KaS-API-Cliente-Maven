package com.kasimxo.api.cliente.views;

import javafx.stage.Stage;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class MainWindow extends Application {



	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            Scene scene = new Scene(root);
            
            primaryStage.setScene(scene);
            primaryStage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 

	}
	
	
}
