package com.example.voaacencoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sinaapp.bashell.RecordAAC;
import com.sinaapp.bashell.RecordAAC.RecordAACListener;
import com.sinaapp.bashell.VoAACEncoder;

public class MainActivity extends Activity {

//	private AudioRecord recordInstance;
//	private boolean isStart;
//	private FileOutputStream fos;
//	private final int READ_SIZE = 2048;
//	private final int simpleRate = 44100;//44100
	RecordAAC aac;
	Button bt1,bt2,bt3,bt4;
	boolean isStart=true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt1=(Button)findViewById(R.id.button1);
		bt1.setOnClickListener(mOnClickListener);
		bt2=(Button)findViewById(R.id.button2);
		bt2.setOnClickListener(mOnClickListener);
		bt3=(Button)findViewById(R.id.button3);
		bt3.setOnClickListener(mOnClickListener);
		bt4=(Button)findViewById(R.id.button4);
		bt4.setOnClickListener(mOnClickListener);
		aac = new RecordAAC("/sdcard/testAAC.aac");
		
		aac.setRecordAACListener(mRecordAACListener);
	}
	
	private OnClickListener mOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v==bt1){
//				String permission=android.Manifest.permission.RECORD_AUDIO;
//				int res = getApplicationContext().checkCallingOrSelfPermission(permission);
//				int res2 = getApplicationContext().checkCallingPermission(permission);
//				int res3=getApplicationContext().checkPermission(permission,android.os.Process.myPid(),android.os.Process.myUid());
//				Log.v("res","res:"+res+"  res2:"+res2+"  res3:"+res3);
////				String msg="error!";
////				getApplicationContext().enforceCallingOrSelfPermission(permission,msg);
//				aac.prepare();
//				if(res == PackageManager.PERMISSION_GRANTED){
//					Toast.makeText(getApplicationContext(), "开始录音！", Toast.LENGTH_SHORT).show();
////					aac.prepare();
////					aac.start();
//
//				}else{
//					Toast.makeText(getApplicationContext(), "请尝试打开录音权限！", Toast.LENGTH_SHORT).show();		//没有录音权限
//				}

//				Context context=getApplicationContext();
//				PackageManager pm = getApplicationContext().getPackageManager();
////				boolean permission = false;
//				ApplicationInfo ai;
//				try {
//					ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
//					AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//
//					Class clazz = manager.getClass();
//					Method m1 = clazz.getMethod("checkOp", int.class, int.class, String.class);
//					int checkResult = (Integer) m1.invoke(manager, 27, ai.uid, context.getPackageName());
//					if(checkResult==AppOpsManager.MODE_ALLOWED){
//
//						Toast.makeText(getApplicationContext(), "开始录音！", Toast.LENGTH_SHORT).show();
//						aac.prepare();
//						aac.start();
//					}else{
//						Toast.makeText(getApplicationContext(), "请尝试打开录音权限！", Toast.LENGTH_SHORT).show();
//					}
//				}catch (Exception e){
//				}
				if(aac.prepare()){
				Toast.makeText(getApplicationContext(), "开始录音！", Toast.LENGTH_SHORT).show();
					aac.start();
				}else{
					Toast.makeText(getApplicationContext(), "请尝试打开录音权限！", Toast.LENGTH_SHORT).show();
				}
			}
			if(v==bt2){
				aac.stop();
			}
			if(v==bt3){
				aac.pause();
			}
			if(v==bt4){
				aac.proceed();
			}
		}
	};

	public boolean checkRecordPermission(Context context){
//		String permName = "android.permission.RECORD_AUDIO";
//		String pkgName = context.getPackageName();
//		int reslut = context.getPackageManager().checkPermission(permName, pkgName);
//		return reslut==PackageManager.PERMISSION_GRANTED;
		context.enforceCallingPermission("android.permission.RECORD_AUDIO", "error message");
		return context.checkPermission("android.permission.RECORD_AUDIO",android.os.Process.myPid(), android.os.Process.myUid())
				== PackageManager.PERMISSION_GRANTED;
	}

	public void execCommand(String command) throws IOException {
		// start the ls command running
		//String[] args =  new String[]{"sh", "-c", command};
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command);        //这句话就是shell与高级语言间的调用
		//如果有参数的话可以用另外一个被重载的exec方法
		//实际上这样执行时启动了一个子进程,它没有父进程的控制台
		//也就看不到输出,所以我们需要用输出流来得到shell执行后的输出
		InputStream inputstream = proc.getInputStream();
		InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
		BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
		// read the ls output
		String line = "";
		StringBuilder sb = new StringBuilder(line);
		while ((line = bufferedreader.readLine()) != null) {
			//System.out.println(line);
			sb.append(line);
			sb.append('\n');
		}
		//tv.setText(sb.toString());
		//使用exec执行不会等执行成功以后才返回,它会立即返回
		//所以在某些情况下是很要命的(比如复制文件的时候)
		//使用wairFor()可以等待命令执行完成以后才返回
		try {
			if (proc.waitFor() != 0) {
				System.err.println("exit value = " + proc.exitValue());
			}
		}
		catch (InterruptedException e) {
			System.err.println(e);
		}
	}
	private RecordAACListener mRecordAACListener=new RecordAACListener() {
		
		@Override
		public void isSuccess(boolean isSuccess) {
			// TODO Auto-generated method stub
			if(isSuccess){
				Toast.makeText(getApplicationContext(), "录制成功！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "录制失败！", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onDestroy() {
//		aac.stop();
		super.onDestroy();
	}
}
