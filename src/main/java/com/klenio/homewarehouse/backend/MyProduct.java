package com.klenio.homewarehouse.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyProduct {
    @JsonIgnore
    private Long id;
    private String date;
    private String name;
    private String place;
    private String status;
}
