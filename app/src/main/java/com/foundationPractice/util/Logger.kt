package com.foundationPractice.util

import timber.log.Timber

class Logger {
    companion object{
        @JvmStatic
        fun e(tag: String?, msg: String?) {
            Timber.tag(tag).e(msg)
        }
        @JvmStatic
        fun i(tag: String?, msg: String?) {
            Timber.tag(tag).i(msg)
        }
        @JvmStatic
        fun d(tag: String?, msg: String?) {
            Timber.tag(tag).d(msg)
        }
        @JvmStatic
        fun v(tag: String?, msg: String?) {
            Timber.tag(tag).v(msg)
        }
        @JvmStatic
        fun w(tag: String?, msg: String?) {
            Timber.tag(tag).w(msg)
        }
    }
}
