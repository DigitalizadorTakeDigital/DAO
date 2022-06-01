package br.com.dao.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.*;

public class ApiConfig {

private static Logger logger = LogManager.getLogger(ApiConfig.class.getName());
	
public static String setApi(Properties mProp) {	
	    String api = "";
	    try {
            logger.info("Setando API");
	    	mProp = PropertiesUtil.getProp();
		    api =  mProp.getProperty("api");
		} catch (IOException e) {
			logger.error("Erro: " + e.toString());
		}
		return api;		
	}

}
