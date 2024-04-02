package com.example.programguide

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programguide.databinding.ActivityBeatBoxBinding
import com.example.programguide.databinding.ListItemSoundBinding

class BeatBox : AppCompatActivity() {
    private lateinit var beatBox: BeatBoxManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 创建 BeatBoxManager 实例
        beatBox = BeatBoxManager(assets)

        // 实例化绑定类
        val binding: ActivityBeatBoxBinding = DataBindingUtil.setContentView(this, R.layout.activity_beat_box)

        // 配置 RecyclerView
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            // 使用 adapter，传入 BeatBox 声音资源
            adapter = SoundAdapter(beatBox.sounds)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        beatBox.release()
    }

    // 创建 SoundHolder
    private inner class SoundHolder(private val binding: ListItemSoundBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        // 在绑定函数中更新视图模型要用到的数据
        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }
    }

    // 创建 SoundAdapter，让 SoundAdapter 和 Sound 对象集合关联起来
    private inner class SoundAdapter(private val sounds: List<Sound>): RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun getItemCount() = sounds.size

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            var sound = sounds[position]
            holder.bind(sound)
        }

    }
}