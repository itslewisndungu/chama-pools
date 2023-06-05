package chamapool.application.auth;

import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @GetMapping("/me")
  public AuthResponse retrieveCurrentMember(Member member) {
    return new AuthResponse(member);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest request) {
    var member = this.authService.login(request);
    return new AuthResponse(member);
  }
}
