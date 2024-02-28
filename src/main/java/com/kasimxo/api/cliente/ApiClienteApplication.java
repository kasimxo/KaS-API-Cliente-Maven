package com.kasimxo.api.cliente;

import java.io.File;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.kasimxo.api.cliente.handlers.Base64ResponseHandler;
import com.kasimxo.api.cliente.handlers.ErrorCodeHandler;
import com.kasimxo.api.cliente.handlers.ListadoResponseHandler;
import com.kasimxo.api.cliente.utils.Configuracion;
import com.kasimxo.api.cliente.views.MainWindow;

import javafx.stage.DirectoryChooser;

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
			CloseableHttpClient httpClient = crearServicio(5);
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
		} catch (ConnectTimeoutException cte){
			System.out.println("Se ha excedido el tiempo máximo para establecer conexión");
			MainWindow.actualizarEstado("Excedido tiempo máximo para establecer conexión");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteImagen(String filename) {
		try {
			CloseableHttpClient httpClient = crearServicio(5);
			String url = Configuracion.direccionCompleta+"/imagenes/" +  filename;
			HttpDelete request = new HttpDelete(url);
			
			ResponseHandler<String> responseHandler = new ErrorCodeHandler(); 
			String e = httpClient.execute(request, responseHandler);
			System.out.println(e);
		
		 	System.out.println("Hemos eliminado el archivo");

		 	
			return;
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (ConnectTimeoutException cte){
			System.out.println("Se ha excedido el tiempo máximo para establecer conexión");
			MainWindow.actualizarEstado("Excedido tiempo máximo para establecer conexión");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static List<String> getAllImagenes() {
		try {
			
			
			CloseableHttpClient httpClient = crearServicio(5);
			
			HttpGet request = new HttpGet(Configuracion.direccionCompleta+"/imagenes");
			
			
			ResponseHandler<List<String>> responseHandler = new ListadoResponseHandler(); 
			List<String> response = httpClient.execute(request, responseHandler);
			
			for(String s : response) {System.out.println(s);}
			imagenes = response;
			return response;
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
			return null;
		} catch (ConnectTimeoutException cte){
			System.out.println("Se ha excedido el tiempo máximo para establecer conexión");
			MainWindow.actualizarEstado("Excedido tiempo máximo para establecer conexión");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Enviamos un multipartfile para poder enviar archivos mas grandes
	 * @param archivo
	 */
	public static void postMultiPartFile(File archivo) {
		try {
			CloseableHttpClient httpClient = crearServicio(50);
			HttpPost request = new HttpPost(Configuracion.direccionCompleta+"/subir" );
			
			byte[] byteArray = Files.readAllBytes(Paths.get(archivo.getAbsolutePath()));
			String encoded = Base64.getUrlEncoder().encodeToString(byteArray);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("file", byteArray, 
			    ContentType.create(Files.probeContentType(archivo.toPath())), 
			    archivo.getName());
			HttpEntity entity = builder.build();
			request.setEntity(entity);
			request.setHeader("filename", archivo.getName());
			httpClient.execute(request);
			
		} catch (UnknownHostException ue) {
			MainWindow.actualizarEstado("Host desconocido (failure in name resolution)");
		} catch (ConnectTimeoutException cte){
			System.out.println("Se ha excedido el tiempo máximo para establecer conexión");
			MainWindow.actualizarEstado("Excedido tiempo máximo para establecer conexión");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Excepcion al subir un archivo");
		}
	
	}
	
	/**
	 * Recupera un archivo del servidor
	 * @param filename
	 */
	public static void getImagen(String filename) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
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
		} catch (ConnectTimeoutException cte){
			System.out.println("Se ha excedido el tiempo máximo para establecer conexión");
			MainWindow.actualizarEstado("Excedido tiempo máximo para establecer conexión");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Establece un cliente http que se desconectará después de un tiempo (2 segundos)
	 * @return
	 */
	public static CloseableHttpClient crearServicio(int timeout) {
		
		RequestConfig config = RequestConfig.custom()
		  .setConnectTimeout(timeout * 1000)
		  .setConnectionRequestTimeout(timeout * 1000)
		  .setSocketTimeout(timeout * 1000).build();
		
		ConnectionConfig connConfig = ConnectionConfig.DEFAULT;
		
		BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		cm.setConnectionConfig(connConfig);
		
		CloseableHttpClient httpClient =  HttpClientBuilder.create()
		    .setDefaultRequestConfig(config)
		    .setConnectionManager(cm)
		    .setConnectionTimeToLive(timeout, TimeUnit.MILLISECONDS)
		    .build();
		return httpClient;
	}


}
