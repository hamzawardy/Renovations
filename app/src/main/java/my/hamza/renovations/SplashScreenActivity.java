package my.hamza.renovations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView img1,img2,img3,img4,img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img1=findViewById(R.id.imgv1);
        img2=findViewById(R.id.imgv2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.imgv4);
        img5=findViewById(R.id.imgv5);

        Thread th=new Thread() {
            @Override
            public void run() {
                //   هنا المقطع الذي يعمل بالتزامن مع مقاطع اخرى
                int ms = 3 * 1000;//millisecond
                try {
                    sleep(ms);
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
        }
    }


