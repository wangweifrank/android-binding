package com.gueei.demos.fbUpload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.gueei.android.binding.observables.StringObservable;

public class PostAccounts {
	private final Facebook mFacebook;
	public PostAccounts(Facebook facebook){
		mFacebook = facebook;
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
	}
	private AsyncFacebookRunner mAsyncRunner;
	public void loadAccounts(final StringObservable oresponse){
		mAsyncRunner.request("me/accounts", new RequestListener(){
			public void onComplete(String response, Object state) {
				oresponse.set(response);
				try {
					JSONObject root = new JSONObject(response);
					JSONArray accounts = root.getJSONArray("data");
					int numOfAccounts = accounts.length();
					for(int i=0; i<numOfAccounts; i++){
						JSONObject a = accounts.getJSONObject(i);
						FacebookAccount account = new FacebookAccount();
						account.Id = a.getString("id");
						account.Name = a.getString("name");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onIOException(IOException e, Object state) {
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
	
}