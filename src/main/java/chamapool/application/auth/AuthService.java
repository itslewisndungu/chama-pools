package chamapool.application.auth;

import chamapool.domain.member.models.Member;
import chamapool.domain.member.repositories.MemberRepository;
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

    public Member login(LoginRequest request) {
        return memberRepository
                .getMemberByUsername(request.username())
                .filter(u -> passwordEncoder.matches(request.password(), u.password()))
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    user.token(token);
                    return user;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }
}
