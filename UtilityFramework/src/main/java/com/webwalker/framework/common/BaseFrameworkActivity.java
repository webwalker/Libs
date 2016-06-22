package com.webwalker.framework.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.webwalker.framework.uiquery.AQuery;
import com.webwalker.framework.widget.LoadingDialog;

/**
 * @author xu.jian
 */
public class BaseFrameworkActivity extends Activity implements OnClickListener {
    protected AQuery aq = null;
    private LoadingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(this);
    }

    protected void showLoading() {
        loading = new LoadingDialog(this);
        loading.showLoading();
    }

    protected void hideLoading() {
        if (loading != null)
            loading.hideLoading();
    }

    @Override
    public void onClick(View arg0) {
    }
}
