package com.kasimxo.api.cliente.views.controller;

import java.io.File;

import com.kasimxo.api.cliente.ApiClienteApplication;
import com.kasimxo.api.cliente.views.MainWindow;
import com.kasimxo.api.cliente.views.ModificarIpWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class Controller {
	
	@FXML
	public void actualizarImagenes(ActionEvent event) {
		System.out.println("Actualizando lista imagenes");
		MainWindow.rellenarListaImagenes();
	}

	@FXML
	public void seleccionarArchivo(ActionEvent event) {
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Selección de archivo");
		File selected = fc.showOpenDialog(null);
		
		if (selected == null) {
			System.out.println("El usuario no ha seleccionado un archivo");
			return;
		} else {
			System.out.println("El usuario ha seleccionado el archivo: " + selected.getName());
			//ApiClienteApplication.postImagen(selected);
			ApiClienteApplication.postMultiPartFile(selected);
		}
		MainWindow.rellenarListaImagenes();
	}

	@FXML
	public void modificarIp(ActionEvent event) {
		try {
			ModificarIpWindow.abrir();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
