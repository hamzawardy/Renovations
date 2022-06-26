package my.hamza.renovations.renovation;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import my.hamza.renovations.R;

public class RenovAdapter extends ArrayAdapter<MyRenovations> {

    /**
     * @param context
     */
    public RenovAdapter(@NonNull Context context,int resourse) {
        super(context, resourse);
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //building item view
        View vitem = LayoutInflater.from(getContext()).inflate(R.layout.renov_item, parent, false);
        TextView tvTitle = vitem.findViewById(R.id.itmTitle );
        TextView tvSubject = vitem.findViewById(R.id.itmSubject );
        TextView tvAnswer = vitem.findViewById(R.id.tvAnswer);
        ImageView imageView = vitem.findViewById(R.id.imageView);
        ImageButton imgp = vitem.findViewById(R.id.imgP);
        ImageButton imgv = vitem.findViewById(R.id.imgV);

        //getting data source
        final MyRenovations myRenovations = getItem(position);
        tvTitle.setText(myRenovations.getTitle());
        tvSubject.setText(myRenovations.getSubject());
        tvAnswer.setText ( myRenovations.getAnswer () );
        downloadImageToLocalFile(myRenovations.getImage(),imgp);   //connect item view to data source


        return vitem;
    }

    private void downloadImageToLocalFile(String fileURL, final ImageView toView) {
        if(fileURL==null)return;
        StorageReference httpsReference = FirebaseStorage.getInstance ( ).getReferenceFromUrl (fileURL);
        final File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");


            httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot> () {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Toast.makeText(getContext(), "downloaded Image To Local File", Toast.LENGTH_SHORT).show();
                    toView.setImageURI( Uri.fromFile(localFile));
                }
            }).addOnFailureListener(new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getContext(), "onFailure downloaded Image To Local File "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }



