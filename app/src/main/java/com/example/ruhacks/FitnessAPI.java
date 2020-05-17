package com.example.ruhacks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FitnessAPI {
    private static final String TAG = "MyActivity";
    private DateFormat dateFormat = DateFormat.getDateInstance();
    private static int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private Activity context;
    private int totalSteps;

    FitnessOptions fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build();

    FitnessAPI(Activity context) {
        this.context = context;

        fitSignIn();
    }

    public void fitSignIn() {
        if (oAuthPermissionsApproved()) {
            readHistoryData();
        } else {
            GoogleSignIn.requestPermissions(context, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, getGoogleAccount(), fitnessOptions);
        }
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    private void oAuthErrorMsg(int requestCode, int resultCode) {
        String message = "There was an error signing into Google Fit";
        Log.e(TAG, message);
    }

    private boolean oAuthPermissionsApproved() {
        return GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions);
    }

    private GoogleSignInAccount getGoogleAccount() {
        return GoogleSignIn.getAccountForExtension(context, fitnessOptions);
    }

    public Task<DataReadResponse> readHistoryData() {
        DataReadRequest readRequest = queryFitnessData();

        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(context, getGoogleAccount())
                .readData(readRequest)
                .addOnSuccessListener(dataReadResponse -> {
                    // For the sake of the sample, we'll print the data so we can see what we just
                    // added. In general, logging fitness information should be avoided for privacy
                    // reasons.
                    printData(dataReadResponse);
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "There was a problem reading the data.", e);
                });
    }

    private DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    private void printData(DataReadResponse dataReadResult) {
        if (!dataReadResult.getBuckets().isEmpty()) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                for (DataSet it : bucket.getDataSets()) {
                    dumpDataSet(it);
                }
            }
        } else if (!dataReadResult.getDataSets().isEmpty()) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet it : dataReadResult.getDataSets()) {
                dumpDataSet(it);
            }
        }
    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field it : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + it.getName() + " Value: " + dp.getValue(it));
                totalSteps += Integer.parseInt(dp.getValue(it).toString()) ;
            }
        }
    }
}
