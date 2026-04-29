package in.anil.meesala.authx.controller;

import in.anil.meesala.authx.entity.User;
import in.anil.meesala.authx.repository.UserRepository;
import in.anil.meesala.authx.security.JwtUtil;
import in.anil.meesala.authx.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.get("email"),
                        request.get("password")
                )
        );

        User user = userRepository.findByEmail(request.get("email"))
                .orElseThrow();

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail(),
                "loginTime", System.currentTimeMillis()
        );
    }

    @PostMapping("/change-password")
    public Map<String, String> changePassword(
            @RequestBody Map<String, String> request,
            Authentication authentication) {

        userService.changePassword(
                authentication.getName(),
                request.get("oldPassword"),
                request.get("newPassword")
        );

        return Map.of("message", "Password updated successfully");
    }
}