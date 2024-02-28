package com.kasimxo.api.cliente.handlers;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import com.kasimxo.api.cliente.views.MainWindow;

/**
 * Simple class for Managing responses without a body.
 * Only care is status code
 * @author andres
 *
 */
public class ErrorCodeHandler implements ResponseHandler {

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		String estado = Integer.toString(response.getStatusLine().getStatusCode());
		if(MainWindow.scene != null) {MainWindow.actualizarEstado(estado);}
		//Devolvemos el código de error como string para evitar devolver un primitivo
		return estado;
	}

}
