package id.ac.unhas.todoapp.ui

import androidx.lifecycle.ViewModel
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import id.ac.unhas.todoapp.data.repository.TodoRepository
import id.ac.unhas.todoapp.internal.lazyDeferred

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    val todoList by lazyDeferred {
        todoRepository.getTodoList()
    }

    fun upsertTodoItem(item: TodoItemEntity) {
        todoRepository.upsertTodoItem(item)
    }

    fun deleteTodoItem(item: TodoItemEntity) {
        todoRepository.deleteTodoItem(item)
    }

}