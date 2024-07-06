package com.academy.Ecommerce;

import com.academy.Ecommerce.model.Customer;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RoleRepository;
import com.academy.Ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.academy.Ecommerce.model.Role;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

//	/*
//	 * use this code for just one time to fill the DB with sample data.
//	 * make sure to comment this code after run the app for the first time.
//	 * this code is written before implementing login and registration features.
//	 * make sure that you created the necessary DB schema and configure the .properties file.
//	 *
//	 */
//	@Bean
//	public CommandLineRunner commandLineRunner(UserRepository userRepository,
//											   RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder){
//		return args -> {
//			Role role1 = new Role("customer");
//			Role role2 = new Role("admin");
//
//			User user1 = new User();
//			user1.setUsername("user");
//			user1.setEmail("user@mail.com");
//			user1.setEnabled(true);
//			user1.setLocked(false);
//			user1.setPassword(passwordEncoder.encode("123"));
//			user1.setRoles(List.of(role1));
//			user1.setCustomer(new Customer(user1.getUsername()));
//
//			User user2 = new User();
//			user2.setUsername("admin");
//			user2.setEmail("admin@mail.com");
//			user2.setEnabled(true);
//			user2.setLocked(false);
//			user2.setPassword(passwordEncoder.encode("456"));
//			user2.setRoles(List.of(role2));
//			user2.setCustomer(new Customer(user2.getUsername()));
//
//			userRepository.saveAllAndFlush(List.of(user1, user2));
//
//		};
//	}
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder(){
//		return new BCryptPasswordEncoder();
//	}

}
