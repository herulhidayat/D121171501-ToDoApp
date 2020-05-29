package id.ac.unhas.todoapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
class TodoItemEntity(size: Int, title: String, b: Boolean) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}