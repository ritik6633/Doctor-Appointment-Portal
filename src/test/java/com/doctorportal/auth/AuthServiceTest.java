package com.doctorportal.auth;

import com.doctorportal.auth.dto.LoginRequest;
import com.doctorportal.auth.dto.RegisterRequest;
import com.doctorportal.common.exception.BadRequestException;
import com.doctorportal.common.exception.UnauthorizedException;
import com.doctorportal.user.Role;
import com.doctorportal.user.UserEntity;
import com.doctorportal.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  AuthService authService;

  @Captor
  ArgumentCaptor<UserEntity> userCaptor;

  @BeforeEach
  void resetMocks() {
    reset(userRepository);
  }

  @Test
  void registerPatient_rejectsDuplicateEmail() {
    when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(new UserEntity()));

    RegisterRequest req = new RegisterRequest(
        "Alice",
        "a@b.com",
        "pass1234",
        "999",
        "FEMALE",
        LocalDate.of(2000, 1, 1)
    );

    assertThatThrownBy(() -> authService.registerPatient(req))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Email already registered");

    verify(userRepository, never()).save(any());
  }

  @Test
  void login_rejectsWrongPassword() {
    UserEntity u = new UserEntity();
    u.setId(10L);
    u.setEmail("a@b.com");
    u.setPassword("$2a$10$aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    u.setRole(Role.PATIENT);
    u.setActive(true);

    when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(u));

    assertThatThrownBy(() -> authService.login(new LoginRequest("a@b.com", "wrong")))
        .isInstanceOf(UnauthorizedException.class)
        .hasMessageContaining("Invalid email or password");
  }
}

