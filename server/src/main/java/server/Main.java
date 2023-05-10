package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "commons")
@ComponentScan(basePackages = "server")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
