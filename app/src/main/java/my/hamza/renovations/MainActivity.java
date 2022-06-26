package my.hamza.renovations;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.hamza.renovations.renovation.MyRenovations;
import my.hamza.renovations.renovation.RenovAdapter;

public class MainActivity extends AppCompatActivity {

    private SearchView srch1;
    private ListView listv;
    private FloatingActionButton fabAdd1;
    private RenovAdapter renovAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView (R.layout.activity_main);

        listv = findViewById ( R.id.listv );
        fabAdd1=findViewById ( R.id.fabAdd1 );
        renovAdapter=new RenovAdapter(getBaseContext(),R.layout.renov_item);
        listv.setAdapter(renovAdapter);
        srch1 = findViewById ( R.id.srch1 );


        fabAdd1.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                Snackbar.make ( v, "Add Renovation", Snackbar.LENGTH_LONG ).setAction ( "Action", null ).show ( );
                Intent intent = new Intent ( getApplication ( ), AddRenovationActivity.class );
                startActivity ( intent );
            }
        } );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ( ).inflate ( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ( ) == R.id.sno) {
            AlertDialog.Builder builder = new AlertDialog.Builder ( this );
            builder.setMessage ( "Are you sure" );
            builder.setCancelable ( true );
            //listener 3. add listener to the buttons
            builder.setPositiveButton ( "yes", new DialogInterface.OnClickListener ( ) {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance ( ).signOut ( );
                    finish ( );
                }
            } );
            builder.setNegativeButton ( "NO", new DialogInterface.OnClickListener ( ) {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            } );
            AlertDialog dialog = builder.create ( );
            dialog.show ( );
        }
        return true;
    }

    //6.
    @Override
    protected void onResume() {
        super.onResume();
        readFromFirebase();
    }

    public void readFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance ( );//to connect to database
        FirebaseAuth auth = FirebaseAuth.getInstance ( );//to get current user UID
        String uid = auth.getUid ( );
        DatabaseReference reference = database.getReference ( );

        //5.6 add listener to update us about any change
        //change group name:
        reference.child ( "ww" );
        reference.addValueEventListener ( new ValueEventListener ( ) {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //5.7
                renovAdapter.clear ( );
                for (DataSnapshot d : dataSnapshot.getChildren ( )) {
                    //5.8
                    MyRenovations t = d.getValue ( MyRenovations.class );
                    Log.d ( "MyUser", t.toString ( ) );
                    renovAdapter.add ( t );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }
}