package ru.samtakoy.core.presentation.favorites.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.composeContent
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModelFactory
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModelImpl
import javax.inject.Inject

class FavoritesFragment : Fragment(), ViewModelOwner {

    @Inject
    internal lateinit var viewModelFactory: FavoritesViewModelFactory.Factory
    private val viewModel: FavoritesViewModelImpl by viewModels {
        viewModelFactory.create()
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private val navigation: FavoritesViewModel.Navigation = FavoritesNavigationImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation.onAttach(context as RouterHolder)
    }

    override fun onDetach() {
        navigation.onDetach()
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Di.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeContent {
            FavoritesScreen(
                viewModel = viewModel,
                onNavigationAction = navigation::onAction,
                modifier = Modifier.fillMaxSize()
            )
        }
    }



    companion object {
        fun newFragment(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}