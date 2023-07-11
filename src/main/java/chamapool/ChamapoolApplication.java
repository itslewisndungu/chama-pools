package chamapool;

import chamapool.config.AfricasTalkingKeyProperties;
import chamapool.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RsaKeyProperties.class, AfricasTalkingKeyProperties.class})
public class ChamapoolApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChamapoolApplication.class, args);
  }
}
