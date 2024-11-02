package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.clickable
import com.tees.s3186984.whereabout.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.tees.s3186984.whereabout.ui.theme.WBlack
import com.tees.s3186984.whereabout.ui.theme.WLightAsh
import com.tees.s3186984.whereabout.ui.theme.WShadyAsh

/**
 * A custom text input field with a leading icon.
 *
 * This composable provides a text field with various customization options, including
 * theme color, keyboard type, and actions when the user presses the IME action button.
 * It also tracks focus state to adjust the label's font size based on whether the field
 * is focused.
 *
 * @param modifier Optional modifier for styling the text field.

 * @param state A mutable state holder for the current value of the text field.
 * @param label The label to display above the text field.
 * @param keyboardType The type of keyboard to display (default is [KeyboardType.Number]).
 * @param imeAction The action button to display on the keyboard (default is [ImeAction.Go]).
 * @param emitFinalValue A lambda function that is called with the final value when the
 *         IME action is triggered.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WTextField(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    label: String,
    maxLine : Int = 1,
    leadingIcon:  ImageVector?,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Go,
    emitFinalValue: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.value,
        maxLines = maxLine,
        onValueChange = { newValue ->
            state.value = newValue
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        label = {
            Text(
                text = label,
                color = WShadyAsh,
                fontSize = if (isFocused || state.value.isNotEmpty()) 11.sp else 16.sp
            )
        },

        leadingIcon = if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null, tint = WBlack) }
        } else null,

        trailingIcon = if (keyboardType == KeyboardType.Password) {
            {
                Icon(
                    painter = painterResource(id = if (passwordVisible) R.drawable.show else R.drawable.hide),
                    contentDescription = null,
                    tint = WBlack,
                    modifier = modifier
                        .size(25.dp)
                        .clickable {
                            passwordVisible = !passwordVisible
                        }
                )
            }
        } else null,

        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = WBlack, lineHeight = 5.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onGo = {
            focusManager.clearFocus()
            keyboardController?.hide()
            emitFinalValue(state.value.trim())
        }),
        shape = RoundedCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = WBlack,
            focusedContainerColor = WLightAsh,
            unfocusedContainerColor = WLightAsh
        ),
        visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}


