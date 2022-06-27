package hu.progmatic.spotilive.appconfig;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class UserToMdcFilter implements javax.servlet.Filter {

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    MDC.put("user", SecurityContextHolder.getContext().getAuthentication().getName());
    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove("user");
    }
  }
}