package com.tothenew.sharda;

import com.tothenew.sharda.Model.Role;
import com.tothenew.sharda.Model.User;
import com.tothenew.sharda.Repository.RoleRepository;
import com.tothenew.sharda.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@SpringBootApplication
public class SpringBootEcommerceApiApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootEcommerceApiApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Role adminRole = new Role();
		adminRole.setAuthority("ROLE_ADMIN");

		Role userRole = new Role();
		userRole.setAuthority("ROLE_CUSTOMER");

		Role sellerRole = new Role();
		sellerRole.setAuthority("ROLE_SELLER");

		//admin
		User adminUser = new User();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("User");
		adminUser.setEmail("admin@sd1876.com");
		adminUser.setPassword(passwordEncoder.encode("Password@123"));
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(adminRole);
		adminUser.setRoles(roleSet);
		adminUser.setIsLocked(false);
		adminUser.setIsActive(true);
		adminUser.setIsDeleted(false);
		adminUser.setIsExpired(false);
		userRepository.save(adminUser);

		roleRepository.save(adminRole);
		roleRepository.save(userRole);
		roleRepository.save(sellerRole);
	}

	@Bean
	public AcceptHeaderLocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}
}
