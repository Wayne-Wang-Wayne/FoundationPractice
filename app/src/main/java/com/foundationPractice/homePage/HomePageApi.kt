package com.foundationPractice.homePage



interface HomePageAPI {
}

class BottomBarModel(
    var id: String = "",
    var title: String = "",
    var icon: String = "",
    var isSelect: Boolean = false)