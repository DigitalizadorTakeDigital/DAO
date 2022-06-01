package br.com.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;

import org.apache.log4j.*;

import br.com.dao.utils.ApiConfig;
import br.com.dao.utils.MonitoraNotas;
import br.com.dao.utils.PropertiesUtil;

public class MonitorationManager {
	static Properties mProp = new Properties();
	static String transportadora = "";
	static String destino = "";
	static int digitalizacoesHoje,digitalizacoesOntem;
	private static Logger logger = LogManager.getLogger(MonitoraNotas.class.getName());
	
	
	public synchronized static void startMonitoration() {
		try {
			//Setando valores da api via properties
			destino = PropertiesUtil.getDestino(mProp);
			transportadora = PropertiesUtil.getTransportadora(mProp);
			File pastaDestino = new File(destino);
	
			//Verificando data atual
			Date dataAtual = new Date(System.currentTimeMillis());
	
			//Verificando a quantidade de digitalizações no destino com a data atual
			MonitoraNotas monitoraNotas = new MonitoraNotas();
			digitalizacoesHoje = monitoraNotas.getDigitalizacoes(pastaDestino, dataAtual);
	        
			//Iniciando Chamada do DB
			if(digitalizacoesHoje > 0) {			
			logger.info("Iniciando Chamada do DB");
			boolean sucesso = Consumer.registrarDigitalizacao(transportadora, digitalizacoesHoje, dataAtual);
			}else {
			logger.info("Nenhuma NF digitalizada até o momento");	
			}
		}catch (Exception e) {
			logger.error("ERRO : " + e.getMessage());
		}
	}
	

}


