package chamapool.application.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Recipient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {
  @Value("${africastalking.username}")
  private String USERNAME;

  @Value("${africastalking.api-key}")
  private String API_KEY = "";

  @Value("${africastalking.short-code}")
  private String shortCode;

  public void sendSmsMessage(String message, String[] recipients) {
    AfricasTalking.initialize(USERNAME, API_KEY);
    SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

    try {
      List<Recipient> response = sms.send(message, shortCode, recipients, true);
      for (Recipient recipient : response) {
        log.info("Sent message to {} with status {}", recipient.number, recipient.status);
      }
    } catch (Exception ex) {
      log.error("Uh ooh. something here went wrong. {}", ex.getMessage());
    }
  }
}
