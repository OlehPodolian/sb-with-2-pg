package oleg.podolyan.multidb.repository.user;

import oleg.podolyan.multidb.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

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
	public void whenFindById_thenReturnUser(){
		User actual = userRepository.findOne(1L);
		assertEquals(user.getFirstName(), actual.getFirstName());
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

	private User getUser(){
		return User
				.builder()
				.firstName("Max")
				.lastName("Travolta")
				.build();
	}
}