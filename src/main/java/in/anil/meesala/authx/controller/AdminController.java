package in.anil.meesala.authx.controller;

import in.anil.meesala.authx.dto.UpdateRoleRequest;
import in.anil.meesala.authx.dto.UserResponse;
import in.anil.meesala.authx.entity.User;
import in.anil.meesala.authx.repository.UserRepository;
import in.anil.meesala.authx.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // GET ALL USERS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // DELETE USER (with proper error handling)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {

        String loggedInEmail = authentication.getName();

        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent self delete
        if (loggedInUser.getId().equals(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "You cannot delete yourself"));
        }

        userService.deleteUser(id);

        return ResponseEntity.ok(
                Map.of("message", "User deleted successfully")
        );
    }

    // UPDATE ROLE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/role")
    public UserResponse updateUserRole(@RequestBody UpdateRoleRequest request) {
        return userService.updateUserRole(request.getUserId(), request.getRole());
    }
}