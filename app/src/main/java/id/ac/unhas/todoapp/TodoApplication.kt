package id.ac.unhas.todoapp

import android.app.Application
import id.ac.unhas.todoapp.data.TodoDatabase
import id.ac.unhas.todoapp.data.repository.TodoRepository
import id.ac.unhas.todoapp.data.repository.TodoRepositoryImpl
import id.ac.unhas.todoapp.ui.TodoViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class
TodoApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@TodoApplication))

        // Setup instances
        bind() from singleton {
            TodoDatabase(
                instance()
            )
        }
        bind() from singleton { instance<TodoDatabase>().todoItemDao() }

        bind<TodoRepository>() with singleton {
            TodoRepositoryImpl(
                instance()
            )
        }
        bind() from provider {
            TodoViewModelFactory(
                instance()
            )
        }
    }
}