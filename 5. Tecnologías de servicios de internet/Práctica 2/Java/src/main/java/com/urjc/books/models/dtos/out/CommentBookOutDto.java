package com.urjc.books.models.dtos.out;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentBookOutDto {

    private String text;
    private String nick;
    private String email;
}
