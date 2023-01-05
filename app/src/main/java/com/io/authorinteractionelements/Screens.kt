package com.io.authorinteractionelements

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

sealed class Screen(val route: String){
    object Survey: Screen(Route.SURVEY.route)

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
    SURVEY("/survey")
}

private fun String?.parseScreen(): Screen? =
    when (this){
        Route.SURVEY.route -> Screen.Survey
        else -> null
    }