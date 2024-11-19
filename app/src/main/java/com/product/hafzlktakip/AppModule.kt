package com.product.hafzlktakip

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuranDatabase {
        return Room.databaseBuilder(
            context,
            QuranDatabase::class.java,
            "quran_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSurahDao(db: QuranDatabase): SurahDao = db.surahDao()

    @Provides
    @Singleton
    fun provideAyahDao(db: QuranDatabase): AyahDao = db.ayahDao()

    // New: Provide NoteDao
    @Provides
    @Singleton
    fun provideNoteDao(db: QuranDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideSurahRepository(
        surahDao: SurahDao,
        ayahDao: AyahDao,
        noteDao: NoteDao, // Include NoteDao
        @ApplicationContext context: Context
    ): SurahRepository {
        return SurahRepository(surahDao, ayahDao, noteDao, context)
    }
}
