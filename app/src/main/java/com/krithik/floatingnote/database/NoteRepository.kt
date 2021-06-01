package com.krithik.floatingnote.database

import androidx.annotation.WorkerThread

class NoteRepository(val dao: NoteDao) {

    val getAllNotes = dao.getAllNotes()


    @WorkerThread
    suspend fun insert(notes: Note): Long {
        return dao.insertNote(notes)
    }

    suspend fun update(notes: Note): Int {
        return dao.updateNote(notes)
    }

    suspend fun delete(notes: Note): Int {
        return dao.deleteNote(notes)
    }

}