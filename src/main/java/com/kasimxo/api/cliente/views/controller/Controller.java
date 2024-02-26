package com.kasimxo.api.cliente.views.controller;

import java.io.File;

import com.kasimxo.api.cliente.ApiClienteApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class Controller {

	@FXML
	public void seleccionarArchivo(ActionEvent event) {
		System.out.println("Vamos a seleccionar un archivo");
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Selección de archivo");
		File selected = fc.showOpenDialog(null);
		
		if (selected == null) {
			System.out.println("El usuario no ha seleccionado un archivo");
			return;
		} else {
			System.out.println("El usuario ha seleccionado el archivo: " + selected.getName());
			ApiClienteApplication.postImagen(selected);
		}
	}

	
}
