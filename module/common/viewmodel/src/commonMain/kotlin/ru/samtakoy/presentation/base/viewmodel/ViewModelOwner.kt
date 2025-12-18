package ru.samtakoy.presentation.base.viewmodel

interface ViewModelOwner {
    fun getViewModel(): AbstractViewModel
    fun onObserveViewModel() = Unit
}