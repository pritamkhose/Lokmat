package com.pritam.lokmat.com.example.utility;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpAsyncTask extends AsyncTask<String, Void, String>{

	@Override
	protected String doInBackground(String... urls) {
		String output = null;
		for (String url : urls) {
			output = getOutputFromUrl(url);
		}
		//System.out.println("********"+output);
		return output;
	}

	private String getOutputFromUrl(String url) {
		StringBuffer output = new StringBuffer("");
		url = url.replace(' ', '+');
		try {
			InputStream stream = getHttpConnection(url);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
			String s = "";
			while ((s = buffer.readLine()) != null)
				output.append(s + '\n');
		} catch (Exception e1) {
		}
		return output.toString();
	}

	// Makes HttpURLConnection and returns InputStream
	private InputStream getHttpConnection(String urlString) throws IOException {
		InputStream stream = null;
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();
			}
		} catch (Exception ex) {
		}
		return stream;
	}

	@Override
	protected void onPostExecute(String output) {

	}
}
