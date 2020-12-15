package com.urjc.books.services;

import com.urjc.books.models.dtos.out.BookOutDto;
import com.urjc.books.models.dtos.out.GetAllBooksOutDto;
import com.urjc.books.models.entities.Book;
import com.urjc.books.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;
    private CommentService commentService;

    public BookService(BookRepository bookRepository, CommentService commentService) {
        this.bookRepository = bookRepository;
        this.commentService = commentService;
    }

    public GetAllBooksOutDto findAll() {
        GetAllBooksOutDto outDto = new GetAllBooksOutDto();
        var books = this.bookRepository.findAll();
        BookOutDto bookDto;
        for (Book book : books) {
            bookDto = BookOutDto.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .build();
            outDto.getBooks().add(bookDto);
        }

        return outDto;
    }

    public Optional<Book> findById(Long id) {
        return this.bookRepository.findById(id);
    }

    public void delete(Long id) {
        this.bookRepository.deleteById(id);
    }

    public Optional<Book> save(Book book) {
        return Optional.of(this.bookRepository.save(book));
    }

}
