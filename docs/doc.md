 # Android wallet technical documentation

### Introduction of Wallet function：
------------

### **1. register**
- The user must first register with the valid mailbox for the first time.
###### 1. Type a valid mailbox in the mailbox input box.
###### 2.  Click the Send button to get the verification code.
###### 3. Go to the mailbox to find the verification code, type the verification code input box.
###### 4. Type password (password not restricted)
###### 5. Finally, click the Register button to register, and when the registration is successful, it will automatically jump to the login interface with the user's mailbox and password.

- Profile code.

##### 1.1 layout

``` 
{
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F19322"
    android:clipToPadding="true"
    android:fitsSystemWindows="true">

    <!-- title bar -->

    <RelativeLayout
        android:id="@+id/login_top"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:paddingBottom="5dp"
        android:paddingTop="5dp">

        <TextView
            android:id="@+id/imgBack"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:paddingLeft="10dp"
            android:paddingRight="10dp"
            android:background="?attr/controlBackground"
            android:drawableLeft="@mipmap/icon_back"
            android:drawablePadding="10dp"
            android:gravity="center"
            android:text="back"
            android:textColor="#ffffff"
            android:textSize="16sp" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:text=" Register"
            android:textColor="#ffffff"
            android:textSize="20sp" />

    </RelativeLayout>
	
    <LinearLayout
        android:layout_below="@id/login_top"
        android:id="@+id/tau"
        android:layout_marginTop="15dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
		
        <ImageView
            android:layout_gravity="center"
            android:id="@+id/logo_img"
            android:layout_centerHorizontal="true"
            android:layout_width="80dp"
            android:layout_height="80dp"
            android:src="@mipmap/logo_w"  />
			
        <TextView
            android:id="@+id/logo_mane"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/logo_img"
            android:layout_marginTop="6dp"
            android:gravity="center_horizontal"
            android:text="TAU"
            android:textColor="#ffffff"
            android:textSize="30sp"
            android:textStyle="bold" />

    </LinearLayout>

    <LinearLayout
        android:id="@+id/login_bottom"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/tau"
        android:layout_marginTop="10dp"
        android:orientation="vertical"
        android:paddingLeft="20dp"
        android:paddingRight="20dp">

        <LinearLayout
            android:id="@+id/linear1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:background="#30000000"
            android:orientation="horizontal"
            android:paddingLeft="10dp"
            android:focusable="true"
            android:focusableInTouchMode="true"
            android:paddingRight="10dp">

            <EditText
                android:id="@+id/edEmail"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_weight="1"
                android:background="@null"
                android:drawableLeft="@mipmap/icon_email"
                android:drawablePadding="8dp"
                android:hint="@string/email"
                android:inputType="text"
                android:padding="5dp"
                android:paddingLeft="15dp"
                android:textColor="#ffffff"
                android:textColorHint=" #A3A3A3"
                android:textSize="14sp"/>

            <ImageView
                android:id="@+id/img_email2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:padding="10dp"
                android:src="@mipmap/clear"/>

        </LinearLayout>
		
        <LinearLayout
            android:id="@+id/linear３"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:background="#30000000"
            android:orientation="horizontal"
            android:paddingLeft="8dp"
            android:focusable="true"
            android:focusableInTouchMode="true"
            android:paddingRight="10dp">

            <EditText
                android:id="@+id/edVerficode"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_weight="1"
                android:background="@null"
                android:drawableLeft="@mipmap/verfi"
                android:drawablePadding="8dp"
                android:hint="Enter verification code"
                android:inputType="text"
                android:padding="5dp"
                android:paddingLeft="15dp"
                android:textColor="#ffffff"
                android:textColorHint=" #A3A3A3"
                android:textSize="14sp"/>

            <TextView
                android:gravity="center"
                android:id="@+id/sendVerifyCodeTV"
                android:layout_width="70dp"
                android:layout_height="30dp"
                android:layout_alignParentRight="true"
                android:text="Send"
                android:background="@drawable/send_code_bg"
                android:textColor="@color/black"
                android:textSize="16sp" />

        </LinearLayout>

        <LinearLayout
            android:id="@+id/linear2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:background="#30000000"
            android:orientation="horizontal"
            android:paddingLeft="10dp"
            android:paddingRight="10dp">

            <EditText
                android:id="@+id/edPassword"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_weight="1"
                android:background="@null"
                android:drawableLeft="@mipmap/icon_password"
                android:drawablePadding="10dp"
                android:hint="@string/password"
                android:padding="5dp"
                android:paddingLeft="15dp"
                android:textColor="#ffffff"
                android:password="true"
                android:textColorHint="#A3A3A3"
                android:singleLine="true"
                android:textSize="14sp"/>

            <ImageView
                android:id="@+id/img_password2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:padding="10dp"
                android:src="@mipmap/clear"/>

        </LinearLayout>

    </LinearLayout>
    <Button
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_alignParentBottom="true"
        android:textAllCaps="false"
        android:id="@+id/btnRegister"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/backgroud_color2"
        android:text="Register"
        android:layout_marginBottom="40dp"
        android:textColor="#ffffff"
        android:textSize="20sp"/>

</RelativeLayout>
}
```
##### 1.2  function
``` 
  {
       private void register(String email,String verficode,String password) {
        generatekeyAddress();
        Register register=new Register();
        register.setEmail(email);
        register.setEmail_code(verficode);
        register.setPassword(password);        register.setAddress(SharedPreferencesHelper.getInstance(RegisterActivity.this).getString("Address","null"));
        register.setPubkey(SharedPreferencesHelper.getInstance(this).getString("Pubkey","null"));
        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.register(register);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        L.i("Status: "+statusMessage.getStatus());
                        L.i("Message: "+statusMessage.getMessage());
                        status=statusMessage.getStatus();
                    }
                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.i("onError");
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                       hideWaitDialog();
                    SharedPreferencesHelper.getInstance(RegisterActivity.this).putString("password",password);
                       handle.sendEmptyMessage(0x11);
                        L.i("onComplete");
                    }
                });
    }
    }
```
------------
### **２. Sign in** 
- The login box and password input box fill in the registered mailbox and password to login.
###### 1. If the user has just registered, he will bring back the account mailbox and password which he has just registered and fill in the mailbox and password box automatically.
###### 2. If a user needs to log in with a previously registered account, click the Minor Button to clear the input box, and then manually type in his account
###### 3. Click Login button to login, login successfully will jump to the main page.
###### 4. If you want to remember the password, you need to click the remember password check box to select it. The password will automatically fill in the password box after login, otherwise the password box will be empty after login.
- Profile code.

