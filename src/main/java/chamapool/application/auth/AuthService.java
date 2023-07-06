package chamapool.application.auth;

import chamapool.application.auth.requests.ChangePasswordRequest;
import chamapool.application.auth.requests.LoginRequest;
import chamapool.application.notifications.NotificationsService;
import chamapool.domain.member.models.Member;
import chamapool.domain.member.models.PasswordResetToken;
import chamapool.domain.member.repositories.MemberRepository;
import chamapool.domain.member.repositories.PasswordResetTokenRepository;
import chamapool.domain.notifications.models.Notification;
import chamapool.domain.notifications.models.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final PasswordResetTokenRepository resetTokenRepository;

  private final NotificationsService notificationsService;

  public Member login(LoginRequest request) {
    return memberRepository
        .getMemberByUsername(request.username())
        .filter(u -> passwordEncoder.matches(request.password(), u.password()))
        .map(
            user -> {
              String token = jwtService.generateToken(user);
              user.token(token);
              return user;
            })
        .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
  }

  public String resetPassword(String username) {
    var user =
        memberRepository
            .getMemberByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

    String token = UUID.randomUUID().toString();
    var PasswordResetToken =
        resetTokenRepository.save(new PasswordResetToken().member(user).token(token));

    this.resetTokenRepository.save(PasswordResetToken);

    return token;
  }

  public void changePassword(String token, ChangePasswordRequest req) {
    var resetToken =
        this.resetTokenRepository
            .findByToken(token)
            .filter(
                t -> {
                  var isExpired = t.expiryDate().compareTo(Instant.now());
                  if (isExpired < 0) throw new RuntimeException("Token expired");

                  return true;
                })
            .orElseThrow(() -> new UsernameNotFoundException("Invalid token"));

    this.updatePassword(resetToken.member(), req);
  }

  public void updatePassword(Member member, ChangePasswordRequest req) {
    member.password(passwordEncoder.encode(req.password()));
    this.memberRepository.save(member);

    var notification =
        new Notification()
            .title("Password changed")
            .type(NotificationType.PASSWORD_RESET)
            .message("Your password has been changed successfully");
    this.notificationsService.sendMemberNotification(member, notification);
  }
}
