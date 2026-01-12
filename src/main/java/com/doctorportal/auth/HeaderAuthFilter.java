package com.doctorportal.auth;

import com.doctorportal.user.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Very small manual auth mechanism based on headers.
 *
 * Per requirements: no Spring Security, no JWT, no OAuth.
 * This filter just reads the headers and stores a principal.
 */
@Component
public class HeaderAuthFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		// public endpoints
		if (path.startsWith("/auth/")) return true;
		if (path.equals("/actuator/health")) return true;
		// Allow CORS preflight
		return HttpMethod.OPTIONS.matches(request.getMethod());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String userIdHeader = request.getHeader(AuthHeaders.USER_ID);
			String roleHeader = request.getHeader(AuthHeaders.ROLE);
			if (userIdHeader != null && roleHeader != null) {
				Long userId = Long.parseLong(userIdHeader);
				Role role = Role.valueOf(roleHeader);
				AuthContext.set(new AuthPrincipal(userId, role));
			}
			filterChain.doFilter(request, response);
		} finally {
			AuthContext.clear();
		}
	}
}

