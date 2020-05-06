package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Game : AppCompatActivity() {

    private var mGameView : GameView? = null
    private var playAs : String =""
    private var gridSizeRow : String =""
    private var gridSizeCol : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //Gets necessary values to start the game from the user's choice
        playAs = intent?.getStringExtra("playAs").toString()
        gridSizeRow = intent?.getStringExtra("gridSizeRow").toString()
        gridSizeCol = intent?.getStringExtra("gridSizeCol").toString()

        mGameView = GameView(this, playAs, listOf(gridSizeRow,gridSizeCol))
        setContentView(mGameView)
    }

    fun finishMe() {finish()}
    fun restartGame() {mGameView = GameView(this, playAs, listOf(gridSizeRow,gridSizeCol));setContentView(mGameView)}


}
