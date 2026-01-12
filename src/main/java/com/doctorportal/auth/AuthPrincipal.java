package com.doctorportal.auth;

import com.doctorportal.user.Role;

/**
 * Request-scoped principal derived from headers.
 */
public record AuthPrincipal(Long userId, Role role) {
}

