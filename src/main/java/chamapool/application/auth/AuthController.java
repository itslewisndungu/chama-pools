package chamapool.application.auth;

import chamapool.application.auth.requests.ChangePasswordRequest;
import chamapool.application.auth.requests.LoginRequest;
import chamapool.domain.member.models.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

  @PostMapping("/{username}/reset-password")
  @ResponseStatus(HttpStatus.CREATED)
  public void resetPassword(@PathVariable String username) {
    this.authService.resetPassword(username);
  }

  @PostMapping("/change-password")
  @ResponseStatus(HttpStatus.CREATED)
  public void changePassword(@RequestBody ChangePasswordRequest req) {
    this.authService.changePassword(req);
  }

  @PostMapping("/update-password")
  @ResponseStatus(HttpStatus.CREATED)
  public void updatePassword(Member member, @RequestBody ChangePasswordRequest req) {
    this.authService.updatePassword(member, req);
  }
}
