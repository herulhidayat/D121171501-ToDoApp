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
import id.ac.unhas.todoapp.R
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import id.ac.unhas.todoapp.ui.adapters.TodoAdapter
import id.ac.unhas.todoapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.android.synthetic.main.todo_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TodoFragment : ScopedFragment(), KodeinAware, TodoAdapter.TodoAdapterListener{
    override val kodein by closestKodein()

    private val viewModelFactory: TodoViewModelFactory by instance()
    private lateinit var viewModel: TodoViewModel
    private lateinit var todoData: LiveData<out List<TodoItemEntity>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TodoViewModel::class.java)

        fetchData()
    }

    private fun fetchData() = launch {
        val todoList = viewModel.todoList.await()
        todoList.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            todoData = todoList

            bindUI()
        })
    }

    private fun bindUI() {
        when(todoData.value!!.isEmpty()) {
            true ->{
                tvPlaceholder.visibility = View.VISIBLE
                lvItems.visibility = View.GONE
            }
            else -> {
                val adapter = activity?.let {TodoAdapter(it, todoData)}
                adapter?.setAdapterListener(this)
                lvItems.adapter = adapter

                tvPlaceholder.visibility = View.GONE
                lvItems.visibility = View.VISIBLE
            }
        }

        fabAdd.setOnClickListener {
            showDialog(null)
        }
    }

    private fun showDialog(item: TodoItemEntity?) {
        val inflater =activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogLayout = inflater.inflate(R.layout.dialog, null)
        dialogLayout.etTitle.setText(item?.title)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogLayout)
            .setPositiveButton("Save", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (dialogLayout.etTitle.text.toString().isNotEmpty()) {
                when (item) {
                    null -> createTodo(dialogLayout.etTitle.text.toString())
                    else -> editTodo(item, dialogLayout.etTitle.text.toString())
                }
                dialog.dismiss()
            } else {
                dialogLayout.tilTitle.error = getString(R.string.dialog_warning)
            }
        }
    }

    private fun createTodo(title: String) {
        val todoItem = TodoItemEntity(todoData.value!!.size, title, false)
        viewModel.upsertTodoItem(todoItem)
    }

    private fun editTodo(item: TodoItemEntity, title: String) {
        item.title = title
        viewModel.upsertTodoItem(item)
    }

    override fun deleteItem(item: TodoItemEntity) {
        viewModel.deleteTodoItem(item)
    }

    override fun editItem(item: TodoItemEntity) {
        showDialog(item)
    }
}