package com.urjc.books.controllers;

import com.urjc.books.controllers.exception.ExistingEntitiesAssociatedException;
import com.urjc.books.models.dtos.in.UpdateUserEmailInDto;
import com.urjc.books.models.dtos.out.GetCommentsByUserOutDto;
import com.urjc.books.models.entities.Comment;
import com.urjc.books.models.entities.User;
import com.urjc.books.services.CommentService;
import com.urjc.books.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private CommentService commentService;

    public UserController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user supplied"
            )
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "The user to be created")
            @RequestBody User user) {
        Optional<User> newUser = this.userService.save(user);
        if (newUser.isPresent()) {
            URI location = fromCurrentRequest().path("/{nick}").buildAndExpand(newUser.get().getNick()).toUri();
            return ResponseEntity.created(location).body(newUser.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Get a user by the nick")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{nick}")
    public ResponseEntity<User> getUser(
            @Parameter(description = "The nick of the user to be searched")
            @PathVariable String nick) {
        Optional<User> user = this.userService.findByNick(nick);
        return ResponseEntity.of(user);
    }

    @Operation(summary = "Update the user's email")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email modified successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateEmail(
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserEmailInDto inDto) {
        Optional<User> user = this.userService.findById(userId);
        if (user.isPresent()) {
            user.get().setEmail(inDto.getEmail());
            this.userService.save(user.get());
        }
        return ResponseEntity.of(user);
    }

    @Operation(summary = "Delete user by its id.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) throws ExistingEntitiesAssociatedException {
        Optional<User> user = this.userService.findById(userId);
        if (user.isPresent()) {
            List<Comment> comments = this.commentService.findByUser(user.get());
            if (CollectionUtils.isEmpty(comments)) {
                this.userService.delete(user.get());
            } else {
                throw new ExistingEntitiesAssociatedException("comments");
            }
        }
        return ResponseEntity.of(user);
    }

    @Operation(summary = "Get comments of an user.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments found.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{userId}/comments")
    public ResponseEntity<GetCommentsByUserOutDto> getCommentsByUser(
            @Parameter(description = "The nick of the user to be searched")
            @PathVariable Long userId) {
        Optional<User> user = this.userService.findById(userId);
        if (user.isPresent()) {
            GetCommentsByUserOutDto outDto = this.userService.getCommentsByUser(user.get());
            return ResponseEntity.ok().body(outDto);
        }
        return ResponseEntity.notFound().build();
    }
}
