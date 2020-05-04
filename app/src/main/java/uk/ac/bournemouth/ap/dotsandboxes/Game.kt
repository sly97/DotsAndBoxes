package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Game : AppCompatActivity() {

    private var mGameView : GameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val playAs = intent.getStringExtra("playAs")
        val gridSizeRow = intent.getStringExtra("gridSizeRow")
        val gridSizeCol = intent.getStringExtra("gridSizeCol")

        mGameView = GameView(this, playAs, listOf(gridSizeRow,gridSizeCol))
        setContentView(mGameView)
    }
}
