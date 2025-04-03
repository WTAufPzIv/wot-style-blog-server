package com.example.blog.model.vo.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BlogEntrieGetVO {
    @NotBlank(message = "主键不能为空")
    private Long id;

    public BlogEntrieGetVO() {}
}
