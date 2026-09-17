package com.coffeewa.coffeewebapp.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Component
public class JwtFilter extends OncePerRequestFilter {


	private final JwtUtil jwtUtil;

	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = null;
		if (request.getCookies() != null) {
			for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
				if ("jwt".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		if (token != null && jwtUtil.validateToken(token)) {
			Long userId = jwtUtil.extractSubject(token);
			String role = jwtUtil.extractRole(token);
			List<SimpleGrantedAuthority> authorities = role != null
				? List.of(new SimpleGrantedAuthority("ROLE_" + role))
				: Collections.emptyList();
			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(String.valueOf(userId), null, authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
