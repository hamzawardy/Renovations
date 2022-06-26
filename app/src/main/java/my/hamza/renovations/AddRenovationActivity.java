package my.hamza.renovations;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import my.hamza.renovations.renovation.MyRenovations;

public class AddRenovationActivity extends AppCompatActivity
{
    private static final int IMAGE_PICK_CODE=100;
    private static final int PERMISSION_CODE=101;
    private TextInputEditText InTitle,InSubject,etAddress;
    private EditText etAnswer;
    private Button btSave,btnUp1,btnUp2;
    private TextView tvGps;
    private ImageView imgV,imgP;
    private Uri toUploadimageUri;
    private Uri downladuri;
    StorageTask uploadTask;
    private ImageButton imgLoc;
    private LocationRequest locationRequest;
    private double longitude,latitude;
    private MyRenovations myRenvo=new MyRenovations();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_renovation2);

    InTitle=findViewById(R.id.InTitle);
    InSubject=findViewById(R.id.InSubject);
    etAddress=findViewById(R.id.etAddress);
    etAnswer=findViewById(R.id.etAnswer);
    btSave=findViewById(R.id.btSave);
    btnUp1=findViewById(R.id.btnUp1);
    btnUp2=findViewById(R.id.btnUp2);
    tvGps=findViewById(R.id.tvGps);
    imgLoc=findViewById(R.id.imGps);
    imgV=findViewById(R.id.imgV);
    imgP=findViewById(R.id.imgP);


       locationRequest = LocationRequest.create();
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       locationRequest.setInterval(5000);
       locationRequest.setFastestInterval(2000);

       imgP.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //check runtime permission
               Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                       //permission not granted, request it.
                       String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                       //show popup for runtime permission
                       requestPermissions(permissions, PERMISSION_CODE);
                   } else {
                       //permission already granted
                       pickImageFromGallery();
                   }

               }
           }
       });
       btnUp2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toUploadimageUri!=null)
                    uploadImage( toUploadimageUri );
                btSave.setEnabled( false );
            }
        } );
       imgV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //check runtime permission
               Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                       //permission not granted, request it.
                       String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                       //show popup for runtime permission
                       requestPermissions(permissions, PERMISSION_CODE);
                   } else {
                       //permission already granted
                       pickVideoFromGallery();
                   }

               }
           }
       });
       btnUp1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toUploadimageUri!=null)
                    uploadImage( toUploadimageUri );
                btSave.setEnabled( false );
            }
        } );
       imgLoc.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               getCurrentLocation();
           }
       });
       btSave.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               validateFeilds();
           }
       } );

    }

    private void validateFeilds() {
        boolean isOk = true;
        String title = InTitle.getText().toString();
        String subject = InSubject.getText().toString();
        if (title.length() == 0) {
            InTitle.setError( "must inter Title" );
            isOk = false;
        }
        if (isOk) {
            myRenvo = new MyRenovations();
            myRenvo.setTitle( title );
            myRenvo.setSubject( subject );
            myRenvo.setLang( longitude );
            myRenvo.setLat( latitude );

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            myRenvo.setOwner( uid );

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference();

            String key = ref.child( "mylocs" ).push().getKey();
            myRenvo.setKey( key );

            ref.child( "mylocs" ).child( key ).setValue( myRenvo ).addOnCompleteListener( new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {//response
                    if (task.isSuccessful()) {
                        Toast.makeText( getApplicationContext(), "Add Successful", Toast.LENGTH_SHORT ).show();
                        finish();
                    } else {
                        Toast.makeText( getApplicationContext(), "Add Not Successful", Toast.LENGTH_SHORT ).show();
                    }
                }

            } );
    }}

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(AddRenovationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(AddRenovationActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(AddRenovationActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                        tvGps.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                }
                else
                {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult( ApiException.class);
                    Toast.makeText(AddRenovationActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(AddRenovationActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    private void pickVideoFromGallery() {

    }

    private void uploadImage(Uri filePath) {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            uploadTask=ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downladuri = task.getResult();
                                    myRenvo.setImage(downladuri.toString());
                                    btSave.setEnabled( true );

                                }
                            });

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else
        {
            myRenvo.setImage("");

        }
    }

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){


                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }
        if(requestCode== PERMISSION_CODE)
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //permission was granted
                pickImageFromGallery();
            }
            else {
                //permission was denied
                Toast.makeText(this,"Permission denied...!",Toast.LENGTH_SHORT).show();
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2)
        {
            if (resultCode == Activity.RESULT_OK) {
                getCurrentLocation();
            }
        }
        if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
            //set image to image view
            toUploadimageUri = data.getData();
            imgP.setImageURI(toUploadimageUri);
        }
    }
}