package com.product.hafzlktakip.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.product.hafzlktakip.model.entity.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NoteEntity>

    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}