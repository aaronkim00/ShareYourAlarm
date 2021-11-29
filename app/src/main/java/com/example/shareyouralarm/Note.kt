package com.example.shareyouralarm


class Note {
    private var _id = 0
    private var todo: String? = null

    fun Note(_id: Int, todo: String?) {
        this._id = _id
        this.todo = todo
    }

    fun get_id(): Int {
        return _id
    }

    fun set_id(_id: Int) {
        this._id = _id
    }

    fun getTodo(): String? {
        return todo
    }

    fun setTodo(todo: String?) {
        this.todo = todo
    }
}