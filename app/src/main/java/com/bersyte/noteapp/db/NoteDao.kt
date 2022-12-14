package com.bersyte.noteapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bersyte.noteapp.model.Note
import com.bersyte.noteapp.model.Tarea


//El room se encarga de guardar los datos de manera local y realiza las query correspondientes
//para las notas y tareas. Esto es basa en el modelo Note de donde obtiene los datos
@Dao
interface NoteDao {
    //consultas de notas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(note: Note)

    @Update
    suspend fun actualizarNota(note: Note)

    @Delete
    suspend fun borrarNota(note: Note)

    @Query("SELECT * FROM NOTES ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE :query OR noteBody LIKE:query or noteSubTitle LIKE:query")
    fun searchNote(query: String?): LiveData<List<Note>>


//consultas de tareas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: Tarea)

    @Update
    suspend fun actualizarTarea(tarea: Tarea)

    @Delete
    suspend fun borrarTarea(tarea: Tarea)

    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun getAllTareas(): LiveData<List<Tarea>>

    @Query("SELECT * FROM tareas WHERE tareaTitle LIKE :query OR tareaBody LIKE:query or tareaSubTitle LIKE:query")
    fun searchTarea(query: String?): LiveData<List<Tarea>>
}