package com.tees.s3186984.whereabout.componets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
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

/**
 * A custom text input field with a leading icon.
 *
 * This composable provides a text field with various customization options, including
 * theme color, keyboard type, and actions when the user presses the IME action button.
 * It also tracks focus state to adjust the label's font size based on whether the field
 * is focused.
 *
 * @param modifier Optional modifier for styling the text field.
 * @param themeColor An optional color used for the text and container. If null, defaults
 *         to black for text and dark gray for the container.
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
    themeColor: Color?,
    state: MutableState<String>,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Go,
    emitFinalValue: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val textColor = themeColor ?: Color.Black
    val themeColor = themeColor ?: Color.DarkGray

    TextField(
        value = state.value,
        onValueChange = { newValue ->
            state.value = newValue.trim()
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .onFocusChanged { isFocused = it.isFocused },
        label = { Text(text = label, color = textColor, fontSize = if (isFocused) 12.sp else 16.sp) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Money",
                tint = themeColor
            )
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 20.sp, color = textColor),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onGo = {
            keyboardController?.hide()
            emitFinalValue(state.value)
        }),
        shape = RoundedCornerShape(6.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = themeColor,
            focusedContainerColor = themeColor.copy(0.3f),
            unfocusedContainerColor = themeColor.copy(0.1f)
        ),
    )
}

