package com.setDDG.api

import android.content.Context
import com.setDDG.homePage.BottomBarModel
import com.setDDG.baseViewPager.NewsTabsModel
import com.rockex6.practiceappfoundation.R
import com.set.app.entertainment.api.APIManager
import com.setDDG.util.AESUtil
import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser


object APIService {
    var isNeedEncrypt = true


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
                                newsTabsModel.id = xrp.getAttributeValue(null, "id")
                                newsTabsModel.title = xrp.getAttributeValue(null, "title")
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

    private fun <T> getApi(domain: String, cls: Class<T>): T {
        return APIManager.getRetrofit(domain)
            .create(cls)
    }

    private fun getEncryptHeader(
        data: String?, key: String?, iv: String?): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers[APIHeader.CONTENT_TYPE] = APIHeader.APPLICATION_JSON
        headers[APIHeader.ACCEPT] = APIHeader.APPLICATION_JSON
        headers[APIHeader.ACCEPT_CHARSET] = APIHeader.UTF8
        if (isNeedEncrypt) headers["Authorization"] = "Bearer ${
            AESUtil.encrypt(data, key, iv)
        }"
        return headers
    }


}
