package ru.samtakoy.core.presentation.log

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.samtakoy.core.presentation.SingleFragmentActivity
import ru.samtakoy.core.presentation.log.LogFragment.Companion.newFragment

class LogActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return newFragment()
    }

    companion object {
        fun newActivityIntent(callerContext: Context): Intent {
            return Intent(callerContext, LogActivity::class.java)
        }
    }
}
