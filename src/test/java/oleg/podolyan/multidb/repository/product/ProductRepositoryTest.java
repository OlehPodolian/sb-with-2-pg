package oleg.podolyan.multidb.repository.product;

import oleg.podolyan.multidb.domain.product.Product;
import oleg.podolyan.multidb.domain.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	private Product product;

	@Before
	public void setUp(){
		product = getProduct();
		entityManager.persist(product);
		entityManager.flush();
	}

	@Test
	public void whenFindByTitle_thenReturnUser(){
		Product actual = productRepository.findByTitle(getProduct().getTitle());
		assertEquals(product.getDescription(), actual.getDescription());
	}

	@Test
	public void whenDelete_thenRepoIsEmpty(){
		productRepository.delete(productRepository.findOne(1L));
		assertTrue(!(productRepository.findAll().iterator().hasNext()));
	}

	private Product getProduct(){
		return Product
				.builder()
				.title("Some awesome product")
				.description("Never buy it")
				.price(new BigDecimal(67.98))
				.category("First")
				.build();
	}

}