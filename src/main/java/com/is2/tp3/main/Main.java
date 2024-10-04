package com.is2.tp3.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.is2.tp3.entity.Articulo;
import com.is2.tp3.entity.Categoria;
import com.is2.tp3.entity.Cliente;
import com.is2.tp3.entity.DetalleFactura;
import com.is2.tp3.entity.Domicilio;
import com.is2.tp3.entity.Factura;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenceAppPU");
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			
			Domicilio domicilio = Domicilio.builder().nombreCalle("Av. San Martin").numero(555).build();
			
			Cliente cliente = Cliente.builder().dni(123456789).nombre("Joaquin").apellido("Ruiz")
					.domicilio(domicilio).build();
			
			Factura factura1 = Factura.builder().numero(12).fecha("10/08/2020").total(100).cliente(cliente).build();

			Categoria perecederos = Categoria.builder().denominacion("Perecederos").build();
			Categoria lacteos = Categoria.builder().denominacion("Lacteos").build();
			Categoria limpieza = Categoria.builder().denominacion("Limpieza").build();
			
			Articulo art1 = Articulo.builder().cantidad(200).denominacion("Yogurt sabor frutilla").precio(1500).build();
			Articulo art2 = Articulo.builder().cantidad(300).denominacion("Detergente Magistral").precio(3000).build();
			
			art1.getCategorias().add(perecederos);
			art1.getCategorias().add(lacteos);
			lacteos.getArticulos().add(art1);
			perecederos.getArticulos().add(art1);
			
			art2.getCategorias().add(limpieza);
			limpieza.getArticulos().add(art2);
			
			DetalleFactura det1 = DetalleFactura.builder().articulo(art1).cantidad(2).factura(factura1).build();
			det1.setSubtotal(det1.getCantidad() * det1.getArticulo().getPrecio());
			
			art1.getDetalles().add(det1);
			factura1.getDetalles().add(det1);
			
			DetalleFactura det2 = DetalleFactura.builder().articulo(art2).cantidad(1).factura(factura1).build();
			det2.setSubtotal(det2.getCantidad() * det2.getArticulo().getPrecio());
			
			art2.getDetalles().add(det2);
			factura1.getDetalles().add(det2);
			
			factura1.setTotal(det1.getSubtotal() + det2.getSubtotal());
			
			em.persist(factura1);
			
			em.flush();
		    em.getTransaction().commit();
		} catch (Exception e) {
		    if (em.getTransaction().isActive()) {
		        em.getTransaction().rollback(); // Rollback transaction if an error occurs
		    }
		    e.printStackTrace(); // Log the exception
		} finally {
		    if (em != null) {
		        em.close(); // Ensure the EntityManager is closed
		    }
		    if (emf != null) {
		        emf.close(); // Ensure the EntityManagerFactory is closed
		    }
		}
	}

}
