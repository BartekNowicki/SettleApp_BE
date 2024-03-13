package com.application.settleApp.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.application.settleApp.controllers.AuthenticationController;
import com.application.settleApp.exceptions.AuthenticationFailedException;
import com.application.settleApp.models.User;
import com.application.settleApp.repositories.UserRepository;
import com.application.settleApp.security.AuthRequest;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private JwtTokenService jwtTokenService;

  private User user;
  private final String rawPassword = "password";
  private final String hashedPassword = "hashedPassword";
  SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  String base64EncodedSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());

  @BeforeEach
  void setUp() {
    user = new User();
    user.setEmail("user@example.com");
    user.setPassword(hashedPassword);

    when(userRepository.findByEmail("user@example.com")).thenReturn(user);
    jwtTokenService = new JwtTokenService(userRepository, passwordEncoder, base64EncodedSecretKey);
  }

  @Test
  void generateTokenSuccess() {
    AuthRequest request = new AuthRequest("user@example.com", rawPassword);
    when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

    String token = jwtTokenService.generateToken(request);
    assertNotNull(token);
  }

  @Test
  void authenticateFailureDueToPasswordMismatch() {
    // Prepare the user with the hashed password
    User user = new User();
    user.setEmail("user@example.com");
    user.setPassword(hashedPassword);

    when(passwordEncoder.matches("incorrectPassword", user.getPassword())).thenReturn(false);
    when(userRepository.findByEmail("user@example.com")).thenReturn(user);

    AuthRequest request = new AuthRequest("user@example.com", "incorrectPassword");

    assertThrows(
        AuthenticationFailedException.class,
        () -> {
          jwtTokenService.generateToken(request);
        },
        "Expected AuthenticationFailedException to be thrown due to password mismatch");
  }
}