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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.samtakoy.R
import ru.samtakoy.core.presentation.RouterHolder
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModel
import ru.samtakoy.core.presentation.favorites.qpacks_with_favs.vm.QPackSelectionViewModelImpl
import ru.samtakoy.presentation.base.composeContent
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

class QPackSelectionFragment : Fragment(), ViewModelOwner {

    private val viewModel: QPackSelectionViewModel by viewModel<QPackSelectionViewModelImpl>()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return composeContent(R.id.compose_root) {
            MyTheme {
                QPackSelectionScreen(
                    viewModel = viewModel,
                    onNavigationAction = navigation::onAction,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    companion object {
        fun buildBundle(): Bundle {
            return bundleOf()
        }
    }
}