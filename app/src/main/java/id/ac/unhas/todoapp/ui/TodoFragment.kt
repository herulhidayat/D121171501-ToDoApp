package id.ac.unhas.todoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.ac.unhas.todoapp.R
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import id.ac.unhas.todoapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.todo_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TodoFragment : ScopedFragment(), KodeinAware{
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
        todoList.observe(this@TodoFragment, Observer {
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

            }
        }
    }
}