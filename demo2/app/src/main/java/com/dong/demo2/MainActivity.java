package com.dong.demo2;

import android.annotation.SuppressLint;
import android.database.Observable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

//import io.reactivex.*;

//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.core.FlowableTransformer;
//import io.reactivex.rxjava3.core.MaybeTransformer;
//import io.reactivex.rxjava3.core.ObservableTransformer;
//import io.reactivex.rxjava3.core.SingleTransformer;
//import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.core.*;


import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        rxjavaDemo();
    }

    private void rxjavaDemo() {
        //创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //调用观察者的回调
                emitter.onNext("我是");
                emitter.onNext("RxJava");
                emitter.onNext("简单示例");
                emitter.onError(new Throwable("出错了"));
                emitter.onComplete();
            }
        });

        //创建观察者
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG,"onCompleted");
            }

            //onSubscribe()方法是最先调用的
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,"subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,s);
            }
        };

        //注册，将观察者和被观察者关联，将会触发OnSubscribe.call方法
        observable.subscribe(observer);
    }

    private void rxjavaDemo2() {
//创建被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                emitter.onNext(getResponse());
            }
        });

        //创建观察者
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String mResponse) throws Exception {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                tv_return.setText(mResponse);
            }
        };
        //subscribeOn() 指定的是发送事件的线程, observeOn() 指定的是接收事件的线程.
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);

    }

    //使用okhttp访问网上提供的接口，由于是同步get请求，需要在子线程进行
    private String getResponse() {
        String url = "http://v.juhe.cn/weather/index?cityname=%E6%9D%AD%E5%B7%9E&dtype=&format=&key=7970495dbf33839562c9d496156e13cc";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response;

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return "error";
        }
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.img_view);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(this);
//        Glide.with(this) // 依赖组件，不要依赖Context，依赖具体的活动(fragment,activiyt) ;glide 会随具体的活动生命周期做活动；防止内存泄漏
//                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599147578905&di=bbbb98f646a43217af128e788c09a1e1&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw560h375%2F20180314%2F7763-fyscsmv5140841.jpg") //需要加载的地址
//                .into(mImageView); // 需要填充的view
    }

    @Override
    public void onClick(View view) {
//        showGif(view);
        showAnim(view);
//        showBitmap(view);
    }

    private void showAnim(View view) {
        String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599147578905&di=bbbb98f646a43217af128e788c09a1e1&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw560h375%2F20180314%2F7763-fyscsmv5140841.jpg";
//        Glide.with(this)
//                .load(uri)
//                .placeholder(R.drawable.default_user_red)
//                .error(R.mipmap.future_studio_launcher)
//                .crossFade()//ps：这个
//                .into(imageViewFade);

        Glide.with(this)
                .load(uri)
                .apply(new RequestOptions()
                        .placeholder(new GlidePlaceholderDrawable(getResources(), R.drawable.placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .transition(new DrawableTransitionOptions().crossFade(1000))
                .into(new DrawableImageViewTarget(mImageView) {
                    @Override
                    @SuppressLint("MissingSuperCall")
                    public void getSize(@NonNull final SizeReadyCallback cb) {
                        //由于图片比较小,加载特别快,可能看不到异常效果,这里延迟调用超类方法可以延迟开始加载图片
                        mImageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callSuperGetSize(cb);
                            }
                        }, 1000);
                    }

                    private void callSuperGetSize(SizeReadyCallback cb) {
                        super.getSize(cb);
                    }
                });
    }

    private void showGif(View view) {
//        File file = new File(Environment.getExternalStorageDirectory(), "xiayu.png");
//        Glide.with(this).asGif().load(file).into(mImageView);

//        String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599152332455&di=0ca12cde8f723bec127850cfdef7e18c&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170303%2F6e1f0bfbbcd146eb8349b4b0ba387ec6.gif";
//        Glide.with(this).asGif().load(uri).into(mImageView);

        Glide.with(this).asGif().load(R.drawable.emoji).into(mImageView);
    }

    private void showBitmap(View view) {
        String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599147578905&di=bbbb98f646a43217af128e788c09a1e1&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw560h375%2F20180314%2F7763-fyscsmv5140841.jpg";
        Glide.with(/*view.getContext()*/this) // 依赖组件，不要依赖Context，依赖具体的活动(fragment,activiyt) ;glide 会随具体的活动生命周期做活动；防止内存泄漏
                .load(uri)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Log.d("dong", "[47]");
                        mImageView.setImageDrawable(resource);
                    }
                }); // 需要填充的view
    }
}
