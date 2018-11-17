package diff.nathan.com.diffutil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nathan.diff.util.BackgroundDarkPopupWindow;
import com.nathan.diff.util.LogUtil;
import com.nathan.diff.util.PopWindow;
import com.nathan.diff.util.plug.ViewPlugBaseLayout;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private View testView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPlugBaseLayout.initFont(this);
        textView = findViewById(R.id.textView);
        testView = LayoutInflater.from(this).inflate(R.layout.activity_test_view,null);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopWindow.Builder().activity(MainActivity.this)
                        //.title("标题啦")
                        .isMustDoClick(false)
                        .setContentView(testView)
                        //.setRLayoutId(R.layout.activity_test_view)
                        .confirmClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtil.tToast(getApplicationContext(),"点击确认");

                            }
                        })
                        .cancelClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtil.tToast(getApplicationContext(),"点击退出");
                            }
                        })
                        .build().popViewOneButton();
            }
        });

    }
    private PopWindow.OnPopWindowListener onPopWindowListener;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                    if(onPopWindowListener!=null){
                        onPopWindowListener.onClose();
                    }
        }
    };
}
