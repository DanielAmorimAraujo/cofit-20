package com.example.ruhacks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ruhacks.ui.home.HomeFragment;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.text.DateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private DateFormat dateFormat = DateFormat.getDateInstance();
    private static int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;

    FitnessOptions fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build();

    FitnessAPI fa;

    private static final int CAMERA_REQUEST_CODE = 21; // TODO: Explain this in a comment

    // TODO: Remove legacy code
    // Vision vision;

    // private static final String API_KEY = "";

    // TODO: This is not ideal at all but I'm not sure what better way to go about this
    String currentMission = "";
    int totalPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        fa = new FitnessAPI(this);
        fa.fitSignIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                fa.readHistoryData();
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                requestMLKitImageLabeling(bitmap);
            }
        }
        updatePoints();
    }

    private void oAuthErrorMsg(int requestCode, int resultCode) {
        String message = "There was an error signing into Google Fit";
        Log.e(TAG, message);
    }

    public void takePictureFromCamera(String mission) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        currentMission = mission;
    }

    /* public void requestGoogleCloudVisionAPI(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        ByteString imgBytes = ByteString.copyFrom(imageBytes);

        List<AnnotateImageRequest> requests = new ArrayList<>();

        Image img = Image.newBuilder().setContent(imgBytes).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION))
                        .setImage(img)
                        .build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    Log.d(TAG, "requestGoogleCloudVisionAPI: " + entity.getName());
                    if (currentMission.contains(entity.getName())) {
                        returnPoints();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } */

    public void requestMLKitImageLabeling(Bitmap bitmap) {
        FirebaseVisionImage image =
                FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler();

        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        // Task completed successfully
                        for (FirebaseVisionImageLabel label: labels) {
                            Log.d("MLKit", label.getText());
                            if (currentMission.contains(label.getText())) {
                                returnPoints();
                            }
                            if (currentMission.contains("Smoothie")) {
                                returnPoints();
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                    }
                });
    }

    public void syncSteps(View view) {
        totalPoints += fa.getTotalSteps();
        updatePoints();
    }

    public void returnPoints() {
        totalPoints += 100;
        updatePoints();
    }

    public void updatePoints() {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();
        fragment.updateBalance(Integer.toString(totalPoints));
    }

    public int getPoints() {
        return totalPoints;
    }

    public void deductPoints(int points) {
        totalPoints -= points;
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();
        fragment.updateBalance(Integer.toString(totalPoints));
    }
}
