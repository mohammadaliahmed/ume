package com.appsinventiv.ume.Adapters;

import android.os.AsyncTask;
import android.os.Environment;

import com.appsinventiv.ume.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImageAsyncTask extends AsyncTask<Void, Void, Void> {
    String imgUrl;

    public DownloadImageAsyncTask(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        downloadImages(imgUrl);
        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

    }

    private void downloadImages(String imgUrl) {

        String domain;
        try {
            URL iimageUrl = new URL(imgUrl);

            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) iimageUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                String filename = "" + imgUrl.substring(imgUrl.length() - 7, imgUrl.length());

                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS) + "/" + filename + ".png");

                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int bufferLength = 0; // used to store a temporary size of the
                // buffer
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
