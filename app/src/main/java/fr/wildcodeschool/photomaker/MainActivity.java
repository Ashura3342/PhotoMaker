package fr.wildcodeschool.photomaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.wildcodeschool.photomaker.adapter.ImageAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button actionPhoto;
    private ImageAdapter adapter;
    private PagerSnapHelper pagerSnapHelper;
    private static final String TAG = MainActivity.class.getName();

    private List<Uri> uriList;

    private DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_list);
        actionPhoto = findViewById(R.id.main_take_action);
        actionPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        uriList = new ArrayList<>();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for(File file : storageDir.listFiles()) {
            Uri uri = FileProvider.getUriForFile(this,
                    "fr.wildcodeschool.photomaker.fileprovider",
                    file);
            uriList.add(uri);
        }
        adapter = new ImageAdapter(uriList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
    }

    public static final int REQUEST_IMAGE_CAPTURE = 1234;

    private File createImageFile() throws IOException {
        String timeStamp = df.format(new Date());
        String imgFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFileName, ".jpg", storageDir);
        return image;
    }

    // chemin de la photo dans le téléphone
    private Uri mFileUri = null;

    private void dispatchTakePictureIntent() {
        // ouvrir l'application de prise de photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // lors de la validation de la photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // créer le fichier contenant la photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                finish();
            }

            if (photoFile != null) {
                // récupèrer le chemin de la photo
                mFileUri = FileProvider.getUriForFile(this,
                        "fr.wildcodeschool.photomaker.fileprovider",
                        photoFile);
                // déclenche l'appel de onActivityResult
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uriList.add(mFileUri);
        }
    }
}
