package id.ac.unhas.todoapp.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ac.unhas.todoapp.R
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import id.ac.unhas.todoapp.ui.adapters.TodoAdapter
import id.ac.unhas.todoapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.dialog.view.etNote
import kotlinx.android.synthetic.main.todo_list_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TodoFragment : ScopedFragment(), KodeinAware, TodoAdapter.TodoListAdapterListener {

    override val kodein by closestKodein()

    private val viewModelFactory: TodoViewModelFactory by instance()
    private lateinit var viewModel: TodoViewModel
    private lateinit var todoListData: LiveData<out List<TodoItemEntity>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TodoViewModel::class.java)

        fetchData()
    }

    private fun fetchData() = launch {
        val todoList = viewModel.todoList.await()
        todoList.observe(this@TodoFragment, Observer {
            if (it == null) return@Observer
            todoListData = todoList

            bindUI()
        })
    }

    private fun bindUI() {
        when(todoListData.value!!.isEmpty()) {
            true -> {
                tvPlaceholder.visibility = View.VISIBLE
                lvTodos.visibility = View.GONE
            }
            else -> {
                val adapter = activity?.let {
                    TodoAdapter(
                        it,
                        todoListData
                    )
                }
                adapter?.setAdapterListener(this)
                lvTodos.adapter = adapter

                tvPlaceholder.visibility = View.GONE
                lvTodos.visibility = View.VISIBLE
            }
        }

        fabAdd.setOnClickListener {
            showDialog(null)
        }
    }

    private fun showDialog(item: TodoItemEntity?) {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogLayout = inflater.inflate(R.layout.dialog, null)
        // set title if item exists
        dialogLayout.etTitle.setText(item?.title)
        dialogLayout.etNote.setText(item?.notex)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogLayout)
            .setPositiveButton(resources.getString(R.string.save_button), null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (dialogLayout.etTitle.text.toString().isNotEmpty()) {
                when (item) {
                    null -> createTodo(dialogLayout.etTitle.text.toString(), dialogLayout.etNote.text.toString())
                    else -> editTodo(item, dialogLayout.etTitle.text.toString(), dialogLayout.etNote.text.toString())
                }
                dialog.dismiss()
            } else {
                dialogLayout.tilTitle.error = resources.getString(R.string.dialog_warning)
                dialogLayout.tilNote.error = resources.getString(R.string.dialog_warning)
            }
        }
    }

    private fun createTodo(title: String, notex: String) {
        val todoItem =
            TodoItemEntity(
                todoListData.value!!.size,
                title, notex,
                false
            )

        viewModel.upsertTodoItem(todoItem)
    }

    private fun editTodo(item: TodoItemEntity, title: String, notex: String) {
        item.title = title
        item.notex = notex
        viewModel.upsertTodoItem(item)
    }

    // interface method to edit item
    override fun editItem(item: TodoItemEntity) {
        showDialog(item)
    }

    // interface method to create item
    override fun upsertItem(item: TodoItemEntity) {
        viewModel.upsertTodoItem(item)
    }

    // interface method to delete item
    override fun deleteItem(item: TodoItemEntity) {
        viewModel.deleteTodoItem(item)
    }

}