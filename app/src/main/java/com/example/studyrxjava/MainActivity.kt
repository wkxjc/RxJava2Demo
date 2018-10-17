package com.example.studyrxjava

import android.os.Bundle
import android.util.Log
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * Description:
 * StudyRxJava2
 * 
 * @author  Alpinist Wang
 * Company: Mobile CPX
 * Date:    2018/10/17
 */
class MainActivity : RxAppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //创建一个被观察者，间隔1秒发射数据
        val observable = createObservable()
        //创建一个观察者，接收数据
        val observer = createObserver()
        //建立订阅
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer)
    }


    private fun createObservable(): Observable<Long> {
        return Observable.interval(0, 1, TimeUnit.SECONDS)

    }

    private fun createObserver(): Observer<Long> {
        return object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                text.append("subscribe\n")
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(data: Long) {
                text.append("$data\n")
                Log.d(TAG, "$data")
            }

            override fun onComplete() {
                text.append("complete.")
                Log.d(TAG, "onComplete")
            }

            override fun onError(e: Throwable) {
                text.append(e.message)
                Log.d(TAG, "onError")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}