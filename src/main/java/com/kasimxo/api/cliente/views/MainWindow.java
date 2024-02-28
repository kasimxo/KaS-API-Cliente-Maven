package com.kasimxo.api.cliente.views;

import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.kasimxo.api.cliente.ApiClienteApplication;
import com.kasimxo.api.cliente.utils.ActualizarListadoTimerTask;
import com.kasimxo.api.cliente.utils.Configuracion;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MainWindow extends Application {

	public static Stage stage;
	public static Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("Clouding");
		System.out.println("Ejecutando inicialización ventana");
		
		try {
			File r = new File("./src/main/java/com/kasimxo/api/cliente/views/fxml/mainwindow.fxml");
			Parent root = FXMLLoader.load(r.toURL());
            scene = new Scene(root);
            
            if(ApiClienteApplication.imagenes != null) {
            	rellenarListaImagenes();
            }
            
            actualizarIpPuerto();
            
            primaryStage.setScene(scene);
            primaryStage.show();
            
          //Creamos una tarea que cada X tiempo actualiza el listado de imágenes de manera automática
    		TimerTask actualizarImagenes = new ActualizarListadoTimerTask();
    		Timer timer = new Timer(true);
    		timer.scheduleAtFixedRate(actualizarImagenes, 15000, 15000);
       } catch(Exception e) {
            e.printStackTrace();
       } 
	}

	/**
	 * Rellena los campos de Ip y Puerto en la parte superior de la pantalla con los datos de la clase Configuración
	 */
	public static void actualizarIpPuerto() {
		Label ip = (Label) scene.lookup("#ip");
		ip.setText("IP: " + Configuracion.direccionCompleta);
	}
	
	public static void actualizarEstado(String code) {
		if(scene != null) {
			Label estado = (Label) scene.lookup("#estado");
			estado.setText(code);
		}
	}
	
	/**
	 * Actualiza el listado de imágenes
	 */
	public static void rellenarListaImagenes() {
		
		ListView<HBox> listaImagenes = (ListView<HBox>) scene.lookup("#lista");
		
        ObservableList<HBox> items = listaImagenes.getItems();
        items.clear();
        
        //Actualizamos la lista con una peticion post
        ApiClienteApplication.getAllImagenes();
        List<String> files = ApiClienteApplication.imagenes;
        if(files == null) {
        	//Prevenimos excepción por recorrer array null
        	return;
        }
        
        for(String i : files) {
        	
	        HBox linea = new HBox();
	        
	        
	    	Label nombre = new Label();
	    	nombre.setText(i);
	        //label1.setGraphic(new ImageView(image));
	    	nombre.setFont(new Font("Arial", 12));
	    	nombre.setTextFill(Color.web("#0076a3"));
	    	nombre.setTextAlignment(TextAlignment.JUSTIFY); 
	    	linea.getChildren().add(nombre);
	    	
	    	
	    	Region espacio = new Region();
	    	HBox.setHgrow(espacio, Priority.ALWAYS);
	    	linea.getChildren().add(espacio);

	    	
	    	Button btn_descargar = new Button("Descargar");
	    	btn_descargar.setFont(new Font("Arial", 12));
	    	btn_descargar.setTextFill(Color.web("#0076a3"));
	    	btn_descargar.setTextAlignment(TextAlignment.JUSTIFY); 
	    	btn_descargar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("A descargar el archivo " + nombre.getText());
					ApiClienteApplication.getImagen(nombre.getText());
				}
	    	});
	    	linea.getChildren().add(btn_descargar);
	    	
	    	Button btn_renombrar = new Button("Renombrar");
	    	btn_renombrar.setFont(new Font("Arial", 12));
	    	btn_renombrar.setTextFill(Color.web("#0076a3"));
	    	btn_renombrar.setTextAlignment(TextAlignment.JUSTIFY); 
	    	btn_renombrar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.out.println("A renombrar el archivo " + nombre.getText());
					//Aquí tenemos que abrir una nueva ventana que tenga un campo label cn el viejo nombre y el lnuevo
					//botón de aceptar y cancelar
					//y depues haga la operación put
					//ApiClienteApplication.getImagen(nombre.getText());
					try {
						RenombrarArchivoWindow.abrir(nombre.getText());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	    	});
	    	linea.getChildren().add(btn_renombrar);

	    	
	    	Button btn_eliminar = new Button("Eliminar");
	    	btn_eliminar.setFont(new Font("Arial", 12));
	    	btn_eliminar.setTextFill(Color.web("#0076a3"));
	    	btn_eliminar.setTextAlignment(TextAlignment.JUSTIFY); 
	    	btn_eliminar.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ApiClienteApplication.deleteImagen(nombre.getText());
					MainWindow.rellenarListaImagenes();
				}
	    	});
	    	linea.getChildren().add(btn_eliminar);
	    	
	    	
	    	linea.setSpacing(20);
	    	items.add(linea);
	    }
        listaImagenes.setItems(items);
        System.out.println("Actualizada lista imagenes");
	}
	
	
}
