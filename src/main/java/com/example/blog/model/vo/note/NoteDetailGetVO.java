package com.example.blog.model.vo.note;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class NoteDetailGetVO {
    @NonNull
    private Long id;

    public NoteDetailGetVO() {}
}
