package com.example.blog.service;

import com.example.blog.common.exception.BusinessException;
import com.example.blog.entity.Note;
import com.example.blog.mapper.NoteMapper;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.note.NoteAddVO;
import com.example.blog.model.vo.note.NoteDelVO;
import com.example.blog.model.vo.note.NoteListGetVO;
import com.example.blog.model.vo.note.NotePutVO;
import com.example.blog.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NoteRepository noteRepository;
    NoteMapper noteMapper;

    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    public ResponseResult<Note> createNote(NoteAddVO noteAdd) {
        Note note = new Note();
        note.setCreateTime(noteAdd.getCreateTime());
        note.setContent(noteAdd.getContent());
        note.setImages(noteAdd.getImages());
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

    public ResponseResult<Note> updateNote(NotePutVO notePut) {
        Long id = notePut.getId();
        Note putedNote = noteRepository.findById(id)
                .map(existingPhoto -> {
                    // 仅更新允许修改的字段
                    existingPhoto.setCreateTime(notePut.getCreateTime());
                    existingPhoto.setContent(notePut.getContent());
                    existingPhoto.setImages(notePut.getImages());
                    return noteRepository.save(existingPhoto);
                })
                .orElseThrow(() -> new BusinessException(404, "记录不存在"));
        return ResponseResult.success(putedNote);
    }

    public ResponseResult<List<Note>> getNoteList(NoteListGetVO noteListGet) {
        List<Note> notes= noteMapper.findNotesByConditions(noteListGet.getCreateTime());
        return ResponseResult.success(notes);
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
