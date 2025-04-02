package com.example.blog.model.vo.blog;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class blogAddVO {
    private String title;
    private LocalDateTime createTime;
    private String category;
    private String headImage;
    private String miniDesc;
    private String mdUrl;

    public blogAddVO() {}

}