##### ２.1 layout

``` 
{
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:clipToPadding="true"
    android:background="@color/bg"
    android:paddingRight="20dp"
    android:paddingLeft="20dp"
    android:fitsSystemWindows="true">

    <ImageView
        android:id="@+id/logo_img"
        android:layout_centerHorizontal="true"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_marginTop="50dp"
        android:src="@mipmap/logo_w" />

    <TextView
        android:id="@+id/logo_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/logo_img"
        android:layout_marginTop="10dp"
        android:gravity="center_horizontal"
        android:text="TAU"
        android:textColor="#ffffff"
        android:textSize="30sp"
        android:textStyle="bold" />

    <LinearLayout
        android:id="@+id/llContent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/logo_name"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="20dp"
        android:layout_marginTop="21dp"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#30000000"
            android:orientation="horizontal"
            android:paddingLeft="10dp"
            android:paddingRight="10dp">

            <EditText
                android:id="@+id/email_input"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_weight="1"
                android:background="@null"
                android:drawableLeft="@mipmap/icon_email"
                android:drawablePadding="8dp"
                android:hint="@string/email"
                android:inputType="textEmailAddress"
                android:padding="5dp"
                android:textColor="#ffffff"
                android:textColorHint="#A3A3A3"
                android:textSize="14sp" />

            <ImageView
                android:id="@+id/img_email"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:padding="10dp"
                android:src="@mipmap/clear" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="15dp"
            android:background="#30000000"
            android:orientation="horizontal"
            android:paddingLeft="10dp"
            android:paddingRight="10dp">

            <EditText
                android:id="@+id/pass_input"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_weight="1"
                android:background="@null"
                android:drawableLeft="@mipmap/icon_password"
                android:drawablePadding="10dp"
                android:hint="@string/password"
                android:padding="5dp"
                android:password="true"
                android:singleLine="true"
                android:textColor="#ffffff"
                android:textColorHint="#A3A3A3"
                android:textSize="14sp" />

            <CheckBox
                android:id="@+id/cbDisplay"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true"
                android:button="@drawable/eye_pass"
                android:checked="false" />

            <ImageView
                android:id="@+id/img_password"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:padding="10dp"
                android:src="@mipmap/clear" />

        </LinearLayout>

        <TextView
            android:id="@+id/login_msg"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:padding="10dp"
            android:text=""
            android:textSize="16sp"
            android:visibility="gone" />

        <TextView
            android:id="@+id/register"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/rem_for_psw"
            android:layout_marginTop="20dp"
            android:text="Register"
            android:textColor="#ffffff"
            android:textSize="16sp" />
    </LinearLayout>

    <Button
        android:textAllCaps="false"
        android:id="@+id/login_bt"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_above="@id/rem_for_psw"
        android:background="@drawable/backgroud_color2"
        android:text="Login"
        android:textColor="#ffffff"
        android:textSize="16sp" />
		
    <LinearLayout
        android:layout_marginBottom="60dp"
        android:layout_alignParentBottom="true"
        android:id="@+id/rem_for_psw"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:orientation="horizontal">
		
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:orientation="horizontal"
            >

            <CheckBox
                android:id="@+id/cbRememberPs"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:buttonTint="#ffffff"
                android:paddingTop="10dp" />

            <TextView
                android:id="@+id/textView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="remember password"
                android:textColor="#ffffff"
                android:textSize="12sp" />
        </LinearLayout>

        <View
            android:layout_width="0dp"
            android:layout_height="1dp"
            android:layout_weight="1" />

        <TextView
            android:id="@+id/forgotPass"
            android:padding="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="forgot password ?"
            android:textColor="#ffffff"
            android:textSize="12sp" />

    </LinearLayout>
	
</RelativeLayout>
}
``` 

