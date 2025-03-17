package com.alejobasilio.receta_xml.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingrediente {

	private Long id;
	private String ingrediente;
	private Integer cantidad;
}
