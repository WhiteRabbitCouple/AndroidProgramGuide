package com.example.programguide

import androidx.databinding.BaseObservable

class SoundViewModel(private val beatBoxManager: BeatBoxManager) : BaseObservable() {

    // adapter 所用到的接口
    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    // 添加绑定函数
    val title: String?
        get() = sound?.name

    // 点击事件
    fun onButtonClicked() {
        sound?.let {
            beatBoxManager.play(it)
        }
    }
}