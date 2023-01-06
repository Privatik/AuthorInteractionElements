package com.io.authorinteractionelements

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

sealed class Screen(val route: String){
    object Survey: Screen(Route.SURVEY.route)
    object MatchBetweenTwoColumn: Screen(Route.MATCH_BETWEEN_TWO_COLUMN.route)

    companion object{
        val Saver = Saver<MutableState<Screen?>, String>(
            save = {
                it.value?.route ?: ""
            },
            restore = {
                mutableStateOf<Screen?>(it.parseScreen())
            }
        )
    }
}


private enum class Route(val route: String){
    SURVEY("/survey"),
    MATCH_BETWEEN_TWO_COLUMN("/MatchBetweenTwoColumn")
}

private fun String?.parseScreen(): Screen? =
    when (this){
        Route.SURVEY.route -> Screen.Survey
        Route.MATCH_BETWEEN_TWO_COLUMN.route -> Screen.MatchBetweenTwoColumn
        else -> null
    }