package uk.ac.bournemouth.ap.dotsandboxes

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class RandomComputerPlayer : ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {

        val almostFinisedBoxes = game.boxes.filter { box -> box.boundingLines.count { it.isDrawn } == 3 }
        if(almostFinisedBoxes.count() > 0){
            almostFinisedBoxes.random().boundingLines.filter { !it.isDrawn }.forEach { it.drawLine() }
            return
        }

        val newBoxes = game.boxes.filter { box -> box.boundingLines.count { it.isDrawn } == 0 }
        if(newBoxes.count() > 0){
            val randomLine = newBoxes.random().boundingLines.filter { !it.isDrawn }.random()
            randomLine.drawLine()
            return
        }

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
