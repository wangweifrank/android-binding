package com.gueei.demos.fbUpload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;

public class OnlineRepository {
	private final Facebook mFacebook;
	public OnlineRepository(Facebook facebook){
		mFacebook = facebook;
	}

	public FacebookAccount[] getAccounts() throws MalformedURLException, IOException, JSONException{
		String response = mFacebook.request("me/accounts");
		JSONObject object = new JSONObject(response);
		JSONArray array = object.getJSONArray("data");
		int numOfAccount = array.length();
		ArrayList<FacebookAccount> accounts = new ArrayList<FacebookAccount>();
		// FacebookAccount[] accounts = new FacebookAccount[numOfAccount];
		for (int i=0; i<numOfAccount; i++){
			JSONObject jAcct = array.getJSONObject(i);
			try{
				FacebookAccount acct = new FacebookAccount();
				acct.Id = (jAcct.getString("id"));
				acct.Name = (jAcct.getString("name"));
				accounts.add(acct);
			}catch(JSONException e){
				e.printStackTrace();
				continue;
			}
		}
		return accounts.toArray(new FacebookAccount[0]);
	}
	
	public FacebookAlbum[] getAlbums(String accountId) throws MalformedURLException, IOException, JSONException{
		String response = mFacebook.request(accountId + "/albums");
		JSONObject object = new JSONObject(response);
		JSONArray array = object.getJSONArray("data");
		int numOfAlbum = array.length();
		ArrayList<FacebookAlbum> albums = new ArrayList<FacebookAlbum>();
		for (int i=0; i<numOfAlbum; i++){
			JSONObject jAlbum = array.getJSONObject(i);
			try{
				if (!jAlbum.getString("type").equals("normal")) continue;
				FacebookAlbum acct = new FacebookAlbum();
				acct.Id = (jAlbum.getString("id"));
				acct.Name = (jAlbum.getString("name"));
				acct.AccountId = accountId;
				albums.add(acct);
			}catch(JSONException e){
				e.printStackTrace();
				continue;
			}
		}
		return albums.toArray(new FacebookAlbum[0]);
	}
	
	public static class FacebookAccount{
		public String Id;
		public String Name;
	}
	
	public static class FacebookAlbum{
		public String Id;
		public String Name;
		public String AccountId;
	}
}