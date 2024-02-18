package com.tagir.notes.controllers;

import com.tagir.notes.controllers.dto.NoteDTO;
import com.tagir.notes.entities.Note;
import com.tagir.notes.services.NoteService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes-service")
public class NoteController {

    private final NoteService noteService;

    private final MeterRegistry meterRegistry;

    /**
     * Добавление заметки
     * @param noteDTO заметка
     * @return заметку
     */
    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody NoteDTO noteDTO){
        return ResponseEntity.ok(noteService.addNote(new Note(noteDTO.getTitle(),
                noteDTO.getContent(),noteDTO.getCreateTime())));
    }

    /**
     * Получение всех заметок
     * @return список заметок
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(){
        meterRegistry.counter("required_noteAll_counters").increment();
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    /**
     * Получение заметки по id
     * @param noteId id заметки
     * @return заметки
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long noteId){
        return ResponseEntity.ok(noteService.getNoteById(noteId).orElseThrow(()->
                new RuntimeException("Note with id = " + noteId + " not found")));
    }

    /**
     * Обновление заметки
     * @param noteId id старой заметки
     * @param noteDTO новая заметка
     * @return заметку
     */
    @PutMapping("/{noteId}")
    public ResponseEntity<Note> updateNote(@PathVariable Long noteId, @RequestBody NoteDTO noteDTO){
        return ResponseEntity.ok(noteService.updateNote(noteId,new Note(noteDTO.getTitle(),
                noteDTO.getContent(), noteDTO.getCreateTime())));
    }

    /**
     * Удаление заметки
     * @param noteId id удаляемой заметки
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
        return ResponseEntity.ok().build();
    }
}
