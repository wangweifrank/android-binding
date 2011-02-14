package com.gueei.demos.fbUpload;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Set;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingLog;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Debugger;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.collections.IRowModel;
import com.gueei.android.binding.cursor.CursorRowModel;
import com.gueei.android.binding.cursor.CursorSource;
import com.gueei.android.binding.cursor.IdField;
import com.gueei.android.binding.cursor.StringField;
import com.gueei.android.binding.observables.ArraySource;
import com.gueei.android.binding.observables.ObjectObservable;
import com.gueei.android.binding.observables.StringObservable;
import com.gueei.demos.fbUpload.OnlineRepository.FacebookAccount;
import com.gueei.demos.fbUpload.OnlineRepository.FacebookAlbum;
import com.gueei.demos.fbUpload.SessionEvents.AuthListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class FBUpload extends Activity implements AuthListener {
	private UploadImageDataViewModel mModel;
	private boolean loadList = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mModel = new UploadImageDataViewModel();
        Binder.setAndBindContentView(this, R.layout.main, mModel);
        SessionEvents.addAuthListener(this);
        if (!FBUploadApplication.getInstance().requestAuthFacebook(this)){
        	loadList = true;
        }
    }
    
    public class UploadImageDataViewModel{
    	public ArraySource<EditImageViewModel> EditImageList = new ArraySource<EditImageViewModel>();
    	public ObjectObservable SelectedAccount = new ObjectObservable();
    	public Command FillAlbumList = new Command(){
			public void Invoke(View view, Object... args) {
				Cursor albums = FBUploadApplication.getInstance().getAccountRepository()
					.getAllAlbumsInAccount(((AccountRowModel)SelectedAccount.get()).Id.get());
				FBUpload.this.startManagingCursor(albums);
				AlbumList.setCursor(albums);
			}
    	};
    	public CursorSource<AccountRowModel> AccountList 
		= new CursorSource<AccountRowModel>(AccountRowModel.class, 
				new CursorRowModel.Factory<AccountRowModel>() {
					public AccountRowModel createRowModel(Context context) {
						return new AccountRowModel();
					}
				}
		);
    	public CursorSource<AlbumRowModel> AlbumList 
    		= new CursorSource<AlbumRowModel>(AlbumRowModel.class, 
    				new CursorRowModel.Factory<AlbumRowModel>() {
						public AlbumRowModel createRowModel(Context context) {
							return new AlbumRowModel();
						}
    				}
			);

    	public UploadImageDataViewModel(){
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            ArrayList<Uri> files = bundle.getParcelableArrayList(Intent.EXTRA_STREAM);
            if (files==null) return;
            EditImageViewModel[] editImageList = new EditImageViewModel[files.size()]; 
            for(int i=0; i<files.size(); i++){
            	editImageList[i] = new EditImageViewModel(files.get(i));
            	//editImageList[i].Caption.set("item: " + i);
            }
            EditImageList.setArray(editImageList);
    	}
    }
    
    public class EditImageViewModel implements IRowModel {
    	public final StringObservable Caption = new StringObservable();
    	public final Observable<Uri> Source = new Observable<Uri>(Uri.class);
    	public final Observable<Bitmap> PreviewImage = new Observable<Bitmap>(Bitmap.class);
    	public EditImageViewModel(Uri source){
    		Source.set(source);
			Caption.set(source.getLastPathSegment());
    	}
    	Thread loadImageThread;
		public void onAttachedToUI() {
			Log.d("Binder", "onAttached" + Source.get());
			loadImageThread = new Thread(){
    			Bitmap previewBmp;
				public void run() {
					String id = Source.get().getLastPathSegment();
					try {
						previewBmp = MediaStore.Images.Thumbnails.getThumbnail
							(getContentResolver(), Long.parseLong(id), 
									MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());
						Thread.sleep(1500);
						PreviewImage.set(previewBmp);
					} catch (InterruptedException e) {
						MediaStore.Images.Thumbnails.cancelThumbnailRequest(getContentResolver(), Long.parseLong(id));
						e.printStackTrace();
					}
				}
    		};
    		//loadImageThread.start();
		}
		public void onDetachedFromUI() {
		}
    }

    public class AccountRowModel extends CursorRowModel{
    	public StringField Id = new StringField(0);
    	public StringField Name = new StringField(1);
		@Override
		public void resetInternalState(int position) {
		}
    }
    
    public class AlbumRowModel extends CursorRowModel{
    	public StringField Id = new StringField(0);
    	public StringField Name = new StringField(1);
		@Override
		public void resetInternalState(int position) {
		}
    }
	
    public void onAuthSucceed() {
    	Toast.makeText(this, "Authed!", Toast.LENGTH_SHORT).show();
    	if (loadList){
    		OnlineRepository pa = new OnlineRepository(FBUploadApplication.getInstance().getFacebook());
    		try {
    			FacebookAccount[] accounts = pa.getAccounts();
    			for(FacebookAccount a : accounts){
    				FBUploadApplication.getInstance()
    					.getAccountRepository().addAccount(a.Id, a.Name);
    				FacebookAlbum[] albums = pa.getAlbums(a.Id);
    				for (FacebookAlbum album : albums){
    					FBUploadApplication.getInstance()
    						.getAccountRepository().addAlbum(album.Id, album.Name, album.AccountId);
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	Cursor accounts = FBUploadApplication.getInstance().getAccountRepository().getAllAccounts();
    	this.startManagingCursor(accounts);
    	mModel.AccountList.setCursor(accounts);
        Debugger.graphObject(mModel, 10, null, this);
	}
    

	public void onAuthFail(String error) {
		
	}
}