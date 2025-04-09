package com.example.blog.model.vo.photograph;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class PhotoDetailGetVO {
    @NonNull
    private Long id;

    public PhotoDetailGetVO() {}
}