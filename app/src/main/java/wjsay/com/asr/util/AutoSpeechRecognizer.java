package wjsay.com.asr.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;

/**
 * Created by wjsay on 2018/10/30
 * Describe: 阿里云，一句话识别。
 * 其中id、secret是我自己实名认证申请的。请换成自己的
 */

public class AutoSpeechRecognizer {
    private static final String TAG = "AutoSpeechRecognizer";

    private static final String appKey = "nls-service";
    private static final String id = "*******";
    private static final String secret = "***********";

    private boolean recognizing;
    private NlsClient mNlsClient;
    private NlsRequest mNlsRequest;

    /**
     * 实例化语音识别
     *
     * @param listener      异步语音服务结果的回调类，回调参数：
     *                      NlsClient.ErrorCode.SUCCESS:
     *                      NlsClient.ErrorCode.RECOGNIZE_ERROR:
     *                      NlsClient.ErrorCode.RECORDING_ERROR:
     *                      NlsClient.ErrorCode.NOTHING:
     * @param stageListener 语音服务引擎状态变更回调接口，服务状态的改变
     *                      音量大小的回调、语音文件的生成通过本接口获取。
     */
    public AutoSpeechRecognizer(Context context, NlsListener listener, StageListener stageListener) {
        NlsClient.openLog(true);
        NlsClient.configure(context.getApplicationContext());

        mNlsRequest = new NlsRequest(new NlsRequestProto(context));
        mNlsRequest.setApp_key(appKey);
        mNlsRequest.setAsr_sc("opu");

        //实例化NlsClient
        mNlsClient = NlsClient.newInstance(context, listener, stageListener, mNlsRequest);
        mNlsClient.setMaxRecordTime(60000);         //设置最长语音
        mNlsClient.setMaxStallTime(1000);           //设置最短语音
        mNlsClient.setMinRecordTime(500);           //设置最大录音中断时间
        mNlsClient.setRecordAutoStop(false);        //设置VAD
        mNlsClient.setMinVoiceValueInterval(100);   //设置音量回调时长
    }

    /**
     * 开始识别
     */
    public void startRecognize() {
        recognizing = true;
        mNlsRequest.authorize(id, secret);
        if (!mNlsClient.start()){
            Log.e(TAG, "startRecognize: can not start recognize");
        }
    }

    /**
     * 停止识别
     */
    public void stopRecognize() {
        if (recognizing)
            mNlsClient.stop();
        recognizing = false;
    }

    /**
     *
     * @return 是否正在录音识别的布尔值
     */
    public boolean isRecognizing() {
        return recognizing;
    }
}

