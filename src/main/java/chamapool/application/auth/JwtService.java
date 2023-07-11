package chamapool.application.auth;

import chamapool.domain.member.models.Member;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
  private final JwtEncoder encoder;

  public String generateToken(Member user) {
    Instant now = Instant.now();

    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(3, ChronoUnit.HOURS))
            .subject(user.username())
            .claim("roles", user.getRoles())
            .build();

    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
