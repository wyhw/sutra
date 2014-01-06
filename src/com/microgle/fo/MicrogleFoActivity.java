package com.microgle.fo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.microgle.fo.R;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.Menu;  
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * @author frankiewei
 * ViewPager控件使用的Demo.
 * @param <MainActivity>
 */
@SuppressLint("HandlerLeak")
public class MicrogleFoActivity<MainActivity> extends Activity implements OnPageChangeListener {
	
	private Context mContext;
	
	private static final String[] ASSERTS_RAW_TEXTS = {
		"pxxyps",
		"amtf",
		"gsypmp",
		"ysf",
		"wssl",
		"dcw",
		"xj",
		"jgj",
		"dbz",
		"dfdsly",
		"wmjss"
		//"ysz"
	};
	private static final String[] MP3_FROM_URLS = {
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload13/%E6%99%AE%E8%B4%A4%E8%8F%A9%E8%90%A8%E8%A1%8C%E6%84%BF%E5%93%81.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload6/wuliang_shoujing_wuxing.mp3",
		"http://file.goodweb.cn/web/d05/PMP04.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload34/%E8%8D%AF%E5%B8%88%E7%BB%8F.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload15/%E6%96%87%E6%AE%8A%E5%B8%88%E5%88%A9%E5%8F%91%E6%84%BF%E7%BB%8F.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload19/%E5%9C%B0%E8%97%8F%E8%8F%A9%E8%90%A8%E6%9C%AC%E6%84%BF%E7%BB%8F.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload43/%E5%BF%83%E7%BB%8F_%E5%8A%A0%E9%85%8D%E4%B9%90.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload7/jinggangjing_huiping.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload14/DBZ16.mp3", 
		//ERR"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload/dabeizhou_88c.mp3",
		"http://buddha.goodweb.cn/music/musicdownload_all/musicdownload2/LYZ10.mp3"
		//"ysz"
	};
	private static final String[] ASSERTS_RAW_NAMES = {
		"普贤行愿品（偈颂）",
		"阿弥陀佛四十八大愿",
		"观世音普门品（偈颂）",
		"药师佛（咒）",
        "文殊菩萨",
        "地藏王菩萨",
        "心经",
        "金刚经",
        "大悲咒（经）",
        "楞严咒（经）",
        "维摩诘所说（经）"
        //"药师咒"
	};
	
	private static final int INDEX_DEF_TINGFOSHUOFA = 1;
	private static final int INDEX_FO_START = 2;
	private static final int INDEX_DEF_GO_PAGE = 13;
	private static final int INDEX_DEF_SET_FONT = 14;
	private static final int INDEX_DEF_CLEAN_MP3 = 15;
	private static final int INDEX_DEF_CONTACT_US = 16;
	private static final int INDEX_DEF_UPDATE = 17;

	private static final int INDEX_PLAY_PREV = 1;
	private static final int INDEX_PLAY_NEXT = 2;
	private static final int INDEX_PLAY_PAUSE = 3;
	private static final int INDEX_PLAY_START = 4;
	private static final int INDEX_PLAY_END = 5;
	private static final int INDEX_CHANGE_END = 6;
	private static final int INDEX_NEW_FOYIN = 7;
    
	private Menu mMenu;
	private ViewPager mViewPager;
	
	/**
	 * 适配器.
	 */
	private ViewPagerAdapter mViewPagerAdapter;
	
	/**
	 * 数据源.
	 */
	private JSONArray mJsonArray;
	
	private TextView mSeekText;
	
	private int fontSizeOff = 0;
	private int currentIndex = 0;
	private int currentPageIndex = 0;
	
	private MediaPlayer mMediaPlayer;
	private boolean onPlaySound = false;
	
	private static final int MENU_STATUS_DEFAULT = 0;
	private static final int MENU_STATUS_PALY = 1;
	private static final int MENU_STATUS_PALY_PAUSE = 2;
	private int menuStatus = 0;
	private boolean onPlayPause = false;
	private static int SEEK_STEP_MS = 1000;
	private static final int SEEK_STATUS_STOP = 0;
	private static final int SEEK_STATUS_PREV = 1;
	private static final int SEEK_STATUS_NEXT = 2;
	private int seekStatus = 0;
	
	protected static final int PLAYSEEKIDENTIFIER = 0x101; 
	protected static final int PLAYSTATUSIDENTIFIER = 0x102;
	protected static final int DOWNLOADPECENTIDENTIFIER = 0x103;
	protected static final int DOWNLOADERRORIDENTIFIER = 0x104;
	protected static final int DOWNLOADEXISTSIDENTIFIER = 0x105;
	protected static final int DOWNLOADEDIDENTIFIER = 0x106;
	protected static final int OPENOPTIONSMENU = 0x107;

	private static final int REQUEST_SAVE = 0;

	private static final int REQUEST_LOAD = 0;
	private Thread mPlayThread;
	private Thread mPlayStatusThread;
	
