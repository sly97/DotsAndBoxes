package uk.ac.bournemouth.ap.dotsandboxes

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class RandomComputerPlayer : ComputerPlayer() {
    override fun makeMove(game: DotsAndBoxesGame) {
        val line = game.lines.filter { !it.isDrawn }.random()
        line.drawLine()
    }

}
