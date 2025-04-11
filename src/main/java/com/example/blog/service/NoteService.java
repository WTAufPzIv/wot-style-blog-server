package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Note;
import com.example.blog.mapper.NoteMapper;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.note.*;
import com.example.blog.repository.NoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class NoteService {
    NoteRepository noteRepository;
    NoteMapper noteMapper;
    private static final ObjectMapper mapper = new ObjectMapper();

    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    public ResponseResult<Note> createNote(NoteAddVO noteAdd) {
        Note note = new Note();
        note.setCreateTime(noteAdd.getCreateTime());
        note.setContent(noteAdd.getContent());
        try {
            note.setImages(mapper.writeValueAsString(noteAdd.getImages()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new BusinessException(400, "images转换错误");
        }
        Note savedNote = noteRepository.save(note);
        return ResponseResult.success(savedNote);
    }

    public ResponseResult<Boolean> deleteNote(NoteDelVO noteDel) {
        Long id = noteDel.getId();
        if (!noteRepository.existsById(id)) {
            throw new BusinessException(404, "记录不存在");
        }
        noteRepository.deleteById(noteDel.getId());
        return ResponseResult.success(true);
    }

    public ResponseResult<Note> updateNote(NotePutVO notePut)  {
        Long id = notePut.getId();
        Note putedNote = noteRepository.findById(id)
                .map(existingPhoto -> {
                    // 仅更新允许修改的字段
                    existingPhoto.setCreateTime(notePut.getCreateTime());
                    existingPhoto.setContent(notePut.getContent());
                    try {
                        existingPhoto.setImages(mapper.writeValueAsString(notePut.getImages()));
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage());
                        throw new BusinessException(400, "images转换错误");
                    }
                    return noteRepository.save(existingPhoto);
                })
                .orElseThrow(() -> new BusinessException(404, "记录不存在"));
        return ResponseResult.success(putedNote);
    }

    public ResponseResult<List<NoteItemDO>> getNoteList(NoteListGetVO noteListGet) {
        List<Note> notes= noteMapper.findNotesByConditions(noteListGet.getCreateTime());
        List<NoteItemDO> noteListDO = new ArrayList<>();

        notes.forEach(note -> {
            NoteItemDO item = new NoteItemDO();
            item.setId(note.getId());
            item.setContent(note.getContent());
            item.setCreateTime(note.getCreateTime());
            try {
                item.setImages(mapper.readValue(note.getImages(), new TypeReference<List<String>>() {}));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new BusinessException(500, "images转换错误");
            }
            noteListDO.add(item);
        });
        return ResponseResult.success(noteListDO);
    }

    public ResponseResult<Note> getNoteById(NoteDelVO noteDel) {
        Long id = noteDel.getId();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "记录不存在"));
        return ResponseResult.success(note);
    }

    public ResponseResult<List<String>> getAllCreateTime() {
        List<String> creatTimes = noteMapper.findAllCreatTime();
        return ResponseResult.success(creatTimes);
    }
}
