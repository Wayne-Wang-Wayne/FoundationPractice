package com.setDDG.util

import timber.log.Timber

class Logger {
    companion object{
        fun e(tag: String?, msg: String?) {
            Timber.tag(tag).e(msg)
        }

        fun i(tag: String?, msg: String?) {
            Timber.tag(tag).i(msg)
        }

        fun d(tag: String?, msg: String?) {
            Timber.tag(tag).d(msg)
        }

        fun v(tag: String?, msg: String?) {
            Timber.tag(tag).v(msg)
        }

        fun w(tag: String?, msg: String?) {
            Timber.tag(tag).w(msg)
        }
    }
}
