package com.example.taska.presentation.screens.main

import androidx.lifecycle.ViewModel
import com.example.taska.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(val navigator: Navigator) : ViewModel()