package com.example.blog.model.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class PageVO {

    @NonNull()
    private Integer pageIndex;

    @NonNull()
    private Integer pageSize;

    public PageVO() {}
}
