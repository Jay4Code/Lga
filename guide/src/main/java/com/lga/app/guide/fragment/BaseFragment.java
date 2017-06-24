package com.lga.app.guide.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * todo
 *
 * @author xiaojie
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        initView(view);
        updateView(getArguments());
        return view;
    }

    @Override
    public void onClick(View v) {}

    protected abstract int getContentView();

    protected abstract void initView(View view);

    protected abstract void updateView(Bundle arguments);
}
