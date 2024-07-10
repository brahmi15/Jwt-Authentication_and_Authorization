package com.rabbitmq.JwtAuthentication.Controller;

import com.rabbitmq.JwtAuthentication.Models.User;
import com.rabbitmq.JwtAuthentication.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController // to handle incoming HTTP requests and returns the response directly to the client.
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    //https://localhost:8080/home/getAll/users
    //TO FETCH DETAILS OF ALL USERS
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAll/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.notFound().build(); // Http code : 404
            } else {
                return ResponseEntity.ok(users); // Http code : 200
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Http code : 403
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Http code : 401
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Http code : 500
        }
    }


    //https://localhost:8080/home/getUserById/{id}
    // GET SINGLE CUSTOMER BASED ON ID
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getDetail(@PathVariable("id") int id) {
        try {
            Optional<User> user = userService.getDetailById(id);
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //https://localhost:8080/home/current-user
    //TO KNOW WHICH USER IS LOGGED IN
    @GetMapping("/current-user")
    public ResponseEntity<String> getLoggedInUser(Principal principal) {
        try {
            String username = principal.getName();
            return ResponseEntity.ok(username);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // https://localhost:8080/home/add/users
    //ADDING A NEW USER
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
                User addedUser = userService.addUser(user);
                return ResponseEntity.ok(addedUser);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //http://localhost:8080/home/delete/{customer_details_Id}
    // DELETE A CUSTOMER
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDetails(@PathVariable("id") int id) {
        try {
            if (userService.exists(id)) {
                userService.deleteDetail(id);
                return ResponseEntity.noContent().build(); // Http code : 204
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete entity with ID " + id);
        }
    }


    //http://localhost:8080?home/update/{customer_details_Id}
    // UPDATE DETAILS
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateDetails(@RequestBody User user, @PathVariable("id") int id) {
        try {
            if (userService.exists(id)) {
                User updatedUser = userService.updateDetail(user, id);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
