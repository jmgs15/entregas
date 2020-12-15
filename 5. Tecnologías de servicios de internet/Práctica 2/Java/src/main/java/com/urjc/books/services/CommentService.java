package com.urjc.books.services;

import com.urjc.books.models.dtos.in.PostCommentInDto;
import com.urjc.books.models.entities.Book;
import com.urjc.books.models.entities.Comment;
import com.urjc.books.models.entities.User;
import com.urjc.books.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> findById(Long commentId) {
        return this.commentRepository.findById(commentId);
    }

    public List<Comment> findByBookId(Long bookId) {
        return this.commentRepository.findByBookId(bookId);
    }

    public Optional<Comment> save(PostCommentInDto inDto, Book book, User user) {
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setAuthor(user);
        comment.setScore(inDto.getScore());
        comment.setText(inDto.getText());
        return Optional.of(this.commentRepository.save(comment));
    }

    public void delete(Long commentId) {
        this.commentRepository.deleteById(commentId);
    }

    public Optional<Comment> findByIdAndBookId(Long bookId, Long commentId) {
        List<Comment> comments = this.findByBookId(bookId);
        return comments.stream().filter(comment -> comment.getId() == commentId).findFirst();
    }

    public List<Comment> findByUser(User author) {
        return this.commentRepository.findByAuthor(author);
    }
}
