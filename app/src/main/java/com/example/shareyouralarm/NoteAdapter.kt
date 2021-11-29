package com.example.shareyouralarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class NoteAdapter : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var items = ArrayList<Note>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutTodo: LinearLayout = itemView.findViewById(R.id.layoutTodo)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        init {
            deleteButton.setOnClickListener{
                val todo = checkBox.text.toString()
                deleteToDo(todo)
                //Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        fun setItem(item: Note){
            checkBox.text = item.getTodo()
        }

        fun setLayout(){
            layoutTodo.visibility = View.VISIBLE
        }

        private fun deleteToDo(todo: String) {
            //
        }

    }

    fun setItems(items: ArrayList<Note>){
        this.items=items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.todo_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
        holder.setLayout()
    }

    override fun getItemCount() = items.size;

