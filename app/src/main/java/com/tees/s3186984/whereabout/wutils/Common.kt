package com.tees.s3186984.whereabout.wutils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * PossibleRequestState represents the various states of a network like request within whereabout app.
 *
 * This class is used to model the different outcomes of a request, such as a network request or any
 * other asynchronous operation. The states represent the possible progress or result of the request.
 *
 * The class is designed to handle:
 * - **Idle**: The default state before a request is initiated.
 * - **ScreenLoading**: Indicates that data for the entire screen is being loaded.
 * - **ComponentLoading**: Indicates that a particular component or part of the screen is being loaded.
 * - **Success**: The request was successful but without returning any data.
 * - **SuccessWithData**: The request was successful and returned some data.
 * - **Failure**: The request failed with a message describing the failure.
 *
 * The class is generic and can hold any data type as a result in **SuccessWithData** (e.g., a list of items or an object), and **Failure** can optionally contain an error message.
 *
 * When to use:
 * - Use this class when you need to represent a request state, such as network calls, loading screens, or other asynchronous operations.
 * - The class allows you to easily track, display, and handle various states of the request lifecycle.
 */
sealed class PossibleRequestState<out T> {
    object Idle : PossibleRequestState<Nothing>()                                       // Default state before a network request
    object ScreenLoading : PossibleRequestState<Nothing>()                             // Indicate a loading/requesting state for screen data
    object ComponentLoading: PossibleRequestState<Nothing>()                          // Indicate a loading/requesting state for component data
    object Success: PossibleRequestState<Nothing>()                                  // Request was successful but without data
    data class SuccessWithData<T>(val data: T) : PossibleRequestState<T>()          // Request was successful with data
    data class Failure(val message: String) : PossibleRequestState<Nothing>()      // Request failed with a possible message
}


/**
 * ViewModelWithContextFactory is a custom implementation of ViewModelProvider.Factory
 * that is used to create ViewModels that require a Context parameter in their constructor.
 *
 * Usage:
 * This factory is designed for cases where a ViewModel needs to be instantiated with a
 * Context, such as when the ViewModel interacts with context-dependent APIs like SharedPreferences,
 * Database, or retrieving system resources (e.g., accessing the system services).
 *
 * When to use:
 * Use this factory when you have a ViewModel that needs access to the Context. For example, if
 * your ViewModel interacts with LocalStorage, prefers using system services, or requires a
 * context for any other initialization.
 *
 * Example:
 *
 * val viewModel: MyViewModel = viewModel(
 *     factory = ViewModelWithContextFactory(context)
 * )
 *
 * The factory will provide the required Context when creating the ViewModel.
 */
class ViewModelWithContextFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java).newInstance(context)
    }
}

