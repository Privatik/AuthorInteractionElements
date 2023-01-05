package com.io.authorinteractionelements

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.io.core.ui.ProjectTheme.dimens
import com.io.core.ui.Theme
import com.io.survey.SurveyQuestionModel

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
                            .fillMaxSize()
                            .padding(horizontal = dimens.horizontalPadding)
                        ,
                        questions = vm.surveyQuestions,
                        onAnsweredOnQuestion = { surveyQuestionModel, surveyAnswerModel ->
                            val index = vm.surveyQuestions.indexOfFirst { it.id == surveyQuestionModel.id }
                            vm.surveyQuestions[index] = surveyQuestionModel.copy(
                                youAnswerId = surveyAnswerModel.id,
                                isAnswered = true,
                            )
                        }
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