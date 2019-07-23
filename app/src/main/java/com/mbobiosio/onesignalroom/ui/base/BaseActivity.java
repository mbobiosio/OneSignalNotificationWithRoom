package com.mbobiosio.onesignalroom.ui.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mbuodile Obiosio on Jul 20,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        setUnBinder(ButterKnife.bind(this));
    }


    public void setUnBinder(Unbinder unBinder) {
        mUnbinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract int getLayoutResID();
}
