package wjsay.com.asr.activity;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;

import org.json.JSONException;
import org.json.JSONObject;

import wjsay.com.asr.R;
import wjsay.com.asr.util.AutoSpeechRecognizer;

public class MainActivity extends AppCompatActivity {
    Button btn_start_recognizer;
    AutoSpeechRecognizer recognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }, 1);

        recognizer = getRecognizer(this);
        btn_start_recognizer = findViewById(R.id.btn_start_recognizer);
        btn_start_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recognizer.isRecognizing()) {
                    btn_start_recognizer.setText("语音输入");
                    recognizer.stopRecognize();
                }
                else {
                    btn_start_recognizer.setText("识别中...点击停止");
                    recognizer.startRecognize();
                }
            }
        });

    }

    public AutoSpeechRecognizer getRecognizer(final Activity activity) {
        if (recognizer == null) {
            recognizer = new AutoSpeechRecognizer(
                    this,
                    new NlsListener() {
                        @Override
                        public void onRecognizingResult(int i, NlsListener.RecognizedResult recognizedResult) {
                            if (i == 0) {
                                try {
                                    JSONObject jsonObject = new JSONObject(recognizedResult.asr_out);
                                    // 识别出来的的字符串。若若做修改，请在这改
                                    String result = jsonObject.getString("result");
                                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                                    //sendRecorder(tmp);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Log.e("ASRError", "onRecognizingResult: error: " + i);
                            }
                        }
                    },
                    new StageListener() {
                        @Override
                        public void onVoiceVolume(int i) {

                        }
                    });
        }
        return recognizer;
    }
}
