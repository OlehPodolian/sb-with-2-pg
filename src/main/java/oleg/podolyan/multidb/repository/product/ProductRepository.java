package oleg.podolyan.multidb.repository.product;

import oleg.podolyan.multidb.domain.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public interface ProductRepository extends CrudRepository<Product, Long> {

	Product findByTitle(String title);
}
