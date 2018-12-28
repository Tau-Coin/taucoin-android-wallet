/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.taucoin.android.wallet.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;

import io.taucoin.android.wallet.module.bean.MessageEvent;
import io.taucoin.android.wallet.util.EventBusUtil;

public abstract class BaseFragment extends Fragment implements OnRefreshListener, OnLoadmoreListener {
    private View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!EventBusUtil.isRegistered(this)) {
            EventBusUtil.register(this);
        }
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }
        mRootView = getViewLayout(inflater, container, savedInstanceState);
        return mRootView;
    }

    @Subscribe
    public void onEvent(MessageEvent event){

    }

    public abstract View getViewLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBusUtil.isRegistered(this)) {
            EventBusUtil.unregister(this);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }
}
