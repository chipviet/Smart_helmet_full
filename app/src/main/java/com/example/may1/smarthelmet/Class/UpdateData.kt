package com.example.may1.smarthelmet.Class

class UpdateData (val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long) {
    constructor() : this("", "", "", "", -1)
}