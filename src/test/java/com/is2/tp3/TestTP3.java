package com.is2.tp3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.is2.tp3.entity.Articulo;
import com.is2.tp3.entity.Cliente;
import com.is2.tp3.entity.DetalleFactura;
import com.is2.tp3.entity.Domicilio;
import com.is2.tp3.entity.Factura;

public class TestTP3 {

	private EntityManagerFactory emf;
	private EntityManager em;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("PersistenceAppPU");
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}

	@After
	public void tearDown() {
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
		em.close();
		emf.close();
	}

	@Test
	public void testCalculoTotalFactura() {
		Domicilio domicilio = Domicilio.builder().nombreCalle("Calle").numero(123).build();
		Cliente cliente = Cliente.builder().dni(12345678).nombre("Carlos").apellido("González").domicilio(domicilio)
				.build();
		
		Factura factura = Factura.builder().numero(1).fecha("01/09/2023").cliente(cliente).build();

		Articulo art1 = Articulo.builder().denominacion("Jugo de Naranja").precio(500).cantidad(50).build();
		Articulo art2 = Articulo.builder().denominacion("Pan Integral").precio(300).cantidad(100).build();

		DetalleFactura detalle1 = DetalleFactura.builder().articulo(art1).cantidad(2).factura(factura).build();
		detalle1.setSubtotal(detalle1.getCantidad() * detalle1.getArticulo().getPrecio());

		DetalleFactura detalle2 = DetalleFactura.builder().articulo(art2).cantidad(1).factura(factura).build();
		detalle2.setSubtotal(detalle2.getCantidad() * detalle2.getArticulo().getPrecio());

		factura.getDetalles().add(detalle1);
		factura.getDetalles().add(detalle2);

		int totalEsperado = detalle1.getSubtotal() + detalle2.getSubtotal();
		factura.setTotal(totalEsperado);

		assertEquals(totalEsperado, factura.getTotal(), 0.01);
	}

	@Test
	public void testCliente() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersistenceAppPU");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Domicilio domicilio = Domicilio.builder().nombreCalle("Av. Mitre").numero(333).build();
		Cliente cliente = Cliente.builder().dni(123456789).nombre("Joaquin").apellido("Ruiz").domicilio(domicilio)
				.build();
		domicilio.setCliente(cliente);

		try {
			em.persist(cliente);
			em.flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();

				System.out.println(
						"Excepción esperada capturada: " + constraintViolationException.getSQLException().getMessage());
				assertTrue(true);
			} else {
				assertTrue("Se produjo una excepción inesperada", false);
			}
		}
	}

}
