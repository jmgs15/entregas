package com.urjc.books.models.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsByUserOutDto {

    private List<CommentByUserOutDto> comments = new ArrayList<>();
}
