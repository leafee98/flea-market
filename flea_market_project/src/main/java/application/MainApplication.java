package application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(value = "model.dao.entity")
@EnableJpaRepositories(value = "model.dao.repo")
@SpringBootApplication
public class MainApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }
}
