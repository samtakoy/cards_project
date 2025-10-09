package ru.samtakoy.core.presentation.online_import

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.samtakoy.R
import ru.samtakoy.core.data.network.ServiceGenerator.api
import ru.samtakoy.core.data.network.pojo.RemoteFilesInfo

class OnlineImportFragment : Fragment() {
    private var mAdapter: RecyclerAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private val mDisposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_remote_update, container, false)


        mRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        initRecycler()

        return view
    }

    override fun onDestroy() {
        mDisposables.clear()

        super.onDestroy()
    }

    private fun initRecycler() {
        mAdapter = RecyclerAdapter()
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(getContext()))
        mRecyclerView!!.setAdapter(mAdapter)

        val observer: Observer<RemoteFilesInfo> =
            object : Observer<RemoteFilesInfo> {
                override fun onSubscribe(d: Disposable) {
                    mDisposables.add(d)
                }

                override fun onNext(remoteFilesInfo: RemoteFilesInfo) {
                    mAdapter!!.setFiles(remoteFilesInfo.files!!.toMutableList())
                }

                override fun onError(e: Throwable) {
                    showMessage(e.message)
                }

                override fun onComplete() {
                    showMessage("ok!")
                }
            }

        this.remoteFileObservable.subscribe(
            observer
        )
    }

    private fun showMessage(text: String?) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show()
    }

    private val remoteFileObservable: Observable<RemoteFilesInfo>
        get() = api.filesInfo
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    companion object {
        fun createInstance(): OnlineImportFragment {
            return OnlineImportFragment()
        }
    }
}
