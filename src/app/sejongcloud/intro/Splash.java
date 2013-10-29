package app.sejongcloud.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import app.sejongcloud.R;
import app.sejongcloud.main.Main;

public class Splash extends Activity implements AnimationListener{
	ImageView splash;
    LinearLayout li;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        
        Animation animation=new AlphaAnimation(0.0f, 1.0f);
        animation.setAnimationListener(new Animation.AnimationListener(){
        	public void onAnimationEnd(Animation arg0){
        		Handler handler=new Handler();
        		handler.postDelayed(new Runnable(){
        			public void run(){
        	            Intent i = new Intent(Splash.this, Main.class);
        	            startActivity(i);
        				Splash.this.finish();
        			}
        		},100);
        	}
            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
        });
        animation.setDuration(2000);
        splash=(ImageView)findViewById(R.id.splash);
        splash.startAnimation(animation);
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
