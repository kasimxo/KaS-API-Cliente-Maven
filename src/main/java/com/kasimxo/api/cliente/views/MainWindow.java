package com.kasimxo.api.cliente.views;

import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import com.kasimxo.api.cliente.ApiClienteApplication;
import com.kasimxo.api.cliente.views.controller.Controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MainWindow extends Application {

	public static Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		System.out.println("Ejecutando inicialización ventana");
		
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            Scene scene = new Scene(root);
            

            
            ListView<HBox> listaImagenes = (ListView<HBox>) scene.lookup("#lista");
            ObservableList<HBox> items = FXCollections.observableArrayList();
            
            for(String i : ApiClienteApplication.imagenes) {
            	System.out.println("iterando");
            	
		        HBox linea = new HBox();
		        
		        
		    	Label nombre = new Label(i);
		        //label1.setGraphic(new ImageView(image));
		    	nombre.setFont(new Font("Arial", 12));
		    	nombre.setTextFill(Color.web("#0076a3"));
		    	nombre.setTextAlignment(TextAlignment.JUSTIFY); 
		    	linea.getChildren().add(nombre);

		    	
		    	Button btn_descargar = new Button("Descargar");
		    	btn_descargar.setFont(new Font("Arial", 12));
		    	btn_descargar.setTextFill(Color.web("#0076a3"));
		    	btn_descargar.setTextAlignment(TextAlignment.JUSTIFY); 
		    	btn_descargar.setOnAction(new EventHandler<ActionEvent>() {

		    		//Accion que realiza el botón cuando le hacen clic
					@Override
					public void handle(ActionEvent event) {
						System.out.println("A descargar el archivo " + nombre.getText());
						ApiClienteApplication.getImagen(nombre.getText());
						
					}
		    		
		    	});
		    	linea.getChildren().add(btn_descargar);

		    	
		    	Button btn_eliminar = new Button("Eliminar");
		    	btn_eliminar.setFont(new Font("Arial", 12));
		    	btn_eliminar.setTextFill(Color.web("#0076a3"));
		    	btn_eliminar.setTextAlignment(TextAlignment.JUSTIFY); 
		    	btn_eliminar.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						ApiClienteApplication.deleteImagen(nombre.getText());
						
					}
		    		
		    	});
		    	linea.getChildren().add(btn_eliminar);
		    	
		    	
		    	linea.setSpacing(20);
		    	items.add(linea);
		    }
            listaImagenes.setItems(items);
            
            
            System.out.println(items.size());
            
            primaryStage.setScene(scene);
            primaryStage.show();
       } catch(Exception e) {
            e.printStackTrace();
       } 

	}
	
	
}
