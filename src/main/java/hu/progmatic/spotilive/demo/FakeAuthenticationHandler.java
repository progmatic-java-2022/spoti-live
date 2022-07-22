package hu.progmatic.spotilive.demo;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class FakeAuthenticationHandler {
    private final SecurityContext originalContext;

    private final AuthenticationConfiguration authenticationConfiguration;

    public FakeAuthenticationHandler(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.originalContext = SecurityContextHolder.getContext();
    }

    public void loginAsUser(String username, String password) throws Exception {
        var authentication = authenticationConfiguration.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        var newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newContext);
    }

    public void resetContext() {
        SecurityContextHolder.setContext(originalContext);
    }
}
