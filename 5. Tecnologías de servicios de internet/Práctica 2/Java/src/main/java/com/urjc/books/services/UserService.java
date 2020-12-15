package com.urjc.books.services;

import com.urjc.books.models.dtos.out.CommentByUserOutDto;
import com.urjc.books.models.dtos.out.GetCommentsByUserOutDto;
import com.urjc.books.models.entities.Comment;
import com.urjc.books.models.entities.User;
import com.urjc.books.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> save(User user) {
        return Optional.of(this.userRepository.save(user));
    }

    public Optional<User> findById(Long userId) {
        return this.userRepository.findById(userId);
    }

    public Optional<User> findByNick(String nick) {
        return this.userRepository.findByNick(nick);
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public GetCommentsByUserOutDto getCommentsByUser(User user) {
        GetCommentsByUserOutDto outDto = new GetCommentsByUserOutDto();
        CommentByUserOutDto newComment;
        for (Comment comment : user.getComments()) {
            newComment = CommentByUserOutDto
                            .builder()
                            .id(comment.getId())
                            .text(comment.getText())
                            .score(comment.getScore())
                            .bookId(comment.getBook().getId())
                            .build();
            outDto.getComments().add(newComment);
        }
        return outDto;
    }
}
