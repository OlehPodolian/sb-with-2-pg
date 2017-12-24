package oleg.podolyan.multidb.repository.user;

import oleg.podolyan.multidb.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(isolation = Isolation.READ_COMMITTED)
public interface UserRepository extends CrudRepository<User, Long> {

	// pretend firstName is unique
	User findByFirstName(String firstName);

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	User save(User user);
}
