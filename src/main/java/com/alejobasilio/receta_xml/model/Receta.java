package com.alejobasilio.receta_xml.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

	private Long id;
	private String nombre;
	private String ingrediente;
	private Integer cantidad;
	private String ingredienteOpc;
	private Integer cantidadOpc;
	
}
