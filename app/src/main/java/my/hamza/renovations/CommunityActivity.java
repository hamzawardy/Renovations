package my.hamza.renovations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class CommunityActivity extends AppCompatActivity {
    private FloatingActionButton fabAdd;
    private ListView listView;
    private SearchView schV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        fabAdd=findViewById(R.id.fabAdd);
        listView=findViewById(R.id.listview);
        schV=findViewById(R.id.schV);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Add Renovation", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(getApplication(),AddRenovationActivity.class);
                startActivity(intent);
            }
        });



    }
}