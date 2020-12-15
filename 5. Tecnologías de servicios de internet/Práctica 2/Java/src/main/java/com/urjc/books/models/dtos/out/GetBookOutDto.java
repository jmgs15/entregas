package com.urjc.books.models.dtos.out;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBookOutDto {

    private Long id;
    private String title;
    private String summary;
    private String author;
    private String publisher;
    private int postYear;
    private List<CommentBookOutDto> comments = new ArrayList<>();
}
