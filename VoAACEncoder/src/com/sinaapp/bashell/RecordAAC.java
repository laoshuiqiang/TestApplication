package com.sinaapp.bashell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RecordAAC {

    private final int MSG_SUCCESS = 0x01;//成功
    private final int MSG_FAIL = 0x02;//失败
    private final int STATE_PAUSE = 0x03;//暂停
    private final int STATE_RECORDING = 0x04;//正在录制
    private final int STATE_STOP = 0x05;//停止

    private int mPrepareCount = 0;

    private int mState = -1;
    private int mMsg = MSG_SUCCESS;

    private Thread mRecordThread = null;

    private VoAACEncoder mVoAACEncoder;
    private AudioRecord mRecordecordInstance;
    private FileOutputStream fos;
    private final int READ_SIZE = 2048;
    private final int mSimpleRate = 44100;//44100
    private String mFilePath;

    private RecordAACListener mRecordAACListener;

    public RecordAAC(String filepath) {
//		mVoAACEncoder = new VoAACEncoder();
//		mVoAACEncoder.Init(mSimpleRate, mSimpleRate * 2, (short)1, (short)1);
        mFilePath = filepath;
    }


    /**
     * @return false 权限没开，true正常录音
     */
    public boolean prepare() {
        if (mPrepareCount == 2) {//初始化两次都不成功返回false
            return false;
        } else {
            mPrepareCount++;
        }

        int min = AudioRecord.getMinBufferSize(mSimpleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (min < READ_SIZE) {
            min = READ_SIZE;
        }
        byte[] tempBuffer = new byte[READ_SIZE];
        mRecordecordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, mSimpleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, min);
        try {
            if (mRecordecordInstance != null) {
                mRecordecordInstance.startRecording();
            } else {
                prepare();
            }
        } catch (IllegalStateException e) {
            return false;
        }

        int bufferRead = AudioRecord.ERROR_INVALID_OPERATION;
        if (mRecordecordInstance != null) {
            bufferRead = mRecordecordInstance.read(tempBuffer, 0, READ_SIZE);
        } else {
            prepare();
        }

        if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
            if (mRecordecordInstance != null) {
                mRecordecordInstance.stop();
                mRecordecordInstance.release();
                mRecordecordInstance = null;
            }
            return false;
        }

        return true;
    }

    /**
     * 录制开始
     */
    public void start() {
        mPrepareCount = 0;
        try {
            fos = new FileOutputStream(mFilePath);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Message msg = mHandler.obtainMessage(MSG_FAIL, 0, 0, null);//录制失败
            msg.sendToTarget();
            return;
        }

        if (mVoAACEncoder == null) {
            mVoAACEncoder = new VoAACEncoder();
            mVoAACEncoder.Init(mSimpleRate, mSimpleRate * 2, (short) 1, (short) 1);
        }

        if (mRecordThread == null) {
            mRecordThread = new Thread(mRecordRunnable);
            mRecordThread.start();
        }

        mState = STATE_RECORDING;
    }

    /**
     * 录制暂停
     */
    public void pause() {
        mState = STATE_PAUSE;
    }

    /**
     * 录制继续
     */
    public void proceed() {
        mState = STATE_RECORDING;
        synchronized (mRecordRunnable) {
            mRecordRunnable.notifyAll();
        }
    }

    /**
     * 录制结束
     */
    public void stop() {
        mState = STATE_STOP;
        synchronized (mRecordRunnable) {
            mRecordRunnable.notifyAll();
        }
    }

    /**
     * 录制过程
     */
    Runnable mRecordRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
//			int min = AudioRecord.getMinBufferSize(mSimpleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//			if(min<READ_SIZE) {
//				min = READ_SIZE;
//			}
            byte[] tempBuffer = new byte[READ_SIZE];
//			mRecordecordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, mSimpleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, min);
//			mRecordecordInstance.startRecording();
            while (mState != STATE_STOP) {
                if (mState == STATE_PAUSE) {//暂停
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            mMsg = MSG_FAIL;//失败
                            RecordAAC.this.recordFinish();
                            return;
                        }
                    }
                }

                int bufferRead = AudioRecord.ERROR_INVALID_OPERATION;
                if (mRecordecordInstance != null) {
                    bufferRead = mRecordecordInstance.read(tempBuffer, 0, READ_SIZE);
                }

                if (bufferRead != AudioRecord.ERROR_INVALID_OPERATION) {
                    byte[] ret = mVoAACEncoder.Enc(tempBuffer);
                    if (bufferRead > 0) {
                        System.out.println("ret:" + ret.length);
                        try {
                            fos.write(ret);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mMsg = MSG_FAIL;//失败
                            mState = STATE_STOP;//停止录制
                        }
                    }
                } else {
                    mMsg = MSG_FAIL;//失败
                    mState = STATE_STOP;//停止录制
                }
            }

            //录制完毕
            RecordAAC.this.recordFinish();
        }
    };

    /**
     * 录制完毕
     */
    private void recordFinish() {

        if (mRecordecordInstance != null) {
            mRecordecordInstance.stop();
            mRecordecordInstance.release();
            mRecordecordInstance = null;
        }
        if (mVoAACEncoder != null) {
            mVoAACEncoder.Uninit();
            mVoAACEncoder = null;
        }
        mRecordThread = null;
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mMsg = MSG_FAIL;//失败
        }

        if (mMsg == MSG_SUCCESS) {
            Message msg = mHandler.obtainMessage(MSG_SUCCESS, 0, 0, null);//录制成功
            msg.sendToTarget();
        } else if (mMsg == MSG_FAIL) {
            Message msg = mHandler.obtainMessage(MSG_FAIL, 0, 0, null);//录制失败
            msg.sendToTarget();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_SUCCESS:
                    if (mRecordAACListener != null) {
                        mRecordAACListener.isSuccess(true);//录制成功
                    }
                    break;
                case MSG_FAIL:
                    if (mRecordAACListener != null) {
                        mRecordAACListener.isSuccess(false);//录制失败
                    }
                    break;
            }
        }
    };

    /**
     * 录音监听器
     */
    public interface RecordAACListener {
        public void isSuccess(boolean isSuccess);
    }

    public void setRecordAACListener(RecordAACListener listener) {
        mRecordAACListener = listener;
    }
}