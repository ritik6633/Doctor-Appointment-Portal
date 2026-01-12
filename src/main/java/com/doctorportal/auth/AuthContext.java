package com.doctorportal.auth;

/**
 * ThreadLocal auth context set by {@link HeaderAuthFilter}.
 */
public final class AuthContext {
	private static final ThreadLocal<AuthPrincipal> PRINCIPAL = new ThreadLocal<>();

	private AuthContext() {
	}

	public static void set(AuthPrincipal principal) {
		PRINCIPAL.set(principal);
	}

	public static AuthPrincipal getRequired() {
		AuthPrincipal p = PRINCIPAL.get();
		if (p == null) {
			throw new IllegalStateException("No auth principal in context");
		}
		return p;
	}

	public static AuthPrincipal getOrNull() {
		return PRINCIPAL.get();
	}

	public static void clear() {
		PRINCIPAL.remove();
	}
}

