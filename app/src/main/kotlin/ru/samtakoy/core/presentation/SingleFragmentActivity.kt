package ru.samtakoy.core.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.samtakoy.R

abstract class SingleFragmentActivity : AppCompatActivity() {
    protected abstract fun createFragment(): Fragment

    @get:LayoutRes
    protected val layoutResId: Int
        get() = R.layout.activity_single_fragment

    protected val fragmentContainerId: Int
        get() = R.id.fragment_cont

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(this.layoutResId)

        val fm = getSupportFragmentManager()
        var f = fm.findFragmentById(this.fragmentContainerId)
        if (f == null) {
            f = createFragment()
            fm.beginTransaction()
                .add(this.fragmentContainerId, f)
                .commit()
        }
    }
}
