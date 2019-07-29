package io.taucoin.android.wallet.module.view.intro;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidpagecontrol.PageControl;
import com.mofei.tau.R;

import io.taucoin.android.wallet.module.view.main.MainActivity;
import io.taucoin.android.wallet.util.SharedPreferencesHelper;

public class IntroActivity extends AppCompatActivity {



    private ViewPager viewPager;
    private PageControl pageControl;
    private IntroAdapter adapter;
    private TextView tv_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        initView();


    }

    private void initView() {

        adapter=new IntroAdapter(getSupportFragmentManager());

        viewPager=findViewById(R.id.view_pager);
        pageControl=findViewById(R.id.page_control);

        tv_next=findViewById(R.id.tv_next);

        viewPager.setAdapter(adapter);
        pageControl.setViewPager(viewPager);
        viewPager.setCurrentItem(0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("DEE",position+"");

                switch (position)
                    {
                        case 0:
                            tv_next.setText(getString(R.string.next));
                            break;
                        case 1:
                            tv_next.setText(getString(R.string.next));
                            break;
                        case 2:
                            tv_next.setText(getString(R.string.start));
                            break;
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewPager.getCurrentItem()<2 && !tv_next.getText().equals("Start"))
                {
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    return;
                }

                if (SharedPreferencesHelper.getInstance().getString("code","none").equals("none"))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(IntroActivity.this);
                    builder.setMessage("Please insert Local pass code");
                    builder.setTitle("local password");
                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();


                    Toast.makeText(IntroActivity.this,"Please insert Local pass code",Toast.LENGTH_LONG).show();
                }else
                {
                    SharedPreferencesHelper.getInstance().putBoolean("first_run",false);
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }




            }
        });


    }
}
