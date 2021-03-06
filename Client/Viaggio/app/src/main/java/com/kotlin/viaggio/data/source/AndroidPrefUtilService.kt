package com.kotlin.viaggio.data.source

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class AndroidPrefUtilService @Inject constructor() {
    @Inject
    lateinit var sp: SharedPreferences

    fun hasKey(key: Key): Single<Boolean> {
        return Single.just(sp.contains(key.name))
    }

    fun remove(key: Key): Completable {
        return Completable.fromAction { sp.edit().remove(key.name).apply() }
    }

    fun getInt(key: Key): Single<Int> {
        return Single.just(sp.getInt(key.name, -1))
    }
    fun getInt(key: Key, defaultVal: Int): Single<Int> {
        return Single.just(sp.getInt(key.name, defaultVal))
    }

    fun getString(key: Key): Single<String> {
        return Single.just(sp.getString(key.name, "")!!)
    }

    fun getBool(key: Key): Single<Boolean> {
        return getBool(key, false)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getBool(key: Key, defaultVal: Boolean): Single<Boolean> {
        return Single.just(sp.getBoolean(key.name, defaultVal))
    }

    fun getFloat(key: Key): Single<Float> {
        return Single.just(sp.getFloat(key.name, 0f))
    }

    fun getLong(key: Key): Single<Long> {
        return Single.just(sp.getLong(key.name, 0))
    }

    fun putInt(key: Key, value: Int): Completable {
        return Completable.fromAction { sp.edit().putInt(key.name, value).apply() }
    }

    fun putString(key: Key, value: String): Completable {
        return Completable.fromAction { sp.edit().putString(key.name, value).apply() }
    }

    fun putBool(key: Key, value: Boolean): Completable {
        return Completable.fromAction { sp.edit().putBoolean(key.name, value).apply() }
    }

    fun putFloat(key: Key, value: Float): Completable {
        return Completable.fromAction { sp.edit().putFloat(key.name, value).apply() }
    }

    fun putLong(key: Key, value: Long): Completable {
        return Completable.fromAction { sp.edit().putLong(key.name, value).apply() }
    }

    // upload mode 0 자동, 1 충전 자동, 2 직접 업로드
    // image mode 0 일반 1 고화질
    enum class Key {
        USER_ID, TOKEN_ID, TUTORIAL_CHECK, TRAVELING, LAST_CONNECT_OF_DAY, TRAVELING_OF_DAY_COUNT
        ,TRAVELING_ID,TRAVELING_KINDS, TRAVELING_LAST_COUNTRIES, SELECTED_TRAVELING_CARD_ID,
        SELECT_TRAVEL_ID, TRAVEL_KINDS, USER_NAME, AWS_ID, AWS_TOKEN, SELECTED_COUNTRY, LOCK_APP,
        FINGER_PRINT_LOCK_APP, LOCK_PW, NEW_AWS, SYNCING,
        IMAGE_MODE, UPLOAD_MODE, USER_IMAGE_PROFILE, USER_IMAGE_PROFILE_URL, FIRST_LOGIN,
        GIVE_REVIEW, FREQUENCY_APP, PERIOD_APP, NEXT_REVIEW_PERIOD_APP, NEXT_GIVE_REVIEW, GOOGLE_LOGIN,
        FIRST_SYNC,

        SHARE_TRAVELING_OF_DAY_COUNT, SHARE_TRAVELING_LAST_COUNTRIES
    }
}