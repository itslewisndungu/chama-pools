package chamapool;

import chamapool.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ChamapoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChamapoolApplication.class, args);
    }

}
