package uk.ac.bournemouth.ap.dotsandboxes

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import java.lang.Exception
import kotlin.random.Random

@SuppressLint("ViewConstructor")
class GameView(context: Context?, playAs: String, gridSize : List<String>) : View(context) {

    private var colCount = 5
    private var rowCount = 5
    private var playAsOption : String = ""

    //Starts gesture detection and tracks coordinates
    private var drawingLine : Boolean = false
    private var startX : Float = 0f
    private var startY : Float = 0f
    private var endX : Float = 0f
    private var endY : Float = 0f

    //Paints for UI
    private var mGridPaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private var mGridDotPaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private var mGridLinePaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.LTGRAY
        strokeWidth = 7.5.toFloat()
    }
    private var mGridLineDrawnPaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
        strokeWidth = 7.5.toFloat()
    }
    private var mBoxHumanPlayerPaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
        textSize = 100f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private var mBoxComputerPlayerPaint : Paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.CYAN
        textSize = 100f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    //declares default player value
    private var player1 : HumanPlayer = HumanPlayer()
    private var player2 : HumanPlayer = HumanPlayer()
    private var players : List<Player> = listOf()
    private var myGame : StudentDotsBoxGame = StudentDotsBoxGame(4,4,listOf(player1))

    init {
        playAsOption = playAs
        if(playAsOption == "Friend"){
            players = listOf(player1,player2)
        }else{
            players = listOf(player1, RandomComputerPlayer())
        }
        colCount = gridSize[0].toInt()+1
        rowCount = gridSize[1].toInt()+1
        myGame = StudentDotsBoxGame(colCount-1, rowCount-1, players)

        //Dialog of GameOver.
        var listenerOnGameOver = object : DotsAndBoxesGame.GameOverListener {
            override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
                val builder : AlertDialog.Builder = (context as Game).let { AlertDialog.Builder(it) }
                builder.apply { setPositiveButton("Close",DialogInterface.OnClickListener { dialog, id ->
                    context.finishMe()
                }) }
                builder.apply { setNegativeButton("Restart",DialogInterface.OnClickListener { dialog, id ->
                    context.restartGame()
                    invalidate()
                }) }
                builder.setMessage("Please choose an option")?.setTitle("Game Over!")
                val dialog = builder.create()
                dialog.show()

                Log.d("MyGame","over!")
            }
        }

        myGame.addOnGameOverListener(listenerOnGameOver)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val chosenDiameter: Float
        val marginTop = 250f

        var drawingFromDot = false
        val drawFromDotRadius = 50f

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        val diameterX: Float = viewWidth / colCount.toFloat()
        val diameterY: Float = viewHeight / rowCount.toFloat()

        // Choose the smallest of the two
        if (diameterX < diameterY) chosenDiameter = diameterX
        else chosenDiameter = diameterY

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mGridPaint)

        val radius = chosenDiameter / 2
        // Draw the circles on the game board
        for (col in 0 until colCount) {
            for (row in 0 until rowCount) {
                // Calculate the co-ordinates of the circle
                val cx = chosenDiameter * col + radius
                val cy = marginTop+chosenDiameter * row + radius

                if(startX in cx-drawFromDotRadius..cx+drawFromDotRadius && startY in cy-drawFromDotRadius..cy+drawFromDotRadius){
                    startX = cx
                    startY = cy
                    drawingFromDot = true

                    if(endX in cx-chosenDiameter-drawFromDotRadius..cx-chosenDiameter+drawFromDotRadius && endY in cy-drawFromDotRadius..cy+drawFromDotRadius){
                        //Drawing Left
                        endX = cx-chosenDiameter
                        endY = cy
                    }else if(endX in cx+chosenDiameter-drawFromDotRadius..cx+chosenDiameter+drawFromDotRadius && endY in cy-drawFromDotRadius..cy+drawFromDotRadius){
                        //Drawing Right
                        endX = cx+chosenDiameter
                        endY = cy
                    }else if(endX in cx-drawFromDotRadius..cx+drawFromDotRadius && endY in cy-chosenDiameter-drawFromDotRadius..cy-chosenDiameter+drawFromDotRadius){
                        //Drawing Down
                        endX = cx
                        endY = cy-chosenDiameter
                    }else if(endX in cx-drawFromDotRadius..cx+drawFromDotRadius && endY in cy+chosenDiameter-drawFromDotRadius..cy+chosenDiameter+drawFromDotRadius){
                        //Drawing Up
                        endX = cx
                        endY = cy+chosenDiameter
                    }else if(!drawingLine){
                        startX = 0f
                        startY = 0f
                        endX = 0f
                        endY = 0f
                    }

                    //detecs a line, its coordinates and draws finally a line
                    if(!drawingLine && startX != 0f && startY != 0f && endX != 0f && endY != 0f){
                        val lineX : Int
                        val lineY : Int
                        if(endX-startX == 0f){
                            if(endY-startY > 0){
                                //Line is vertical
                                lineX = col
                                lineY = row*2+1
                            }else{
                                //Line is reverse vertical
                                lineX = col
                                lineY = row*2-1
                            }
                        }else{
                            if(endX-startX > 0){
                                //Line is horizontal
                                lineX = col
                                lineY = row*2
                            }else{
                                //Line is reverse horizontal
                                lineX = col-1
                                lineY = row*2
                            }
                        }
                        try {
                            myGame.lines[lineX,lineY].drawLine()
                        }catch (e : Exception){
                            if(e.message?.contains("out of range") == false){
                                Toast.makeText(this.context,e.message,Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }

                //Draw the grid lines and dots
                if (row != rowCount-1 || col != colCount-1) {
                    when {
                        row == rowCount-1 -> {
                            canvas.drawLine(cx,cy,cx+chosenDiameter,cy,mGridLinePaint)
                        }
                        col == colCount-1 -> {
                            canvas.drawLine(cx,cy,cx,cy+chosenDiameter,mGridLinePaint)
                        }
                        else              -> {
                            canvas.drawLine(cx,cy,cx+chosenDiameter,cy,mGridLinePaint)
                            canvas.drawLine(cx,cy,cx,cy+chosenDiameter,mGridLinePaint)
                        }
                    }
                }
                canvas.drawCircle(cx, cy, 10.toFloat(), mGridDotPaint)
            }
        }

        //Takes scores per player
        val allScores  = ArrayList<Int>(players.size)
        players.forEach { allScores.add(0) }
        var ownedBoxes = 0
        for (b in myGame.boxes){
            val owner = b.owningPlayer
            if(owner != null){
                allScores[players.indexOf(owner)] += 1
                ownedBoxes++
            }
        }

        //Identidies the curent player
        val player1Current = if(players.indexOf(myGame.currentPlayer) == 0) "> " else ""
        val player2Current = if(players.indexOf(myGame.currentPlayer) == 1) "> " else ""
        val player1Name = "You"
        var player2Name = ""
        if(playAsOption == "Friend"){
            player2Name = "Friend"
        }else{
            player2Name = "Computer"
        }
        canvas.drawText(player1Current+player1Name+": "+allScores[0],(viewWidth / 2),120.toFloat(),mBoxHumanPlayerPaint)
        canvas.drawText(player2Current+player2Name+": "+allScores[1],(viewWidth / 2),marginTop,mBoxComputerPlayerPaint)

        //Draw the gesture detected if any and if valid
        if(drawingFromDot && drawingLine) canvas.drawLine(startX, startY, endX, endY, mGridLineDrawnPaint)

        //Draw occupied boxes
        for (box in myGame.boxes){
            val boxComplete = box.boundingLines.all { it.isDrawn }
            val boxStartX : Float = box.boxX * chosenDiameter + radius
            val boxStartY : Float = box.boxY * chosenDiameter + radius + marginTop
            val boxEndX : Float = boxStartX + chosenDiameter
            val boxEndY : Float = boxStartY + chosenDiameter
            if (boxComplete){
                if(players.indexOf(box.owningPlayer) == 0) canvas.drawRect(boxStartX,boxStartY,boxEndX,boxEndY,mBoxHumanPlayerPaint)
                else canvas.drawRect(boxStartX,boxStartY,boxEndX,boxEndY,mBoxComputerPlayerPaint)
            }
        }

        //Draw drawn lines visually on the grid
        for (line in myGame.lines){
            if(line.isDrawn){
                var lineStartX : Float
                var lineStartY : Float
                var lineEndX : Float
                var lineEndY : Float
                if(line.lineY % 2 == 0){
                    //Line is horizontal
                    lineStartX = ((line.lineX)*chosenDiameter + radius)
                    lineStartY = ((line.lineY/2)*chosenDiameter + radius + marginTop)
                    lineEndX = lineStartX+chosenDiameter
                    lineEndY = lineStartY
                }else{
                    //Line is vertical
                    lineStartX = ((line.lineX)*chosenDiameter + radius)
                    lineStartY = (((line.lineY-1)/2)*chosenDiameter + radius + marginTop)
                    lineEndX = lineStartX
                    lineEndY = lineStartY+chosenDiameter
                }
                canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, mGridLineDrawnPaint)
            }
        }


    }

    //Detects gesture
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val DEBUG_TAG = "MyTask"
        val action: Int = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                drawingLine = true
                startX = event.getX()
                startY = event.getY()
                // Set the end to prevent initial jump (like on the demo recording)
                endX = event.getX()
                endY = event.getY()
                invalidate() //Call onDraw
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.getX()
                endY = event.getY()
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                drawingLine = false
                invalidate()
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }




}