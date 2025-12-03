package ru.samtakoy.oldlegacy.features.views.presentation.history

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
import ru.samtakoy.oldlegacy.core.presentation.RouterHolder
import ru.samtakoy.oldlegacy.features.views.presentation.history.vm.ViewsHistoryViewModel
import ru.samtakoy.oldlegacy.features.views.presentation.history.vm.ViewsHistoryViewModelImpl
import ru.samtakoy.oldlegacy.core.oldutils.composeContent
import ru.samtakoy.presentation.base.viewmodel.AbstractViewModel
import ru.samtakoy.presentation.base.viewmodel.ViewModelOwner
import ru.samtakoy.presentation.core.design_system.base.theme.MyTheme

class ViewsHistoryFragment : Fragment(), ViewModelOwner {

    private val viewModel: ViewsHistoryViewModel by viewModel<ViewsHistoryViewModelImpl>()
    override fun getViewModel(): AbstractViewModel = viewModel

    private val navigation: ViewsHistoryViewModel.Navigation = ViewsHistoryNavigationImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigation.onAttach(context as RouterHolder)
    }

    override fun onDetach() {
        navigation.onDetach()
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return composeContent(R.id.compose_root) {
            MyTheme {
                ViewsHistoryScreen(
                    viewModel = viewModel,
                    onNavigation = navigation::onAction,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}