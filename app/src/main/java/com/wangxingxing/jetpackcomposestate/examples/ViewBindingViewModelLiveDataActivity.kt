package com.wangxingxing.jetpackcomposestate.examples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wangxingxing.jetpackcomposestate.databinding.ActivityHelloNormalStateBinding

class ViewBindingViewModelLiveDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelloNormalStateBinding
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloNormalStateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 普通方式
        /*binding.etInput.doAfterTextChanged {
            binding.tvShow.text = it
        }*/

        // ViewModel + LiveData
        // 事件“向上”流动到ViewModel
        binding.etInput.doAfterTextChanged {
            viewModel.onTextChanged(it.toString())
        }
        // 状态“向下”流动到Activity
        viewModel.text.observe(this) {
            binding.tvShow.text = it
        }
    }
}

class MyViewModel : ViewModel() {
    // backing property（幕后属性）
    private val _text = MutableLiveData("")
    // val text: LiveData<...>：LiveData的固定写法，text不能写，要写只能通过改变_text，而约定俗成通过事件（比如onTextChanged）来改变_text。
    val text: LiveData<String> = _text

    fun onTextChanged(text: String) {
        _text.value = text
    }
}