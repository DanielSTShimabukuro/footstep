package footstep.footstep.services;

import java.time.Instant;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import footstep.footstep.models.User;

@Service
public class TokenService {
  private final JwtEncoder jwtEncoder;

  public TokenService(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  public String generateToken(User user, Long expiresIn) {
    Instant now = Instant.now();

    List<String> scopes = user
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

    JwtClaimsSet claims = JwtClaimsSet
                  .builder()
                  .issuer("footstep")
                  .issuedAt(now)
                  .subject(user.getUsername())
                  .expiresAt(now.plusSeconds(expiresIn))
                  .claim("scopes", scopes)
                  .build();
                  
    return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
