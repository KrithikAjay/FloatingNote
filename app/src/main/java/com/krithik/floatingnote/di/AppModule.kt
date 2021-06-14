package com.krithik.floatingnote.di


import android.app.Application
import android.content.Context

import com.krithik.floatingnote.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton
//For Dagger
//@Module
//
//class AppModule (
//        private val context: Context){
//
//    @Singleton
//    @Provides
//    fun noteDatabase(): NoteDatabase {
//        return NoteDatabase.getInstance(context.applicationContext)
//    }
//
//
//    @Provides
//    fun noteDao(db: NoteDatabase) = db.noteDao
//
//}

//For Hilt
@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun noteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return NoteDatabase.getInstance(context)
    }


    @Provides
    fun noteDao(db: NoteDatabase) = db.noteDao

}