package com.xalles.gameoflife

import android.view.View

class GOLCell (var id: Int, var isAlive: Boolean, var cell: View)
{
    var neighbors = mutableListOf<GOLCell>()

    fun copyNeighbors(nbs: MutableList<GOLCell>)
    {
        for (n in nbs) {
            var nb = GOLCell(n.id, n.isAlive, n.cell)
            neighbors.add(nb)
        }
    }

//    fun getNumNeighbors () : Int
//    {
//        var numNeighbors = 0
//        for (n in neighbors)
//        {
//            if (n != null)
//            {
//                numNeighbors++
//            }
//        }
//
//        return numNeighbors
//    }

}
