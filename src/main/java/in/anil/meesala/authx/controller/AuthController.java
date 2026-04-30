package in.anil.meesala.authx.controller;

import in.anil.meesala.authx.dto.RegisterRequest;
import in.anil.meesala.authx.dto.UserResponse;
import in.anil.meesala.authx.entity.User;
import in.anil.meesala.authx.repository.UserRepository;
import in.anil.meesala.authx.security.JwtUtil;
import in.anil.meesala.authx.service.UserService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // REGISTER
    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userService.registerUser(user);
    }

    // Login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!userService.checkPassword(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

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
    
    // CHANGE PASSWORD
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