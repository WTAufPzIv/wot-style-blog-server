package com.example.blog.model.vo.note;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class NoteDelVO {
    @NonNull
    private Long id;

    public NoteDelVO() {}
}
