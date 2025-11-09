package com.athletica.backend.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.athletica.backend.model.User;
import com.athletica.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtAuthenticationFilter:
 * - Valida el token JWT con JwtUtil.
 * - Usa siempre el "subject" (claims.getSubject()) como principal (String).
 * - Asigna authorities si el claim "role" está presente.
 * - Carga la entidad User opcionalmente y la coloca en authentication.details.
 *
 * Importante: con este enfoque authentication.getName() devolverá el userId
 * (subject).
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith("Bearer ")) {
      // No token present -> continue chain (anonymous)
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7).trim();
    if (token.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      Claims claims = jwtUtil.validateAndGetClaims(token);

      // Subject (se espera que sea el user id)
      String subject = claims.getSubject();
      if (subject == null || subject.isBlank()) {
        logger.warn("JwtAuthenticationFilter - token sin subject");
        filterChain.doFilter(request, response);
        return;
      }

      // Extraer role claim (opcional)
      Object roleObj = claims.get("role");
      String roleClaim = roleObj != null ? roleObj.toString() : null;

      // Construir authorities (si hay role)
      List<SimpleGrantedAuthority> authorities = Collections.emptyList();
      if (roleClaim != null && !roleClaim.isBlank()) {
        String granted = roleClaim.startsWith("ROLE_") ? roleClaim : ("ROLE_" + roleClaim);
        authorities = List.of(new SimpleGrantedAuthority(granted));
      }

      // Intentar cargar entidad User (opcional). No usar como principal para mantener
      // contractos.
      User user = null;
      try {
        user = userRepository.findById(subject).orElse(null);
      } catch (Exception e) {
        logger.debug("JwtAuthenticationFilter - error al cargar user desde repo: {}", e.getMessage(), e);
      }

      // Crear Authentication usando subject (String) como principal
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null,
          authorities);

      // Guardar la entidad User como details si está disponible
      if (user != null) {
        authentication.setDetails(user);
      }

      // Poner en SecurityContext
      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Logs útiles para debugging (no muy verbosos en producción)
      if (logger.isDebugEnabled()) {
        logger.debug("JwtAuthenticationFilter - autenticación establecida para subject={}, authorities={}",
            subject, Objects.toString(authorities));
      }

    } catch (Exception ex) {
      // Token inválido o expirado -> no autenticamos, dejamos pasar la petición como
      // anónima
      logger.warn("JwtAuthenticationFilter - token inválido: {}", ex.getMessage());
      // opcional: podrías responder 401 aquí y no continuar la chain, pero
      // normalmente se delega a entry point
    }

    filterChain.doFilter(request, response);
  }
}
