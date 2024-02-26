package com.kasimxo.api.cliente;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import com.kasimxo.api.cliente.utils.ListadoResponseHandler;
import com.kasimxo.api.cliente.utils.Base64ResponseHandler;
import com.kasimxo.api.cliente.utils.ParameterStringBuilder;
import com.kasimxo.api.cliente.views.MainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

@SpringBootApplication
public class ApiClienteApplication {
	
	public static boolean funcionando;
	public static Input i;
	
	public static MainWindow mw;
	
	public static List<String> imagenes; //Temporal, guarda los nombres de las imágenes recuperadas del servidor

	public static void main(String[] args) {
		
		imagenes = getAllImagenes();
		System.out.println(imagenes.size());
		
		MainWindow.launch(MainWindow.class);
		
		funcionando = true;

	}

	
	
	public static List<String> getAllImagenes() {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpGet request = new HttpGet("http://localhost:8081/imagenes");
			
			//CloseableHttpResponse response = httpClient.execute(request);
			ResponseHandler<List<String>> responseHandler = new ListadoResponseHandler(); 
			List<String> response = httpClient.execute(request, responseHandler);
			
			for(String s : response) {System.out.println(s);}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void postImagen(File imagen) {
		try {

			URL url = new URL("http://localhost:8081/upload");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
		
			System.out.println(imagen.getName());
			
			byte[] byteArray = Files.readAllBytes(Paths.get(imagen.getAbsolutePath()));
			String encoded = Base64.getUrlEncoder().encodeToString(byteArray);
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("file", encoded);
			parameters.put("fileName", imagen.getName());
			
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
	
	public static void getImagen(String filename) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpGet request = new HttpGet("http://localhost:8081/imagenes/" + filename);
			
			ResponseHandler<String> responseHandler = new Base64ResponseHandler(); 
			
			String rawBase64Response = httpClient.execute(request, responseHandler);
			
			byte[] decodedBytes = Base64.getUrlDecoder().decode(rawBase64Response);
		 	System.out.println(decodedBytes.length);
		 	//File f = new File("./src/main/resources/imagenes/"+fileName);
		 	
		 	DirectoryChooser chooser = new DirectoryChooser();
		 	chooser.setTitle("Selecciona el directorio en el que guardar el archivo \"" + filename + "\"");


		 	File selectedDirectory = chooser.showDialog(MainWindow.stage);
		 	
		 	File finalFile = new File(selectedDirectory.getAbsolutePath()+"/"+filename);
			
	 		finalFile.createNewFile();
	 		System.out.println("Vamos a iniciar fos");
	 		
		 	FileOutputStream fos = new FileOutputStream(finalFile);
		 	
		 	System.out.println("Hemos iniciado fos");
		 	fos.write(decodedBytes);
		 	fos.flush();
		 	fos.close();

		 	System.out.println("Hemos guardado el archivo");

		 	
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
