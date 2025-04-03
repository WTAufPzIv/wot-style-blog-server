package com.example.blog.model.vo.blog;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BlogPutVO {
    @NotBlank(message = "主键不能为空")
    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "创建时间不能为空")
    private LocalDateTime createTime;

    @NotBlank(message = "分类不能为空")
    private String category;

    @NotBlank(message = "头图不能为空")
    private String headImage;

    @NotBlank(message = "描述不能为空")
    private String miniDesc;

    @NotBlank(message = "markdown文件链接不能为空")
    private String mdUrl;

    public BlogPutVO() {}
}
