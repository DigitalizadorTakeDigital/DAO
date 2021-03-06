package br.com.dao.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.*;

import br.com.dao.MainMonitaramentoTakeDigital;

public class PropertiesUtil {
	private static Logger logger = LogManager.getLogger(PropertiesUtil.class.getName());
	
	public static String getTransportadora(Properties mProp) {	
		String	transportadora = "";
		try {
			logger.info("Setando Transportadora");
			mProp = getProp();
		    transportadora = mProp.getProperty("transportadora");
	    } catch (IOException e) {
		logger.error("Erro : " + e.getMessage());
		}
		return transportadora; 
	}
	
	public static String getDestino(Properties mProp) {	
		String	destino = "";
		try {
			logger.info("Setando Destino");
			mProp = getProp();
		    destino = mProp.getProperty("destino");
	    } catch (IOException e) {
			logger.error("Erro : " + e.getMessage());
	    }
		return destino; 
   }
	
	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		try {
			logger.info("Lendo Arquivo Properties");
			FileInputStream file = new FileInputStream(
					"C:\\BGCC-ClientCaptura\\dao\\configTakeDigital.properties");
			props.load(file);	
		}catch (Exception e) {
			logger.error("Erro : " + e.getMessage());
		}
		return props;
	}
	
}
