package sl.testapp.serverapp;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import sl.testapp.serverapp.customer.Customer;
import sl.testapp.serverapp.customer.CustomerRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"sl.testapp.otherservice","sl.testapp.serverapp"})
@Slf4j
public class SlTestServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SlTestServerApplication.class, args);
	}
	
	@Bean
	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		// not needed as Spring Loads file "schema.sql" automagically
		//initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
		
		return initializer;
	}
	
	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {
			repository.saveAll(Arrays.asList(new Customer("Jack", "Bauer"),
					new Customer("Chloe", "O'Brian"),
					new Customer("Kim", "Bauer"),
					new Customer("David", "Palmer"),
					new Customer("Michelle","Dessler"))).blockLast(Duration.ofSeconds(10));
			
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			repository.findAll().doOnNext(customer -> {log.info(customer.toString());}).blockLast(Duration.ofSeconds(10));
			
			log.info("");
			
			repository.findById(1L).doOnNext(customer -> {
				log.info("Customer found with findById(1L):");
				log.info("---------------------------------");
				log.info(customer.toString());
				log.info("");
			}).block(Duration.ofSeconds(10));
			
			
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repository.findByLastName("Bauer").doOnNext(bauer -> log.info(bauer.toString())).blockLast(Duration.ofSeconds(10));
			log.info("");
		};
	}

}
