package uk.ac.bournemouth.ap.dotsandboxes

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class RandomComputerPlayer : ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {

        //Search for boxes with last line not drawn
        for(box in game.boxes){
            var drawnLines = 0
            for(line in box.boundingLines){
                if (line.isDrawn) drawnLines++
            }
            if (drawnLines == 3){
                box.boundingLines.filter { !it.isDrawn }.forEach { it.drawLine() }
                return
            }
        }

        val line = game.lines.filter { !it.isDrawn }.random()
        line.drawLine()
    }

}
