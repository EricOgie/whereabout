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
import com.tees.s3186984.whereabout.componets.FormContainer
import com.tees.s3186984.whereabout.componets.WTextField
import com.tees.s3186984.whereabout.ui.theme.WBlack
import com.tees.s3186984.whereabout.ui.theme.WLightGray
import com.tees.s3186984.whereabout.ui.theme.WhereaboutTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tees.s3186984.whereabout.componets.SubmitButton
import com.tees.s3186984.whereabout.navigation.Screens
import com.tees.s3186984.whereabout.viewmodel.SignUpViewModel
import com.tees.s3186984.whereabout.wutils.AGREE
import com.tees.s3186984.whereabout.wutils.AGREE_MESSAGE
import com.tees.s3186984.whereabout.wutils.EMAIL
import com.tees.s3186984.whereabout.wutils.NAME
import com.tees.s3186984.whereabout.wutils.PASSWORD
import com.tees.s3186984.whereabout.wutils.REGISTER
import com.tees.s3186984.whereabout.wutils.SIGNUP_GREETING



@Composable
fun SignUpScreen(navController: NavController, signUpVM : SignUpViewModel){

    Surface(modifier = Modifier.fillMaxSize(), color = WLightGray){
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(80.dp))

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
                Text(text = SIGNUP_GREETING)

                Spacer(modifier = Modifier.height(10.dp))

            //---------- FullName TextField -----------------//
                WTextField(
                    state =  signUpVM.fullNameState,
                    label = NAME,
                    leadingIcon = Icons.Rounded.Person,
                    keyboardType = KeyboardType.Text

                ) { finalValue -> signUpVM.fullNameState.value  = finalValue}

            // ---------- Email TextField ------------ //
                WTextField(
                    state = signUpVM.emailState,
                    label = EMAIL,
                    leadingIcon = Icons.Rounded.Email,
                    keyboardType = KeyboardType.Email
                ) {finalValue ->  signUpVM.emailState.value= finalValue}

            // ----------Password TextField ------------ //
                WTextField(
                    state = signUpVM.passwordState,
                    label = PASSWORD,
                    leadingIcon = Icons.Rounded.Lock,
                    keyboardType = KeyboardType.Password
                ) { finalValue -> signUpVM.passwordState.value = finalValue }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = AGREE_MESSAGE, fontSize = 13.sp,)

                SubmitButton(text = AGREE) {
                    navController.navigate(Screens.SubmitSignUp.name)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview
@Composable
fun SignUpPreview() {
    WhereaboutTheme {

        SignUpScreen(
            rememberNavController(),
            SignUpViewModel()
        )
    }
}