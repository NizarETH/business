package com.euphor.paperpad.activities.fragments;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class SampleUploadListener implements RequestListener {

	@Override
	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub

	}

}
