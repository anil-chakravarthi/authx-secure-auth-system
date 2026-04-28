package in.anil.meesala.authx.controller;

import in.anil.meesala.authx.dto.UpdateRoleRequest;
import in.anil.meesala.authx.dto.UserResponse;
import in.anil.meesala.authx.service.UserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/user/role")
    public UserResponse updateUserRole(@RequestBody UpdateRoleRequest request) {
        return userService.updateUserRole(request.getUserId(), request.getRole());
    }
}