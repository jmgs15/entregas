package com.urjc.books.models.dtos.out;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOutDto {

    private Long id;
    private String title;
}
