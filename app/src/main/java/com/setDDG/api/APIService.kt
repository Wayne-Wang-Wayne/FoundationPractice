package com.setDDG.api

import android.content.Context
import com.rockex6.practiceappfoundation.R
import com.set.app.entertainment.api.APIManager
import com.setDDG.baseViewPager.NewsTabsModel
import com.setDDG.homePage.BottomBarModel
import com.setDDG.recyclerViewFunction.PokeModelEnum
import com.setDDG.recyclerViewFunction.model.BaseFormatPokeModel
import com.setDDG.recyclerViewFunction.model.pokeDetailModel.PokeDetailModel
import com.setDDG.recyclerViewFunction.pokemonApiInterface.PokeDetailInfo
import com.setDDG.util.AESUtil
import com.setDDG.videomanager.VideoModel
import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser


object APIService {
    //寶可夢
    private const val POKE_BASE_URL = "https://pokeapi.co"


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

    fun getVideoInfo(context: Context): Single<VideoModel> {
        return Single.create {
            val videoModel = VideoModel()
            videoModel.url =
                "https://www.youtube.com/watch?v=430N__0EzI8&t=122s&ab_channel=Nintendo%E5%85%AC%E5%BC%8F%E3%83%81%E3%83%A3%E3%83%B3%E3%83%8D%E3%83%AB"
            videoModel.imageUrl = "https://i.ytimg.com/vi/C3eTzM0M4E0/maxresdefault.jpg"
            it.onSuccess(videoModel)
        }

    }

    fun getPokeDetailData(count:Int): Single<PokeDetailModel> {
        return getApi(POKE_BASE_URL, PokeDetailInfo::class.java).getPokeDetailInfo(count)
    }

    fun formatPokeData(pokeDetailList: ArrayList<PokeDetailModel>): Single<ArrayList<BaseFormatPokeModel>> {
        return Single.create {

            val formatList = ArrayList<BaseFormatPokeModel>()
            for (pokeDetailModel in pokeDetailList) {
                val formatPicItem = BaseFormatPokeModel(PokeModelEnum.POKEMON_PICTURE,
                    pokeDetailModel.sprites.front_default)
                formatList.add(formatPicItem)
                val typeList = ArrayList<String>()
                for (type in pokeDetailModel.types) {
                    typeList.add(type.type.name)
                }
                val formatTypesItem = BaseFormatPokeModel(PokeModelEnum.POKEMON_TYPES, typeList)
                formatList.add(formatTypesItem)
            }

            it.onSuccess(formatList)
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
