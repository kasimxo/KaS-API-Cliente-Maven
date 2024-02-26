package com.kasimxo.api.cliente.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

/**
 * Simple class for Managing responses without a body.
 * Only care is status code
 * @author andres
 *
 */
public class ErrorCodeHandler implements ResponseHandler {

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		//Devolvemos el código de error como string para evitar devolver un primitivo
		return Integer.toString(response.getStatusLine().getStatusCode());
	}

}
