package uk.ac.bournemouth.ap.dotsandboxes

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class RandomComputerPlayer : ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {

        //If 3 lines are drawn, it completes the box
        val almostFinishedBoxes = game.boxes.filter { box -> box.boundingLines.count { it.isDrawn } == 3 }
        if(almostFinishedBoxes.count() > 0){
            almostFinishedBoxes.random().boundingLines.filter { !it.isDrawn }.forEach { it.drawLine() }
            return
        }

        //If 0 lines are drawn, it draws a line randomly
        val newBoxes = game.boxes.filter { box -> box.boundingLines.count { it.isDrawn } == 0 }
        if(newBoxes.count() > 0){
            val randomLine = newBoxes.random().boundingLines.filter { !it.isDrawn }.random()
            randomLine.drawLine()
            return
        }

        //If 1 line is drawn, it draws another line in the same box
        val oneLineBoxes = game.boxes.filter { box -> box.boundingLines.count { it.isDrawn } == 1 }
        if(oneLineBoxes.count() > 0){
            val randomLine = oneLineBoxes.random().boundingLines.filter { !it.isDrawn }.random()
            randomLine.drawLine()
            return
        }

        val line = game.lines.filter { !it.isDrawn }.random()
        line.drawLine()
        return
    }

}
