package in.anil.meesala.authx.service;

import in.anil.meesala.authx.dto.UserResponse;
import in.anil.meesala.authx.entity.User;
import in.anil.meesala.authx.exception.InvalidCredentialsException;
import in.anil.meesala.authx.exception.UserAlreadyExistsException;
import in.anil.meesala.authx.exception.UserNotFoundException;
import in.anil.meesala.authx.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Mapper
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // Register
    public UserResponse registerUser(User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            logger.warn("Registration failed - user already exists: {}", user.getEmail());
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {}", savedUser.getEmail());

        return mapToUserResponse(savedUser);
    }

    // Login
    public UserResponse loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Login failed - user not found: {}", email);
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed - invalid password for: {}", email);
            throw new InvalidCredentialsException("Invalid password");
        }

        logger.info("User logged in successfully: {}", email);

        return mapToUserResponse(user);
    }

    // Get all users
    public List<UserResponse> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    // Delete user
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Delete failed - user not found: {}", id);
                    return new UserNotFoundException("User not found");
                });

        userRepository.delete(user);

        logger.info("User deleted successfully: id={}, email={}", user.getId(), user.getEmail());
    }

    // Update role
    public UserResponse updateUserRole(Long id, String role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Role update failed - user not found: {}", id);
                    return new UserNotFoundException("User not found");
                });

        user.setRole(role);

        User updatedUser = userRepository.save(user);

        logger.info("User role updated: id={}, newRole={}", id, role);

        return mapToUserResponse(updatedUser);
    }
}