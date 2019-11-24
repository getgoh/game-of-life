package com.xalles.gameoflife

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.GridLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var deviceWidth : Int = 0
    private var deviceHeight : Int = 0

    private var cellsPerRow : Int = 16

    private var cellWidth : Int = 0
    private var cellHeight : Int = 0

    private var cellList = mutableListOf<GOLCell>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get device's width/height
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        deviceWidth = displayMetrics.widthPixels
        deviceHeight = displayMetrics.heightPixels

        cellWidth = (deviceWidth / cellsPerRow) - 4
        cellHeight = cellWidth

        canvas.rowCount = cellsPerRow
        canvas.columnCount = cellsPerRow

        btnNext.setOnClickListener(this)

        drawCells()
        setNeighbors()
    }

    private fun setNeighbors()
    {
        for (r in 1..cellsPerRow) {
            for (c in 1..cellsPerRow) {

                val currIdx = ((cellsPerRow* (r-1)) + c)
                val currCell = cellList[currIdx-1]

                // north neighbor
                var nbIdx = currIdx - cellsPerRow
                val nNeighbor = if (nbIdx > 0) cellList[nbIdx-1] else null

                // south neighbor
                nbIdx = currIdx + cellsPerRow
                val sNeighbor = if (nbIdx <= (cellsPerRow*cellsPerRow)) cellList[nbIdx-1] else null

                // west neighbor
                nbIdx = currIdx - 1
                val wNeighbor = if (nbIdx > 0 && (nbIdx%cellsPerRow)!=0) cellList[nbIdx-1] else null

                // east neighbor
                nbIdx = currIdx + 1
                val eNeighbor = if ((nbIdx <= (cellsPerRow*cellsPerRow)) && ((nbIdx%cellsPerRow)!=1)) cellList[nbIdx-1] else null

                // northwest neighbor
                nbIdx = currIdx - cellsPerRow - 1
                val nwNeighbor = if (nbIdx>0 && (nbIdx%cellsPerRow)!=0) cellList[nbIdx-1] else null

                // northeast neighbor
                nbIdx = currIdx - cellsPerRow + 1
                val neNeighbor = if (nbIdx>0 && (nbIdx%cellsPerRow)!=1) cellList[nbIdx-1] else null

                // southwest neighbor
                nbIdx = currIdx + cellsPerRow - 1
                val swNeighbor = if (nbIdx<=(cellsPerRow*cellsPerRow) && (nbIdx%cellsPerRow)!=0) cellList[nbIdx-1] else null

                // southeast neighbor
                nbIdx = currIdx + cellsPerRow + 1
                val seNeighbor = if (nbIdx<=(cellsPerRow*cellsPerRow) && (nbIdx%cellsPerRow)!=1) cellList[nbIdx-1] else null

                if (nNeighbor!=null) currCell.neighbors.add(nNeighbor)
                if (sNeighbor!=null) currCell.neighbors.add(sNeighbor)
                if (wNeighbor!=null) currCell.neighbors.add(wNeighbor)
                if (eNeighbor!=null) currCell.neighbors.add(eNeighbor)
                if (nwNeighbor!=null) currCell.neighbors.add(nwNeighbor)
                if (neNeighbor!=null) currCell.neighbors.add(neNeighbor)
                if (swNeighbor!=null) currCell.neighbors.add(swNeighbor)
                if (seNeighbor!=null) currCell.neighbors.add(seNeighbor)
            }
        }
    }

    override fun onClick(v: View?) {

        if (v == (btnNext))
        {
            // Go to next generation
            updateCells()
        }
        else {
            var currCell = cellList[v!!.id]
            if (currCell.isAlive) {
                // alive? kill!
                v?.setBackgroundColor(Color.WHITE)
            } else {
                // dead? revive!!
                v?.setBackgroundColor(Color.BLACK)
            }
            currCell.isAlive = !currCell.isAlive
            Log.d("CellClick", "Id: " + v?.id + ", neighbors: " + currCell.neighbors.count())
        }
    }

    private fun updateCells() {
        // create a list of life states
        var cellLives = mutableListOf<Boolean>()
        for (cell in 0 until cellList.size-1) {

            val c = cellList[cell]

            cellLives.add(c.isAlive)

            // check neighbors' state
            var neighborsAlive = 0
            for (n in c.neighbors) {
                if (n.isAlive) {
                    neighborsAlive++
                }
            }

            /*
            RULES
            1. Any live cell with two or three neighbors survives.
            2. Any dead cell with three live neighbors becomes a live cell.
            3. All other live cells die in the next generation. Similarly, all other dead cells stay dead.
            */
            if (c.isAlive) {
                if (neighborsAlive<2 || neighborsAlive>3) {
                    // overpopulation
                    c.cell.setBackgroundColor(Color.WHITE)
                    cellLives[cell] = false
                }
            }
            else {
                if (neighborsAlive == 3) {
                    // repopulation
                    c.cell.setBackgroundColor(Color.BLACK)
                    cellLives[cell] = true
                }
            }
        }

        for (l in 0 until cellLives.size-1) {
            cellList[l].isAlive = cellLives[l]
        }
    }

    private fun drawCells() {
        // loop for row
        for (r in 1..cellsPerRow)
        {
            // loop for each cell
            for (c in 1..cellsPerRow)
            {
                // dynamically create cell (view)
                var cell = View(this)
                // set color to gray, because gray looks dead..
                cell.setBackgroundColor(Color.WHITE)
                // some props for cell
                val param = GridLayout.LayoutParams()
                param.setMargins(2,2,2,2)
                param.width = cellWidth
                param.height = cellHeight
                cell.layoutParams = param

                // assign id to each cell, so that we can identify it later on click
                cell.id = ((cellsPerRow * (r-1)) + c) - 1
                // set click for cell
                cell.setOnClickListener(this)

                // add cell to canvas
                canvas.addView(cell)

                // create a GOLCell object for this cell
                var cellObj = GOLCell(cell.id, false, cell)
                // add GOLCell object to list
                cellList.add(cellObj)
            }
        }
    }

}
