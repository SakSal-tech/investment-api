package com.sakhiya.investment.clientmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

/*This is a REST controller, annotated with @RestController.
It handles HTTP requests and responses.
It is injected with UserService.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	// controller uses Service class
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Get all users
	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	// Get user by username
	@GetMapping("/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		try {
			// ResponseEntity controls the HTTP status and body returned to the client.

			return ResponseEntity.ok(userService.getUserByUserName(username));
		} catch (NoSuchElementException e) {
			// The .build() method is used with ResponseEntity to create a response without
			// a body. This returns a 404 Not Found response with no content.
			return ResponseEntity.notFound().build();
		}
	}

	// Get user by email
	@GetMapping("/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		try {
			return ResponseEntity.ok(userService.getUserByEmail(email));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Get user by username or email
	@GetMapping("/search")
	// means that the parameter is optional. The user can provide either username,
	// email, both, or neither.
	public ResponseEntity<User> getUserByUsernameOrEmail(@RequestParam(required = false) String username,
			@RequestParam(required = false) String email) {
		// If both username and email are either null or empty, the method immediately
		// returns a 400 Bad Request response
		if ((username == null || username.isEmpty()) && (email == null || email.isEmpty())) {
			return ResponseEntity.badRequest().body(null);
		}
		try {
			return ResponseEntity.ok(userService.getUserByEmailOrUsername(username, email));
		} catch (NoSuchElementException | IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Get all active users
	@GetMapping("/active")
	public List<User> getActiveUsers() {
		return userService.getActiveUsers();
	}

	// User login
	@PostMapping("/login")
	// <?> because on success it rerurns User objec but on failure after 3 attempts
	// it returns
	// <?> because on success it returns User object but on failure after 3 attempts
	public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
		int maxAttempts = 3;
		String username = userDTO.getUsername();
		String password = userDTO.getRawPassword(); // Use rawPassword from request body
		// looping through 3 max attempts
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				// call the login method in service class
				User loggedInUser = userService.userLogin(username, password);
				return ResponseEntity.ok(loggedInUser); // login successful
			} catch (NoSuchElementException e) {
				if (attempt == maxAttempts) {
					// After 3 failed login attempts, the controller now generates a reset token and
					// returns it in the response body,
					// so the user can use it to reset their password( I am using this to test on
					// Postman. In reality this should be sent to user by email)
					User userWithToken = userService.generateResetToken(username);
					String resetToken = userWithToken.getResetToken();
					// Return a JSON object with reset instructions and example
					// resetJson is a Java Map that holds information to send back to the client (
					// Potman) in JSON format.
					java.util.Map<String, Object> resetJson = new java.util.HashMap<>();
					// A message explaining what happened.
					resetJson.put("message", "Too many failed attempts. Password reset token generated.");
					// The endpoint to use for resetting the password
					resetJson.put("resetEndpoint", "http://localhost:8080/api/users/reset-password"
							+ "?username=" + username
							+ "&resetToken=" + resetToken
							+ "&newPassword=YOUR_NEW_PASSWORD");
					// The HTTP method to use
					resetJson.put("method", "POST");
					// The parameters user need to send (username, new password, reset token
					resetJson.put("parameters", java.util.Map.of(
							"username", username,
							"newPassword", "YOUR_NEW_PASSWORD",
							"resetToken", resetToken));
					// A note to help user fill in your new password
					resetJson.put("note", "Replace YOUR_NEW_PASSWORD with your new password");
					return ResponseEntity.status(403).body(resetJson);
				}
				// Optionally, you could return a message for each failed attempt
			}
		}
		// This should never be reached
		java.util.Map<String, Object> errorJson = new java.util.HashMap<>();
		errorJson.put("message", "Login failed. Please check your credentials and try again.");
		errorJson.put("status", 401);
		return ResponseEntity.status(401).body(errorJson);
	}
	/**
	 * Generate password reset token for a user.
	 * Returns the reset token as a string if the user exists,
	 * otherwise returns 404 Not Found.
	 */
	@PostMapping("/reset-token")
	public ResponseEntity<String> generateResetToken(@RequestParam String username) {
		try {
			User user = userService.generateResetToken(username);
			return ResponseEntity.ok(user.getResetToken());
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Find user by reset token
	@GetMapping("/reset-token/{token}")
	public ResponseEntity<User> findByResetToken(@PathVariable String token) {
		try {
			return ResponseEntity.ok(userService.findByResetToken(token));
		} catch (NoSuchElementException | IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Reset user password (requires reset token and validates it before resetting)
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String username, @RequestParam String newPassword,
			@RequestParam String resetToken) {
		try {
			// Validate reset token before allowing password reset
			User user = userService.findByResetToken(resetToken);
			if (user == null || !user.getUsername().equals(username)) {
				java.util.Map<String, String> errorJson = new java.util.HashMap<>();
				errorJson.put("error", "Invalid reset token or username.");
				return ResponseEntity.status(403).body(errorJson); // Return structured error message
			}
			User updatedUser = userService.resetPassword(username, newPassword);
			return ResponseEntity.ok(updatedUser);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Create a new user
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User createdUser = userService.createUser(user);
			return ResponseEntity.ok(createdUser);
		} catch (IllegalArgumentException e) {
			java.util.Map<String, String> errorJson = new java.util.HashMap<>();
			errorJson.put("error", e.getMessage());
			return ResponseEntity.badRequest().body(errorJson);
		}
	}

	// Update an existing user
	@PutMapping("/{username}")
	public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
		try {
			User updatedUser = userService.updateUser(username, user);
			return ResponseEntity.ok(updatedUser);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// Delete a user
	@DeleteMapping("/{username}")
	public ResponseEntity<Void> deleteUser(@PathVariable String username) {
		try {
			userService.deleteUser(username);
			return ResponseEntity.noContent().build();
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
