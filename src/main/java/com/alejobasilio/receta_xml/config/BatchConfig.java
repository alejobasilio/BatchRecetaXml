package com.alejobasilio.receta_xml.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


import com.alejobasilio.receta_xml.launcher.JobLauncherRecetas;
import com.alejobasilio.receta_xml.model.Ingrediente;
import com.alejobasilio.receta_xml.model.Receta;
import com.alejobasilio.receta_xml.processor.RecetasProcessor;
import com.alejobasilio.receta_xml.reader.RecetasReader;
import com.alejobasilio.receta_xml.tasklet.LeerIngredientesTasklet;
import com.alejobasilio.receta_xml.writer.RecetasWriter;

@Configuration
public class BatchConfig {

	@Bean
	List<Ingrediente> ingredientes(){
		return new ArrayList<>();
	}
	
	@Bean
	 DataSource dataSource() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
		dataSourceBuilder.url("jdbc:mysql://localhost:3306/receta");
		dataSourceBuilder.username("root");
		dataSourceBuilder.password("Lahojarota_1324");
		return dataSourceBuilder.build();
	}
	
	@Bean
	RecetasProcessor recetasProcessor() {
		return new RecetasProcessor(ingredientes());
		
	}
	
	@Bean
    Job leerProductosJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("leerIngredientesJob", jobRepository)
        		.flow(leerIngredientesStep(jobRepository, transactionManager))
                .end().build();
    }
	
	@Bean
	Step leerIngredientesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("leerIngredientesStep", jobRepository)
				.tasklet(new LeerIngredientesTasklet(ingredientes()),transactionManager)
				.build();
	}
	
	@Bean
    Job recestasXmlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("recestasXmlJob", jobRepository)
        		.flow(recetasXmlStep(jobRepository, transactionManager))
                .end().build();
	}
	
	@Bean
	 Step recetasXmlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("recetasXmlStep", jobRepository)
				.<Receta, Receta>chunk(10, transactionManager)
				.reader(new RecetasReader())
				.processor(new RecetasProcessor(ingredientes()))
				.writer(new RecetasWriter())
				.build();
	}
	
	@Bean
	JobLauncherRecetas jobLauncherRecetas(JobLauncher jobLauncher,
			@Qualifier("leerProductosJob")Job leerIngredientesJob,
			@Qualifier("recestasXmlJob")Job recestasXmlJob){
				return new JobLauncherRecetas(jobLauncher, leerIngredientesJob, recestasXmlJob);
				
			}
	
	@Bean
	CommandLineRunner commandLineRunner (JobLauncherRecetas jobLauncherRecetas) {
		return args -> {
			jobLauncherRecetas.runJob();
			System.exit(0);
		};
	}
}
