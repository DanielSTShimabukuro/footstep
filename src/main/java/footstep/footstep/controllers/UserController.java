package footstep.footstep.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import footstep.footstep.dtos.users.LoginResponseDTO;
import footstep.footstep.dtos.users.RegisterRequestDTO;
import footstep.footstep.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping("/register/admin")
  public ResponseEntity<LoginResponseDTO> registerAdmin(@Valid @RequestBody RegisterRequestDTO data) throws Exception {
    LoginResponseDTO response = this.service.registerAdmin(data);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/register/user")
  public ResponseEntity<LoginResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO data) throws Exception {
    LoginResponseDTO response = this.service.registerUser(data);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
