package com.example.shareyouralarm

class User {

    public lateinit var fullName: String
    public lateinit var roomNum: String
    public lateinit var email: String
    public lateinit var token: String
    constructor(){
        //
    }

    constructor(fullName: String, roomNum: String, email: String, token: String){
        this.fullName = fullName
        this.roomNum = roomNum
        this.email = email
        this.token = token

    }
}