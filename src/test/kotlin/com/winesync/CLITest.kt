package com.winesync

import org.junit.Test

class CLITest {

    @Test
    fun progress() {
        val cli = CLI()
        val progress = cli.newProgress(10)

        for (i in 1..10) {
            progress.increment("Saving ${i}")
            //Thread.sleep(500)
        }
    }

}
