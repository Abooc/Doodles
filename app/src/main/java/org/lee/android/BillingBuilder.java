package org.lee.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.lee.android.billing.util.IabHelper;
import org.lee.android.billing.util.IabResult;
import org.lee.android.billing.util.Inventory;
import org.lee.android.billing.util.Purchase;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-14.
 */
public class BillingBuilder {

    public interface OnInvalidateUi{
        void onInvalidate();
    }

    // 移除广告
    public static final String SKU_REMOVE_ADS = "remove_ads";
    // (arbitrary) request code for the purchase flow
    public static final int RC_REQUEST = 10001;
    int mTank;
    // The helper object
    private IabHelper mHelper;
    private Activity mActivity;

    public BillingBuilder(Activity activity) {
        mActivity = activity;
        mHelper = new IabHelper(activity, DefaultBuild.base64EncodedPublicKey);
        // 开启Debug日志，正式发布产品时，应该关闭日志
        mHelper.enableDebugLogging(true);

        // 这是异步操作，在执行完成时，会执行回调。
        Log.anchor("Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.anchor("Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
                    Toast.show("应用内支付错误: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                // IAB成功后，获取应用内商品目录
                Log.anchor("Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    private OnInvalidateUi mOnInvalidateUi;

    public void setOnInvalidateUi(OnInvalidateUi onInvalidateUi){
        mOnInvalidateUi = onInvalidateUi;
    }

    public IabHelper getIabHelper(){
        return mHelper;
    }

    public void removeAds(IabHelper.OnIabPurchaseFinishedListener listener){
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        mHelper.launchPurchaseFlow(mActivity, SKU_REMOVE_ADS, RC_REQUEST,
                listener, payload);
    }

    public boolean isDisposed(){
        return mHelper == null;
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    // 获取应用内商品结果的回调
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.anchor("Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            // 获取失败，弹出错误对话框
            if (result.isFailure()) {
                Toast.show("应用内商品获取失败: " + result);
                return;
            }

            Log.anchor("Query inventory was successful.");
            Log.anchor("应用内商品获取成功.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            // 交付购买到的汽油，如果获得了汽油，则及时加入油箱
            Purchase gasPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.anchor("We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_REMOVE_ADS), mConsumeFinishedListener);
                return;
            }

            updateUi();
//            setWaitScreen(false);
            Log.anchor("Initial inventory query finished; enabling main UI.");
        }
    };


    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.anchor("Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            // 因为只有一个sku='gas'，所以没有做检查
            // 如果有多个sku，别忘了做检查处理
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.anchor("Consumption successful. Provisioning.");
                mTank = 1;
                saveData();
            }
            else {
                Toast.show("消费错误: " + result);
            }
            updateUi();
//            setWaitScreen(false);
            Log.anchor("End consumption flow.");
        }
    };

    private void updateUi(){
//        Toast.show(mTank > 0 ?
//                "广告移除成功！" : "出错！");
        if(mOnInvalidateUi != null){
            mOnInvalidateUi.onInvalidate();
        }
    }


    /** Verifies the developer payload of a purchase. */
    public boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }


    void saveData() {

        /*
         * WARNING: on a real application, we recommend you save data in a secure way to
         * prevent tampering. For simplicity in this sample, we simply store the data using a
         * SharedPreferences.
         * 警告：在实际开发中，推荐使用更安全的方式存储，防止数据被篡改
         */

        SharedPreferences.Editor spe = getPreferences(Context.MODE_PRIVATE).edit();
        spe.putInt("tank", mTank);
        spe.commit();
        Log.anchor("Saved data: tank = " + String.valueOf(mTank));
    }

    void loadData() {
        // 应用默认初始化2个单位的油量，用完后将提示支付购买油量
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        mTank = sp.getInt("tank", 2);
        Log.anchor("Loaded data: tank = " + String.valueOf(mTank));
    }

    private SharedPreferences getPreferences(int mode){
        return mActivity.getPreferences(mode);
    }

    public void flagEndAsync() {
        if (mHelper != null) mHelper.flagEndAsync();
    }

    public void dispose(){
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
