package com.gueei.demos.fbUpload;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;

public class TestActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.test);
	    String[] arr = new String[30];
	    for (int i=0; i<arr.length; i++){
	    	arr[i] = "Item: " + i;
	    }
	    ((AdapterView)this.findViewById(R.id.lvTest)).setAdapter(new TestAdapter(arr));
	}

	public class TestAdapter extends ArrayAdapter<String>{
		String[] mArr;
		public TestAdapter(String[] arr) {
			super(TestActivity.this, R.layout.edit_image_row, arr);
			mArr = arr;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("Binder", "getView " + position);
			if (convertView == null){
				convertView = LayoutInflater.from(TestActivity.this).inflate(R.layout.edit_image_row, parent, false);
			}
			((EditText)convertView.findViewById(R.id.image_caption)).setText(mArr[position]);
			return convertView;
		}
	}
	
}
