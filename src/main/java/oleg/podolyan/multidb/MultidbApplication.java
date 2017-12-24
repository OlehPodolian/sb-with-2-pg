package oleg.podolyan.multidb;

import oleg.podolyan.multidb.domain.product.Product;
import oleg.podolyan.multidb.domain.user.User;
import oleg.podolyan.multidb.repository.product.ProductRepository;
import oleg.podolyan.multidb.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class MultidbApplication {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	@Autowired
	public MultidbApplication(UserRepository userRepository, ProductRepository productRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(MultidbApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(){
//		return args -> {
//			User user = userRepository.save(getUser());
//			System.out.println(user);
//
//			Product product = productRepository.save(getProduct());
//			System.out.println(product);
//		};
//	}

	private User getUser(){
		return User
				.builder()
				.firstName("John")
				.lastName("Travolta")
				.build();
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
