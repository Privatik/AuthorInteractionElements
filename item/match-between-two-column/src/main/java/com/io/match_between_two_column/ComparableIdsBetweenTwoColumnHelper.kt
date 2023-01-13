package com.io.match_between_two_column

import androidx.compose.runtime.MutableState

internal class ComparableIdsBetweenTwoColumnHelper(
    private val foundMatchItems: (Long) -> Unit,
){

    fun comparable(
        currentId: MutableState<Long?>,
        otherId: MutableState<Long?>,
        notCompareWithOtherItem: () -> Unit,
    ){
        comparable(
            firstId = currentId.value,
            secondId = otherId.value,
            doIfCompare = {
                foundMatchItems(currentId.value!!)
                currentId.value = null
                otherId.value = null
            },
            doIfNotCompare = {
                notCompareWithOtherItem()
                currentId.value = null
            }
        )
    }

    private inline fun comparable(
        firstId: Long?,
        secondId: Long?,
        doIfCompare: () -> Unit,
        doIfNotCompare: () -> Unit,
    ) {
        if (firstId != null && secondId != null){
            if (firstId == secondId){
                doIfCompare()
            } else {
                doIfNotCompare()
            }
        }
    }
}