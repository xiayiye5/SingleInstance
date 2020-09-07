package com.example.singleinstance

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @author xiayiye5
 * 2020年8月25日09:31:32
 */
class MyThread {
    fun createThread() {
        object : Thread() {
            override fun run() {
                super.run()
            }
        }.start()
    }

    fun newInstance() {
        ThreadPoolExecutor(
            3,
            9,
            10,
            TimeUnit.MINUTES,
            LinkedBlockingDeque(4),
            Executors.defaultThreadFactory(),
            ThreadPoolExecutor.AbortPolicy()
        )
            .execute { }
    }
}