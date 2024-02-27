package com.kasimxo.api.cliente;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kasimxo.api.cliente.utils.ListadoResponseHandler;
import com.kasimxo.api.cliente.utils.Base64ResponseHandler;
import com.kasimxo.api.cliente.utils.Configuracion;
import com.kasimxo.api.cliente.utils.ErrorCodeHandler;
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
	
	public static Configuracion config;
	
	public static List<String> imagenes; //Temporal, guarda los nombres de las imágenes recuperadas del servidor

	public static void main(String[] args) {
		
		config = new Configuracion();
		
		imagenes = getAllImagenes();
		
		MainWindow.launch(MainWindow.class);
		
		funcionando = true;

	}
	
	/**
	 * Petición PUT para renombrar un archivo en el servidor
	 * @param id -> Id de la imagen a renombrar
	 * @param filename -> Nuevo nombre para la imagen
	 */
	public static void renombrarImagen(String id, String filename) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String url = Configuracion.direccionCompleta+"/imagenes/" + id;
			HttpPut request = new HttpPut(url);
			
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			NameValuePair param = new BasicNameValuePair("name", filename);
			nameValuePairs.add(param);
			
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));

			ResponseHandler<String> responseHandler = new ErrorCodeHandler(); 
			String e = httpClient.execute(request, responseHandler);
			System.out.println(e);
		
		 	System.out.println("Archivo renombrado");

		 	
			return;
		} catch (UnknownHostException ue) {
			System.out.println("Excepcion");
			ue.printStackTrace();
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteImagen(String filename) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String url = Configuracion.direccionCompleta+"/imagenes/" +  filename;
			HttpDelete request = new HttpDelete(url);
			
			ResponseHandler<String> responseHandler = new ErrorCodeHandler(); 
			String e = httpClient.execute(request, responseHandler);
			System.out.println(e);
		
		 	System.out.println("Hemos eliminado el archivo");

		 	
			return;
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<String> getAllImagenes() {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpGet request = new HttpGet(Configuracion.direccionCompleta+"/imagenes");
			
			ResponseHandler<List<String>> responseHandler = new ListadoResponseHandler(); 
			List<String> response = httpClient.execute(request, responseHandler);
			
			for(String s : response) {System.out.println(s);}
			imagenes = response;
			return response;
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void postImagen(File imagen) {
		try {

			URL url = new URL(Configuracion.direccionCompleta+"/upload");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
		
			System.out.println(imagen.getName());
			
			byte[] byteArray = Files.readAllBytes(Paths.get(imagen.getAbsolutePath()));
			String encoded = Base64.getUrlEncoder().encodeToString(byteArray);
			System.out.println(imagen.getName());
			String name = URLEncoder.encode(imagen.getName(), StandardCharsets.UTF_8);
			System.out.println(name);
			Map<String, String> parameters = new HashMap<>();
			parameters.put("file", encoded);
			parameters.put("fileName", name);
			
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			
			int status = con.getResponseCode();
			
			System.out.println(status);
			out.flush();
			out.close();
			System.out.println("end");
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getImagen(String filename) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			//String url = Configuracion.direccionCompleta+"/imagenes/" +  URLEncoder.encode(filename, StandardCharsets.UTF_8);
			HttpGet request = new HttpGet(Configuracion.direccionCompleta+"/imagenes/" +  filename);
			
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
	 		
		 	FileOutputStream fos = new FileOutputStream(finalFile);
		 	
		 	fos.write(decodedBytes);
		 	fos.flush();
		 	fos.close();

		 	System.out.println("Hemos guardado el archivo");

			return;
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
