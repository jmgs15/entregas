package com.urjc.books.controllers;

import com.urjc.books.models.dtos.in.PostCommentInDto;
import com.urjc.books.models.dtos.out.CommentBookOutDto;
import com.urjc.books.models.dtos.out.GetAllBooksOutDto;
import com.urjc.books.models.dtos.out.GetBookOutDto;
import com.urjc.books.models.entities.Book;
import com.urjc.books.models.entities.Comment;
import com.urjc.books.models.entities.User;
import com.urjc.books.services.BookService;
import com.urjc.books.services.CommentService;
import com.urjc.books.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;
    private CommentService commentService;
    private UserService userService;

    public BookController(BookService bookService, CommentService commentService, UserService userService) {
        this.bookService = bookService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        Book kikaSuperBrujaBook = new Book();
        kikaSuperBrujaBook.setTitle("Kika superbruja: y el libro de hechizos");
        kikaSuperBrujaBook.setSummary("¿Quién es realmente la bruja Elviruja? ¿Por qué su libro de hechizos acabó en manos de Kika? " +
                "¿Cómo influyó en ello el dragón Héctor? " +
                "¿Y qué pintan el perverso brujo Jerónimo y el malaspulgas de su perro Serafín en esta historia?" +
                " ¡Por fin vas a saberlo TODO sobre cómo empezaron las aventuras de Kika Superbruja!");
        kikaSuperBrujaBook.setAuthor("Knister");
        kikaSuperBrujaBook.setPublisher("Anaya");
        kikaSuperBrujaBook.setPostYear(2008);
        Book losPilaresDeLaTierraBook = new Book();
        losPilaresDeLaTierraBook.setTitle("Los pilares de la Tierra");
        losPilaresDeLaTierraBook.setSummary("Los pilares de la Tierra es una novela histórica del autor británico Ken Follett, " +
                "ambientada en Inglaterra en la Edad Media, en concreto en el siglo XII, " +
                "durante un periodo de guerra civil conocido como la anarquía inglesa, entre el hundimiento " +
                "del White Ship y el asesinato del arzobispo Thomas Becket.");
        losPilaresDeLaTierraBook.setAuthor("Ken Follett");
        losPilaresDeLaTierraBook.setPublisher("Grupo Planeta");
        losPilaresDeLaTierraBook.setPostYear(1990);

        User andrea = new User();
        andrea.setNick("Andrea");
        andrea.setEmail("andea@urjc.es");
        andrea = this.userService.save(andrea).get();

        Comment andreaComment = new Comment();
        andreaComment.setAuthor(andrea);
        andreaComment.setText("Me encanta leer, pedazo de libro");
        andreaComment.setScore(5);
        andreaComment.setBook(kikaSuperBrujaBook);

        User juanma = new User();
        juanma.setNick("Juanma");
        juanma.setEmail("juanma@urjc.es");
        juanma = this.userService.save(juanma).get();

        Comment juanmaComment = new Comment();
        juanmaComment.setAuthor(juanma);
        juanmaComment.setText("El mejor libro que he leído nunca");
        juanmaComment.setScore(1);
        juanmaComment.setBook(kikaSuperBrujaBook);

        User antonio = new User();
        antonio.setNick("Antonio");
        antonio.setEmail("antonio@urjc.es");
        antonio = this.userService.save(antonio).get();

        Comment antonioComment = new Comment();
        antonioComment.setAuthor(antonio);
        antonioComment.setText("Un poco rara la historia, pero entretenida");
        antonioComment.setScore(4);
        antonioComment.setBook(kikaSuperBrujaBook);

        var comentariosKika = Arrays.asList(andreaComment, juanmaComment, antonioComment);
        kikaSuperBrujaBook.setComments(comentariosKika);

        User nacho = new User();
        nacho.setNick("Nacho");
        nacho.setEmail("nacho@urjc.es");
        nacho = this.userService.save(nacho).get();

        Comment nachoComment = new Comment();
        nachoComment.setAuthor(nacho);
        nachoComment.setText("Buen libro para los viajes entre partido y partido");
        nachoComment.setScore(4);
        nachoComment.setBook(losPilaresDeLaTierraBook);

        User elisa = new User();
        elisa.setNick("Elisa");
        elisa.setEmail("elisa@urjc.es");
        elisa = this.userService.save(elisa).get();

        Comment elisaComment = new Comment();
        elisaComment.setAuthor(elisa);
        elisaComment.setText("Me ha ayudado mucho a relajarme");
        elisaComment.setScore(5);
        elisaComment.setBook(losPilaresDeLaTierraBook);

        var comentariosPilares = Arrays.asList(nachoComment, elisaComment);
        losPilaresDeLaTierraBook.setComments(comentariosPilares);

        this.bookService.save(kikaSuperBrujaBook);
        this.bookService.save(losPilaresDeLaTierraBook);
    }
    
    @Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Books found",
                    content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class)
                            )}
            )
    })
    @GetMapping()
    public GetAllBooksOutDto getBooks() {
        return this.bookService.findAll();
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book found",
                    content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Book.class)
                            )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found"
            )
    })
    @GetMapping("/{bookId}")
    public ResponseEntity<GetBookOutDto> getBook(
            @Parameter(description = "The id of the book to be searched")
            @PathVariable Long bookId) {

        Optional<Book> book = this.bookService.findById(bookId);
        GetBookOutDto outDto = new GetBookOutDto();

        if (book.isPresent()) {

            outDto = GetBookOutDto.builder()
                    .id(book.get().getId())
                    .title(book.get().getTitle())
                    .summary(book.get().getSummary())
                    .author(book.get().getAuthor())
                    .publisher(book.get().getPublisher())
                    .postYear(book.get().getPostYear())
                    .comments(new ArrayList<>())
                    .build();

            CommentBookOutDto commentDto;
            for (Comment comment : book.get().getComments()) {
                User author = comment.getAuthor();
                commentDto = CommentBookOutDto.builder()
                        .text(comment.getText())
                        .nick(author.getNick())
                        .email(author.getEmail())
                        .build();
                outDto.getComments().add(commentDto);
            }
            return ResponseEntity.ok(outDto);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a book")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book supplied"
            )
    })
    @PostMapping()
    public ResponseEntity<Book> createBook(
            @Parameter(description = "The book to be created")
            @RequestBody Book book) {
        Optional<Book> savedBook = this.bookService.save(book);
        if (savedBook.isPresent()) {
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedBook.get().getId()).toUri();
            return ResponseEntity.created(location).body(savedBook.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Create a comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid comment supplied"
            )
    })
    @PostMapping("/{bookId}/comments")
    public ResponseEntity<Comment> createComment(
            @Parameter(description = "The book to be commented")
            @PathVariable Long bookId,
            @Parameter(description = "The comment to be created")
            @RequestBody PostCommentInDto inDto) {
        var book = this.bookService.findById(bookId);
        var user = this.userService.findByNick(inDto.getNick());
        if (book.isPresent() && user.isPresent()) {
            var savedComment = this.commentService.save(inDto, book.get(), user.get());
            if (savedComment.isPresent()) {
                var location = fromCurrentRequest().path("/{id}").buildAndExpand(savedComment.get().getId()).toUri();
                return ResponseEntity.created(location).body(savedComment.get());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Get a comment by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id supplied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found"
            )
    })
    @GetMapping("/{bookId}/comments/{commentId}")
    public ResponseEntity<Comment> getComment(
            @Parameter(description = "The id of the book")
            @PathVariable Long bookId,
            @Parameter(description = "The id of the comment to be searched")
            @PathVariable Long commentId) {
        Optional<Comment> comment = this.commentService.findByIdAndBookId(bookId, commentId);
        return ResponseEntity.of(comment);
    }

    @Operation(summary = "Delete a comment by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment already deleted"
            )
    })
    @DeleteMapping("/{bookId}/comments/{commentId}")
    public ResponseEntity<Comment> deleteComment(
            @Parameter(description = "The id of book")
            @PathVariable Long bookId,
            @Parameter(description = "The id of the comment to be deleted")
            @PathVariable Long commentId) {
        Optional<Comment> comment = this.commentService.findByIdAndBookId(bookId, commentId);
        if (comment.isPresent()) {
            this.commentService.delete(commentId);
        }
        return ResponseEntity.of(comment);
    }
}