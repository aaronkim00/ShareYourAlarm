package com.example.todolist

class User {

    public lateinit var fullName: String
    public lateinit var age: String
    public lateinit var email: String
    
    constructor(){
        //
    }

    constructor(fullName: String, age: String, email: String){
        this.fullName = fullName
        this.age = age
        this.email = email

    }
}