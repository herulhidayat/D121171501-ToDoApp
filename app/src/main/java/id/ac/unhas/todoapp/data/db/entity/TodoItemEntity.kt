package id.ac.unhas.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
class TodoItemEntity {
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
}