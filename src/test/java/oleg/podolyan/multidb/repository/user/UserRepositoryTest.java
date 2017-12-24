package oleg.podolyan.multidb.repository.user;

import oleg.podolyan.multidb.domain.product.Product;
import oleg.podolyan.multidb.domain.user.Purchase;
import oleg.podolyan.multidb.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@Before
	public void setUp(){
		user = getUser();
		entityManager.persist(user);
		entityManager.flush();
	}

	@Test
	public void whenFindByFirstNameAndLastName_thenReturnUser(){
		User actual = userRepository.findByFirstName(user.getFirstName());
		assertEquals(user.getFirstName(), actual.getFirstName());
	}

	@Test
	public void whenDelete_thenRepoIsEmpty(){
		userRepository.delete(userRepository.findByFirstName("Max"));
		assertTrue(!(userRepository.findAll().iterator().hasNext()));
	}

	@Test
	public void whenSaveUserWithUniquePurchases_thenTransactionCommittedOk(){
		User buyer = getUser("Bill", "Thomson");
		buyer.getPurchases().add(Purchase.of(getProduct("#1", "#1", 15.99)));
		buyer.getPurchases().add(Purchase.of(getProduct("#2", "#2", 15.99)));
		buyer.getPurchases().add(Purchase.of(getProduct("#3", "#3", 15.99)));

		entityManager.persist(buyer);
		entityManager.flush();

		User found = userRepository.findByFirstName(buyer.getFirstName());
		assertEquals(3, found.getPurchases().size());
	}

	@Test(expected = RuntimeException.class)
	public void whenSaveUserWithNotUniquePurchase_thenTransactionCommittedFailed(){
		User buyer = getUser("Bill", "Thomson");
		buyer.getPurchases().add(Purchase.of(getProduct("#1", "#1", 15.99)));
		buyer.getPurchases().add(Purchase.of(getProduct("#1", "#2", 15.99))); // fails uq constraint
		buyer.getPurchases().add(Purchase.of(getProduct("#3", "#3", 15.99)));

		entityManager.persist(buyer);
		entityManager.flush();
		List<User> users = (List<User>) userRepository.findAll();
		assertEquals(1, users.size());
	}

	@Test(expected = RuntimeException.class)
	public void whenAddingUserNotUniquePurchase_thenTransactionCommittedFailed(){
		User buyer = userRepository.findByFirstName(user.getFirstName());
		buyer.getPurchases().add(Purchase.of(getProduct("#1", "#1", 15.99)));
		buyer.getPurchases().add(Purchase.of(getProduct("#1", "#2", 15.99))); // fails uq constraint
		buyer.getPurchases().add(Purchase.of(getProduct("#3", "#3", 15.99)));

		entityManager.persist(buyer);
		entityManager.flush();
		List<User> users = (List<User>) userRepository.findAll();
		assertEquals(1, users.size());
		assertTrue(buyer.getPurchases().isEmpty());
	}

	private User getUser(){
		return getUser("Max", "Travolta"); // John's younger brother
	}

	private User getUser(String firstName, String lastName){
		return User
				.builder()
				.firstName(firstName)
				.lastName(lastName)
				.build();
	}

	private Product getProduct(String title, String description, double price){
		return Product
				.builder()
				.title(title)
				.description(description)
				.price(new BigDecimal(price))
				.category("First")
				.build();
	}

}