package com.lga.app.guide.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.lga.app.guide.BaseGuideActivity;
import com.lga.app.guide.R;
import com.lga.app.guide.constant.Key;

/**
 * todo
 *
 * @author xiaojie
 */
public class GuideFragment extends BaseFragment {
    private BaseGuideActivity mActivity;
    private ImageView mLabel;

    public static Fragment newInstance(int imgId, String label) {
        Fragment fragment = new GuideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Key.IMG_ID, imgId);
        bundle.putString(Key.LABEL, label);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView(View view) {
        mActivity = (BaseGuideActivity) getActivity();

        mLabel = (ImageView) view.findViewById(R.id.label);
    }

    @Override
    protected void updateView(Bundle args) {
        mLabel.setImageResource(args.getInt(Key.IMG_ID));
        mLabel.setContentDescription(args.getString(Key.LABEL));
    }
}
