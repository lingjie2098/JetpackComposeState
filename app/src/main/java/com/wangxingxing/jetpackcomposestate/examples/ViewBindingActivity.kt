package com.wangxingxing.jetpackcomposestate.examples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.wangxingxing.jetpackcomposestate.databinding.ActivityHelloNormalStateBinding

class ViewBindingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelloNormalStateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 传统findViewById方式
        /*setContentView(R.layout.activity_hello_normal_state)
        findViewById<EditText>(R.id.et_input).doAfterTextChanged {
            findViewById<TextView>(R.id.tv_show).text = it
        }*/

        // ViewBinding（视图绑定）
        binding = ActivityHelloNormalStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etInput.doAfterTextChanged {
            binding.tvShow.text = it
        }
    }
}