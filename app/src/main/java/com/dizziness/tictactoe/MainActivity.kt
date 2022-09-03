package com.dizziness.tictactoe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Vibrator


class Box(i:Int, j:Int, v: TextView){

    var viewId = v
    var isMarked = false
    var value = -1
}

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object Game{
        var CURRENT_PLAYER = true
        var TURNS = 0
        var board: Array<Array<Box?>> = arrayOf(arrayOfNulls<Box>(3),arrayOfNulls<Box>(3),arrayOfNulls<Box>(3))


    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetGame()
        resetButton.setOnClickListener{
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(200)
            resetGame()
        }
    }
    ////   ids might be required to be compared here... lets see  .. else replace by p0.id... and R.id.box<x>
    override fun onClick(p0: View?) {
        val x= when(p0){
            box1 -> Pair(0,0); box2 -> Pair(0,1); box3 -> Pair(0,2)
            box4 -> Pair(1,0); box5 -> Pair(1,1); box6 -> Pair(1,2)
            box7 -> Pair(2,0); box8 -> Pair(2,1); else -> Pair(2,2)
        }
        updateBox(x)

        TURNS++
        val q= anyWinner()
        if(q!=-1){
            turnteller.text="${if(q==0) "O" else "X"}  WON !"
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
            disableAll()
        }else {
            CURRENT_PLAYER = !CURRENT_PLAYER
            turnteller.text = "It's ${if (CURRENT_PLAYER) "X" else "O"} \'s turn!"
        }

        if(TURNS==9 && anyWinner()==-1){
            turnteller.text="It's a TIE !"
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)

        }


    }
    private fun updateBox(coordinates : Pair<Int,Int>){

        board[coordinates.first][coordinates.second]?.apply {
            isMarked= true
            value = if(CURRENT_PLAYER) 1 else 0
            viewId.apply {
                text= if(CURRENT_PLAYER) "X" else "O"
                isEnabled= false
            }
        }

    }

    private fun resetGame(){
        turnteller.text= getString(R.string.intromsg)
        for(i in 0..2) {
            for (j in 0..2) {
                board[i][j] = Box(i, j, ("box${(3*i) + j + 1 }".getViewName()).apply {
                    setOnClickListener(this@MainActivity)
                    text=""
                    isEnabled= true
                })
            }
        }
        CURRENT_PLAYER = true
        TURNS = 0;
    }

    private fun anyWinner(): Int{
        var winner = -1
        for(i in 0..2){
            if(board[i][0]!!.value==board[i][1]!!.value && board[i][0]!!.value==board[i][2]!!.value){
                winner = board[i][0]!!.value
                break
            }
        }
        for(i in 0..2){
            if(board[0][i]!!.value==board[1][i]!!.value && board[0][i]!!.value==board[2][i]!!.value){
                winner = board[0][i]!!.value
                break
            }
        }
        if(board[0][0]!!.value==board[1][1]!!.value && board[0][0]!!.value==board[2][2]!!.value){
            winner = board[0][0]!!.value
        }
        if(board[2][0]!!.value==board[1][1]!!.value && board[0][2]!!.value==board[1][1]!!.value){
            winner = board[0][2]!!.value
        }
        return winner
    }

    private fun disableAll(){
        for(i in 0..2) for(j in 0..2) board[i][j]!!.viewId.isEnabled=false
    }


    /////       This is non-sense.. I need a solution..
    private fun String.getViewName(): TextView{
        return when(this.last()){
            '1' -> box1; '2' -> box2; '3' -> box3
            '4' -> box4; '5' -> box5; '6' -> box6
            '7' -> box7; '8' -> box8; else -> box9

        }
    }
    ////////////////////////////////////////////////



}

