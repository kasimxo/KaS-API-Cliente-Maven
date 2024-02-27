package com.kasimxo.api.cliente.views.controller;

import com.kasimxo.api.cliente.ApiClienteApplication;
import com.kasimxo.api.cliente.views.ModificarIpWindow;
import com.kasimxo.api.cliente.views.RenombrarArchivoWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RenombrarArchivoController {
	
	@FXML
	public void cambiarNombre(ActionEvent event) {
		Label oldName = (Label) RenombrarArchivoWindow.stage.getScene().lookup("#nombreactual");
		TextField tfnewname = (TextField) RenombrarArchivoWindow.stage.getScene().lookup("#newname");
		String newName = tfnewname.getText();
		if(newName.length() < 1) {
			//No cumple los requisitos, descartamos
			RenombrarArchivoWindow.stage.close();
			return;
		}
		
		//Si el usuario no introduce también el formato, es su problema
		
		ApiClienteApplication.renombrarImagen(oldName.getText(), newName);
		RenombrarArchivoWindow.stage.close();
		
	}

	@FXML
	public void cancelar(ActionEvent event) {
		try {
			RenombrarArchivoWindow.stage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
