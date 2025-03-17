package com.alejobasilio.receta_xml.tasklet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;

import com.alejobasilio.receta_xml.model.Ingrediente;

public class LeerIngredientesTasklet implements Tasklet{

	private final List<Ingrediente> ingredientes;
	
	public LeerIngredientesTasklet(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("input/ingredientes.csv").getInputStream()))) {
			String linea;
			reader.readLine();
			while ((linea = reader.readLine()) !=null) {
				String[]campos = linea.split(";");
				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setId(Long.parseLong(campos[0]));
				ingrediente.setIngrediente(campos[1]);
				ingrediente.setCantidad(Integer.parseInt(campos[2]));
				ingredientes.add(ingrediente);
			}
			return RepeatStatus.FINISHED;
		} 
		
	}

}
