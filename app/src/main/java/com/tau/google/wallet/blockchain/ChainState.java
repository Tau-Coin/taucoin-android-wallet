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

package com.mofei.tau.google.wallet.blockchain;

public class ChainState {

    private static ChainState sSingleton = null;

    private long chainHeight;

    protected ChainState() {
        chainHeight = 76324L;
    }

    public synchronized static ChainState getInstance() {
        if (sSingleton == null) {
            sSingleton = new ChainState();
        }

        return sSingleton;
    }

    public synchronized long getHeight() {
        return chainHeight;
    }

    public synchronized void setHeight(long height) {
        this.chainHeight = height;
    }
}
