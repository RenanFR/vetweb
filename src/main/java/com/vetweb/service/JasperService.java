package com.vetweb.service;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.vetweb.model.report.Report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Component
public class JasperService {
	
	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:postgresql://localhost:5432/vetweb_database", "postgres", "postgres");
		} catch (SQLException sqlException) {
			throw new RuntimeException(sqlException);
		}
	}

	public void gerarRelatorio(Report report, OutputStream outputStream) {
		try {
			Connection connection = getConnection();
			String reportName = report.getType().name();
			Map<String, Object> parameterMap = report
													.getParameters()
													.stream()
													.collect(Collectors.toMap(param -> param.getKey(), param -> param.getValue()));
			String reportLocation = System.getProperty("user.dir") + "/src/main/resources/";
			String path = reportLocation.concat(reportName + ".jrxml");
			JasperCompileManager.compileReportToFile(path);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportLocation.concat(reportName + ".jasper"), parameterMap, connection);;
			JRExporter jrExporter = new JRPdfExporter();
			jrExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//			FileOutputStream value = new FileOutputStream(reportLocation.concat(reportName + ".pdf"));
			jrExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			jrExporter.exportReport();
			connection.close();
		} catch (JRException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
