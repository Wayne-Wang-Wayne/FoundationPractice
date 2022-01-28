package com.setDDG.util

interface OpenModeEnum {
    companion object {
        const val OPEN_IN_APP = 0 //開內頁

        const val OPEN_BROWSER = 1 //外開瀏覽器

        const val OPEN_WEBVIEW_IN_APP = 2 //開app webview
    }
}