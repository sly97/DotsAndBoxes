package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var mGameView : GameView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGameView = GameView(this)
        //setContentView(mGameView)

        val button : Button = findViewById(R.id.button2)

        button.setOnClickListener{
            setContentView(mGameView)
        }



    }
}
