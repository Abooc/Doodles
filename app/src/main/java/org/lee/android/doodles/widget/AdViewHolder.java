package org.lee.android.doodles.widget;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.lee.android.BillingBuilder;
import org.lee.android.billing.util.IabHelper;
import org.lee.android.billing.util.IabResult;
import org.lee.android.billing.util.Purchase;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * 广告
 */
public class AdViewHolder extends RecyclerView.ViewHolder implements Toolbar.OnMenuItemClickListener {

    // 移除广告
    static final String SKU_REMOVE_ADS = "remove_ads";
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    int mTank;
    // The helper object
    IabHelper mHelper;
    private BillingBuilder mBillingBuilder;

    public AdViewHolder(View itemView, Activity activity) {
        super(itemView);
        MainActivity mainActivity = (MainActivity) activity;
        mBillingBuilder = mainActivity.getBillingBuilder();

        Toolbar toolbar = (Toolbar) itemView.findViewById(R.id.ToolbarMenu);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.adview_menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mBillingBuilder.flagEndAsync();
        mBillingBuilder.removeAds(null);
        return false;
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
