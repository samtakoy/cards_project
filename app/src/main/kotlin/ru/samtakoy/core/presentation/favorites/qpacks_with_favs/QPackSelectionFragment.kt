package ru.samtakoy.core.presentation.favorites.qpacks_with_favs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.samtakoy.core.app.di.Di
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.base.composeContent
import ru.samtakoy.core.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.core.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModelFactory
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModelImpl
import javax.inject.Inject

class QPackSelectionFragment : Fragment(), ViewModelOwner {

    @Inject
    internal lateinit var viewModelFactory : QPackSelectionViewModelFactory
    private val viewModel by viewModels<QPackSelectionViewModelImpl> {
        viewModelFactory
    }
    override fun getViewModel(): AbstractViewModel = viewModel

    private val navigation = QPacksWithFavsNavigationImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation.onAttach(context as RouterHolder)
    }

    override fun onDetach() {
        navigation.onDetach()
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Di.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeContent {
            QPackSelectionScreen(
                viewModel = viewModel,
                onNavigationAction = navigation::onAction,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    companion object {
        fun buildBundle(): Bundle {
            return bundleOf()
        }
    }
}