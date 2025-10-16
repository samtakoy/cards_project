package ru.samtakoy.core.presentation.favorites.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.samtakoy.R
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModel
import ru.samtakoy.core.presentation.favorites.onboarding.vm.FavoritesViewModelImpl
import ru.samtakoy.presentation.base.composeContent
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner

class FavoritesFragment : Fragment(), ViewModelOwner {

    private val viewModel: FavoritesViewModel by viewModel<FavoritesViewModelImpl>()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeContent(R.id.compose_root) {
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