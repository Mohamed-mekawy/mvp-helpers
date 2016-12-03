package io.blackbox_vision.mvphelpers.logic.interactor

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

import io.blackbox_vision.mvphelpers.utils.Preconditions


open class BaseInteractor {
    private val NUMBER_OF_THREADS = 5

    private val scheduledExecutor = Executors.newScheduledThreadPool(NUMBER_OF_THREADS)
    private var executor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    private val handler = Handler(Looper.getMainLooper())

    private var scheduledFuture: ScheduledFuture<*>? = null

    protected fun runOnUiThread(runnable: Runnable) {
        Preconditions.checkNotNull(runnable, "runnable shouldn't be null")

        handler.post(runnable)
    }

    protected fun runOnBackground(runnable: Runnable) {
        Preconditions.checkNotNull(runnable, "runnable shouldn't be null")

        if (executor.isTerminated) {
            executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        }

        val task = executor.submit(runnable)

        if (task.isDone) {
            executor.shutdown()
        }
    }

    protected fun runScheduledTaskOnBackground(runnable: Runnable, time: Long, unit: TimeUnit) {
        Preconditions.checkNotNull(runnable, "runnable shouldn't be null")
        Preconditions.checkNotNull(time, "time shouldn't be null")
        Preconditions.checkNotNull(unit, "unit shouldn't be null")

        scheduledFuture = scheduledExecutor.schedule(runnable, time, unit)
    }

    protected fun cancelScheduledTask() {
        scheduledFuture!!.cancel(true)
    }
}
