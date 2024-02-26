package com.kasimxo.api.cliente.views;

import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import com.kasimxo.api.cliente.ApiClienteApplication;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

public class MainWindow extends Application {



	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            Scene scene = new Scene(root);
            
           
            
        	ListView<String> lista = (ListView<String>) scene.lookup("#lista");
        	ObservableList<String> items = FXCollections.observableArrayList();
        	for (String i : ApiClienteApplication.imagenes) { items.add(i); };
        	lista.setItems(items);

            
            
            primaryStage.setScene(scene);
            primaryStage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 

	}
	
	
}
