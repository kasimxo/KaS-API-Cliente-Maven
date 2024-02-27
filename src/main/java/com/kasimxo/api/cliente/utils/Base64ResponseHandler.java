package com.kasimxo.api.cliente.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.springframework.http.HttpStatus;

import com.kasimxo.api.cliente.views.MainWindow;

/**
 * Interpreta respuestas del servidor que son puro String en base 64
 * y las devuelve
 */
public class Base64ResponseHandler implements ResponseHandler {

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		String respuesta = "";
		
		int status = response.getStatusLine().getStatusCode();
		if(MainWindow.scene != null) {MainWindow.actualizarEstado(Integer.toString(status));}
		if (status < 200 || status >= 300) {
			System.out.println("Problema con la respuesta");
			System.out.println(status);
			return null;
		}
		
		InputStream in = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		char c;
		while ((line = br.readLine()) != null) {
			respuesta += line;
		}
		return respuesta;
	}

}
