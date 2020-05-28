package id.ac.unhas.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.ac.unhas.todoapp.data.db.dao.TodoItemDao
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity

@Database(
    entities = [TodoItemEntity::class],
    exportSchema = false,
    version = 1
)
abstract class TodoDatabse : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao

    companion object {
        @Volatile private var instance: TodoDatabse? = null
        private val LOCK = Any ()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                TodoDatabse::class.java, "todo.db")
                .build()
    }
}