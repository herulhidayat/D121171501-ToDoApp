package id.ac.unhas.todoapp.data.repository

import androidx.lifecycle.LiveData
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import id.ac.unhas.todoapp.data.db.dao.TodoItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoRepositoryImpl(
    private val todoItemDao: TodoItemDao
) : TodoRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getTodoList(): LiveData<out List<TodoItemEntity>> {
        return todoItemDao.getTodoItem()
    }

    override fun upsertTodoItem(item: TodoItemEntity)= scope.launch {
        todoItemDao.upsert(item)
    }

    override fun deleteTodoItem(item: TodoItemEntity) = scope.launch {
        todoItemDao.delete(item)
    }
}