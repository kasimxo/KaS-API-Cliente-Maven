package com.kasimxo.api.cliente.utils;

import java.util.TimerTask;

import com.kasimxo.api.cliente.views.MainWindow;

import javafx.application.Platform;

public class ActualizarListadoTimerTask extends TimerTask{

	@Override
	public void run() {
		//Hacemos una llamada al thread principal para que actualice la interfaz
		Platform.runLater(() -> MainWindow.rellenarListaImagenes());
	}
}
