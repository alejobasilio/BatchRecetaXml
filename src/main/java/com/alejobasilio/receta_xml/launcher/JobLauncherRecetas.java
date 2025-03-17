package com.alejobasilio.receta_xml.launcher;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

public class JobLauncherRecetas {

	private JobLauncher jobLauncher;
	private Job leerIngredientesJob;
	private Job recestasXmlJob;

	public JobLauncherRecetas(JobLauncher jobLauncher, Job leerIngredientesJob, Job recestasXmlJob) {
		super();
		this.jobLauncher = jobLauncher;
		this.leerIngredientesJob = leerIngredientesJob;
		this.recestasXmlJob = recestasXmlJob;
	}

	public void runJob() {
		JobParameters jobLeerIngredientes = new JobParametersBuilder()
				.addString("ID", "Leer ingredientes")
				.addDate("date", new Date())
				.toJobParameters();
		
		JobParameters jobRecestasXml = new JobParametersBuilder()
				.addString("ID", "Crear Xml de Recetas")
				.addDate("date", new Date())
				.toJobParameters();
		
		try {
			jobLauncher.run(leerIngredientesJob,jobLeerIngredientes);
			jobLauncher.run(recestasXmlJob,jobRecestasXml);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
