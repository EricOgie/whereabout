package com.tees.s3186984.whereabout.view.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tees.s3186984.whereabout.componets.FormContainer
import com.tees.s3186984.whereabout.componets.WTextField
import com.tees.s3186984.whereabout.ui.theme.WBlack
import com.tees.s3186984.whereabout.ui.theme.WLightGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.componets.ScreenAddOn
import com.tees.s3186984.whereabout.componets.SubmitButton
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.viewmodel.LogInViewModel
import com.tees.s3186984.whereabout.wutils.DONT_HAVE

import com.tees.s3186984.whereabout.wutils.EMAIL
import com.tees.s3186984.whereabout.wutils.EMAIL_ERROR_MSG
import com.tees.s3186984.whereabout.wutils.FORGET_PASS
import com.tees.s3186984.whereabout.wutils.LOGIN
import com.tees.s3186984.whereabout.wutils.PASSWORD
import com.tees.s3186984.whereabout.wutils.PASSWORD_ERROR_MSG
import com.tees.s3186984.whereabout.wutils.REGISTER


@Composable
fun LogInScreen(navController: NavController, logInVM: LogInViewModel){

    Surface(modifier = Modifier.fillMaxSize(), color = WLightGray){
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(80.dp))

        // ---------- Form Title ------------ //
            Text(
                text = LOGIN,
                color = WBlack,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(50.dp))

        // ---------- Form Container ------------ //
            FormContainer {
             //------- Display LogIn Request Error if any-------//
                if (logInVM.displayLogInRequestError.value){
                    val message by logInVM.logInRequestMessage
                    Text(text = message, color = Color.Red, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

            //------- Display Form Data Error if any-------//
                if (logInVM.displayFormError.value){
                    FormDataErrorAlert()
                }

                // ---------- Email TextField ------------ //
                WTextField(
                    state = logInVM.emailState,
                    label = EMAIL,
                    leadingIcon = Icons.Rounded.Email,
                    keyboardType = KeyboardType.Email
                ) {finalValue ->  logInVM.emailState.value= finalValue}

                // ----------Password TextField ------------ //
                WTextField(
                    state = logInVM.passwordState,
                    label = PASSWORD,
                    leadingIcon = Icons.Rounded.Lock,
                    keyboardType = KeyboardType.Password
                ) { finalValue -> logInVM.passwordState.value = finalValue }


                Text(
                    modifier = Modifier.clickable{
                        navController.navigate(Screens.PasswordResetRequest.name)
                    },
                    text = FORGET_PASS,
                    fontSize = 15.sp,
                    color = Color.Blue
                )

                Spacer(modifier = Modifier.height(20.dp))
                SubmitButton(text = LOGIN, loadingState = logInVM.isLoading) {
                    if (logInVM.isValidEmailPassword()){
                        logInVM.logIn()
                    }

                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ScreenAddOn(navController, DONT_HAVE, REGISTER, Screens.SignUp.name)

                    Spacer(modifier = Modifier.height(20.dp))
                }

            }
        }
    }


// ------------------------   Observe LogIn Request result state --------------------------//
    LaunchedEffect(logInVM.result){
        logInVM.result.collect { (success, _) ->
            if (success) {
                navController.navigate(Screens.Home.name) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                logInVM.displayLogInRequestError.value = true
            }
        }


    }




}


@Composable
fun FormDataErrorAlert(){
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = EMAIL_ERROR_MSG, color = Color.Red, fontSize = 13.sp)
        Text(text = PASSWORD_ERROR_MSG, color = Color.Red, fontSize = 13.sp)
    }

}

@Preview
@Composable
fun LogInPreview() {
    WhereaboutTheme {
        LogInScreen(rememberNavController(), LogInViewModel())
    }
}