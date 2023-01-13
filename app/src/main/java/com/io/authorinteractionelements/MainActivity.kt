package com.io.authorinteractionelements

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.io.authorinteractionelements.screens.*
import com.io.core.ui.ProjectTheme.palette
import com.io.core.ui.Theme

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(palette.backgroundPrimary)
                ) {
                    var currentScreen by rememberSaveable(saver = Screen.Saver) {
                        mutableStateOf<Screen?>(null)
                    }

                    BackHandler(currentScreen != null) {
                        currentScreen = null
                    }

                    when (currentScreen){
                        Screen.Survey -> SurveyExampleScreen(
                            modifier = Modifier
                                .fillMaxSize(),
                            tasks = vm.surveyTasks,
                            answerOnQuestion = vm::updateSurveyQuestion
                        )
                        Screen.MatchBetweenTwoColumn -> MatchBetweenTwoColumnExampleScreen(
                            modifier = Modifier
                                .fillMaxSize(),
                            pairsOrderedColumn = listOf(
                                PairOrderedColumn(
                                    orderedFirstColumn = vm.firstOrderedColumn,
                                    orderedSecondColumn = vm.secondOrderedColumn
                                )
                            ),
                            foundMatchItems = vm::foundMatchItems
                        )
                        Screen.PutSkipItemPerEdit -> PutSkipItemPerEditExampleScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            interactionTextItems = vm.interactionItemsForEdit,
                            addIndexAsAnswered = vm::addIndexAsAnsweredInEditTask
                        )
                        Screen.PutSkipItemPerDrag -> PutSkipItemPerDragExampleScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            interactionTextItems = vm.interactionItemsForDrag,
                            addIndexAsAnswered = vm::addIndexAsAnsweredInDragTask
                        )
                        Screen.Stars -> StarsScreen(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        else -> ButtonsScreen(
                            modifier = Modifier.fillMaxSize(),
                            changeScreen = { currentScreen = it }
                        )
                    }
                }
            }
        }
    }
}