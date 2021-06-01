package com.krithik.floatingnote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "note_data_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="UserId")
    var id : Int,

    @ColumnInfo(name = "Note")
    var note : String
)