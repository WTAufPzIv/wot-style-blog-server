package com.example.blog.model.vo.blog;

import com.example.blog.model.vo.PageVO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BlogListGetVO extends PageVO {
    private String keywords;
    private String category;
    private String createTime;

    public BlogListGetVO() {}
}
