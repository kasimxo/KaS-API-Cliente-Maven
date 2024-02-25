package com.kasimxo.api.cliente;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

/**
 * Maneja la respuesta Http, interpreta el body y lo transforma a una lista de strings
 * @author andres
 *
 */
public class MyResponseHandler implements ResponseHandler{

	@Override
	public List<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		List<String> respuesta = new ArrayList<String>();
		InputStream in = response.getEntity().getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line=br.readLine()) != null) {
			respuesta.add(line);
		}
		return respuesta;
	}

}
