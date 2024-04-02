package com.example.programguide

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException
import java.lang.Exception
import kotlin.math.log

// 记录日志
private const val TAG = "BeatBoxManager"
// 声音资源文件目录名
private const val SOUNDS_FOLDER = "soundsPKS"
// 同时播放音频数（配合优先级使用）
private const val MAX_SOUNDS = 1

/**
 * @Author 李泽億
 * @Date 2024/4/2 13:59
 * @Description 资源管理类，能定位、管理、播放assets中的音频文件
 */
class BeatBoxManager(private val assets: AssetManager) {
    val sounds: List<Sound>

    // 创建 SoundPool 对象，制定同时播放音频数
    private val soundPool = SoundPool.Builder().setMaxStreams(MAX_SOUNDS).build()

    init {
        sounds = loadSounds()
    }

    // 查看 assets 资源，获取指定目录下的所有文件名
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (e: Exception) {
            Log.e(TAG, "Could not list assets", e)
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            // 载入全部音频文件
            try {
                load(sound)
                sounds.add(sound)
            } catch (IOE: IOException) {
                Log.e(TAG, "Cound not load sound $filename", IOE)
            }
        }
        return sounds
    }

    /**
     * @Author 李泽億
     * @Date 2024/4/1 16:03
     * @Description soundPool.load() 函数可以把音频文件载入 SoundPool 中待播放，会返回一个 Int 型 ID
     *              openFd() 可能抛出 IOException，因此必须处理可能发生的 IOException
     * @Return 会返回 Int 型的 ID，方便管理、重播或卸载音频文件（就是春存在 soundId 中的 ID）
     */
    private fun load(sound: Sound) {
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd, 1)
        sound.soundId = soundId
    }

    /**
     * @Author 李泽億
     * @Date 2024/4/1 16:12
     * @Description 播放音频
     * @Param int soundID：音频 ID, float leftVolume：左音量, float rightVolume：右音量，int priority：优先级, int loop：是否循环播放, float rate：播放速率
     */
    fun play(sound: Sound) {
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 2, 1.0f)
        }
    }

    /**
     * @Author 李泽億
     * @Date 2024/4/2 14:19
     * @Description 释放 SoundPool
     */
    fun release() {
        soundPool.release()
    }
}