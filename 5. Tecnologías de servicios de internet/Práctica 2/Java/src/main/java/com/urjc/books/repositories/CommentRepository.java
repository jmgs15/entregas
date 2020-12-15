package com.urjc.books.repositories;

import com.urjc.books.models.entities.Comment;
import com.urjc.books.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByBookId(Long bookId);

    List<Comment> findByAuthor(User author);
}
