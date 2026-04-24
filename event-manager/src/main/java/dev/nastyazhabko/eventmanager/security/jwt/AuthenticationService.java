package dev.nastyazhabko.eventmanager.security.jwt;

import dev.nastyazhabko.eventcommon.security.SignInRequest;
import dev.nastyazhabko.eventcommon.security.User;
import dev.nastyazhabko.eventcommon.security.enums.UserRole;
import dev.nastyazhabko.eventmanager.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.login(),
                        signInRequest.password()
                )
        );

        var user = (CustomUserDetails) auth.getPrincipal();
        Integer userId = user.getId();
        GrantedAuthority authority = user.getAuthorities().iterator().next();
        UserRole role = UserRole.valueOf(authority.getAuthority());

        return jwtUtil.generateToken(signInRequest.login(), userId, role);
    }

    public User getCurrentAuthenticatedUserOrThrow() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }
        return (User) authentication.getPrincipal();
    }
}
