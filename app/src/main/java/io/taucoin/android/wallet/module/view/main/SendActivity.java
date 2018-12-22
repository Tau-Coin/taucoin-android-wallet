package io.taucoin.android.wallet.module.view.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.naturs.logger.Logger;
import com.mofei.tau.R;
import com.tau.wallet.Wallet;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.taucoin.android.wallet.base.BaseActivity;
import io.taucoin.android.wallet.base.TransmitKey;
import io.taucoin.android.wallet.db.entity.TransactionHistory;
import io.taucoin.android.wallet.module.presenter.TxService;
import io.taucoin.android.wallet.module.presenter.TxPresenter;
import io.taucoin.android.wallet.module.view.main.iview.ISendView;
import io.taucoin.android.wallet.util.KeyboardUtils;
import io.taucoin.android.wallet.util.ProgressManager;
import io.taucoin.android.wallet.widget.ActionSheetDialog;
import io.taucoin.android.wallet.widget.CommonDialog;

public class SendActivity extends BaseActivity implements ISendView {

    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_memo)
    EditText etMemo;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.et_fee)
    EditText etFee;
    @BindView(R.id.btn_send)
    Button btnSend;

    private TxPresenter mTxPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        mTxPresenter = new TxPresenter(this);
        ProgressManager.showProgressDialog(this);
        TxService.startTxService(TransmitKey.ServiceType.GET_SEND_DATA);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_fee)
    public void onTvFeeClicked() {
        KeyboardUtils.hideSoftInput(this);

        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle(R.string.send_choose_fee_title, R.string.send_choose_fee_tips)
                .addSheetItem(R.string.send_priority, R.string.send_priority_vice, R.string.send_priority_value,
                        which -> tvFee.setText(which.id))
                .addSheetItem(R.string.send_normal, R.string.send_normal_vice, R.string.send_normal_value,
                        which -> tvFee.setText(which.id))
                .setCancel(R.string.send_customize,
                        which -> showSoftInput())
                .show();
    }

    @OnTextChanged(value = R.id.et_fee, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterFeeChanged() {
        tvFee.setText(etFee.getText());
    }

    @OnClick(R.id.btn_send)
    public void onBtnSendClicked() {
        KeyboardUtils.hideSoftInput(this);

        mTxPresenter.isAnyTxPending();
    }

    @Override
    public void checkForm() {
        String address = etAddress.getText().toString().trim();
        String amount = etAmount.getText().toString().trim();
        String memo = etMemo.getText().toString().trim();
        String fee = tvFee.getText().toString().trim();

        TransactionHistory tx = new TransactionHistory();
        tx.setToAddress(address);
        tx.setMemo(memo);
        tx.setValue(amount);
        tx.setFee(fee);

        if(Wallet.getInstance().validateTxParamer(tx)){
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
                    ProgressManager.showProgressDialog(this);
                    mTxPresenter.getBalanceAndUTXO(tx);
                })
                .create().show();

    }

    @Override
    public void startService() {
        TxService.startTxService(TransmitKey.ServiceType.GET_UTXO_LIST);
    }

    private void showSoftInput() {
        Observable.timer(220, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onComplete() {
                        etFee.setFocusable(true);
                        etFee.setFocusableInTouchMode(true);
                        etFee.requestFocus();
                        etFee.setSelection(etFee.getText().length());
                        KeyboardUtils.showSoftInput(etFee);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "showSoftInput error");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.hideSoftInput(this);
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