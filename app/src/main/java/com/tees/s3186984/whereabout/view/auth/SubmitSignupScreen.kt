package com.tees.s3186984.whereabout.view.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.componets.WBackButton
import com.tees.s3186984.whereabout.componets.WFormContainer
import com.tees.s3186984.whereabout.componets.WTextField
import com.tees.s3186984.whereabout.ui.theme.WBlack
import com.tees.s3186984.whereabout.ui.theme.WLightGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.content.Context
import com.tees.s3186984.whereabout.componets.WSubmitButton
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.viewmodel.SignUpViewModel

import com.tees.s3186984.whereabout.wutils.ANSWER
import com.tees.s3186984.whereabout.wutils.PASSWORD_RECOVERY_INFO
import com.tees.s3186984.whereabout.wutils.PHRASE_ERROR_MSG
import com.tees.s3186984.whereabout.wutils.REGISTER
import com.tees.s3186984.whereabout.wutils.SECURITY_QUESTION
import com.tees.s3186984.whereabout.wutils.SIGNUP_NUDGE


@Composable
fun SubmitSignUpScreen(navController: NavController, signUpVM : SignUpViewModel){

    Surface(modifier = Modifier.fillMaxSize(), color = WLightGray){
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxSize()
        ) {
            WBackButton { navController.navigate(Screens.SignUp.name) }

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = REGISTER,
                color = WBlack,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            WFormContainer {
            // ---------- Display Error Alert if signup request error ------------- //
                if (signUpVM.displaySignUpRequestError.value){
                    val message by signUpVM.sinUpRequestMessage
                    Text(text = message, color = Color.Red, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

            // ---------- Display Error Alert if form validation error ------------- //
                if (signUpVM.displayScreen2ErrorState.value){
                    Text(text = PHRASE_ERROR_MSG, color = Color.Red, fontSize = 13.sp)
                }else{
                    Text(text = SIGNUP_NUDGE)
                }


            // ---------- Password Recovery Question TextField ------------- //
                WTextField(
                    state =  signUpVM.questionState,
                    label = SECURITY_QUESTION,
                    maxLine = 3,
                    leadingIcon = null,
                    keyboardType = KeyboardType.Text

                ) { finalValue -> signUpVM.questionState.value  = finalValue}

            // ---------- Password Recovery Answer TextField ------------- //
                WTextField(
                    state = signUpVM.answerState,
                    label = ANSWER,
                    maxLine = 3,
                    leadingIcon = null,
                    keyboardType = KeyboardType.Email
                ) {finalValue ->  signUpVM.answerState.value = finalValue }


                Spacer(modifier = Modifier.height(10.dp))

                Text(text = PASSWORD_RECOVERY_INFO, fontSize = 13.sp)

                WSubmitButton(text = REGISTER, loadingState = signUpVM.loadingState) {
                    if(signUpVM.isValidFormData()){
                        signUpVM.signUp()
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

// ------------------------   Observe result state --------------------------//

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        signUpVM.result.observe(lifecycleOwner, Observer { result ->
            result?.let { (success, _) ->
                if (success) {
                    navController.navigate(Screens.Home.name) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }else {
                    signUpVM.displaySignUpRequestError.value = true
                }
            }
        })
    }
}


@Preview
@Composable
fun SubmitSignUpScreenPreview() {
    WhereaboutTheme {
    }
}