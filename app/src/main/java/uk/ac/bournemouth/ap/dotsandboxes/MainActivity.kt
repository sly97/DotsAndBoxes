package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button : Button = findViewById(R.id.button2)
        val gridSize : RadioGroup = findViewById(R.id.gridSize)
        val radioGroup : RadioGroup = findViewById(R.id.playAsOptions)

        button.setOnClickListener{
            val intent = Intent(this,Game::class.java)
            if(radioGroup.checkedRadioButtonId == R.id.playAsFriend){
                intent.putExtra("playAs","Friend")
            }else{
                intent.putExtra("playAs","Bot")
            }
            val selectedGridSize : String = findViewById<RadioButton>(gridSize.checkedRadioButtonId).text.toString()
            val rowCount = selectedGridSize.split("x")[0]
            val colCount = selectedGridSize.split("x")[1]
            intent.putExtra("gridSizeRow", rowCount)
            intent.putExtra("gridSizeCol", colCount)
            startActivity(intent)
        }



    }
}
