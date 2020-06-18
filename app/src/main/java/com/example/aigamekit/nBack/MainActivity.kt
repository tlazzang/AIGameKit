package com.example.aigamekit.nBack

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import com.example.aigamekit.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NBackViewModel
    lateinit var gridLayout: GridLayout
    lateinit var btn_yes: Button
    lateinit var btn_no: Button
    lateinit var iv_answer: ImageView
    val imageViewList: ArrayList<ImageView> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        observeData()

        btn_yes.setOnClickListener {
            viewModel.checkAnswer(true)
        }
        btn_no.setOnClickListener {
            viewModel.checkAnswer(false)
        }

        btn_startAndStop.setOnClickListener {
            viewModel.startAndStopGame()
        }
    }

    fun initView(){
        viewModel = ViewModelProviders.of(this).get(NBackViewModel::class.java)
        gridLayout = findViewById(R.id.gridLayout)
        btn_yes = NBackActivity_btn_yes
        btn_no = NBackActivity_btn_no
        iv_answer = NBackActivity_iv_answer

        val childCount = gridLayout.childCount
        for(i in 0 until childCount){
            imageViewList.add(gridLayout.getChildAt(i) as ImageView)
        }
    }

    fun observeData(){
        viewModel.nowPosition.observe(this, Observer {
            it?.let {
                imageViewList[it].setBackgroundResource(R.drawable.filled_rectangle)
            }
        })

        viewModel.prevPosition.observe(this, Observer {
            it?.let {
                imageViewList[it].setBackgroundResource(R.drawable.unfilled_rectangle)
            }
        })

        viewModel.isRightAnswer.observe(this, Observer {
            it?.let {
                if(it){
                    iv_answer.setBackgroundColor(Color.GREEN)
                }else{
                    iv_answer.setBackgroundColor(Color.RED)
                }
            }
        })

        viewModel.isPlaying.observe(this, Observer {
            it?.let{
                if(it){
                    btn_startAndStop.text = "STOP"
                }else{
                    btn_startAndStop.text = "START\nN=${viewModel.N}"
                    for(i in 0 until imageViewList.size){
                        imageViewList[i].setBackgroundResource(R.drawable.unfilled_rectangle)
                    }
                }
            }
        })
    }
}
