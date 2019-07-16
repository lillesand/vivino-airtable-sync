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

    fun newProgress(size: Int): Progress {
        return Progress(size)
    }

}

class Progress(val max: Int) {

    private var counter = 0

    init {
        printUpdate()
    }

    fun printUpdate(status: String? = null) {

        val statusString = if (status != null) " $status" else ""

        // Would ideally like to use print here to update the same line, but it looks like that doesn't work with IntelliJ print :(
        println("\r($counter/$max)$statusString")
        System.out.flush()
    }

    fun increment(status: String? = null) {
        counter++
        printUpdate(status)
    }
}
