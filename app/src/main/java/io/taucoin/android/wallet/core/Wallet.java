/**
 * Copyright 2018 TauCoin Core Developers.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.taucoin.android.wallet.core;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Script;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

import io.taucoin.android.wallet.MyApplication;
import io.taucoin.android.wallet.core.blockchain.Constants;
import io.taucoin.android.wallet.core.keystore.KeyStore;
import io.taucoin.android.wallet.core.transactions.CreateTransactionResult;
import io.taucoin.android.wallet.core.transactions.Transaction;
import io.taucoin.android.wallet.core.transactions.TransactionFailReason;
import io.taucoin.android.wallet.core.transactions.TransactionInput;
import io.taucoin.android.wallet.core.transactions.TransactionOutPoint;
import io.taucoin.android.wallet.core.transactions.TransactionOutput;
import io.taucoin.android.wallet.core.utxo.CoinsSelector;
import io.taucoin.android.wallet.core.utxo.UTXOManager;
import io.taucoin.android.wallet.db.entity.KeyValue;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.db.entity.UTXORecord;
import io.taucoin.android.wallet.util.FmtMicrometer;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.foundation.util.StringUtil;
import io.taucoin.platform.adress.KeyManager;

public class Wallet implements Serializable {

    private static final long serialVersionUID = 3L;

    private static Wallet sSingleton = null;

    private final NetworkParameters params;

    private final KeyStore keyStore;

    private final UTXOManager coinsManager;

    private Wallet() {
        params = NetworkParameters.mainNet();
        keyStore = KeyStore.getInstance();
        coinsManager = UTXOManager.getInstance();
    }

    public synchronized static Wallet getInstance() {
        if (sSingleton == null) {
            synchronized (Wallet.class) {
                sSingleton = new Wallet();
            }
        }
        return sSingleton;
    }

    public synchronized CreateTransactionResult createTransaction(Map<String, BigInteger> receipts, String txFee, Transaction tx) {
        boolean bSubtractFeeFromReceipts = false;
        CreateTransactionResult result = new CreateTransactionResult();

        // Step1: validate address
        if (receipts.isEmpty()) {
            result.failReason = TransactionFailReason.EMPTY_RECIPIENTS;
            return result;
        }

        BigInteger nTotal = BigInteger.ZERO;
        ArrayList<Recipient> arrSendTo = new ArrayList<>();
        for (Map.Entry<String, BigInteger> entry : receipts.entrySet()) {
            Recipient r;
            try {
                BigInteger v = entry.getValue();
//                if (!Constants.duringMoneyRange(v)) {
//                    System.out.println("Address " + entry.getKey() + " value " + v.toString() + " invalid!");
//                    continue;
//                }
                Address to = new Address(params, Utils.hexStringToBytes(Utils.getHash160FromTauAddress(entry.getKey())));
                r = new Recipient(Script.createOutputScript(to), v, bSubtractFeeFromReceipts);
                arrSendTo.add(r);
                nTotal = nTotal.add(v);
            } catch (AddressFormatException e) {
                result.failReason = TransactionFailReason.INVALID_RECIPIENT_OR_ADDRESS;
                return result;
            }
        }

        if (arrSendTo.isEmpty()) {
            result.failReason = TransactionFailReason.INVALID_RECIPIENT_OR_ADDRESS;
            return result;
        }

        // Step2: get available coins.
        ArrayList<UTXORecord> availableCoins = new ArrayList<>();
        availableCoins.clear();
        coinsManager.getAvailableCoins(null, availableCoins);
        if (availableCoins.isEmpty()) {
            result.failReason = TransactionFailReason.NO_COINS;
            return result;
        }

        // Step3: Start with no fee and loop until there is enough fee
        BigInteger nValue = nTotal;
        int nSubtractFeeFromAmount = 0;
        if (bSubtractFeeFromReceipts) {
            nSubtractFeeFromAmount = receipts.size();
        }
        result.feeRet = BigInteger.ZERO;

        while (true) {
            tx.inputs.clear();
            tx.rewards.clear();
            tx.outputs.clear();

            boolean fFirst = true;

            BigInteger nValueToSelect = nValue;
            if (nSubtractFeeFromAmount == 0)
                nValueToSelect = nValueToSelect.add(result.feeRet);

            // vouts to the payees
            for (Recipient recipient : arrSendTo) {
                TransactionOutput txout = new TransactionOutput(params, recipient.nAmount, recipient.scriptPubKey);

                if (recipient.fSubtractFeeFromAmount) {
                    // Subtract fee equally from each selected recipient
                    txout.value = txout.value.subtract(result.feeRet.divide(BigInteger.valueOf((long) nSubtractFeeFromAmount)));

                    // first receiver pays the remainder not divisible by output count
                    if (fFirst) {
                        fFirst = false;
                        txout.value = txout.value.subtract(result.feeRet.mod(BigInteger.valueOf((long) nSubtractFeeFromAmount)));
                    }
                }

//                if (txout.value.compareTo(Constants.DEFAULT_TX_FEE_MIN) < 0) {
//                    result.failReason = TransactionFailReason.AMOUNT_TO_SMALL;
//                    return result;
//                }
                tx.outputs.add(txout);
            }

            // select coins
            ArrayList<UTXORecord> selectedCoins = new ArrayList<>();
            selectedCoins.clear();
            BigInteger selectedValue = CoinsSelector.SelectCoins(availableCoins, nValueToSelect, selectedCoins);
            if (nValueToSelect.compareTo(selectedValue) > 0 || selectedCoins.isEmpty()) {
                result.failReason = TransactionFailReason.NO_ENOUGH_COINS;
                return result;
            }

            // handle change
            BigInteger change = selectedValue.subtract(nValueToSelect);

            if (change.compareTo(BigInteger.ZERO) > 0) {
                // We do not move dust-change to fees, because the sender would end up paying more than requested.
                // This would be against the purpose of the all-inclusive feature.
                // So instead we raise the change and deduct from the recipient.
                if (nSubtractFeeFromAmount > 0 && change.compareTo(Constants.DEFAULT_TX_FEE_MIN) < 0) {
                    BigInteger nDust = Constants.DEFAULT_TX_FEE_MIN;
                    nDust = nDust.subtract(change);
                    // raise change until no more dust
                    change = change.add(nDust);

                    // subtract from first recipient
                    for (int i = 0; i < arrSendTo.size(); i++) {
                        if (arrSendTo.get(i).fSubtractFeeFromAmount) {
                            tx.outputs.get(i).value = tx.outputs.get(i).value.subtract(nDust);
                            if (tx.outputs.get(i).value.compareTo(Constants.DEFAULT_TX_FEE_MIN) < 0) {
                                result.failReason = TransactionFailReason.AMOUNT_TO_SMALL;
                                return result;
                            }
                            break;
                        }
                    }
                }

                if (change.compareTo(Constants.DEFAULT_TX_FEE_MIN) < 0) {
                    result.feeRet = result.feeRet.add(change);
                } else {
                    // To keep simple, just get the first input as the change scriptPubkey
                    Address changeAddress;
                    try {
                        changeAddress = new Address(params,
                                Utils.hexStringToBytes(Utils.getHash160FromTauAddress(selectedCoins.get(0).address)));
                    } catch (AddressFormatException e) {
                        System.out.println(e.toString());
                        result.failReason = TransactionFailReason.UTXO_INVALID;
                        return result;
                    }

                    byte[] changeScriptPubkey = Script.createOutputScript(changeAddress);
                    // just for test
                    TransactionOutput newTxOut = new TransactionOutput(params, change, changeScriptPubkey);
                    tx.outputs.add(newTxOut);
                }
            }

            // Fill vin
            for (UTXORecord utxo : selectedCoins) {
                TransactionOutPoint outPoint;

                try {
                    outPoint = new TransactionOutPoint(params, utxo);
                } catch (ProtocolException e) {
                    System.out.println(e.toString());
                    result.failReason = TransactionFailReason.UTXO_INVALID;
                    return result;
                }

                TransactionInput input = new TransactionInput(params, outPoint);
                tx.inputs.add(input);
            }

            // TODO: fill input rewards

            // Sign input and rewards

            try {
                tx.signInputsAndRewards(Transaction.SigHash.ALL, keyStore);
            } catch (ScriptException e) {
                System.out.println(e.toString());
                result.failReason = TransactionFailReason.SIGNATURE_EXCEPTION;
                return result;
            }

            // Compute tx fee
            long nBytes = tx.getVirtualSize();
            if (nBytes >= Constants.MAX_STANDARD_TX_WEIGHT) {
                result.failReason = TransactionFailReason.TX_TOO_LARGE;
                return result;
            }

            BigInteger needFee = BigInteger.ZERO;
            if (StringUtil.isNotEmpty(txFee)) {
                String fee = FmtMicrometer.fmtTxValue(txFee);
                needFee = new BigInteger(fee, 10);
            }

            if (result.feeRet.compareTo(needFee) >= 0) {
                coinsManager.markCoinsSpent(selectedCoins);
                break;
            }

            result.feeRet = needFee;
        }

        return result;
    }

    /**
     * validate transaction parameter
     */
    public synchronized boolean validateTxParameter(TransactionHistory tx) {
        if (tx == null) {
            return false;
        }
        // validate receive address
        if (StringUtil.isEmpty(tx.getToAddress())) {
            ToastUtils.showShortToast("Please enter address");
            return false;
        }
        if (!KeyManager.validateAddress(tx.getToAddress())) {
            ToastUtils.showShortToast("Incorrect address");
            return false;
        }
        // validate transaction amount
        if (StringUtil.isEmpty(tx.getValue())) {
            ToastUtils.showShortToast("Please enter amount");
            return false;
        }
        BigInteger minAmount = Constants.MIN_CHANGE;
        BigInteger maxAmount = Constants.MAX_MONEY;
        String amountStr = FmtMicrometer.fmtTxValue(tx.getValue());
        BigInteger amount = new BigInteger(amountStr, 10);
        if (amount.compareTo(minAmount) < 0) {
            ToastUtils.showShortToast(TransactionFailReason.AMOUNT_TO_SMALL.getMsg());
            return false;
        } else if (amount.compareTo(maxAmount) >= 0) {
            ToastUtils.showShortToast(TransactionFailReason.AMOUNT_TO_LARGE.getMsg());
            return false;
        }
        // validate transaction fee
        if (StringUtil.isEmpty(tx.getFee())) {
            ToastUtils.showShortToast("Please enter transaction fee");
            return false;
        }

        BigInteger minFee = Constants.DEFAULT_TX_FEE_MIN;
        BigInteger maxFee = Constants.DEFAULT_TX_FEE_MAX;
        String feeStr = FmtMicrometer.fmtTxValue(tx.getFee());
        BigInteger fee = new BigInteger(feeStr, 10);
        if (fee.compareTo(minFee) < 0) {
            ToastUtils.showShortToast(TransactionFailReason.TX_FEE_TOO_SMALL.getMsg());
            return false;
        } else if (fee.compareTo(maxFee) > 0) {
            ToastUtils.showShortToast(TransactionFailReason.TX_FEE_TOO_LARGE.getMsg());
            return false;
        }

        // validate balance is enough
        KeyValue keyValue = MyApplication.getKeyValue();
        if (keyValue != null) {
            String balanceStr = String.valueOf(keyValue.getBalance());
            BigInteger balance = new BigInteger(balanceStr, 10);
            if (balance.compareTo(amount.add(fee)) < 0) {
                ToastUtils.showShortToast(TransactionFailReason.NO_ENOUGH_COINS.getMsg());
                return false;
            }
        }
        return true;
    }
}