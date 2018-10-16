package diff.nathan.com.diffutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nathan.diff.util.BackgroundDarkPopupWindow;
import com.nathan.diff.util.LogUtil;
import com.nathan.diff.util.PopWindow;
import com.nathan.diff.util.plug.ViewPlugBaseLayout;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPlugBaseLayout.initFont(this);

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopWindow.Builder().activity(MainActivity.this)
                        .title("标题啦")
                        .content("好多内存")
                        .confirmClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtil.tToast(getApplicationContext(),"点击确认");

                            }
                        })
                        .build().popOneButton();
            }
        });
        new PopWindow.Builder().activity(this)
                .title("标题啦")
                .content("好多内存")
                .confirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.tToast(getApplicationContext(),"点击确认");

                    }
                })
                .build().popOneButton();
    }


}
