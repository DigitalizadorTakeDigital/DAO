package br.com.dao;

import java.nio.file.Paths;

import org.apache.log4j.*;
import br.com.dao.utils.MonitoraNotas;

public class MainMonitaramentoTakeDigital {
	private static Logger logger = null;
	

	public static void main(String[] args) {
		while(true) {
			try {
				String path = Paths.get(MainMonitaramentoTakeDigital.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();
				PropertyConfigurator.configure(path + "\\log4j.properties");
				logger = LogManager.getLogger(MainMonitaramentoTakeDigital.class.getName());
				logger.info("Iniciando Monitaramento de Notas Fiscais");
				MonitorationManager.startMonitoration();
				logger.info("Pausando o Monitaramento de Notas Fiscais");
				Thread.sleep(1800000);
			} catch (Exception e) {
				logger.error("Erro : "+ e.getMessage());
			}
		}
    }
	
}
