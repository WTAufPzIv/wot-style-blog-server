package com.example.blog.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransVO {
    @NotBlank(message = "输入为空")
    private String text;

    public TransVO() {}

}