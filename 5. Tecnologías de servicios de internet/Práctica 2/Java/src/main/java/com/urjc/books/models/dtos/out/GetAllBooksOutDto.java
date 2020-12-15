package com.urjc.books.models.dtos.out;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllBooksOutDto {

    private List<BookOutDto> books = new ArrayList<>();
}
