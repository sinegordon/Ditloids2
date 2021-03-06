package com.ditloids2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FaqActivity extends Activity implements OnClickListener, OnKeyListener {
	private static Game game = null;
	private static BitmapDrawable bmd = null;
	
	public static void SetGame(Game _game){
		game = _game;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq);
    	View v = findViewById(R.id.faqLayout);
    	v.setBackgroundDrawable(bmd);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		findViewById(R.id.arrowButton).setOnClickListener(this);
        // ���������� ����� �� header
        ((TextView)findViewById(R.id.textView1)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf"));
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
	    case R.id.arrowButton:
	    	// �� ����� ������
	    	finish();
	    	break;
	    default:
	    	break;
	    }
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return false;
	}
   
    // ������������ ����������
    @Override
    protected void onPause() {
        super.onPause();
        game.SetPauseMusic(true);
    }
    
    // �������������� ����������
    @Override
    protected void onResume() {
        super.onResume();
        game.SetPauseMusic(false);
    }
    
    public static void SetDrawable(BitmapDrawable _bmd){
    	bmd = _bmd;
    }

}