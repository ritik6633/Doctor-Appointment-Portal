package com.doctorportal.auth;

/**
 * Minimal "basic authentication logic" using headers.
 *
 * The frontend will send:
 * - X-USER-ID: numeric user id
 * - X-ROLE: role enum value
 *
 * This is NOT secure and is intended for localhost demo only,
 * per requirement (no Spring Security/JWT/OAuth).
 */
public final class AuthHeaders {
	public static final String USER_ID = "X-USER-ID";
	public static final String ROLE = "X-ROLE";

	private AuthHeaders() {
	}
}

