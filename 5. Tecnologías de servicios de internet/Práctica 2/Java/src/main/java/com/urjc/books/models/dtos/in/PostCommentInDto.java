package com.urjc.books.models.dtos.in;

import com.urjc.books.models.entities.Book;
import com.urjc.books.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentInDto {

    @NotNull
    private String nick;
    private String text;
    private int score;
}