##### 2.２ function
``` 
{
 private void login(String email,String password) {
        Login login=new Login();
        login.setEmail(email);
        login.setPassword(password);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<LoginRes> observable=apiService.login(login);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
					
                    }
					
                    @Override
                    public void onNext(LoginRes loginRes) {
                        status=loginRes.getStatus();
                        L.e(status);
                        L.e(loginRes.getMessage());
                        L.e(loginRes.getEmail());
             SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("email",loginRes.getEmail());
                        user_nane=loginRes.getEmail();
                   SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("user_nane",user_nane);
                        L.e(loginRes.getAddress());
                        L.e(loginRes.getPubkey()); SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("Pubkey", loginRes.getPubkey());         SharedPreferencesHelper.getInstance(LoginActivity2.this).putString("Address",loginRes.getAddress());
                    }
					
                    @Override
                    public void onError(Throwable e) {
                        handler.sendEmptyMessage(0x15);
                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                    }
					
                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        handler.sendEmptyMessage(0x12);
                        L.i("onComplete");
                        SharedPreferencesHelper.getInstance(LoginActivity2.this).putBoolean("isLogin",true);
                    }
                });
    }
}
``` 
  

------------


### **３. Forget the password**
- If the user forgets his registered password, click the forgot password on the login page to retrieve the password
###### 1. Enter a valid mailbox in the mailbox input box and click the Get button to get the security code you need to change your password
###### 2. Find the security code in the mailbox and enter the security code input box.
###### 3. Then enter the new password to the password input box.
###### 4. Click the Submit button to submit, and when the submission is successful, you will jump to the login interface with your email and password, fill in the box automatically, and click the Login button to login.
- Profile code.

##### ３.1 layout

