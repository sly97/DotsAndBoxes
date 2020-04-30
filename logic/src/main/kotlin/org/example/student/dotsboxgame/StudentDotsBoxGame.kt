package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.*
import java.lang.Exception

class StudentDotsBoxGame(val columns : Int, val rows : Int, players : List<Player>) : AbstractDotsAndBoxesGame() {
    override val players: List<Player> = players.toList()

    override var currentPlayer: Player = players[0]
    // get()= TODO("Determine the current player, like keeping" + "the index into the players list")

    // NOTE: you may want to me more specific in the box type if you use that type in your class
    override var boxes: Matrix<StudentBox> = MutableMatrix(columns,rows, ::StudentBox)
    //TODO("Create a matrix initialized with your own box type")

    override var lines: SparseMatrix<StudentLine> = MutableSparseMatrix(columns + 1,rows * 2 + 1, ::StudentLine){
            x, y -> y % 2 == 1 || x < columns
        } // TODO("Create a matrix initialized with your own line type")

    override var isFinished: Boolean = false
        //get() = TODO("Provide this getter. Note you can make it a var to do so")

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false
        //TODO("Provide this getter. Note you can make it a var to do so")


        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                if(lineX == 0 && lineY % 2 == 1){
                    // Line is vertical origin
                    val boxA = null
                    val boxB = boxes[lineX, lineY / 2]
                    return Pair(boxA,boxB)
                }else if(lineY == 0){
                    // Line is horizontal origin
                    val boxA = null
                    val boxB = boxes[lineX, lineY]
                    return Pair(boxA,boxB)
                }else if (lineX == columns) {
                    // Line is vertical end
                    val boxA = boxes[lineX-1, lineY / 2]
                    val boxB = null
                    return Pair(boxA,boxB)
                }else if (lineY == rows * 2) {
                    // Line is horizontal end
                    val boxA = boxes[lineX, lineY / 2 - 1]
                    val boxB = null
                    return Pair(boxA,boxB)
                }else if(lineY % 2 == 1){
                    // Line is vertical
                    val boxA = boxes[lineX-1, lineY / 2]
                    val boxB = boxes[lineX, lineY / 2]
                    return Pair(boxA,boxB)
                }else{
                    // Line is horizontal
                    val boxA = boxes[lineX, lineY - (lineY / 2 + 1)]
                    val boxB = boxes[lineX, lineY - (lineY / 2)]
                    return Pair(boxA,boxB)
                }
                //TODO("You need to look up the correct boxes for this to work")
            }

        override fun drawLine(){
            var extraMove = false
            if(!this.isDrawn){
                println(""+lineX+","+lineY)
                this.isDrawn = true
                for (b in adjacentBoxes.toList()){
                    if (b != null) {
                        val boxOwned = b.boundingLines.all { it.isDrawn }
                        if(boxOwned){
                            //boxes[b.boxX,b.boxY].owningPlayer = null
                            //this.adjacentBoxes.first.owningPlayer = currentPlayer
                            b.owningPlayer = currentPlayer
                            extraMove = true
                        }
                    }
                }
                fireGameChange()


                if(!extraMove) {
                    if (players.indexOf(currentPlayer) == players.size - 1) {
                        currentPlayer = players[0]
                    } else {
                        currentPlayer = players[players.indexOf(currentPlayer) + 1]
                    }
                    playComputerTurns()
                }else{
                    val allScores  = ArrayList<Pair<Player,Int>>(players.size)
                    var ownedBoxes = 0
                    for (b in boxes){
                        if(b.owningPlayer != null){
                            allScores.add(Pair(b.owningPlayer as Player, getScores()[players.indexOf(currentPlayer)]))
                            ownedBoxes++
                        }
                    }
                    if(ownedBoxes == boxes.count()){
                        isFinished = true
                        println(allScores.toString())
                        println(getScores().toList().toString())
                        fireGameOver(allScores)
                    }
                }
            }else{
                throw Exception("Line is already drawn")
            }

            //TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null
        // TODO("Provide this getter. Note you can make it a var to do so")

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() {
                val boxLines : ArrayList<DotsAndBoxesGame.Line> = ArrayList(4)
                boxLines.add(lines[boxX, boxY*2]) // Top Line
                boxLines.add(lines[boxX, boxY*2+1]) // Left Line
                boxLines.add(lines[boxX, (boxY+1)*2]) // Bottom Line
                boxLines.add(lines[boxX+1, boxY*2+1]) // Right Line
                return boxLines
            }

                //TODO("Look up the correct lines from the game outer class")

    }
}