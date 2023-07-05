package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "commons")
@SpringBootApplication
public class Main {

    /**
     * Starts server
     * @param args run time arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
