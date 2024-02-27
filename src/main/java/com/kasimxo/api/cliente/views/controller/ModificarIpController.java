package com.kasimxo.api.cliente.views.controller;

import java.io.File;

import com.kasimxo.api.cliente.ApiClienteApplication;
import com.kasimxo.api.cliente.utils.Configuracion;
import com.kasimxo.api.cliente.views.MainWindow;
import com.kasimxo.api.cliente.views.ModificarIpWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ModificarIpController {
	
	@FXML
	public void restaurarDireccion(ActionEvent event) {
		Configuracion.restaurarPredeterminado();
        MainWindow.actualizarIpPuerto();
        ModificarIpWindow.stage.close();
	}
	
	/**
	 * Guarda la nueva dirección en el archivo config
	 * @param event
	 */
	@FXML
	public void guardarDireccion(ActionEvent event) {
		TextField ipInput = (TextField) ModificarIpWindow.stage.getScene().lookup("#ipinput");
        String newIp = ipInput.getText();
        if(newIp.length()>=1) {
        	//Simplemente comprobamos que la ip no está vacía, pero no la validamos de ninguna forma
        	Configuracion.actualizarIp(newIp);
        }
        
        TextField puertoInput = (TextField) ModificarIpWindow.stage.getScene().lookup("#puertoinput");
        String newPuerto = puertoInput.getText();
        if(newPuerto.length()>1) {
        	Configuracion.actualizarPuerto(newPuerto);
        }
        
        //Aqui ya hemos actualizado valores, así que cerramos y actualizamosla ventana principal
        MainWindow.actualizarIpPuerto();
        ModificarIpWindow.stage.close();
	}
	
	
	@FXML
	public void cancelar(ActionEvent event) {
		try {
			ModificarIpWindow.stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
