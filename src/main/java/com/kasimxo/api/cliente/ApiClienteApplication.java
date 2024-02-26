package com.kasimxo.api.cliente;

import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kasimxo.api.cliente.utils.Input;
import com.kasimxo.api.cliente.utils.ParameterStringBuilder;
import com.kasimxo.api.cliente.views.MainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class ApiClienteApplication {
	
	public static boolean funcionando;
	public static Input i;
	
	public static MainWindow mw;

	public static void main(String[] args) {
		
		
		MainWindow.launch(MainWindow.class);
		
		funcionando = true;
		i = new Input();
		while (funcionando) {
			menu();
		}
	}
	
	public static void menu() {
		String[] opciones = {
				"1. Subir imagen",
				"2. Descargar imagen",
				"3. Directorio de imágenes",
				"4. Salir"
				};
		
		for (String s : opciones) { System.out.println(s);}
		
		switch (Input.leerInt()) {
		case 1:
			//Subir imagen
			postImagen();
			break;
		case 2:
			//Descargar imagen
			getImagen();
			break;
		case 3:
			//Directorio imágenes
			getAllImagenes();
			break;
		case 4:
			funcionando = false;
			break;
		default:
			System.out.println("Opción no reconocida");
			break;
		
		}

	}
	
	
	public static void getAllImagenes() {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpGet request = new HttpGet("http://localhost:8081/imagenes");
			
			//CloseableHttpResponse response = httpClient.execute(request);
			ResponseHandler<List<String>> responseHandler = new MyResponseHandler(); 
			List<String> response = httpClient.execute(request, responseHandler);
			
			for(String s : response) {System.out.println(s);}
			System.out.println();
			//System.out.println(response.getStatusLine());
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void postImagen() {
		try {

			URL url = new URL("http://localhost:8081/upload");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			File transfer = new File("./imagenes/1.png");
		
			System.out.println(transfer.getName());
			
			byte[] byteArray = Files.readAllBytes(Paths.get(transfer.getAbsolutePath()));
			String encoded = Base64.getUrlEncoder().encodeToString(byteArray);
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("file", encoded);
			parameters.put("fileName", transfer.getName());
			
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			
			int status = con.getResponseCode();
			
			System.out.println(status);
			out.flush();
			out.close();
			System.out.println("end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getImagen() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