``` 
{
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:context=".activity.ChangePasswActivity"
    tools:showIn="@layout/activity_change_passw"
    android:background="@color/bg"
    >
	
    <RelativeLayout
        android:id="@+id/login_top"
        android:layout_width="match_parent"
        android:layout_height="45dp"
        android:paddingBottom="5dp"
        android:paddingTop="5dp">
		
        <TextView
            android:id="@+id/back_to_login"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:paddingLeft="10dp"
            android:paddingRight="10dp"
            android:background="?attr/controlBackground"
            android:drawableLeft="@mipmap/icon_back"
            android:drawablePadding="10dp"
            android:gravity="center"
            android:text="back"
            android:textColor="#ffffff"
            android:textSize="16sp" />
			
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:text="Change Password"
            android:textColor="#ffffff"
            android:textSize="20sp" />
			
    </RelativeLayout>
	
        <LinearLayout
            android:layout_marginRight="20dp"
            android:layout_marginLeft="20dp"
            android:layout_marginTop="60dp"
            android:layout_gravity="center"
            android:id="@+id/rlName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:background="@drawable/edit_bg"
            android:focusable="true"
            android:focusableInTouchMode="true">
			
            <TextView
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/enter_email"
                android:textColor="@color/text_dark"
                android:layout_marginTop="7dp"
                android:layout_marginBottom="9dp"/>
				
            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:layout_height="wrap_content">
				
                <EditText
                    android:id="@+id/email_in"
                    android:layout_width="match_parent"
                    android:layout_height="50dp"
                    android:textSize="12sp"
                    android:textColor="@color/text_dark"
                    android:background="@drawable/edit_bg"
                    android:padding="8dp"
                    android:hint="@string/enter_email"
                    android:singleLine="true"/>
					
                <TextView
                    android:gravity="center"
                    android:layout_centerVertical="true"
                    android:id="@+id/send_save_code"
                    android:layout_width="70dp"
                    android:layout_height="30dp"
                    android:layout_alignParentRight="true"
                    android:text="Get"
                    android:layout_marginRight="5dp"
                    android:background="@drawable/send_code_bg"
                    android:textColor="@color/black"
                    android:textSize="16sp" />
					
            </RelativeLayout>
			
            <TextView
                android:id="@+id/device_name"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/enter_new_psw_re"
                android:textColor="@color/text_dark"
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:layout_marginTop="7dp"
                android:layout_marginBottom="9dp"/>
				
            <EditText
                android:id="@+id/confirm_new_password"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:textSize="12sp"
                android:textColor="@color/text_dark"
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:background="@drawable/edit_bg"
                android:padding="8dp"
                android:hint="@string/enter_new_psw_confirm"
                android:singleLine="true"/>
				
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/enter_new_psw"
                android:textColor="@color/text_dark"
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:layout_marginTop="7dp"
                android:layout_marginBottom="9dp"/>
				
            <EditText
                android:id="@+id/new_password"
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:textSize="12sp"
                android:textColor="@color/text_dark"
                android:layout_marginLeft="12dp"
                android:layout_marginRight="12dp"
                android:layout_marginBottom="18dp"
                android:background="@drawable/edit_bg"
                android:padding="8dp"
                android:hint="@string/enter_new_psw"
                android:password="true"
                android:singleLine="true"/>
        </LinearLayout>
		
    <Button
        android:textAllCaps="false"
        android:id="@+id/submission"
        android:layout_marginRight="20dp"
        android:layout_marginLeft="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/rem_for_psw"
        android:layout_marginTop="50dp"
        android:background="@drawable/backgroud_color2"
        android:text="Submit"
        android:textColor="#ffffff"
        android:textSize="16sp" />
		
</LinearLayout>
}
``` 

##### ３.２ function
``` 
{
 private void changePassword(String email,String password,String safetycode) {
        ChangePassword changePassword=new ChangePassword();
        changePassword.setEmail(email);
        changePassword.setNew_password(password);
        changePassword.setSafety_code(safetycode);
        ApiService apiService= NetWorkManager.getApiService();
        Observable<StatusMessage> observable=apiService.changePassword(changePassword);
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StatusMessage>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StatusMessage statusMessage) {
                        status=statusMessage.getStatus();
                        L.i(""+statusMessage.getStatus());
                        L.i(statusMessage.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.sendEmptyMessage(0x17);
                        L.e("error");
                        e.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        L.i("complete");
                        handler.sendEmptyMessage(0x16);
                    }
                });
    }
}
``` 


------------


### **４. Get Balance** 
- Click the Balance button on the main interface to enter the Balance interface.
######  Check your TAU amount
- Profile code.

##### ４.１ layout

``` 
{
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".activity.BalanceActivity">

    <com.mofei.tau.view.CustomToolBar
        android:id="@+id/balance_titlebar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.mofei.tau.view.CustomToolBar>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:layout_marginTop="20dp"
        android:text="@string/laverdetstein"
        android:layout_gravity="center"/>
		
    <LinearLayout
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:orientation="horizontal">
		
        <TextView
            android:id="@+id/balance_tau"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="#0000ff"
            android:textSize="24sp"
            android:layout_marginTop="20dp"
            android:textStyle="bold"
            android:text=""
            android:layout_gravity="center"/>
			
        <TextView
            android:layout_marginLeft="15dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="24sp"
            android:layout_marginTop="20dp"
            android:textStyle="bold"
            android:text="TAU"
            android:layout_gravity="center"/>

    </LinearLayout>

    <LinearLayout
        android:gravity="center"
        android:layout_marginTop="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
		
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:layout_margin="5dp"
            android:text="Including"/>
			
        <TextView
            android:layout_margin="5dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:textStyle="bold"
            android:text="Harvest Reward:"
            android:layout_gravity="center"/>
			
        <TextView
            android:id="@+id/reward_tau"
            android:layout_margin="5dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:textColor="#FFAB00"
            android:text="" />
			
        <TextView
           android:layout_margin="5dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:layout_marginTop="20dp"
            android:textStyle="bold"
            android:text="TAU" />
    </LinearLayout>
	
</LinearLayout>
 }
```

