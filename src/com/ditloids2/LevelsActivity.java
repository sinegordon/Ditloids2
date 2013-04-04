package com.ditloids2;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A more complex demo including using a RadioGroup as "tabs" for the pager and showing the
 * dual-scrolling capabilities when a vertically scrollable element is nested inside the pager.
 */
public class LevelsActivity extends Activity implements OnClickListener, OnKeyListener {
	private RadioGroup radioGroup = null;
	private HorizontalPager pager = null;
	// ������ ��������� ����� ������� � ����������� �������� �������
	private TextView[] countViews = null;
	// ������ ��������� ����� ������� � ������� �������
	private TextView[] nameViews = null;
	// ������ ������ �������� �� ������
	private Button[] countButtons = null;
	// ������ �������� ������ �� ������
	private int checkedLevelIndex = -1;
	private static Game game = null;
	// ������ �� ��������� ������
	private AlertDialog.Builder adb = null;
	private static BitmapDrawable bmd = null;
	
	// ������ ���������� in-app billing ����� � �������
	IInAppBillingService mService;

	ServiceConnection mServiceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);	
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	   
	};
	// ����� ������ ���������� in-app billing ����� � �������
	
	
	// ������ ������� ������� �������
	final int DIALOG_EXIT = 1;
	
	protected Dialog onCreateDialog(int id) {
	    if (id == DIALOG_EXIT) {
	      AlertDialog.Builder adb = new AlertDialog.Builder(this);
	      // ���������
	      adb.setTitle(R.string.hint_title);
	      // ���������
	      adb.setMessage(R.string.hint_message);
	      // ������
	      adb.setIcon(android.R.drawable.ic_dialog_info);
	      // ������ �������������� ������
	      adb.setPositiveButton(R.string.yes, dialogClickListener);
	      // ������ ������������ ������
	      adb.setNeutralButton(R.string.no, dialogClickListener);
	      // ������� ������
	      return adb.create();
	   }
	   return super.onCreateDialog(id);
	}
	
	android.content.DialogInterface.OnClickListener dialogClickListener = new android.content.DialogInterface.OnClickListener(){
		public void onClick(DialogInterface dialog, int which){
			switch (which) {
		    // ������������� ������ - ��������� ������� (������� �����, ��� � ������ ����������))
		    case Dialog.BUTTON_POSITIVE:
		    	try {
					Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(), "unlock_levels", "inapp", null);
					PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
					startIntentSenderForResult(pendingIntent.getIntentSender(),
							   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
							   Integer.valueOf(0));
				} catch (RemoteException e) {
					Toast.makeText(null, getResources().getText(R.string.inap_error), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (SendIntentException e) {
					Toast.makeText(null, getResources().getText(R.string.inap_error), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
		        break;
		    // ����������� ������  
		    case Dialog.BUTTON_NEUTRAL:
		    	break;
		    }
		}
	};
	// ����� ������ ������� ������� �������
	
	// ���������� �������� ������
	private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener =
            new HorizontalPager.OnScreenSwitchListener() {
                @Override
                public void onScreenSwitched(final int screen) {
                	int id = getResources().getIdentifier("radio_btn_" + Integer.toString(screen), "id", getApplicationContext().getPackageName());
                	radioGroup.check(id);
                	checkedLevelIndex = screen + 1;
                }
            };
    
    // ���������� ����� �� �������������
    private final RadioGroup.OnCheckedChangeListener onCheckedChangedListener =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final RadioGroup group, final int checkedId) {
                	for(int i = 0; i < game.GetCountLevels(); i++){
                		int id = getResources().getIdentifier("radio_btn_" + Integer.toString(i), "id", getApplicationContext().getPackageName());
                		if (id == checkedId){
                			pager.setCurrentScreen(i, true);
                			checkedLevelIndex = i + 1;
                			break;
                		}
                	}
                }
            };

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels);
    	View v = findViewById(R.id.levelsLayout);
    	v.setBackgroundDrawable(bmd);
    	
    	//DisplayMetrics metrics = new DisplayMetrics();
    	//getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	//ImageView v1 = (ImageView)findViewById(R.id.imageView1);
    	//v1.setMaxWidth(metrics.widthPixels);
    	
        game.SetPauseMusic(false);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        radioGroup = (RadioGroup) findViewById(R.id.tabs);
        radioGroup.setOnCheckedChangeListener(onCheckedChangedListener);
        pager = (HorizontalPager) findViewById(R.id.horizontal_pager);
        pager.setOnScreenSwitchListener(onScreenSwitchListener);
        // ���������� ����� �� header
        ((TextView)findViewById(R.id.textView1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));
        // ���������� ������� �� ��������� ������
        adb = new AlertDialog.Builder(this);
        // ������ �������
        adb.setIcon(android.R.drawable.ic_dialog_info);
        // ������ �������������� ������ �������
        adb.setPositiveButton(R.string.yes, null);
        // ������� ������
        adb.create();
        countViews = new TextView[game.GetCountLevels()];
        nameViews = new TextView[game.GetCountLevels()];
        countButtons = new Button[game.GetCountLevels()];
        // ��������� ������� ������ ������� � �������� �� ������� � ������������� �� �����
        for(int i = 1; i < game.GetCountLevels() + 1; i++){
        	int id = getResources().getIdentifier("TextView" + Integer.toString(i), "id", getApplicationContext().getPackageName());
        	TextView countView = (TextView)findViewById(id);
        	countView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        	int idn = getResources().getIdentifier("TextViewName" + Integer.toString(i), "id", getApplicationContext().getPackageName());
        	TextView nameView = (TextView)findViewById(idn);
        	nameView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        	int idb = getResources().getIdentifier("level" + Integer.toString(i) + "button", "id", getApplicationContext().getPackageName());
        	Button but = (Button)findViewById(idb);
        	countViews[i-1] = countView;
        	nameViews[i-1] = nameView;
        	countButtons[i-1] = but;
        };
        // ���������� ����������� �������
    	for(int i = 1; i < game.GetCountLevels() + 1; i++){
    		int id = getResources().getIdentifier("level" + Integer.toString(i) +"button", "id", getApplicationContext().getPackageName());
    		findViewById(id).setOnClickListener(this);
    	}
        findViewById(R.id.arrowButton).setOnClickListener(this);
        findViewById(R.id.arrowButton).bringToFront();
        // ������������ � in-app billing �������
        bindService(new 
                Intent("com.android.vending.billing.InAppBillingService.BIND"),
                        mServiceConn, Context.BIND_AUTO_CREATE);
    }
    
    
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// ���� ������ �����
	    case R.id.arrowButton:
	    	finish();
	    	break;
	    default:
	    	// ���� ���-�� ������ (��������� �� ������ �� ������ �������� �� �������)
	    	for(int i = 1; i < game.GetCountLevels() + 1; i++){
	    		int id = getResources().getIdentifier("level" + Integer.toString(i) +"button", "id", getApplicationContext().getPackageName());
	    		// �c�� ������
	    		if (id == view.getId()){
	    			// ��������� ������� �� �������������� ������, ���� ��� ������� ���� �������
	    			if(!game.GetSaleInfo() && i > 1) {
	    				Dialog d = onCreateDialog(DIALOG_EXIT);
	    				d.show();
	    				break;
	    			};
	    			// ��������� ����������� ������
	    	    	if(game.GetLevelAccess(i)){
	    	    		// ���� �������� - ���������
	    		    	game.LoadLevel(i);
	    		    	startActivity(new Intent(LevelsActivity.this, TasksActivity.class));
	    		    	//finish();
	    		    	break;
	    	    	}
	    	    	else{
	    	    		// ���� ���������� - �������� ������������ ������� ����� ��� ������ ��� ��� ��������
	    	    		adb.setMessage("��� ������� � ����� ������ �������� �������� �� " + 
	    			    		  Integer.toString(game.GetLevelsDivisor()*(checkedLevelIndex - 1) - game.GetCountRight()) + " �������(�,��).");
	    		    	adb.show();
	    	    	}
	    		}
	    	}
	    	break;
	    }
	}
	
	// ���������� ����
	public void DrawLevelInfo() {
        // ���������� ���������� ����� � ��������
        for(int i = 1; i < game.GetCountLevels() + 1; i++) {
        	countViews[i-1].setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf"));
        	if(!game.GetLevelAccess(i)) {
        		countButtons[i-1].setBackgroundResource(R.drawable.level_lock);
        		nameViews[i-1].setVisibility(View.INVISIBLE);
            	countViews[i-1].setText("������� " + Integer.toString(i));
        	}
        	else {
        		//int drawableId = getResources().getIdentifier("level" + Integer.toString(i), "drawable", getApplicationContext().getPackageName());
        		countButtons[i-1].setBackgroundColor(getResources().getColor(R.color.bg_open_level));
        		nameViews[i-1].setVisibility(View.VISIBLE);
            	countViews[i-1].setText(Integer.toString(game.AnswersCount(i)) + " �� " + Integer.toString(game.GetLevel(i).GetDitloidsCount()));
        	}
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	   if (requestCode == 1001) {           
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	        
	      if (resultCode == RESULT_OK) {
	         try {
	        	 JSONObject jo = new JSONObject(purchaseData);
	             String sku = jo.getString("productId");
	             game.SetSaleInfo();
	             Toast.makeText(this, getResources().getText(R.string.inapp_ok), Toast.LENGTH_SHORT).show();
	          }
	          catch (JSONException e) {
	        	 Toast.makeText(this, getResources().getText(R.string.inap_error), Toast.LENGTH_SHORT).show();
	             e.printStackTrace();
	          }
	      }
	   }
	}
	
    // ������������ ����������
    @Override
    protected void onPause() {
        super.onPause();
        game.SetPauseMusic(true);       
    }
    
    // ��������� ����� �����-������ ��� �������� �� ����������  
    // �������� ����������� ���������� �� ������� ��� �������� �� ���������� 
    @Override
    protected void onResume() {
    	super.onResume();
    	game.SetPauseMusic(true);
    	DrawLevelInfo();
    }
	
	static public void SetGame(Game _game) {
		game = _game;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}
	
    public static void SetDrawable(BitmapDrawable _bmd){
    	bmd = _bmd;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
        }   
    }
}
