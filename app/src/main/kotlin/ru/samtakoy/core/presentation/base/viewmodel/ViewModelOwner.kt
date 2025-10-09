package ru.samtakoy.core.presentation.base.viewmodel

interface ViewModelOwner {
    fun getViewModel(): AbstractViewModel
    fun onObserveViewModel() = Unit
}