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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tees.s3186984.whereabout.componets.BackButton
import com.tees.s3186984.whereabout.componets.FormContainer
import com.tees.s3186984.whereabout.componets.WTextField
import com.tees.s3186984.whereabout.ui.theme.WBlack
import com.tees.s3186984.whereabout.ui.theme.WLightGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.runtime.remember

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.componets.SubmitButton
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.viewmodel.SignUpViewModel

import com.tees.s3186984.whereabout.wutils.ANSWER
import com.tees.s3186984.whereabout.wutils.PASSWORD_RECOVERY_INFO
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
            BackButton { navController.navigate(Screens.SignUp.name) }

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = REGISTER,
                color = WBlack,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            FormContainer {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = SIGNUP_NUDGE)

            // ---------- Password Recovery Question TextField ------------- //
                WTextField(
                    state =  signUpVM.questionState,
                    label = SECURITY_QUESTION,
                    maxLine = 3,
                    leadingIcon = null,
                    keyboardType = KeyboardType.Text

                ) { finalValue -> signUpVM.questionState.value  = finalValue}

            // ---------- Password Recovery Question TextField ------------- //
                WTextField(
                    state = signUpVM.answerState,
                    label = ANSWER,
                    maxLine = 3,
                    leadingIcon = null,
                    keyboardType = KeyboardType.Email
                ) {finalValue ->  signUpVM.answerState.value = finalValue }


                Spacer(modifier = Modifier.height(10.dp))

                Text(text = PASSWORD_RECOVERY_INFO, fontSize = 13.sp)

                SubmitButton(text = REGISTER) {
                    if(signUpVM.isValidFormData()){
                        signUpVM.signUp()
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}



@Preview
@Composable
fun SubmitSignUpScreenPreview() {
    WhereaboutTheme {
        SubmitSignUpScreen(
            rememberNavController(),
            SignUpViewModel()
        )
    }
}