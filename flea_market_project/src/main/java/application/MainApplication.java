package application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"model.dao.entity"})
@EnableJpaRepositories(basePackages = {"model.dao.repo"})
@ComponentScan(basePackages = {"service", "webController.api", "application"})
@ServletComponentScan(basePackages = {"webController.filter"})
@SpringBootApplication
@Slf4j
public class MainApplication extends SpringApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class);

        // initAdmin(context);
    }

    // private static void initAdmin(ConfigurableApplicationContext context) {
    //     UserRepo userRepo = context.getBean(UserRepo.class);

    //     UserEntity user = new UserEntity();
    //     user.setAvatarUrl("example.org");
    //     user.setNickname("init admin user");
    //     user.setUsername("initAdmin");
    //     user.setPassword("initAdmin");
    //     user.setJoinTime(new Date());
    //     user.setBanUntil(new Date(0));
    //     userRepo.save(user);
    // }
}
