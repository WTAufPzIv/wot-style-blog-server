package com.example.blog.controller;

import com.example.blog.annotation.LoginCheck;
import com.example.blog.entity.Note;
import com.example.blog.model.dto.ResponseResult;
import com.example.blog.model.vo.note.NoteAddVO;
import com.example.blog.model.vo.note.NoteDelVO;
import com.example.blog.model.vo.note.NotePutVO;
import com.example.blog.service.NoteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auroraWeb")
public class NoteController {
    NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    //================= 增 =================
    @PostMapping("/note/add")
    @LoginCheck
    public ResponseResult<Note> createNode(
            @RequestBody
            @Valid
            NoteAddVO noteAdd
    ){
        return noteService.createNote(noteAdd);
    }

    //================= 删 =================
    @PostMapping("/note/delete")
    @LoginCheck
    public ResponseResult<Boolean> deleteNote(
            @RequestBody
            @Valid
            NoteDelVO noteDel
    ){
        return noteService.deleteNote(noteDel);
    }

    //================= 改 =================
    @PostMapping("/note/put")
    @LoginCheck
    public ResponseResult<Note> updateNote(
            @RequestBody
            @Valid
            NotePutVO notePut
    ){
        return noteService.updateNote(notePut);
    }

    //================= 查所有 =================
    @PostMapping("/note/list")
    public ResponseResult<List<Note>> getNoteList(){
        return noteService.getNoteList();
    }

    //================= id查询 =================
    @PostMapping("/note/detail")
    public ResponseResult<Note> getNoteById(
            @RequestBody
            @Valid
            NoteDelVO noteDel
    ){
        return noteService.getNoteById(noteDel);
    }
}
