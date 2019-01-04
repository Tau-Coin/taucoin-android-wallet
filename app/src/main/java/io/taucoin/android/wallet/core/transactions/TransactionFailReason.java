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

package io.taucoin.android.wallet.core.transactions;

public enum TransactionFailReason {
    NO_ERROR(0, "no error"),
    EMPTY_RECIPIENTS(1, "empty recipients"),
    INVALID_RECIPIENT_OR_ADDRESS(2, "invalid recipient or address"),
    NO_COINS(3, "No Balance"),
    NO_ENOUGH_COINS(4, "Insufficient Balance"),
    AMOUNT_TO_SMALL(5, "Transaction amount is too low"),
    UTXO_INVALID(6, "internal error: invalid utxo"),
    SIGNATURE_EXCEPTION(7, "internal error: sign fail"),
    TX_TOO_LARGE(8, "Transaction too large"),
    TX_FEE_TOO_SMALL(9, "Transaction fee is too low"),
    TX_FEE_TOO_LARGE(10, "Transaction fee is too high"),
    AMOUNT_TO_LARGE(11, "Transaction amount is too high");

    private int code;
    private String msg;

    TransactionFailReason(int code, String msg) {
        this.code = code;
        this.msg  = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
