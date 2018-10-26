package com.mofei.tau.net;

import com.mofei.tau.entity.req_parameter.ChangePassword;
import com.mofei.tau.entity.req_parameter.FBAddressEmail;
import com.mofei.tau.entity.req_parameter.FBAddressEmlVeri;
import com.mofei.tau.entity.req_parameter.FBAddressImage;
import com.mofei.tau.entity.req_parameter.FBAddressPubKey;
import com.mofei.tau.entity.req_parameter.Login;
import com.mofei.tau.entity.req_parameter.Logout;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.entity.req_parameter.FBAddress;
import com.mofei.tau.entity.res_post.Club;
import com.mofei.tau.entity.res_post.ClubRet;
import com.mofei.tau.entity.res_post.Login1;
import com.mofei.tau.entity.res_post.Login1Ret;
import com.mofei.tau.entity.res_post.Login1RetSerializer;
import com.mofei.tau.entity.res_post.LoginRes;
import com.mofei.tau.entity.res_post.ReferralURL;
import com.mofei.tau.entity.res_post.StatusMessage;
import com.mofei.tau.entity.res_post.TalkUpdateRet;
import com.mofei.tau.entity.res_put.BuyCoins;
import com.mofei.tau.entity.res_put.BuyCoinsRet;
import com.mofei.tau.entity.res_put.Login0;
import com.mofei.tau.entity.res_put.Login0Ret;
import com.mofei.tau.entity.res_put.Register;


import java.util.Map;

import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiService {

    @POST("balance/")
    Observable<Balance<BalanceRet>> getBalance(@Body FBAddress fbAddress);

    @PUT("login0/")
    Observable<Login0<Login0Ret>> getLogin0(@Body FBAddressPubKey fbAddressPubKey);

    @POST("club/")
    Observable<Club<ClubRet>> getClub(@Body FBAddress fbAddress );

    @POST("bounty/submitEmail/")
    Observable<StatusMessage> getEmailVerification(@Body FBAddressEmail fbAddressEmail);

    @PUT("bounty/confirmEmail/")
    Observable<StatusMessage> getConfirmEmail(@Body FBAddressEmlVeri fbAddressEmlVeri);

    @POST("bounty/talkUpdate/")
    Observable<Balance<TalkUpdateRet>> getTalkUpdate(@Body FBAddress fbAddress);

    @POST("bounty/talkUpload/")
    Observable<Balance<BalanceRet>> getTalkUpload(@Body FBAddressImage fbAddress);

    @POST("buyCoins/")
    Observable<BuyCoins<BuyCoinsRet>> getBuyCoins(@Body FBAddress fbAddress);

    @POST("login1/")
    Observable<Login1<Login1Ret<Login1RetSerializer>>> getLogin1(@Body FBAddress fbAddress);


    @Multipart //请求体有多部分，使用@MultiPart上传
    @POST("bounty/talkUpload/")
    Observable<StatusMessage> getTalkUpload(@Part("facebookid") RequestBody facebookid, @Part("address") RequestBody address, @Part MultipartBody.Part image);


    @POST("bounty/referralURL/")
    Observable<ReferralURL> getreferralURL(@Body FBAddress fbAddress);

    @POST("verifyCode/")
    Observable<StatusMessage> verifyCode(@Body Map<String,String> email);

    @PUT("register/")
    Observable<StatusMessage> register(@Body Register register);

    @POST("login/")
    Observable<LoginRes> login(@Body Login login);

    @POST("logout/")
    Observable<Logout> logout();

    @POST("changePassword/")
    Observable<StatusMessage> changePassword(@Body ChangePassword changePassword);

    @POST("getBalance/")
    Observable<Balance<BalanceRet>> getBalance2(@Body Map<String,String> email);

}
