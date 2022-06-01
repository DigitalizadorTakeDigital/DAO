package br.com.dao.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import org.apache.log4j.*;

public class MonitoraNotas {
	private static Logger logger = Logger.getLogger(MonitoraNotas.class.getName());
	

	public int getDigitalizacoes(File destino,Date data){
		logger.info("Verificando arquivos digitalizados no destino");
		int qtdDigitalizacoes = 0;
		try {
		File[] arquivos = destino.listFiles();
		List<File> arquivosDigitalizados = new ArrayList();
		
		for(File arquivo : arquivos){
		Date dataArquivo = new Date(arquivo.lastModified());
			if(dataArquivo.getMonth() == data.getMonth()) {
				if(dataArquivo.getDate() == data.getDate() ) {
				arquivosDigitalizados.add(arquivo);
				}		
			}			
		}		
		qtdDigitalizacoes = arquivosDigitalizados.size();
		arquivosDigitalizados.clear();
		logger.info("Econtrados " + qtdDigitalizacoes + " Arquivos digitalizados em " + data.toString() );
		}catch (Exception e) {
		logger.error("Erro : "+ e.getMessage());
		}
	    return qtdDigitalizacoes;
		}
	
//	public Date getYesterday(){
//	     return new Date(System.currentTimeMillis()-24*60*60*1000);
//	}

	}


