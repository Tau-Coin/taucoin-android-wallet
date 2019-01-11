package io.taucoin.android.wallet.module.view.tx;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;

import butterknife.OnTouch;
import io.reactivex.ObservableOnSubscribe;
import io.taucoin.android.wallet.core.Wallet;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.module.service.TxService;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.module.view.main.iview.ISendView;
import io.taucoin.android.wallet.util.KeyboardUtils;
import io.taucoin.android.wallet.util.MoneyValueFilter;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.util.ToastUtils;
import io.taucoin.android.wallet.widget.ActionSheetDialog;
import io.taucoin.android.wallet.widget.CommonDialog;
import io.taucoin.android.wallet.widget.EditInput;
import io.taucoin.android.wallet.widget.SelectionEditText;
import io.taucoin.foundation.net.callback.LogicObserver;

public class SendActivity extends BaseActivity implements ISendView {

    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_memo)
    EditText etMemo;
    @BindView(R.id.iv_fee)
    ImageView ivFee;
    @BindView(R.id.et_fee)
    EditInput etFee;
    @BindView(R.id.btn_send)
    Button btnSend;

    private TxPresenter mTxPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        mTxPresenter = new TxPresenter(this);
        ProgressManager.showProgressDialog(this);
        TxService.startTxService(TransmitKey.ServiceType.GET_SEND_DATA);
        initView();
    }

    private void initView() {
        etAmount.setFilters(new InputFilter[]{new MoneyValueFilter()});
        initTxFeeView();

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if(etFee != null){
                boolean isFeeFocus = etFee.hasFocus();
                boolean isVisible = KeyboardUtils.isSoftInputVisible(SendActivity.this);
                if(isFeeFocus && !isVisible){
                    resetViewFocus(llRoot);
                }
            }
        });

        Observable.create((ObservableOnSubscribe<View>)
                e -> btnSend.setOnClickListener(e::onNext))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new LogicObserver<View>() {
                    @Override
                    public void handleData(View view) {
                        KeyboardUtils.hideSoftInput(SendActivity.this);
                        mTxPresenter.isAnyTxPending();
                    }
                });
    }

    private void initTxFeeView() {
        etFee.setText(R.string.send_normal_value);
        SelectionEditText editText = etFee.getEditText();
        editText.setTextAppearance(this, R.style.style_normal_blue);
        editText.setFilters(new InputFilter[]{new MoneyValueFilter().setDigits(4).setEndSpace()});
        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setMaxLines(1);
    }

    @OnClick({R.id.iv_fee})
    void onFeeSelectedClicked() {
        KeyboardUtils.hideSoftInput(this);
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setSelectValue(etFee.getText())
                .setCanceledOnTouchOutside(true)
                .setTitle(R.string.send_choose_fee_title, R.string.send_choose_fee_tips)
                .addSheetItem(R.string.send_priority, R.string.send_priority_vice, R.string.send_priority_value,
                        which -> etFee.setText(which.id))
                .addSheetItem(R.string.send_normal, R.string.send_normal_vice, R.string.send_normal_value,
                        which -> etFee.setText(which.id))
                .setCancel(R.string.send_customize,
                        which -> showSoftInput())
                .show();
    }

    @OnTouch(R.id.et_fee)
    boolean onTxFeeClick(){
        return true;
    }

    private void resetViewFocus(View view) {
        if(view != null){
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
    }

    @Override
    public void checkForm() {
        String address = etAddress.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String memo = etMemo.getText().toString().trim();
        String fee = etFee.getText();

        TransactionHistory tx = new TransactionHistory();
        tx.setToAddress(address);
        tx.setMemo(memo);
        tx.setValue(amount);
        tx.setFee(fee);

        if(Wallet.getInstance().validateTxParameter(tx)){
            showSureDialog(tx);
        }
    }

    private void showSureDialog(TransactionHistory tx) {

        View view = LinearLayout.inflate(this, R.layout.view_dialog_send, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tvToAddress.setText(tx.getToAddress());
        viewHolder.tvToAmount.setText(tx.getValue());
        viewHolder.tvToMemo.setText(tx.getMemo());
        new CommonDialog.Builder(this)
                .setContentView(view)
                .setNegativeButton(R.string.send_dialog_no, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.send_dialog_yes, (dialog, which) -> {
                    dialog.dismiss();
                    handleSendTransaction(tx);
                })
                .create().show();

    }

    private void handleSendTransaction(TransactionHistory tx) {
        ProgressManager.showProgressDialog(this);
        mTxPresenter.getBalanceAndUTXO(tx, new LogicObserver<Boolean>() {
            @Override
            public void handleData(Boolean isSuccess) {
                ProgressManager.closeProgressDialog();
                if(isSuccess){
                    // clear all editText data
                    etAddress.getText().clear();
                    etAmount.getText().clear();
                    etMemo.getText().clear();
                    etFee.setText(R.string.send_normal_value);
                }else {
                    ToastUtils.showShortToast(R.string.send_tx_invalid_error);
                }
            }
        });
    }

    private void showSoftInput() {
        // clear transaction fee
        Observable.timer(220, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onComplete() {
                        etFee.setText(etFee.getText());
                        resetViewFocus(etFee.getEditText());
                        KeyboardUtils.showSoftInput(etFee.getEditText());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "showSoftInput error");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if(KeyboardUtils.isSoftInputVisible(this)){
            KeyboardUtils.hideSoftInput(this);
        }
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        super.onDestroy();
    }

    class ViewHolder {
        @BindView(R.id.tv_to_address)
        TextView tvToAddress;
        @BindView(R.id.tv_to_amount)
        TextView tvToAmount;
        @BindView(R.id.tv_to_Memo)
        TextView tvToMemo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}