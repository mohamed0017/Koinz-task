package com.task.koinztask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [PhotoEntity::class,  RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photosDao(): PhotosDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}