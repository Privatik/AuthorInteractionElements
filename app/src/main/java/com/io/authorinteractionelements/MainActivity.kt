package com.io.authorinteractionelements

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.io.core.ui.Theme

class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
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
                        questions = vm.surveyQuestions,
                        answerOnQuestion = vm::updateSurveyQuestion
                    )
                    Screen.MatchBetweenTwoColumn -> MatchBetweenTwoColumnExampleScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        orderedFirstColumn = vm.firstOrderedColumn,
                        orderedSecondColumn = vm.secondOrderedColumn,
                        foundMatchItems = vm::foundMatchItems
                    )
                    Screen.PutSkipItem -> PutSkipItemExampleScreen(
                        modifier = Modifier
                            .fillMaxSize()
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