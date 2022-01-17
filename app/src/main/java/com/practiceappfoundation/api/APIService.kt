package com.practiceappfoundation.api

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.practiceappfoundation.homePage.BottomBarModel
import com.practiceappfoundation.news.NewsTabsModel
import com.rockex6.practiceappfoundation.R
import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser


object APIService {


    fun getBottomBar(context: Context): Single<ArrayList<BottomBarModel>> {
        return Single.create {
            val bottomBarModels = ArrayList<BottomBarModel>()
            val res = context.resources
            val xrp = res.getXml(R.xml.menu_home_bottom_bar)
            try {
                var eventType = xrp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xrp.name == "item") {
                            val bottomBarModel = BottomBarModel()
                            bottomBarModel.id = xrp.getAttributeValue(null, "id")
                            bottomBarModel.icon = xrp.getAttributeValue(null, "selector")
                            bottomBarModel.title = xrp.getAttributeValue(null, "title")
                            bottomBarModel.isSelect =
                                xrp.getAttributeBooleanValue(null, "select", false)
                            bottomBarModels.add(bottomBarModel)
                        }

                    }
                    eventType = xrp.next()
                }
                xrp.close()
            } catch (e: Exception) {
            }
            it.onSuccess(bottomBarModels)
        }
    }

    fun getNewsTabs(context: Context): Single<List<NewsTabsModel>> {
            return Single.create {
                val newsTabsModels = ArrayList<NewsTabsModel>()
                val res = context.resources
                val xrp = res.getXml(R.xml.news_tabs)
                try {
                    var eventType = xrp.eventType
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xrp.name == "item") {
                                val newsTabsModel = NewsTabsModel()
                                newsTabsModel.type = xrp.getAttributeValue(null, "type")
                                newsTabsModel.id = xrp.getAttributeValue(null, "id")
                                newsTabsModel.title = xrp.getAttributeValue(null, "title")
                                newsTabsModel.url =
                                    xrp.getAttributeValue(null, "url")
                                newsTabsModels.add(newsTabsModel)
                            }

                        }
                        eventType = xrp.next()
                    }
                    xrp.close()
                } catch (e: Exception) {
                }
                it.onSuccess(newsTabsModels)

            }
    }


}
