package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class GameView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val colCount = 5
    private val rowCount = 5

    private var startX : Float = 0f
    private var startY : Float = 0f
    private var endX : Float = 0f
    private var endY : Float = 0f

    private var mGridPaint : Paint
    private var mGridDotPaint : Paint
    private var mGridLinePaint : Paint
    private var mGridLineDrawnPaint : Paint
    private var mBoxNoPlayerPaint : Paint
    private var mBoxHumanPlayerPaint : Paint
    private var mBoxComputerPlayerPaint : Paint

    init {
        mGridPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }

        mGridDotPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        mGridLinePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
            strokeWidth = 7.5.toFloat()
        }

        mGridLineDrawnPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
            strokeWidth = 7.5.toFloat()
        }

        mBoxNoPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
        }

        mBoxHumanPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
            textSize = 100f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        mBoxComputerPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.CYAN
            textSize = 100f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val chosenDiameter: Float
        var tokenAtPos: Int
        var paint: Paint
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
                val cy = chosenDiameter * row + radius

                if(startX in cx-drawFromDotRadius..cx+drawFromDotRadius && startY in marginTop+cy-drawFromDotRadius..marginTop+cy+drawFromDotRadius){
                    startX = cx
                    startY = marginTop+cy
                    drawingFromDot = true
                }
                if (row != rowCount-1 || col != colCount-1) {
                    if(row == rowCount-1){
                        canvas.drawLine(cx,marginTop+cy,cx+chosenDiameter,marginTop+cy,mGridLinePaint)
                    }else if(col == colCount-1){
                        canvas.drawLine(cx,marginTop+cy,cx,marginTop+cy+chosenDiameter,mGridLinePaint)
                    }else{
                        canvas.drawLine(cx,marginTop+cy,cx+chosenDiameter,marginTop+cy,mGridLinePaint)
                        canvas.drawLine(cx,marginTop+cy,cx,marginTop+cy+chosenDiameter,mGridLinePaint)
                    }
                }
                canvas.drawCircle(cx, marginTop+cy, 10.toFloat(), mGridDotPaint)


            }
        }

        canvas.drawText("Human",(viewWidth / 2),120.toFloat(),mBoxHumanPlayerPaint)
        canvas.drawText("Computer",(viewWidth / 2),marginTop,mBoxComputerPlayerPaint)

        if(drawingFromDot) canvas.drawLine(startX, startY, endX, endY, mGridLineDrawnPaint);

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val DEBUG_TAG = "MyTask"
        val action: Int = event.actionMasked
        val eventString = event.toString()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.getX()
                startY = event.getY()
                // Set the end to prevent initial jump (like on the demo recording)
                endX = event.getX()
                endY = event.getY()
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.getX()
                endY = event.getY()
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                startX = 0f
                startY = 0f
                endX = 0f
                endY = 0f
                invalidate()
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(DEBUG_TAG, "Action was CANCEL")
                return true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d(DEBUG_TAG, "Outside bounds of current screen element")
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }


}