package com.pax.android.demoapp;

import android.app.Application;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.RPCService;
import com.pax.market.android.app.sdk.StoreSdk;

import net.grandcentrix.tray.AppPreferences;

/**
 * Created by fojut on 2017/8/24.
 */

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();

    private boolean isReadyToUpdate=true;

    //replace with your appkey and appsecret
    private static final String appkey = "GUEVVVQPUTYGYWVNYWVN";
    private static final String appSecret = "EQLNI2TXAQPYW0XCXZU4ZKTN1KOQCK1KVMA8X1XS";
    private static final String SN = Build.SERIAL;
    public static AppPreferences appPreferences;



    @Override
    public void onCreate() {
        super.onCreate();
        //initial the SDK
        initPaxStoreSdk();
        appPreferences = new AppPreferences(getApplicationContext()); // this Preference comes for free from the library

    }


    private void initPaxStoreSdk() {
        //1. Init AppKey，AppSecret and SN
        StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret, SN, new BaseApiService.Callback() {
            @Override
            public void initSuccess() {
                initInquirer();
            }

            @Override
            public void initFailed(RemoteException e) {
                Toast.makeText(getApplicationContext(), "Cannot get API URL from PAXSTORE, Please install PAXSTORE first.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initInquirer() {
        //2. Init checking of whether app can be updated

        StoreSdk.getInstance().initInquirer(new StoreSdk.Inquirer() {
            @Override
            public boolean isReadyUpdate() {
                Log.i(TAG, "call business function....isReadyUpdate = " + isReadyToUpdate);
                //todo call your business function here while is ready to update or not
                return isReadyToUpdate;
            }
        });
    }

    public boolean isReadyToUpdate() {
        return isReadyToUpdate;
    }

    public void setReadyToUpdate(boolean readyToUpdate) {
        isReadyToUpdate = readyToUpdate;
        if(isReadyToUpdate){
            Toast.makeText(getApplicationContext(), getString(R.string.label_ready_to_update), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.label_not_ready_to_update), Toast.LENGTH_SHORT).show();
        }
    }
}