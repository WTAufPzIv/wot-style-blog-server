package com.example.blog.model.vo.photograph;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PhotoPutVO {
    @NonNull
    private Long id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 匹配前端传入格式
    private LocalDateTime createTime;

    @NotBlank(message = "正文不能为空")
    private String content;

    @NonNull
    @NotEmpty(message = "图片不能为空")
    private List<String> images;

    public PhotoPutVO() {}
}
