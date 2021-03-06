package br.com.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.*;

import br.com.dao.utils.ApiConfig;

public class Consumer {

	static Properties prop = new Properties();
	static String api = "";
	private static Logger logger = Logger.getLogger(Consumer.class.getName());

	public static boolean registrarDigitalizacao(String transportadora, int qtdeDigitacoes, Date dateToFind) {
		
		api = ApiConfig.setApi(prop);
		String token = getAuthToken();
		String transportadoraId = getTransportadoraId(transportadora, token);
		int qtdDigitalizacoes = getdigitalizacoes(transportadoraId, token, dateToFind);
		if(qtdDigitalizacoes < qtdeDigitacoes)	{
		logger.info("Existem Nostas Digitalizadas a subir no DB");
		int qtdASalvar =  qtdeDigitacoes - qtdDigitalizacoes ;	
		return postDigitacoes(transportadoraId, token, qtdASalvar);
		}
		else {
		logger.info("DB Atualizado");
		return false;
		}
	}

	private static String getAuthToken() {
		String token = "";
		try {
			logger.info("Iniciando request, para receber Token");
			URL url = new URL(api+"/v1/api/usuarios/login");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String input = "{\"email\":\"email@10email.com\",\"password\":\"senha123\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			while ((output = br.readLine()) != null) {
				token = output.substring(output.indexOf("token")).replace("token\":\"", "").replace("\"}}", "");
		        logger.info("Token Gerado" + token);
			}
			
			conn.disconnect();
		} catch (Exception e) {
			logger.error("Erro: " + e.toString());
			if(e.toString().contains("Connection refused"))
				logger.error("Erro de conex???o, provavelmente a API n???o est??? rodando");
		}
		return token;
	}
	
	private	static String getTransportadoraId(String transportadora, String token) {
		
		String transportadoraId = "";
		try {
			logger.info("Iniciando request para pegar o ID da Transportadora");
			URL url = new URL(api+"/v1/api/transportadoras/nome/" + transportadora);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				System.out.print("deu erro... HTTP error code : " + conn.getResponseCode());
			    logger.error("Erro: " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output, json = "";
			while ((output = br.readLine()) != null) {
				json += output;
			}
			transportadoraId = json.substring(json.indexOf("_id"), json.indexOf("_id") + 30).replace("_id\":\"", "");
			logger.info("Id recebido com sucesso : " + transportadoraId);
			conn.disconnect();
		} catch (Exception e) {
			logger.error("Erro: " + e.toString());
		}
		return transportadoraId;
	}

	private	static int getdigitalizacoes(String transportadoraId, String token, Date dataDigitalizacao) {
		String dataAPesquisar = String.valueOf(dataDigitalizacao);
		int QtdDigitalizada = 0;
		try {
			logger.info("Iniciando request para validar quantidade digitalizada no DB");
			URL url = new URL(api+"/v1/api/digitalizacoes");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				logger.error("Erro: " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output, json = "";
			String[] jsonSplitter ;

			while ((output = br.readLine()) != null) {
				json += output;
			}
			
			jsonSplitter = json.split("}");
			String[] transportaDigitInfo = null;
			for (String currentDigitInfo : jsonSplitter) {
				if(currentDigitInfo.contains(transportadoraId)) {
					if(currentDigitInfo.contains(dataAPesquisar)) {
					transportaDigitInfo = currentDigitInfo.split(",");
					}					
				}
			}
			if(transportaDigitInfo!=null) {
				String qtdD = transportaDigitInfo[4].replace("\"qtde_digitalizacoes\":", "");
				QtdDigitalizada = Integer.parseInt(qtdD);
				logger.info(QtdDigitalizada + " documentos digitalizados no DB no momento ");
			}else {
				logger.info("Nenhum registro no DB Hoje");
				QtdDigitalizada = 0;
			}
			conn.disconnect();
		} catch (Exception e) {
		    logger.error("Erro: " + e.toString());
		}
		return QtdDigitalizada;
	}

	
	
	private static boolean postDigitacoes(String transportadoraId, String token, int i) {
		String digitalizacoes = "";

		try {
			logger.info("Iniciando o post no DB");
			URL url = new URL(api+"/v1/api/digitalizacoes/?transportadora="+transportadoraId+"&qtde_digitalizacoes=" + i);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Ecommercy " + token);

			String input = "{\"transportadora\":\"" + transportadoraId + "\",\"qtde_digitalizacoes\":\"" + i + "\"}";
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				digitalizacoes+=output;
			}

			conn.disconnect();

		} catch (Exception e) {
			logger.error("Erro: "+e.toString());
			return false;
		}
		
		if(digitalizacoes.equals(""))
			return false;
		else
			return true;
		
	}

}