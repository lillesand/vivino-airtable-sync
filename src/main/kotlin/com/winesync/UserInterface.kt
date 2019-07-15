package com.winesync

class CLI {
    fun prompt(message: String, question: String, onConfirmation: () -> Unit, onRejection: () -> Unit) {
        println(message + "\n")
        println(question)
        val confirmation = readLine()

        if (listOf("", "Y", "y").contains(confirmation)) {
            onConfirmation()
        } else {
            onRejection()
        }


    }

}
