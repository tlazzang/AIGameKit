package com.example.aigamekit.nBack

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import android.util.Log
import java.util.*


class NBackViewModel : ViewModel() {

    var N: Long = 2

    val nowPosition: MutableLiveData<Int> = MutableLiveData() // 0 ~ 11
    val prevPosition: MutableLiveData<Int> = MutableLiveData()
    val isRightAnswer: MutableLiveData<Boolean> = MutableLiveData()
    val isPlaying: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    val queue: Queue<Int> = LinkedList<Int>()
    var nowAnswer: Boolean? = null

    var cnt = 0
    var numOfCorrectAnswer = 0
    var isSolved = false

    lateinit var countDownTimer: CountDownTimer

    fun startAndStopGame() {
        cnt = 0
        numOfCorrectAnswer = 0

        isPlaying.postValue(!isPlaying.value!!)

        if(isPlaying.value!!){
            countDownTimer.cancel()
            return
        }

        countDownTimer = object : CountDownTimer(2000 * (30 + N), 2000) {
            override fun onFinish() {
                val ratio: Double = String.format("%.3f", (numOfCorrectAnswer.toDouble() / (cnt - N).toDouble()) * 100.0).toDouble()
                Log.d("MainActivity", "정답률 = " + ratio.toString() + " " + numOfCorrectAnswer + "/" + cnt)
                isPlaying.postValue(false)
            }

            override fun onTick(millisUntilFinished: Long) {
                isSolved = false
                val n = (0..11).random()
                queue.add(n)
                prevPosition.postValue(nowPosition.value)

                val timer = Timer()
                val timertask = object : TimerTask() {

                    override fun run() {
                        nowPosition.postValue(n)
                        if (cnt >= N) {
                            Log.d("MainActivity", millisUntilFinished.toString() + queue.peek() + "==" + n + (queue.peek() == n) + " cnt = " + cnt.toString())
                            nowAnswer = queue.peek() == n
                            isRightAnswer.postValue(queue.poll() == n)
                        }
                        cnt++
                    }

                }
                timer.schedule(timertask, 1000)    // 1초 후에 1번 실행하고 종료
            }
        }
        countDownTimer.start()

    }

    fun checkAnswer(answer: Boolean) {
        Log.d("MainActivity", nowAnswer.toString() + "==" + answer.toString() + (nowAnswer == answer))
        if(nowAnswer == answer && !isSolved && cnt >= N){
            isRightAnswer.postValue(nowAnswer == answer)
            numOfCorrectAnswer++
        }
        isSolved = true
    }

}