	private boolean onDownload = false;
	private boolean onChangeDownload = false;
	private boolean onCleanMp3 = false;
	private Thread mDownloadThread;
	
	@SuppressLint("HandlerLeak")
	Handler playHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {
	             case MicrogleFoActivity.PLAYSEEKIDENTIFIER:
	            	 if(mMediaPlayer != null && mPlayThread != null) {
            			 int seek = mMediaPlayer.getCurrentPosition();
            			 if (seekStatus == SEEK_STATUS_NEXT) {
            				 int mDuration = mMediaPlayer.getDuration();
            				 if (seek + SEEK_STEP_MS <= mDuration) {
            					mMediaPlayer.seekTo(seek + SEEK_STEP_MS);
            					mSeekText.setText(milliSecondsToTimer(seek + SEEK_STEP_MS));
                  				break;
            				 }
            			 } else if (seekStatus == SEEK_STATUS_PREV) {
            				 if (seek >= SEEK_STEP_MS) {
            					 mMediaPlayer.seekTo(seek - SEEK_STEP_MS);
             					 mSeekText.setText(milliSecondsToTimer(seek - SEEK_STEP_MS));
            					 break;
            				 }
            			 }
            			 releasePlayThread();
         				 mMediaPlayer.start();
         				 onPlayPause = false;
         				 if (mMenu != null) mMenu.close();
          				 if (seekStatus != SEEK_STATUS_STOP)
          					 Toast.makeText(mContext, "到" + (seekStatus == SEEK_STATUS_NEXT ? "结尾" : "头") + "了，从开始位置播放", Toast.LENGTH_SHORT)
          					 	.show();
          				 else
          					 seekStatus = SEEK_STATUS_STOP;
	            	 } else {
	            		 mSeekText.setText("");
	            	 }
	            	 break;
	             case MicrogleFoActivity.PLAYSTATUSIDENTIFIER:
	            	 if(mMediaPlayer != null) {
	            		 mSeekText.setText(milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
	            	 } else {
	             		mSeekText.setText("");
	            	 }
	            	 break;
	             case MicrogleFoActivity.DOWNLOADPECENTIDENTIFIER:
	            	 mSeekText.setText(msg.arg1 + "KB" + "(" + msg.arg2 + "MB)");
	            	 break;
	             case MicrogleFoActivity.DOWNLOADEDIDENTIFIER:
	            	 mSeekText.setText("");
	              	 Toast.makeText(mContext, (!onCleanMp3 ? "下载" : "移动") + "成功", Toast.LENGTH_SHORT)
	                     .show();
	              	 if (onCleanMp3) {
	              		 FileUtils fileUtil = new FileUtils();
	              		 fileUtil.removeSDFile("download/microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex]);
	              	 }
	              	 onDownload = false;
	              	 onChangeDownload = false;
	              	 onCleanMp3 = false;
	              	 releaseDownloadThread();
	            	 break;
	             case MicrogleFoActivity.DOWNLOADEXISTSIDENTIFIER:
	            	 mSeekText.setText("");
	            	 //Toast.makeText(mContext, "文件已经存在", Toast.LENGTH_SHORT)
                     //.show();
		             onDownload = false;
		             onChangeDownload = false;
	              	 onCleanMp3 = false;
		             releaseDownloadThread();
		             new AlertDialog.Builder(MicrogleFoActivity.this)
		    		    .setTitle("警告")
		    		    .setMessage("原来的音频（可能是坏音频）将被删除")
		    		    .setNegativeButton("不下了", null)
		    		    .setPositiveButton("我要换新的", new DialogInterface.OnClickListener() {
		    	            public void onClick(DialogInterface dialog, int which) {
		    	        		onChangeDownload = true;
		    	        		prepareDownload();
		    	            }
		    	         })
		    	         .show();
	            	 break;
	             case MicrogleFoActivity.DOWNLOADERRORIDENTIFIER:
	            	 mSeekText.setText("");
	            	 Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT)
                     .show();
		             onDownload = false;
		             onChangeDownload = false;
	              	 onCleanMp3 = false;
		             releaseDownloadThread();
	            	 break;
	             case MicrogleFoActivity.OPENOPTIONSMENU:
	 	            openOptionsMenu();
	             	break;
             }
             super.handleMessage(msg);   
        }   
	};
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);  
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        
        mSeekText = (TextView)findViewById(R.id.app_seek);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
    	mViewPager.setOnPageChangeListener(this);

    	LocaleConfiguration configuration = new LocaleConfiguration();
    	configuration.key = "fo_page_conf";
    	readConfiguration(this, configuration);
	    if (configuration != null) {
	    	fontSizeOff = configuration.fontOffSize;
	    	currentIndex = configuration.index;
	    	currentPageIndex = configuration.pageIndex;
	    }
	    setupViews(currentIndex);

        if (checkWifi()) {
        	checkUpdate();
        	UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
        }
    }

    private void setupViews(int index){
    	currentIndex = index;
    	//初始化JSonArray,给ViewPageAdapter提供数据源用.
    	AssetManager am = getAssets();
    	InputStream inputStream = null;
    	ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
        	byte[] buffer = new byte[1024];
            inputStream = am.open(ASSERTS_RAW_TEXTS[currentIndex]);
            int len=-1;
            while ((len = inputStream.read(buffer)) != -1){
            	outSteam.write(buffer, 0, len); 
            }
            outSteam.close();
            inputStream.close();
            
        } catch (IOException e) {   
            e.printStackTrace();   
        }
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int viewHeight = wm.getDefaultDisplay().getHeight();
        int viewWidth = wm.getDefaultDisplay().getWidth();
        //1280 720 ==  42  
        int line = (int)(viewHeight / ((viewWidth * 480 / 320) / 20));//((viewWidth * ) / 18));
        String article = new String(outSteam.toByteArray());
        String[] lines = article.split("\n");
    	mJsonArray = new JSONArray();
    	int pageCount = lines.length;
    	String name = ASSERTS_RAW_NAMES[currentIndex];
    	String pageText = "";
    	int l = 0;
    	int p = 0;
    	for(int i = 0;i< pageCount; i++){
    		pageText += lines[i] + "\n";
    		if (l == (line - 1)) {
        		JSONObject object = new JSONObject();
        		try {
        			object.put("font", fontSizeOff + "");
    				object.put("text", pageText);
    				object.put("name", name + " " + (p++ + 1));
    	    		mJsonArray.put(object);
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
        		l = 0;
        		pageText = "";
    		} else {
    			l++;
    		}
    	}
    	if (pageText.length() > 0) {
    		JSONObject object = new JSONObject();
    		try {
    			object.put("font", fontSizeOff + "");
				object.put("text", pageText);
				object.put("name", name + " " + (p + 1));
	    		mJsonArray.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}

    	mSeekText.setText("");
    	mViewPagerAdapter = new ViewPagerAdapter(this, mJsonArray);
    	mViewPager.setAdapter(mViewPagerAdapter);
    	
    	if (currentPageIndex < mViewPagerAdapter.getCount()) {
    		mViewPager.setCurrentItem(currentPageIndex);
    	}
    	
    	writeLocalConfiguration();
    	
    }
    
    @SuppressLint("SdCardPath")
	private void playSound(){
    	releaseMediaPlayer();
    	int resId = getResources().getIdentifier(ASSERTS_RAW_TEXTS[currentIndex], 
    			"raw", getPackageName());
    	if (resId == 0) {
    		String mp3Path = Environment.getExternalStorageDirectory().getPath() 
    				+ "/download/microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex];
			Uri playUri = Uri.parse("file://" + mp3Path);
		
			mMediaPlayer = MediaPlayer.create(mContext, playUri);
			if (mMediaPlayer == null) {
				//Toast.makeText(mContext, "抱歉，还没有这个音频", Toast.LENGTH_SHORT)
			 	//.show();
    			onPlaySound = false;
    			prepareDownload();
    			return;
			}
    	} else {
    		mMediaPlayer = MediaPlayer.create(mContext, resId);
    	}
    	mMediaPlayer.setLooping(true);  
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override  
            public void onCompletion(MediaPlayer mp) {
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
            @Override  
            public void onSeekComplete(MediaPlayer mp) {
            	mSeekText.setText(milliSecondsToTimer(mp.getCurrentPosition()));
            }
        });
        mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
        		onPlaySound = false;
        		//stop play
        		if (mp.isPlaying())
        			mp.stop();
        		mSeekText.setText("抱歉");
				return false;
			}
        });
        mMediaPlayer.start();
        releasePlayStatusThread();
        mPlayStatusThread = new Thread(new playStatusThread());
        mPlayStatusThread.start();
    }
    
    private void prepareDownload() {
    	if(!android.os.Environment.getExternalStorageState()
    			.equals(android.os.Environment.MEDIA_MOUNTED)) {
    		Toast.makeText(mContext, "抱歉，需要存储卡(SDCard)来保存音频及播放", Toast.LENGTH_SHORT)
		 	.show();
    		return;
    	}
    	if (checkWifi()) {
			downloadMp3();
		} else {
			new AlertDialog.Builder(MicrogleFoActivity.this)
		    .setTitle("非 Wifi 网络下载警告")
		    .setMessage("请使用 Wifi 网络下载音频，否则网络流量费用很高")
		    .setNegativeButton("不下了", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	onDownload = false;
	            	onChangeDownload = false;
	            }
	         })
	        .setNeutralButton("本地选择", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	findMp3OnLocation();
	            }
	         })
		    .setPositiveButton("马上下载", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	downloadMp3();
	            }
	         })
	         .show();
		}
    }
    
    private void downloadMp3() {
    	final EditText downloadServer = new EditText(this);
    	downloadServer.setText(MP3_FROM_URLS[currentIndex]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("可换成你喜欢的音频网址").setView(downloadServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	onDownload = false;
    	            	onChangeDownload = false;
    	            }
    	         });
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (onChangeDownload) {
            		playEnd();
            	}
                String mp3Url = downloadServer.getText().toString();
                downloadThread t = new downloadThread();
                t.urlStr = mp3Url;
                t.path = "download/";
                t.fileName = "microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex];
                t.willChanged = onChangeDownload;
                onDownload = true;
                releaseDownloadThread();
                mDownloadThread = new Thread(t);
                mDownloadThread.start();
            }
        }).setNeutralButton("本地选择", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	findMp3OnLocation();
            }
         });
        builder.show();
    }
    private void findMp3OnLocation() {
    	Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, 
        		Environment.getExternalStorageDirectory() + "/");
        
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        
        //alternatively you can set file filter
        //intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "png" });
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);
        startActivityForResult(intent, REQUEST_SAVE);
    }
    
    public synchronized void onActivityResult(final int requestCode,
            int resultCode, final Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                    /*if (requestCode == REQUEST_SAVE) {
                            System.out.println("Saving...");
                    } else if (requestCode == REQUEST_LOAD) {
                            System.out.println("Loading...");
                    }*/
            	if (onChangeDownload) {
            		playEnd();
            	}
            	String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                
                localFileThread t = new localFileThread();
                t.fromPath = filePath;
                t.path = "download/";
                t.fileName = "microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex];
                onDownload = true;
                releaseDownloadThread();
                mDownloadThread = new Thread(t);
                mDownloadThread.start();
             	//Toast.makeText(mContext, "选择本地文件成功", Toast.LENGTH_SHORT)
                //.show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                    //Logger.getLogger(AccelerationChartRun.class.getName()).log(
                      //              Level.WARNING, "file not selected");

            	onDownload = false;
            	onChangeDownload = false;
            	Toast.makeText(mContext, "选择本地文件不成功", Toast.LENGTH_SHORT)
               .show();
            }

    }
    private String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
 
        // Convert total duration into time
           int hours = (int)( milliseconds / (1000*60*60));
           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
           // Add hours if there
           if(hours > 0){
               finalTimerString = hours + ":";
           }
 
           // Prepending 0 to seconds if it is one digit
           if(seconds < 10){
               secondsString = "0" + seconds;
           }else{
               secondsString = "" + seconds;}
 
           finalTimerString = finalTimerString + minutes + ":" + secondsString;
 
        // return timer string
        return finalTimerString;
    }
    
    private void writeLocalConfiguration() {
    	LocaleConfiguration configuration = new LocaleConfiguration();
    	configuration.key = "fo_page_conf";
    	
    	configuration.fontOffSize = fontSizeOff;
    	configuration.index = currentIndex;
    	configuration.pageIndex = currentPageIndex;
    	writeConfiguration(this, configuration);
    }
    
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (onPlaySound) {
        	if (onPlayPause) {
        		if (menuStatus != MENU_STATUS_PALY_PAUSE) {
        			createPlayMenu(menu);
                	menuStatus = MENU_STATUS_PALY_PAUSE;
        		}
        	} else {
        		if (menuStatus != MENU_STATUS_PALY) {
        			createPlayMenu(menu);
                	menuStatus = MENU_STATUS_PALY;
        		}
        	}
        } else {
        	if (menuStatus != MENU_STATUS_DEFAULT) {
        		createDefaultMenu(menu);
            	menuStatus = MENU_STATUS_DEFAULT;
            	onPlayPause = false;
        	}
        }
        return true;
    }
    
    private void createDefaultMenu(Menu menu) {
    	menu.clear();
        menu.add(1, 1, 1, "听佛音");
    	int itemId = INDEX_FO_START;
        menu.add(itemId, itemId, itemId++, "普贤菩萨");
        menu.add(itemId, itemId, itemId++, "阿弥陀佛"); 
        menu.add(itemId, itemId, itemId++, "观世音菩萨");
        menu.add(itemId, itemId, itemId++, "药师佛");
        menu.add(itemId, itemId, itemId++, "文殊菩萨");
        menu.add(itemId, itemId, itemId++, "地藏王菩萨");
        menu.add(itemId, itemId, itemId++, "心经");
        menu.add(itemId, itemId, itemId++, "金刚经");
        menu.add(itemId, itemId, itemId++, "大悲咒");
        menu.add(itemId, itemId, itemId++, "楞严咒");
        menu.add(itemId, itemId, itemId++, "维摩诘所说");
        //menu.add(0, itemId, itemId++, "药师咒");
        
        menu.add(itemId, itemId, itemId++, "跳转页码");
        menu.add(itemId, itemId, itemId++, "设置字体");
        menu.add(itemId, itemId, itemId++, "清除音频");
        menu.add(itemId, itemId, itemId++, "联系造者");
        menu.add(itemId, itemId, itemId++, "获取新版");
    }
    
    private void createPlayMenu(Menu menu) {
    	menu.clear();
    	int itemId = 1;
        menu.add(itemId, itemId, itemId++, "快退");
        menu.add(itemId, itemId, itemId++, "快进");
        menu.add(itemId, itemId, itemId++, onPlayPause ? "继续" : "暂停");
        menu.add(itemId, itemId, itemId++, "重播");
        menu.add(itemId, itemId, itemId++, "结束");
        menu.add(itemId, itemId, itemId++, "为本经换首佛音");
        menu.add(itemId, itemId, itemId++, "推荐佛音给造者");
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
        mMenu = menu;
    	createDefaultMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    private boolean defaultMenuItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
        switch(itemId){
        	case INDEX_DEF_TINGFOSHUOFA:
        		onPlaySound = true;
        		playSound();
        		break;
        	case INDEX_DEF_GO_PAGE:
        		final EditText inputServer = new EditText(this);
        		inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入要跳转的页码").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       //inputServer.getText().toString();
                    	int toPage = Integer.valueOf(inputServer.getText().toString());
                    	if (toPage > mViewPagerAdapter.getCount()) toPage = mViewPagerAdapter.getCount();
                    	if (toPage < 1) toPage = 1;
                    	currentPageIndex = toPage - 1;
                    	mViewPager.setCurrentItem(currentPageIndex);
                    	writeLocalConfiguration();
                     }
                });
                builder.show();
        		break;
	        case INDEX_DEF_SET_FONT:
	        	resize();
	        	break;
	        case INDEX_DEF_CLEAN_MP3:
	        	cleanMp3();
	        	break;
	        case INDEX_DEF_CONTACT_US:
	        	UMFeedbackService.openUmengFeedbackSDK(this);
	        	break;
	        case INDEX_DEF_UPDATE:
	        	checkUpdate();
	        	break;
	        default:
	        	if (itemId > 1 && itemId < (ASSERTS_RAW_TEXTS.length + INDEX_FO_START) && currentIndex != (itemId - INDEX_FO_START)) {
		        	currentPageIndex = 0;
	        		setupViews(itemId - INDEX_FO_START);
	        	}
	        	break;
        } 
        return false;
    }
    
    private boolean playMenuItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
        switch(itemId){
        	case INDEX_PLAY_PREV:
        		onPlayPause = true;
        		seekStatus = SEEK_STATUS_PREV;
        		if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
        		releasePlayThread();
    			mPlayThread = new Thread(new playSeekThread());
    			mPlayThread.start();
    			break;
        	//case INDEX_PLAY_NORMAL:
        	//	onPlayPause = false;
        	//	seekStatus = SEEK_STATUS_STOP;
        	//	break;
        	case INDEX_PLAY_NEXT:
        		onPlayPause = true;
        		seekStatus = SEEK_STATUS_NEXT;
        		if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
        		releasePlayThread();
    			mPlayThread = new Thread(new playSeekThread());
    			mPlayThread.start();
    			break;
        	case INDEX_PLAY_START:
        		onPlayPause = false;
        		setPlayStatusNormal();
        		mMediaPlayer.pause();
        		mMediaPlayer.seekTo(0);
        		mMediaPlayer.start();
        		break;
        	case INDEX_PLAY_PAUSE:
        		if (seekStatus != SEEK_STATUS_STOP) {
        			onPlayPause = false;
        			seekStatus = SEEK_STATUS_STOP;
        			break;
        		}
        		//Pause or Resume play 
        		if (onPlayPause) {
        			mMediaPlayer.start();
        		} else {
        			mMediaPlayer.pause();
        		}
        		onPlayPause = !onPlayPause;
        		break;
        	case INDEX_PLAY_END:
        		playEnd();
        		break;
        	case INDEX_CHANGE_END:
        		new AlertDialog.Builder(MicrogleFoActivity.this)
    		    .setTitle("警告")
    		    .setMessage("修改后原来的音频将被删除")
    		    .setNegativeButton("不换了", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	            	onDownload = false;
    	            	onChangeDownload = false;
    	            }
    	         })
    		    .setPositiveButton("我要换新的", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	        		onChangeDownload = true;
    	        		prepareDownload();
    	            }
    	         })
    	         .show();
        		break;
        	case INDEX_NEW_FOYIN:
        		UMFeedbackService.openUmengFeedbackSDK(this);
        		break;
        	default:
        		break;
        }
        return false;
    }
    
    private void playEnd() {
		onPlaySound = false;
		onPlayPause = false;
		seekStatus = SEEK_STATUS_STOP;
		//stop play
		releaseMediaPlayer();
		setPlayStatusNormal();
		releasePlayStatusThread();
		mSeekText.setText("");
    }
    
    private void cleanMp3() {
    	new AlertDialog.Builder(MicrogleFoActivity.this)
	    .setTitle("提示")
	    .setMessage("确认删除音频，请选择音频操作范围（改名后可在本机找到）")
	    .setPositiveButton("仅本经", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	new AlertDialog.Builder(MicrogleFoActivity.this)
        	    .setTitle("提示")
        	    .setMessage("再确认，删了就没了")
        	    .setNeutralButton("不删了", null)
        	    .setPositiveButton("马上删", new DialogInterface.OnClickListener() {
        	    	public void onClick(DialogInterface dialog, int which) {
        	    		FileUtils fileUtil = new FileUtils();
        	    		fileUtil.removeSDFile("download/microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex]);
        	    		Toast.makeText(mContext, "删除完毕", Toast.LENGTH_SHORT).show();
        	    	}
        	    }).show();
            }
         })
        .setNeutralButton("改名", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	FileUtils fileUtil = new FileUtils();
            	
            	localFileThread t = new localFileThread();
                t.fromPath = fileUtil.SDPATH + "download/microgle_fo_" + ASSERTS_RAW_TEXTS[currentIndex];
                t.path = "download/";
                t.fileName = "mf_" + ASSERTS_RAW_NAMES[currentIndex] + ".mp3";
                int i = 1;
                while (fileUtil.isFileExist(t.path + t.fileName)) {
                	t.fileName = "mf_" + ASSERTS_RAW_NAMES[currentIndex] + "(" + (i++) + ")" + ".mp3";
                }
                onDownload = true;
                releaseDownloadThread();
                Toast.makeText(mContext, "开始把音频移到" + t.fileName + ",目录为" + fileUtil.SDPATH + t.path, Toast.LENGTH_SHORT)
                .show();
                onCleanMp3 = true;
                mDownloadThread = new Thread(t);
                mDownloadThread.start();
            }
         })
	    .setNegativeButton("全清理", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	new AlertDialog.Builder(MicrogleFoActivity.this)
        	    .setTitle("提示")
        	    .setMessage("再确认，删了就没了")
        	    .setNeutralButton("再想想", null)
        	    .setPositiveButton("全部删", new DialogInterface.OnClickListener() {
        	    	public void onClick(DialogInterface dialog, int which) {
                    	FileUtils fileUtil = new FileUtils();
                    	int indexLen = ASSERTS_RAW_TEXTS.length;
                    	for(int i = 0; i < indexLen; i++ )
                    		fileUtil.removeSDFile("download/microgle_fo_" + ASSERTS_RAW_TEXTS[i]);
                    	Toast.makeText(mContext, "全部清理完毕", Toast.LENGTH_SHORT).show();
        	    	}
        	    }).show();
            }
         })
         .show();
    }
    
    private void setPlayStatusNormal() {
		//Resume play
		releasePlayThread();
		seekStatus = SEEK_STATUS_STOP;
    }
    
    private void checkUpdate() {
    	UmengUpdateAgent.setUpdateAutoPopup(false);
    	UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
    	        @Override
    	        public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
    	            switch (updateStatus) {
    	            case 0: // has update
    	                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
    	                break;
    	            case 1: // has no update
    	                Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT)
    	                        .show();
    	                break;
    	            case 2: // none wifi
    	                Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
    	                        .show();
    	                break;
    	            case 3: // time out
    	                Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT)
    	                        .show();
    	                break;
    	            }
    	        }
    	});
    	UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener(){

			@Override
			public void OnDownloadEnd(int result) {
				Toast.makeText(mContext, "下载结果 : " + result , Toast.LENGTH_SHORT)
				.show();
			}
			
		});
		
		UmengUpdateAgent.update(mContext);
    }
    
    class playSeekThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				message.what = MicrogleFoActivity.PLAYSEEKIDENTIFIER;
				MicrogleFoActivity.this.playHandler.sendMessage(message);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}                   
			}
		}
	}
    
    class playStatusThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				message.what = MicrogleFoActivity.PLAYSTATUSIDENTIFIER;
				MicrogleFoActivity.this.playHandler.sendMessage(message);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}                   
			}
		}
	}
    
    public boolean onOptionsItemSelected(MenuItem item){
    	if (onDownload || onChangeDownload) {
    		new AlertDialog.Builder(this)
            /**设置标题**/
            .setTitle("提示")
            /**设置icon**/
            //.setIcon(android.R.drawable.alert_dark_frame)
            /**设置内容**/
            .setMessage("正在下载音频，请稍后...")
            .setPositiveButton("继续" + (onCleanMp3 ? "移动" : "下载"), new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int which) {
            	}})
            .setNeutralButton("取消" + (onCleanMp3 ? "移动" : "下载"), new DialogInterface.OnClickListener(){
            	public void onClick(DialogInterface dialog, int which) {
                	if (mDownloadThread != null)
                		mDownloadThread.interrupt();
            	}})
            .show();
    		return false;
    	}
    	if (onPlaySound) {
    		return playMenuItemSelected(item);
    	} else {
    		return defaultMenuItemSelected(item);
    	} 
    }
    
    @Override
    public void onOptionsMenuClosed(Menu menu) {
    	super.onOptionsMenuClosed(menu);
     	if (seekStatus == SEEK_STATUS_NEXT || seekStatus == SEEK_STATUS_PREV)
     	{
			Message message = new Message();
			message.what = MicrogleFoActivity.OPENOPTIONSMENU;
			MicrogleFoActivity.this.playHandler.sendMessage(message);
     	}
    }
    
    private boolean checkWifi() {
    	WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);  
        WifiInfo wifiInfo = wifiM.getConnectionInfo();  
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();  
        if (wifiM.isWifiEnabled() && ipAddress != 0) {  

        } else {  
            return false; 
        }
        return true;
    }
    
    class downloadThread implements Runnable {
    	public String urlStr;
    	public String path;
    	public String fileName;
    	public boolean willChanged = false;
    	
		public void run() {
			try {
	    		URL url = new URL(urlStr);
	    		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	    		urlConnection.setRequestMethod("GET");
	    		//urlConnection.setDoOutput(true);
	    		urlConnection.connect();
	    		
	    		FileUtils fileUtils = new FileUtils();  
	            
	            if(fileUtils.isFileExist(path + fileName)){
	            	if (willChanged) {
	            		fileUtils.removeSDFile(path + fileName);
	            	} else {
	            		//return 1
	            		Message message = new Message();
	    				message.what = MicrogleFoActivity.DOWNLOADEXISTSIDENTIFIER;
	    				MicrogleFoActivity.this.playHandler.sendMessage(message);
	    				return;
	            	}
	            }
	            fileUtils.createSDDir(path);
	    		File file = fileUtils.createSDFile(path + fileName); 
	    		FileOutputStream fileOutput = new FileOutputStream(file);
	    		InputStream inputStream = urlConnection.getInputStream();
	    		long totalSize = urlConnection.getContentLength();
	    		//long downloadedSize = 0;

	    		byte[] buffer = new byte[1024];
	    		int bufferLength = 0;
	    		int loop = 0;
	    		int totalMB = (int) totalSize / (1024 * 1024);
	    		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	    			if (Thread.currentThread().isInterrupted()) {
	    				fileOutput.close();
	    				urlConnection.disconnect();
	    				inputStream.close();
	    				fileUtils.removeSDFile(path + fileName);
	            		Message message = new Message();
	    				message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
	    				MicrogleFoActivity.this.playHandler.sendMessage(message);
	    				return;
	    			}

	    			fileOutput.write(buffer, 0, bufferLength);
	    			//downloadedSize += bufferLength;
	    			if (totalSize > 0) {
	            		Message message = new Message();
	    				message.what = MicrogleFoActivity.DOWNLOADPECENTIDENTIFIER;
	    				message.arg1 = loop++;
	    				message.arg2 = totalMB;
	    				MicrogleFoActivity.this.playHandler.sendMessage(message);
	    			}
	    			
	    		}
	    		
	    		fileOutput.close();
	    		inputStream.close();
	    	} catch (MalformedURLException e) {
	    		e.printStackTrace();
	    		//return -1;
        		Message message = new Message();
				message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
				MicrogleFoActivity.this.playHandler.sendMessage(message);
				return;
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    		//return -1;
        		Message message = new Message();
				message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
				MicrogleFoActivity.this.playHandler.sendMessage(message);
				return;
	    	}
	    	//return 0;
    		Message message = new Message();
			message.what = MicrogleFoActivity.DOWNLOADEDIDENTIFIER;
			MicrogleFoActivity.this.playHandler.sendMessage(message);
		}
	}
    
    class localFileThread implements Runnable {
    	public String fromPath;
    	public String path;
    	public String fileName;
    	
		public void run() {
			File file = new File(fromPath);
	    	if (file.exists() && file.canRead()) {
	    		FileUtils fileUtils = new FileUtils();
	    		fileUtils.removeSDFile(path + fileName);
	    		InputStream inputStream = null;
	    		FileOutputStream fileOutput = null;
	    		
	    		try {
	        		File fromFile = new File(fromPath);
	        		fileUtils.createSDDir(path);
	        		File toFile = fileUtils.createSDFile(path + fileName);
	        		
	        		inputStream = new FileInputStream(fromFile);
	        		
	        		fileOutput = new FileOutputStream(toFile);

	        		byte[] buffer = new byte[1024];
	        		int bufferLength = 0;
	        		int loop = 0;
	        		int totalMB = (int) (fromFile.length() / (1024 * 1024));
	        		while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	        			if (Thread.currentThread().isInterrupted()) {
	        				fileOutput.close();
	        				inputStream.close();
	        				fileUtils.removeSDFile(path + fileName);
	                		Message message = new Message();
	        				message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
	        				MicrogleFoActivity.this.playHandler.sendMessage(message);
	        				return;
	        			}

	        			fileOutput.write(buffer, 0, bufferLength);

	            		Message message = new Message();
	    				message.what = MicrogleFoActivity.DOWNLOADPECENTIDENTIFIER;
	    				message.arg1 = loop++;
	    				message.arg2 = totalMB;
	    				MicrogleFoActivity.this.playHandler.sendMessage(message);
	        		}
	        		
	    		} catch (IOException e) {
					//e.printStackTrace();
	        		Message message = new Message();
					message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
					MicrogleFoActivity.this.playHandler.sendMessage(message);
				}
	    		finally{  
		            try {
		            	if (fileOutput != null)
		            		fileOutput.close();  
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }
		            try {
		            	if (inputStream != null)
		            		inputStream.close(); 
		            } catch (IOException e) {  
		                e.printStackTrace();  
		            }
		        } 
	    		
	    	} else {
	    		Message message = new Message();
				message.what = MicrogleFoActivity.DOWNLOADERRORIDENTIFIER;
				MicrogleFoActivity.this.playHandler.sendMessage(message);
	    	}

			Message message = new Message();
			message.what = MicrogleFoActivity.DOWNLOADEDIDENTIFIER;
			MicrogleFoActivity.this.playHandler.sendMessage(message);
	    }
    }
    
    public void resize(){
        new AlertDialog.Builder(this)
        /**设置标题**/
        .setTitle("提示")
        /**设置icon**/
        //.setIcon(android.R.drawable.alert_dark_frame)
        /**设置内容**/
        .setMessage("设置字体大小：可能有文章被截断，可以通过减小字体解决。")
        .setPositiveButton("减小", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which) {
            	fontSizeOff--;
            	setupViews(currentIndex);
        	}})
        .setNeutralButton("增大", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which) {
            	fontSizeOff += 3;
            	setupViews(currentIndex);
        	}})
        .setNegativeButton("默认", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                	fontSizeOff = 0;
                	setupViews(currentIndex);
                }})
        .show();
    }
    
    private void releaseMediaPlayer() {  
        if (mMediaPlayer != null) {
        	if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
            mMediaPlayer.release();  
            mMediaPlayer = null;  
        }
    }
    
    private void releaseThread(Thread t) {
    	if (t == null) return;
    	if (!t.isInterrupted()) 
    		t.interrupt();
    }
    
    private void releasePlayThread() {
        releaseThread(mPlayThread);
        mPlayThread = null;
    }
    
    private void releasePlayStatusThread() {
        releaseThread(mPlayStatusThread);
        mPlayStatusThread = null;
    }
    
    private void releaseDownloadThread() {
        releaseThread(mDownloadThread);
        mDownloadThread = null;
    }
    
    /**
     * 屏幕旋转时调用此方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(MicrogleFoActivity.this, "现在是竖屏", Toast.LENGTH_SHORT).show();
        	setupViews(currentIndex);
        }
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            //Toast.makeText(MicrogleFoActivity.this, "现在是横屏", Toast.LENGTH_SHORT).show();
        	setupViews(currentIndex);
        }
    }
  
    @Override  
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        releasePlayThread();
        releasePlayStatusThread();
        releaseDownloadThread();
    }
    
    @Override
    public void onPageScrollStateChanged(int arg0) {
      // TODO Auto-generated method stub
      
    }
    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
      // TODO Auto-generated method stub
      
    }
    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
      //设置底部小点选中状态
    	currentPageIndex = arg0;

    	LocaleConfiguration configuration = new LocaleConfiguration();
    	configuration.key = "fo_page_conf";
    	configuration.fontOffSize = fontSizeOff;
    	configuration.index = currentIndex;
    	configuration.pageIndex = currentPageIndex;
    	writeConfiguration(this, configuration);
    }
    
    private static final String PREFERENCES = "launcher.preferences";
    private static class LocaleConfiguration {  
        public String key;  
        public int fontOffSize = 0;  
        public int index = 0;  
        public int pageIndex = 0; 
    }
    
    private static void writeConfiguration(Context context,
			LocaleConfiguration configuration) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(context.openFileOutput(PREFERENCES,
					MODE_PRIVATE));
			out.writeUTF(configuration.key);
			out.writeInt(configuration.fontOffSize);
			out.writeInt(configuration.index);
			out.writeInt(configuration.pageIndex);
			out.flush();
		} catch (FileNotFoundException e) {
			// Ignore
		} catch (IOException e) {
			// noinspection ResultOfMethodCallIgnored
			context.getFileStreamPath(PREFERENCES).delete();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}
    
    private static void readConfiguration(Context context,
			LocaleConfiguration configuration) {
		DataInputStream in = null;
		try {
			in = new DataInputStream(context.openFileInput(PREFERENCES));
			configuration.key = in.readUTF();
			configuration.fontOffSize = in.readInt();
			configuration.index = in.readInt();
			configuration.pageIndex = in.readInt();
		} catch (FileNotFoundException e) {
			// Ignore
			configuration = null;
		} catch (IOException e) {
			// Ignore
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

}