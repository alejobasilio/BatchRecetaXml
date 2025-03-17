package com.alejobasilio.receta_xml.processor;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.alejobasilio.receta_xml.model.Ingrediente;
import com.alejobasilio.receta_xml.model.Receta;

/**
 * Clase que procesa las recetas y verifica si hay suficientes ingredientes para prepararlas.
 * 
 * @author Alejo
 * @version 1.0
 * @since 1.0
 */
@Component
public class RecetasProcessor implements ItemProcessor<Receta,Receta>{

	private final List<Ingrediente> ingredientes;
	
	public RecetasProcessor(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	@Override
	public Receta process(Receta item) throws Exception {
		
		for (Ingrediente ingrediente : ingredientes) {
			if (item.getIngrediente().equals(ingrediente.getIngrediente())) {
				if (ingrediente.getCantidad()>=item.getCantidad()) {
					System.out.println(item.getIngrediente());
					return item;
				}
			}
		}
		return null;
	}
	
	
}
