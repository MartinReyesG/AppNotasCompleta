package com.bersyte.noteapp.repositorio

import com.bersyte.noteapp.db.NoteDatabase
import com.bersyte.noteapp.model.Note

class RepositorioNotas(private val db: NoteDatabase) { //accede a la base de datos local

    //accede a las consultas directamente desde la base de datos
    suspend fun insertNote(note: Note) = db.getNoteDao().insertarNota(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().borrarNota(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().actualizarNota(note)
    fun getAllNotes() = db.getNoteDao().getAllNotes()
    fun searchNote(query: String?) = db.getNoteDao().searchNote(query)

}