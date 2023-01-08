package com.io.authorinteractionelements

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

sealed class Screen(val route: String){
    object Survey: Screen(Route.SURVEY.route)
    object MatchBetweenTwoColumn: Screen(Route.MATCH_BETWEEN_TWO_COLUMN.route)
    object PutSkipItemPerEdit: Screen(Route.PUT_SKIP_ITEM_PER_EDIT.route)
    object PutSkipItemPerDrag: Screen(Route.PUT_SKIP_ITEM_PER_DRAG.route)

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
    SURVEY("/Survey"),
    MATCH_BETWEEN_TWO_COLUMN("/MatchBetweenTwoColumn"),
    PUT_SKIP_ITEM_PER_EDIT("/PutSkipItemPerEdit"),
    PUT_SKIP_ITEM_PER_DRAG("/PutSkipItemPerDrag"),
}

private fun String?.parseScreen(): Screen? =
    when (this){
        Route.SURVEY.route -> Screen.Survey
        Route.MATCH_BETWEEN_TWO_COLUMN.route -> Screen.MatchBetweenTwoColumn
        Route.PUT_SKIP_ITEM_PER_EDIT.route -> Screen.PutSkipItemPerEdit
        Route.PUT_SKIP_ITEM_PER_DRAG.route -> Screen.PutSkipItemPerDrag
        else -> null
    }