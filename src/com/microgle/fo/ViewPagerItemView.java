package com.microgle.fo;

import org.json.JSONException;
import org.json.JSONObject;

import com.microgle.fo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author frankiewei
 * 相册的ItemView,自定义View.方便复用.
 */
public class ViewPagerItemView extends FrameLayout {

	/**
	 * 图片的ImageView.
	 */
	private LinearLayout mContentView;
	
	/**
	 * 图片名字的TextView.
	 */
	private TextView mNameTextView;
	
	/**
	 * 图片的Bitmap.
	 */
	private Bitmap mBitmap;
	
	/**
	 * 要显示图片的JSONOBject类.
	 */
	private JSONObject mObject;
	
	private Context mContext;
	
	public ViewPagerItemView(Context context){
		super(context);
		mContext = context;
		setupViews();
	}
	
	public ViewPagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	//初始化View.
	private void setupViews(){
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.viewpager_itemview, null);
		
		mContentView = (LinearLayout)view.findViewById(R.id.article_text);
		mNameTextView = (TextView)view.findViewById(R.id.article_name); 
		addView(view);
	}

	/**
	 * 填充数据，共外部调用.
	 * @param object
	 */
	public void setData(JSONObject object){
		this.mObject = object;
		try {
			String text = object.getString("text");
			String name = object.getString("name");
			String fontSizeOff = object.getString("font");

	        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            int viewHeight = wm.getDefaultDisplay().getHeight();
            int viewWidth = wm.getDefaultDisplay().getWidth();
	        //1280 720 ==  42 
            int fontOffSize = Integer.valueOf(fontSizeOff);
	        int size = (int)(viewWidth / 16.9)  + fontOffSize ;
	        int line = (int)(viewHeight / ((viewWidth * 480 / 320) / 20));//((viewWidth * ) / 18));
	        //480 320== 18
			//measureTextWidth(mContentView, text);

	        //Log.i("WindowH", "WindowH" + viewHeight + "W" + viewWidth+"S" + size + "L"+line);
			TextViewMultilineEllipse tvMultilineEllipse = new TextViewMultilineEllipse(mContext);  
	        //TextView tvMultilineEllipse = new TextView(mContext);
			//tvMultilineEllipse.setEllipsis("...");//...替换剩余字符串 
	        tvMultilineEllipse.setTextSize(size);//设置文字大小  
	        tvMultilineEllipse.setTextColor(Color.WHITE);  
	        tvMultilineEllipse.setText(text);//设置文本
	        //在代码里添加tvMultilineEllipse,暂时不支持Layout里直接添加
	        if (fontOffSize > 0) {
	        	ScrollView scrollView = new ScrollView(mContext);
	        	scrollView.setScrollContainer(true);
	        	scrollView.setFocusable(true);
	        	scrollView.addView(tvMultilineEllipse);
	        	//tvMultilineEllipse.setMaxLines(line * 2);
	        	mContentView.addView(scrollView, mContentView.getLayoutParams());
	        } else {
	        	tvMultilineEllipse.setMaxLines(line);
				tvMultilineEllipse.setLayoutParams(mContentView.getLayoutParams());//限制TextView的宽高  
	        	mContentView.addView(tvMultilineEllipse);
	        }
	        //Log.i("WindowH", "WindowHX" + mContentView.getMeasuredHeight() + "W" + mContentView.getMeasuredWidth());
	        
			mNameTextView.setText(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
		
	/**
	 * 这里内存回收.外部调用.
	 */
	public void recycle(){
		
	}
	
	
	/**
	 * 重新加载.外部调用.
	 */
	public void reload(){
		
	}
	

    private void measureTextWidth(TextView textView,String txt){
    	//txt = "所有十方世界中，三世一切人师子，所所";
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int viewLength = wm.getDefaultDisplay().getWidth() - 10;
        Paint paint  = textView.getPaint(); 
        paint.setTextSize(textView.getTextSize()); 
        String temp = (String) TextUtils.ellipsize(txt, (TextPaint) paint, viewLength , TextUtils.TruncateAt.END); 
        textView.setText(temp);
    } 
	
}
