package com.mofei.tau.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mofei.tau.R;
import com.mofei.tau.activity.DetailsActivity;
import com.mofei.tau.activity.KeysAddressesActivity;
import com.mofei.tau.info.SharedPreferencesHelper;
import com.mofei.tau.util.ZXingUtils;
import com.mofei.tau.view.DialogWaitting;

/**
 *
 * create an instance of this fragment.
 */
public class ReceiveFragment extends Fragment {
    private DialogWaitting mWaitDialog = null;
    private Toast mToast = null;
    private TextView mDetailsTV;
   // private RelativeLayout mDetailsRL;
    private TextView mWalletAddressTV;
    private ImageView mAddressTwoDimensionCodeIV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receive, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData(view);

        initEvent();

    }

    private void initData(View view) {
        mDetailsTV=view.findViewById(R.id.to_details_);
      //  mDetailsRL=view.findViewById(R.id.to_details_);
        mWalletAddressTV=view.findViewById(R.id.wallet_address_);
        mAddressTwoDimensionCodeIV=view.findViewById(R.id.address_two_dimension_code_img_);
    }

    private void initEvent() {

        String address= SharedPreferencesHelper.getInstance(getActivity()).getString("Address","Address");
        mWalletAddressTV.setText(address);

        mDetailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getActivity(),DetailsActivity.class);
               /* intent.putExtra("Pubkey",key.getPubkey());
                intent.putExtra("Privkey",key.getPrivkey());
                intent.putExtra("Address",key.getAddress());*/
                startActivity(intent);
            }
        });

       /* mDetailsRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent( getActivity(),DetailsActivity.class);
               *//* intent.putExtra("Pubkey",key.getPubkey());
                intent.putExtra("Privkey",key.getPrivkey());
                intent.putExtra("Address",key.getAddress());*//*
                startActivity(intent);

            }
        });*/
        //生成二维码
        Bitmap qrImage= ZXingUtils.createQRImage(address,400,400);
        mAddressTwoDimensionCodeIV.setImageBitmap(qrImage);
        mAddressTwoDimensionCodeIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // SaveImageToSysAlbum();  //保存到相册
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        // .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle("Save address two-dimensional code?")//设置对话框的标题
                        .setMessage("If you want to save, please click OK or click Cancel.")//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImage(mAddressTwoDimensionCodeIV);
                                dialog.dismiss();
                            }
                        }).create();

                dialog.show();

                return false;
            }
        });
    }

    private void saveImage(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "这是title", "这是description");
        showToast("save success");
        imageView.setDrawingCacheEnabled(false);//关闭catch

    }

   /* private void SaveImageToSysAlbum() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Bitmap bitmap = convertViewToBitmap(mAddressTwoDimensionCodeIV);
            if (bitmap != null) {
                try {
                    ContentResolver cr = getContentResolver();
                    String url = MediaStore.Images.Media.insertImage(cr, bitmap,
                            String.valueOf(System.currentTimeMillis()), "");
                    showToast("save success");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                showToast("save failure");
            }
        }else {
            showToast("save failure");
        }
    }*/

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void showToast(String text) {
        if (null != text) {
            if (null != mToast) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }


    public void showWaitDialog(String text) {
        if (null == mWaitDialog) {
            mWaitDialog = new DialogWaitting(getActivity());
        }
        mWaitDialog.show(text);
    }


}
