package com.alejobasilio.receta_xml.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa una receta, con sus respectivos atributos.
 * 
 * @author Alejo
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "receta")
public class Receta {

	@XmlAttribute(name = "id")
	private Long id;
	@XmlElement(name = "nombre")
	private String nombre;
	@XmlElement(name = "ingrediente")
	private String ingrediente;
	@XmlElement(name = "cantidad")
	private Integer cantidad;
	@XmlElement(name = "ingredienteOpc")
	private String ingredienteOpc;
	@XmlElement(name = "cantidadOpc")
	private Integer cantidadOpc;
	
}
