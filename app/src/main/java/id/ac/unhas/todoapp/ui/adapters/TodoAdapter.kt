package id.ac.unhas.todoapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import id.ac.unhas.todoapp.R
import id.ac.unhas.todoapp.data.db.entity.TodoItemEntity
import kotlinx.android.synthetic.main.list_item.view.*

class TodoAdapter(
    activity: Activity,
    private var data: LiveData<out List<TodoItemEntity>>
) : BaseAdapter() {
    private  val mInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var callBack: TodoAdapterListener

    interface TodoAdapterListener {
        fun deleteItem(item: TodoItemEntity)
        fun editItem(item: TodoItemEntity)
    }

    fun setAdapterListener(mCallback: TodoAdapterListener) { this.callBack = mCallback }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder

        var view = convertView
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item, null)

            holder = ViewHolder()
            holder.tvTitle = view.tvTitle
            holder.ivEdit = view.ivEdit
            holder.ivDelete = view.ivDelete

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val item = data.value?.get(position)

        holder.tvTitle!!.text = item.title

        holder.ivEdit!!.setOnClickListener {
            callBack.editItem(item)
        }
        holder.ivDelete!!.setOnClickListener {
            callBack.deleteItem(item)
        }
        return view!!
    }

    override fun getItem(position: Int): Any {
        return data.value!![position]
    }

    override fun getItemId(position: Int): Long {
        return data.value!![position].id.toLong()
    }

    override fun getCount(): Int {
        return data.value!!.size
    }

    private inner class ViewHolder {
        internal var tvTitle: TextView? = null
        internal var ivEdit: ImageView? = null
        internal var ivDelete: ImageView? = null
    }

}