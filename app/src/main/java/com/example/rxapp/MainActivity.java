package com.example.rxapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rxapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private List<Integer> ids = Arrays.asList(2, 3, 4, 5, 6, 7, 8);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        load();
        setupSearch();
    }

    private void load() {
        /*binding.progressBar.setVisibility(View.VISIBLE);

        Single.just(ids)
                .delay(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Integer> integers) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.text.setText(integers.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });*/
    }

    private void setupSearch() {


        Observable.create((ObservableOnSubscribe<String>) emitter -> binding.search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        emitter.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }))
                .debounce(500, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(s -> !s.isEmpty())
                .flatMapSingle((Function<String, SingleSource<String>>) this::getUserName)
                .map(s -> "mapped" + s)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d("RXLOG", s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Single<String> getUserName(String id) {
        return Single.fromCallable(() -> "Name" + id)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());
    }

    private void streamApi() {
        List<String> list = new ArrayList<>();
        List<Integer> integerList = list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
