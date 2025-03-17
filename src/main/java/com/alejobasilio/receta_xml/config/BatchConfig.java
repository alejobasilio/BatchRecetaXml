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

/**
 * Clase de configuración para la aplicación de procesamiento por lotes.
 * 
 * @author Alejo
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class BatchConfig {

    /**
     * Crea una lista de ingredientes vacía que se utiliza para almacenar los ingredientes leídos de la base de datos.
     * 
     * @return la lista de ingredientes vacía.
     */
    @Bean
    List<Ingrediente> ingredientes() {
        return new ArrayList<>();
    }

    /**
     * Crea un objeto DataSource que se utiliza para conectarse a la base de datos.
     * 
     * @return el objeto DataSource configurado.
     */
    @Bean
    DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/receta");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("Lahojarota_1324");
        return dataSourceBuilder.build();
    }

    /**
     * Crea un objeto RecetasProcessor que se utiliza para procesar las recetas.
     * 
     * @return el objeto RecetasProcessor configurado.
     */
    @Bean
    RecetasProcessor recetasProcessor() {
        return new RecetasProcessor(ingredientes());
    }

    /**
     * Crea un objeto Job que se utiliza para definir el flujo de trabajo de lectura de ingredientes.
     * 
     * @param jobRepository el repositorio de trabajos que se utiliza para almacenar la información de los trabajos.
     * @param transactionManager el administrador de transacciones que se utiliza para gestionar las transacciones de la base de datos.
     * @return el objeto Job configurado.
     */
    @Bean
    Job leerProductosJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("leerIngredientesJob", jobRepository)
                .flow(leerIngredientesStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    /**
     * Crea un objeto Step que se utiliza para definir el paso de lectura de ingredientes.
     * 
     * @param jobRepository el repositorio de trabajos que se utiliza para almacenar la información de los trabajos.
     * @param transactionManager el administrador de transacciones que se utiliza para gestionar las transacciones de la base de datos.
     * @return el objeto Step configurado.
     */
    @Bean
    Step leerIngredientesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("leerIngredientesStep", jobRepository)
                .tasklet(new LeerIngredientesTasklet(ingredientes()), transactionManager)
                .build();
    }

    /**
     * Crea un objeto Job que se utiliza para definir el flujo de trabajo de creación de recetas.
     * 
     * @param jobRepository el repositorio de trabajos que se utiliza para almacenar la información de los trabajos.
     * @param transactionManager el administrador de transacciones que se utiliza para gestionar las transacciones de la base de datos.
     * @return el objeto Job configurado.
     */
    @Bean
    Job recestasXmlJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("recestasXmlJob", jobRepository)
                .flow(recetasXmlStep(jobRepository, transactionManager))
                .end()
                .build();
    }

    /**
     * Crea un objeto Step que se utiliza para definir el paso de creación de recetas.
     * 
     * @param jobRepository el repositorio de trabajos que se utiliza para almacenar la información de los trabajos.
     * @param transactionManager el administrador de transacciones que se utiliza para gestionar las transacciones de la base de datos.
     * @return el objeto Step configurado.
     */
    @Bean
    Step recetasXmlStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("recetasXmlStep", jobRepository)
                .<Receta, Receta> chunk(10, transactionManager)
                .reader(new RecetasReader())
                .processor(new RecetasProcessor(ingredientes()))
                .writer(new RecetasWriter())
                .build();
    }

    /**
     * Crea un objeto JobLauncherRecetas que se utiliza para lanzar los trabajos de forma secuencial.
     * 
     * @param jobLauncher el objeto JobLauncher que se utiliza para lanzar los trabajos.
     * @param leerIngredientesJob el objeto Job que se utiliza para leer los ingredientes.
     * @param recestasXmlJob el objeto Job que se utiliza para crear las recetas.
     * @return el objeto JobLauncherRecetas configurado.
     */
    @Bean
    JobLauncherRecetas jobLauncherRecetas(JobLauncher jobLauncher,
            @Qualifier("leerIngredientesJob") Job leerIngredientesJob,
            @Qualifier("recestasXmlJob") Job recestasXmlJob) {
        return new JobLauncherRecetas(jobLauncher, leerIngredientesJob, recestasXmlJob);
    }

    /**
     * Crea un objeto CommandLineRunner que se utiliza para lanzar los trabajos de forma secuencial al iniciar la aplicación.
     * 
     * @param jobLauncherRecetas el objeto JobLauncherRecetas que se utiliza para lanzar los trabajos.
     * @return el objeto CommandLineRunner configurado.
     */
    @Bean
    CommandLineRunner commandLineRunner(JobLauncherRecetas jobLauncherRecetas) {
        return args -> {
            jobLauncherRecetas.runJob();
            System.exit(0);
        };
    }
}
