package com.example.blog.mapper;

import com.example.blog.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface NoteMapper {
    List<Note> findNotesByConditions(
            @Param("createTime") String createTime
    );

    List<String> findAllCreatTime();
}
