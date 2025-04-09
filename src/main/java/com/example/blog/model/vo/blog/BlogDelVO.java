package com.example.blog.model.vo.blog;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class BlogDelVO {
    @NonNull
    private Long id;

    public BlogDelVO() {}
}
