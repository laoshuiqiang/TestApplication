package poco.uiautomatortest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/2/2.
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView=new TextView(this);
        textView.setText("fjskfjsfjsdf");
        textView.setTextColor(0xff000000);
        textView.setTextSize(28.0f);
        this.setContentView(textView);
    }
}
