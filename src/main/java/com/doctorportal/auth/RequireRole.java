package com.doctorportal.auth;

import com.doctorportal.common.exception.ForbiddenException;
import com.doctorportal.common.exception.UnauthorizedException;
import com.doctorportal.user.Role;

public final class RequireRole {
	private RequireRole() {
	}

	public static AuthPrincipal requireAny(Role... allowed) {
		AuthPrincipal p = AuthContext.getOrNull();
		if (p == null) {
			throw new UnauthorizedException("Missing auth headers");
		}
		for (Role r : allowed) {
			if (p.role() == r) {
				return p;
			}
		}
		throw new ForbiddenException("Access denied");
	}
}

