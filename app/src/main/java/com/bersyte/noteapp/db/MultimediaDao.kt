package com.bersyte.noteapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bersyte.noteapp.model.Multimedia

//El room se encarga de guardar los datos de manera local y realiza las query correspondientes
//para la multimedia. Esto es basa en el modelo Multimedia de donde obtiene los datos
@Dao
interface MultimediaDao {
    @Insert//metodos que acceden a la tabla con los datos
    fun insert(multimedia: Multimedia): Long

    @Query("SELECT * FROM multimedia WHERE idNote=:idNote")
    fun getMultimedia(idNote: Int): List<Multimedia>

    @Delete
    fun delete(multimedia: Multimedia)
}