package app.sejongcloud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import app.sejongcloud.R;

public class Tab1Content extends Activity {
	private int index;
	private int follower_count;
	private int count;
	private ProgressDialog dialog;
	String name;
	String email;
	String hp;
	String password;
	EditText eName;
    EditText eEmail;
    EditText ePassword;
    EditText eHp;
    LinearLayout linear;
    
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.tab1_content); 

    	int owner= getIntent().getIntExtra("owner",0);
    	String subject = getIntent().getStringExtra("subject"); 
        String content = getIntent().getStringExtra("content"); 
        String follower = getIntent().getStringExtra("follower"); 
        index  = getIntent().getIntExtra("index",0); 
        count  = getIntent().getIntExtra("count",0); 
        follower_count  = getIntent().getIntExtra("follower_count",0); 
		
    	/* 사람들 정보 가져오기 */
    	JSONArray json = JSONfunctions.getJSONfromURL("http://yhjun0229.cafe24.com/sejongstick/php/memberList.php");
    	try {
			JSONObject data = json.getJSONObject(owner);
			String name=data.getString("name");
			String email=data.getString("email");
			String hp=data.getString("hp");
			((TextView)findViewById(R.id.owner_name)).setText(name);
			((TextView)findViewById(R.id.owner_email)).setText(email);
			((TextView)findViewById(R.id.owner_hp)).setText(hp);
		} catch (JSONException e){}
		
        Button followBtn=(Button)findViewById(R.id.followButton);
        followBtn.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
		        profileSetting(); // 프로필 입력 설정
        		AlertDialog.Builder ab = new AlertDialog.Builder(Tab1Content.this);
        		ab.setTitle("참여자 정보")
		        //.setIcon(R.drawable.icon)
		        .setView(linear)
		        .setPositiveButton("참여", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						profileWrite(); // 프로필 재구성
						followThread();
					}
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						profileWrite(); // 프로필 재구성
						dialog.dismiss();
					}
		        }).create();
        		ab.show();
        	}
        });
        ((TextView)findViewById(R.id.subject)).setText(subject);
        ((TextView)findViewById(R.id.content)).setText(content);
        
        //Log.d("",follower_count+"/"+count);
        if(follower_count==0){
            ((TextView)findViewById(R.id.followerCount)).setText("아무도 참여하지 않았습니다.");
	        ((ProgressBar)findViewById(R.id.followerCountBar)).setVisibility(View.GONE);
        }else if(follower_count==count){
	        ((ProgressBar)findViewById(R.id.followerCountBar)).setMax(count);
	        ((ProgressBar)findViewById(R.id.followerCountBar)).setProgress(follower_count);
	        ((TextView)findViewById(R.id.followerCount)).setText("남은 자리가 없습니다");
        	followBtn.setVisibility(View.GONE);
        }else{
	        ((ProgressBar)findViewById(R.id.followerCountBar)).setMax(count);
	        ((ProgressBar)findViewById(R.id.followerCountBar)).setProgress(follower_count);
	        ((TextView)findViewById(R.id.followerCount)).setText(follower_count+"명이 참여중 입니다.");
        }
	}
	public void profileWrite(){
		SharedPreferences pref=getSharedPreferences("profile",0);
		SharedPreferences.Editor edit=pref.edit();

		name=eName.getText().toString();
	    email=eEmail.getText().toString();
		password=ePassword.getText().toString();
		hp=eHp.getText().toString();

		edit.putString("name", name);
		edit.putString("email", email);
		edit.putString("password", password);
		edit.putString("hp", hp);
		
		edit.commit();
	}
	public void profileSetting(){
        linear=(LinearLayout)View.inflate(Tab1Content.this, R.layout.tab1_follow, null);

    	eName=(EditText)linear.findViewById(R.id.followName);
	    eEmail=(EditText)linear.findViewById(R.id.followEmail);
	    ePassword=(EditText)linear.findViewById(R.id.followPassword);
	    eHp=(EditText)linear.findViewById(R.id.followHp);

    	/* 프레프런스 설정 */
    	SharedPreferences pref=getSharedPreferences("profile",0);
		name=pref.getString("name", "");
		email=pref.getString("email", "");
		password=pref.getString("password", "");
		hp=pref.getString("hp", "");
		
		eName.setText(name);
		eEmail.setText(email);
		ePassword.setText(password);
		eHp.setText(hp);
	}
    public void followThread(){
        dialog=ProgressDialog.show(this,"","로딩중.... ",true);
	    Thread thread = new Thread(new Runnable() {
	        public void run() {
				try{
					URL url=new URL("http://yhjun0229.cafe24.com/sejongstick/php/follow.php");
					HttpURLConnection httpURLCon=(HttpURLConnection)url.openConnection();
					httpURLCon.setDefaultUseCaches(false);
					httpURLCon.setDoInput(true);
					httpURLCon.setDoOutput(true);
					httpURLCon.setRequestMethod("POST");
					httpURLCon.setRequestProperty("content-type", "application/x-www-form-urlencoded");

					StringBuffer sb=new StringBuffer();
					sb.append("index").append("=").append(index).append("&");
					sb.append("follower_name").append("=").append(name).append("&");
					sb.append("follower_email").append("=").append(email).append("&");
					sb.append("follower_hp").append("=").append(hp).append("&");
					sb.append("follower_password").append("=").append(password).append("");
					Log.d("ss","good"+sb.toString());
					
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(httpURLCon.getOutputStream(),"UTF-8"));
					pw.write(sb.toString());
					pw.flush();
					
					
					BufferedReader bf=new BufferedReader(new InputStreamReader(httpURLCon.getInputStream(),"UTF-8"));
					/*
					StringBuilder buff=new StringBuilder();
					String line;
					while((line=bf.readLine())!=null){
						buff.append(line);
					}
					*/
					httpURLCon.disconnect();
					bf.close();
					pw.close();
					
					followHandler.sendEmptyMessage(0);
				}catch(Exception e){;}
	        }
	    });
	    thread.start();
    }
	private Handler followHandler = new Handler() {
	    public void handleMessage(Message msg) {
	    	dialog.dismiss();
			Toast.makeText(Tab1Content.this, "참여하셨습니다", Toast.LENGTH_LONG).show();
	    	Intent i=new Intent(Tab1Content.this,Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	i.putExtra("bol", 1);
	    	startActivity(i);
	    }
	};
}
