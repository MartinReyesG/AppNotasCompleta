package com.bersyte.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


//modelo de la multimedia (tabla)
@Entity
class Multimedia (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idNote: Int,
    val type: String,
    val path: String,
    val description: String
) {
    constructor(idNote: Int, type: String, path: String, description: String)
            : this(0, idNote, type, path, description )
}