package com.is2.tp3.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "articulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Articulo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull private int cantidad;
	@NonNull private String denominacion;
	@NonNull private int precio;
	@OneToMany(mappedBy = "articulo", cascade = CascadeType.PERSIST)
	@Builder.Default
	private List<DetalleFactura> detalles = new ArrayList<DetalleFactura>();
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name="articulo_categoria",
			joinColumns = @JoinColumn(name="articulo_id"),
			inverseJoinColumns = @JoinColumn(name="categoria_id"))
	@Builder.Default
	private List<Categoria> categorias = new ArrayList<Categoria>();	
}
