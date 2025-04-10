package com.example.blog.model.vo.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BlogPutVO {
    @NonNull
    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 匹配前端传入格式
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
