package com.kasimxo.api.cliente.views;

import javafx.stage.Stage;

import java.io.IOException;
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

public class MainWindow extends Application {



	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("nepe");
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            //Parent root = FXMLLoader.load(getClass().getResource("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 
		
		/*
		//Create Stage
		Stage newWindow = new Stage();
		newWindow.setTitle("New Scene");
		//Create view from FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainwindow.fxml"));
		//Set view in window
	
		newWindow.setScene(new Scene(loader.load()));
		newWindow.show();
		*/
	}
	
	
}
