package org.lee.android.doodles.widget;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.lee.android.BillingBuilder;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.adview.PropertiesActivity;
import org.lee.android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * 广告
 */
public class AdViewHolder extends RecyclerView.ViewHolder implements Toolbar.OnMenuItemClickListener {

    private Activity mActivity;
    private BillingBuilder mBillingBuilder;
    private TextView mDateText;
    private AdView mAdView;

    public AdViewHolder(View itemView, Activity activity) {
        super(itemView);
        Log.anchor();
        mActivity = activity;
        MainActivity mainActivity = (MainActivity) activity;
        mBillingBuilder = mainActivity.getBillingBuilder();

        mDateText = (TextView) itemView.findViewById(R.id.DateTime);

        Toolbar toolbar = (Toolbar) itemView.findViewById(R.id.ToolbarMenu);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.adview_menu);

        mAdView = (AdView) itemView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(DefaultBuild.MEDIUM_RECTANGLE_AD_UNIT_ID)
                .build();
        mAdView.setAdListener(mAdListener);
        mAdView.loadAd(adRequest);

    }

    private AdListener mAdListener = new AdListener() {

        @Override
        public void onAdLoaded() {
            isAvailable = true;
            DateFormat format = DateFormat.getDateTimeInstance();
            mDateText.setText(format.format(new Date()));
        }

        @Override
        public void onAdClosed() {
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            mDateText.setText("ERROR: " + errorMessage(errorCode));
        }

        @Override
        public void onAdLeftApplication() {
        }

        @Override
        public void onAdOpened() {

        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.RemoveAd:
                mBillingBuilder.flagEndAsync();
                mBillingBuilder.removeAds(null);
                break;
            case R.id.Properties:
                PropertiesActivity.launch(mActivity);
                break;
        }
        return false;
    }

    private boolean isAvailable;

    public void resume() {
        if (isAvailable) {
            mAdView.resume();
        } else
            mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void pause() {
        mAdView.pause();
    }

    private String errorMessage(int errorCode) {
        String error = null;
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                error = "ERROR_CODE_INTERNAL_ERROR";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                error = "ERROR_CODE_INVALID_REQUEST";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                error = "ERROR_CODE_NETWORK_ERROR";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                error = "ERROR_CODE_NO_FILL";
                break;
        }
        return error;
    }

//    // Callback for when a purchase is finished
//    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            Log.anchor("Purchase finished: " + result + ", purchase: " + purchase);
//
//            // if we were disposed of in the meantime, quit.
//            if (mBillingBuilder.isDisposed()) return;
//
//            if (result.isFailure()) {
////                complain("Error purchasing: " + result);
//                Toast.show("购买错误: " + result);
////                setWaitScreen(false);
//                return;
//            }
//            if (!verifyDeveloperPayload(purchase)) {
////                complain("Error purchasing. Authenticity verification failed.");
//                Toast.show("购买错误. Authenticity verification failed.");
////                setWaitScreen(false);
//                return;
//            }
//
//            Log.anchor("Purchase successful.");
//            Log.anchor("购买成功.");
//
//            if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
//                // bought 1/4 tank of gas. So consume it.
//                Log.anchor("Purchase is gas. Starting gas consumption.");
//                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
//            }
//        }
//    };
}
