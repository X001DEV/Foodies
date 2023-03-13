package dev.x001.foodies.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.x001.foodies.model.entities.Dish

@Database(entities = [Dish::class], version = 1)
abstract class DishRoomDatabase: RoomDatabase() {

    abstract fun dishDao(): DishDao

    companion object{
        @Volatile
        private var INSTANCE: DishRoomDatabase? = null

        fun getDatabase(context: Context): DishRoomDatabase{
            //if INSTANCE is not null, then return it,
            //if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DishRoomDatabase::class.java,
                    "dish_ database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}