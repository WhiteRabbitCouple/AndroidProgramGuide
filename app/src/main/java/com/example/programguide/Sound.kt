package com.example.programguide

private const val WAV = ".wav"

/**
 * @Author 李泽億
 * @Date 2024/4/2 14:11
 * @Description 创建 Sound 对象，管理声音资源文件名等一些其他信息
 * @Param assetPath: 文件路径，soundId: 声音文件 ID
 */

class Sound(val assetPath: String, var soundId: Int? = null) {
    // 分离文件名并删除后缀
    val name = assetPath.split("/").last().removeSuffix(WAV)
}