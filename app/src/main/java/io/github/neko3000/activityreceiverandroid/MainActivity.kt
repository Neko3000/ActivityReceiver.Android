package io.github.neko3000.activityreceiverandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.util.Log
import android.content.ClipData
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.widget.RelativeLayout



class MainActivity : AppCompatActivity() {

    private var _xDelta: Int = 0
    private var _yDelta: Int = 0

    private var questionString: String = "a|wise|man|will|make|more|opportunities|than|he|finds"
    private var questionWords: List<String>? = null

    private var wordItems: MutableList<TextView>? = null

    private var answer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Log.d("TAG","view controller has been initialized")

        initializeLayout()

    }

    private fun initializeLayout(){

        val horizontalSpacing:Int = 10
        val verticalSpacing:Int = 10

        questionWords= questionString.split("|")

        val touchListener = View.OnTouchListener{
            view,motionEvent ->
            var x = motionEvent.rawX.toInt()
            var y = motionEvent.rawY.toInt()

            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {

                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    _xDelta = x - lParams.leftMargin
                    _yDelta = y - lParams.topMargin
                }
                MotionEvent.ACTION_MOVE -> {

                    val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                    layoutParams.leftMargin = x - _xDelta
                    layoutParams.topMargin = y - _yDelta

                    view.layoutParams = layoutParams

                }
                MotionEvent.ACTION_UP -> {

                    val answerTextView = findViewById<TextView>(R.id.answerTextView)
                    answerTextView.text = convertToAnswer()

                }
                else ->
                {
                    Log.d("TAG","nothing happen")
                }
            }
            true
        }

        //
        wordItems = ArrayList<TextView>()

        for(index in 0..(questionWords!!.count()-1)){
            val widthContent = RelativeLayout.LayoutParams.WRAP_CONTENT
            val heightContent = RelativeLayout.LayoutParams.WRAP_CONTENT

            val relativeLayout:RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(widthContent,heightContent)
            relativeLayout.topMargin = index * 80
            relativeLayout.leftMargin = 10

            val mainArea = findViewById<RelativeLayout>(R.id.MainArea)

            val wordItem = TextView(this)
            wordItem.text = questionWords!![index]
            wordItem.setTextColor(Color.WHITE)
            wordItem.setBackgroundResource(R.color.colorPurpleHeavy)
            wordItem.setPadding(15,6,15,6)
            wordItem.layoutParams = relativeLayout
            wordItem.setOnTouchListener(touchListener)

            val gd = GradientDrawable()
            gd.shape = GradientDrawable.RECTANGLE
            gd.cornerRadius = 10.0f
            gd.setColor(ContextCompat.getColor(this,R.color.colorPurpleHeavy))
            wordItem.background = gd

            wordItems!!.add(wordItem)
            mainArea.addView(wordItem)
        }
    }

    private fun convertToAnswer():String{

        wordItems!!.sortBy { it.x }

        answer = ""
        for(index in 0..(wordItems!!.count()-1)) {

           answer = answer + wordItems!![index].text + " "
        }

        return answer.capitalize() + "."
    }


}
