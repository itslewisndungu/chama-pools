package chamapool.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "africastalking")
public record AfricasTalkingKeyProperties(String shortCode, String username, String apiKey) {}
