/**
 * 
 */
package org.lee.android.doodles;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 负责呈现没有获取到数据时的提示 </br> 通常作为ListView、GridView等呈现网络数据的布局，在其获取数据失败时的提示页面； <li>
 * 方便loading和非loading状态的提示； <li>方便获取数据结果的提示，如获取数据为空，获取数据失败，网络错误等提示。 </br></br>
 * 
 * @author liruiyu E-mail:allnet@live.cn
 * @version 创建时间：2014-5-14 类说明
 */
public class MessageView extends RelativeLayout {

	private View mProgress;
    private View mMessageLayout;
	private TextView mMessageText;
	private TextView mReTryText;
    private boolean mRetryEnable = false;

	public MessageView(Context context) {
		super(context);
	}

	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MessageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    @Override
    protected void onFinishInflate() {
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.messageview, this);
        mProgress = view.findViewById(android.R.id.progress);
        mMessageLayout = view.findViewById(R.id.MessageViewLayout);
        mMessageText = (TextView) view.findViewById(R.id.MessageText);
        mReTryText = (TextView) view.findViewById(R.id.RetryText);
    }

	/**
	 * 设置提示的消息内容 </p>该内容会显示在布局中心，如果当前是loading状态，则显示为loading的消息，
	 * 如果非loading状态，则作为提示消息。
	 * @param text
	 */
	public void setMessage(CharSequence text) {
		mMessageText.setText(text);
	}

	public void setOnRetryListener(OnClickListener retryListener) {
        mRetryEnable = retryListener != null;
		mReTryText.setOnClickListener(retryListener);
	}

    public void setReTryText(String text){
        mRetryEnable = TextUtils.isEmpty(text);
        mReTryText.setText(text);
    }

	public void setRetryEnable(boolean enable) {
        mRetryEnable = enable;
		mReTryText.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	/**
	 * 启动loading状态
	 */
	public void loading() {
		this.setClickable(false);
		mProgress.setVisibility(View.VISIBLE);
        mMessageLayout.setVisibility(View.GONE);
	}

	/**
	 * 停止loading状态
	 */
	public void stop() {
		this.setClickable(true);
        mProgress.setVisibility(View.GONE);
        mMessageLayout.setVisibility(VISIBLE);
        mReTryText.setVisibility(mRetryEnable ? View.VISIBLE : View.GONE);
    }

    /**
     * 没网络
     */
    public void noNetwork() {
        this.setClickable(true);
        mProgress.setVisibility(View.GONE);
        mMessageLayout.setVisibility(VISIBLE);
        mMessageText.setText("网络不给力");
        mReTryText.setVisibility(mRetryEnable ? View.VISIBLE : View.GONE);
    }

}
