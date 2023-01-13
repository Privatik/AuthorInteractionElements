package com.io.authorinteractionelements

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

sealed class Screen(
    @StringRes val label: Int,
    val route: String,
){
    object Survey: Screen(R.string.survey_label,"/Survey")
    object MatchBetweenTwoColumn: Screen(R.string.match_between_two_column_label,"/MatchBetweenTwoColumn")
    object PutSkipItemPerEdit: Screen(R.string.put_skip_item_per_edit_label,"/PutSkipItemPerEdit")
    object PutSkipItemPerDrag: Screen(R.string.put_skip_item_per_drag_label,"/PutSkipItemPerDrag")
    object Stars: Screen(R.string.evaluate_in_stars,"/Stars")

    companion object{
        fun values(): List<Screen> = listOf(
            Survey,
            MatchBetweenTwoColumn,
            PutSkipItemPerEdit,
            PutSkipItemPerDrag,
            Stars,
        )

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

private fun String?.parseScreen(): Screen? =
    when (this){
        Screen.Survey.route -> Screen.Survey
        Screen.MatchBetweenTwoColumn.route -> Screen.MatchBetweenTwoColumn
        Screen.PutSkipItemPerEdit.route -> Screen.PutSkipItemPerEdit
        Screen.PutSkipItemPerDrag.route -> Screen.PutSkipItemPerDrag
        Screen.Stars.route -> Screen.Stars
        else -> null
    }