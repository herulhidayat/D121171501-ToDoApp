package id.ac.unhas.todoapp.data.repository

import androidx.lifecycle.LiveData
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import kotlinx.coroutines.Job

interface TodoRepository {
    suspend fun getTodoList() : LiveData<out List<TodoItemEntity>>

    fun upsertTodoItem(item: TodoItemEntity): Job
    fun deleteTodoItem(item: TodoItemEntity): Job
}