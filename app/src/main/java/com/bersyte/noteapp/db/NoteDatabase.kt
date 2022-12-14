package com.bersyte.noteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bersyte.noteapp.model.Note
import com.bersyte.noteapp.model.Tarea
import com.bersyte.noteapp.model.Multimedia

//archivo que contiene la base de datos donde accede a los modelos de notas, tareas y multimedia
//sirviendo de punto de acceso principal de la app de los datos
@Database(entities = [Note::class, Tarea::class, Multimedia::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
    abstract fun MultimediaDao(): MultimediaDao

    companion object {
        private var INSTANCE: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = //crea una instancia a la base de datos
                        Room.databaseBuilder(context,NoteDatabase::class.java, "note")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}