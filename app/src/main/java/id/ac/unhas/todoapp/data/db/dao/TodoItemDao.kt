package id.ac.unhas.todoapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity

@Dao
interface TodoItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(todoItemEntity: TodoItemEntity)

    @Delete
    fun delete(todoItemEntity: TodoItemEntity)

    @Query("select * from todo_items")
    fun getTodoItem(): LiveData<List<TodoItemEntity>>
}