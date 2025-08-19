package footstep.footstep.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import footstep.footstep.dtos.users.LoginRequestDTO;
import footstep.footstep.dtos.users.LoginResponseDTO;
import footstep.footstep.dtos.users.RegisterRequestDTO;
import footstep.footstep.models.User;
import footstep.footstep.models.UserRole;
import footstep.footstep.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {
  private final UserRepository repository;
  private final TokenService tokenService;
  private final BCryptPasswordEncoder passwordEncoder;

  public UserService(UserRepository repository,
  TokenService tokenService,
  BCryptPasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public LoginResponseDTO registerAdmin(RegisterRequestDTO data) throws Exception {
    User user = new User(data);

    this.validateRegisterRequest(data);

    user.setPassword(this.passwordEncoder.encode(data.password()));
    user.setRole(UserRole.ADMIN);

    this.repository.save(user);

    return this.loginByUsername(new LoginRequestDTO(data.username(), data.password()));
  }

  @Transactional
  public LoginResponseDTO registerUser(RegisterRequestDTO data) throws Exception {
    User user = new User(data);

    this.validateRegisterRequest(data);

    user.setPassword(this.passwordEncoder.encode(data.password()));
    user.setRole(UserRole.USER);

    this.repository.save(user);

    return this.loginByUsername(new LoginRequestDTO(data.username(), data.password()));
  }

  private void validateRegisterRequest(RegisterRequestDTO data) throws Exception {
    if (this.repository.findUserByUsername(data.username()).isPresent()) {
      throw new IllegalAccessException("Invalid Username.");
    }

    if (this.repository.findUserByEmail(data.email()).isPresent()) {
      throw new IllegalAccessException("Invalid Email.");
    }
  }
  
  public LoginResponseDTO loginByUsername(LoginRequestDTO data) throws Exception {
    Long expiresIn = 300L;
    User user = this.findUserByUsername(data.username());
    String token = this.tokenService.generateToken(user, expiresIn);
    
    if (!this.passwordEncoder.matches(data.password(), user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    return new LoginResponseDTO(token, expiresIn);
  }

  public User findUserByUsername(String username) throws Exception {
    return this.repository.findUserByUsername(username).orElseThrow(() -> new Exception("User Not Found."));
  }
}
