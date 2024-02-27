package com.kasimxo.api.cliente.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.HashMap;

public class Configuracion {
	
	//Direcciones que se usarán durante la ejecución del programa
	public static String ip;
	public static String puerto;
	
	public static String direccionCompleta;
	
	public static File archivoConfig;

	private static Map<String, String> rutas;
	
	public Configuracion() {
		rutas = new HashMap<String, String>();
		//Windows
		//archivoConfig = new File(".\\files\\config.txt"); 
		//Linux
		archivoConfig = new File("./src/main/resources/config.txt"); 
		
		try {
			
			if(!archivoConfig.exists()) {
				archivoConfig.createNewFile();
				actualizarArchivoConfig();
			}
			
			for (String linea : Files.readAllLines(archivoConfig.toPath())) {
				String[] separado = linea.split("=");
				
				rutas.put(separado[0], separado[1]);
			}
		} catch (IOException e) {
			
		}
		//Aqui ponemos cada ruta
		rutas.forEach((K, V) -> {
			switch (K) {
			case "ip":
				ip = V;
				break;
			case "puerto":
				puerto = V;
				break;
			default:
				break;
			}
		});
		
		direccionCompleta = "http://"+ip+":"+puerto;
	}
	
	public static void actualizarArchivoConfig() {
		
		try {
			FileWriter fw = new FileWriter(archivoConfig);
			
			rutas.forEach((K, V) -> {
				String linea = String.format("%s=%s\n", K, V);
				try {
					fw.write(linea, 0, linea.length()); 
					fw.flush(); 
				} catch (Exception e) {
				}
			});
			

			fw.close(); 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public static void actualizarRutas() {
		rutas.put("ip", ip);
		rutas.put("puerto", puerto);
		direccionCompleta = "http://"+ip+":"+puerto;
		actualizarArchivoConfig();
	}
	
	public static void actualizarIp(String newIp) {
		ip = newIp;
		actualizarRutas();
	}
	
	public static void actualizarPuerto(String newPuerto) {
		puerto = newPuerto;
		actualizarRutas();
	}
	
	/**
	 * Restaura la configuración predeterminada <br>
	 * IP: localhost<br>
	 * Puerto: 8081
	 */
	public static void restaurarPredeterminado() {
		ip = "localhost";
		puerto = "8081";
		actualizarRutas();
	}
	
	
}