##### ４.２ function
``` 
		 public void getBalanceData(String email) {
		 Map<String,String> emailMap=new HashMap<>();
        emailMap.put("email",email);
        ApiService apiService=NetWorkManager.getApiService();
        Observable<Balance<BalanceRet>> observable=apiService.getBalance2(emailMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Balance<BalanceRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Balance<BalanceRet> balanceRetBalance) {
                        L.e(balanceRetBalance.getStatus()+"");
                        L.e(balanceRetBalance.getMessage());
                        status=balanceRetBalance.getStatus();
                        double_coins=balanceRetBalance.getRet().getCoins();
                        reward= balanceRetBalance.getRet().getRewards();
                        L.e("Coins"+balanceRetBalance.getRet().getCoins());
                        L.e(balanceRetBalance.getRet().getPubkey());
                        L.e(balanceRetBalance.getRet().getUtxo()+"");
                        L.e(balanceRetBalance.getRet().getRewards()+"");
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        L.e("error");
                        e.printStackTrace();
                        hand.sendEmptyMessage(0x20);
                    }

                    @Override
                    public void onComplete() {
                        hand.sendEmptyMessage(0x21);
                        hideWaitDialog();
                        L.e("complete");
                    }
                });
    }
}

``` 
------------


 ### **５. Sideslip menu**
 - Click the sideslip menu button on the left side of the main interface to enter the left pop-up menu.
######  1. View user profile
###### 2. View user mail boxes
###### 3. Click Our Website to enter our official website homepage.
###### 4. Point About to view the version number of the software
###### 5. Logon button, when it is finished, it will jump to the login screen.

- Profile code.

##### ５.１ layout
``` 
{
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/slidingMenu"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@drawable/background_gradient"
    android:gravity="center_horizontal"
    android:clickable="true"
    android:layout_gravity="start">
	
    <de.hdodenhof.circleimageview.CircleImageView
        android:layout_centerHorizontal="true"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/icon_img"
        android:layout_width="61dp"
        android:layout_marginTop="50dp"
        android:layout_marginBottom="10dp"
        android:layout_height="61dp"
        android:src="@mipmap/logo"
        app:civ_border_width="0.6dp"
        app:civ_border_color="#FFD2D1D5"/>
		
    <LinearLayout
        android:id="@+id/content"
        android:layout_below="@id/icon_img"
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">
		
        <TextView
            android:textSize="16sp"
            android:textStyle="italic"
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="E-mail"/>
			
        <TextView
            android:id="@+id/username"
            android:textSize="16sp"
            android:textStyle="italic"
            android:layout_marginTop="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="User name"/>
			
        <TextView
            android:id="@+id/help"
            android:textSize="16sp"
            android:textStyle="italic"
            android:layout_marginTop="60dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Help"
            android:textColor="@drawable/text_color"/>
			
        <TextView
            android:textColor="@drawable/text_color"
            android:id="@+id/about"
            android:textSize="16sp"
            android:textStyle="italic"
            android:layout_marginTop="30dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="About"/>
			
        <Button
            android:id="@+id/about_us"
            android:gravity="center"
            android:textAllCaps="false"
            android:background="@drawable/click_item"
            android:textSize="16sp"
            android:textStyle="italic"
            android:textColor="#3C3F41"
            android:layout_marginTop="50dp"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:text="Our Website"  />
			
    </LinearLayout>
	
    <Button
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:id="@+id/logout"
        android:layout_width="100dp"
        android:textAllCaps="false"
        android:layout_height="wrap_content"
        android:text="@string/lagout"
        android:textSize="18sp"
        android:textColor="#ffffff"
        android:layout_marginBottom="40dp"
        android:background="@drawable/backgroud_color3" />
</RelativeLayout>
 }
```
##### ５.2 function
```
｛
 private void logout() {
       // LoginManager.getInstance().logOut();
        ApiService apiService= NetWorkManager.getApiService();
        Observable<Logout> observable=apiService.logout();
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Logout>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Logout logout) {
                        L.i(logout.getStatus());
                        L.i(logout.getMessage());
                        L.e("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError");
                        e.printStackTrace();
                        hideWaitDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideWaitDialog();
                        h.sendEmptyMessage(0x13);
                        startActivity(new Intent(MainActivity.this,LoginActivity2.class));
                        //  showToast("login successful");
                        L.i("onComplete");
                    }
                });
    }
｝
```

- Remarks：
###### The private key is displayed when login and registration is performed on the same device.


