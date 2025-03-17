package com.alejobasilio.receta_xml.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa un ingrediente en la aplicación de recetas.
 * 
 * @author Alejo
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingrediente {

	private Long id;
	private String ingrediente;
	private Integer cantidad;
}
