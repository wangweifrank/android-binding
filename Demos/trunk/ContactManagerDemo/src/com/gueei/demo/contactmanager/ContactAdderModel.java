package com.gueei.demo.contactmanager;

import java.util.ArrayList;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.accounts.OnAccountsUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactAdderModel implements OnAccountsUpdateListener {
	private Activity mContext;
	public Observable<CharSequence> Name = new Observable<CharSequence>("");
	public Observable<CharSequence> Phone = new Observable<CharSequence>("");
	public Observable<CharSequence> Email = new Observable<CharSequence>("");
	public Observable<Adapter> AccountList = new Observable<Adapter>();
	public Observable<Adapter> PhoneTypeList = new Observable<Adapter>();
	public Observable<Adapter> EmailTypeList = new Observable<Adapter>();
	public Observable<PhoneLabelPair> SelectedPhoneType = new Observable<PhoneLabelPair>();
	public Observable<EmailLabelPair> SelectedEmailType = new Observable<EmailLabelPair>();
	public Observable<AccountData> SelectedAccount=new Observable<AccountData>();
	public Command SaveContact = new Command(){
		public void Invoke(View view, Object... args) {
			String message = "Here save is not going to perform, what you have entered: \n" +
					"Name: " + Name.get() + "\n" +
					"Phone: " + Phone.get() + "(" +  SelectedPhoneType.get().toString() + ")\n" +
					"Email: " + Email.get() + "(" +  SelectedEmailType.get().toString() + ")\n" +
					"In account: " + SelectedAccount.get().getName();
			(Toast.makeText(mContext, message, Toast.LENGTH_LONG)).show();
		}
	};
	
	public ContactAdderModel(Activity context){
		mContext = context;
		PhoneLabelPair[] PhoneTypes = new PhoneLabelPair[]{
				new PhoneLabelPair(ContactsContract.CommonDataKinds.Phone.TYPE_HOME),
				new PhoneLabelPair(ContactsContract.CommonDataKinds.Phone.TYPE_WORK),
				new PhoneLabelPair(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE),
				new PhoneLabelPair(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
		};
		EmailLabelPair[] EmailTypes = new EmailLabelPair[]{
				new EmailLabelPair(ContactsContract.CommonDataKinds.Email.TYPE_HOME),
				new EmailLabelPair(ContactsContract.CommonDataKinds.Email.TYPE_WORK),
				new EmailLabelPair(ContactsContract.CommonDataKinds.Email.TYPE_MOBILE),
				new EmailLabelPair(ContactsContract.CommonDataKinds.Email.TYPE_OTHER)
		};
		ArrayAdapter<PhoneLabelPair> phone =  
			new ArrayAdapter<PhoneLabelPair>
			(mContext, android.R.layout.simple_spinner_item, PhoneTypes);
		PhoneTypeList.set(phone);
		ArrayAdapter<EmailLabelPair> email =  
			new ArrayAdapter<EmailLabelPair>
			(mContext, android.R.layout.simple_spinner_item,EmailTypes);
		EmailTypeList.set(email);
		
		AccountManager.get(mContext).addOnAccountsUpdatedListener(this, null, true);
	}
	
	public void onAccountsUpdated(Account[] a) {
		AuthenticatorDescription[] accountTypes = AccountManager.get(mContext).getAuthenticatorTypes();
		ArrayList<AccountData> accounts = new ArrayList<AccountData>();
        // Populate tables
        for (int i = 0; i < a.length; i++) {
            // The user may have multiple accounts with the same name, so we need to construct a
            // meaningful display name for each.
            String systemAccountType = a[i].type;
            AuthenticatorDescription ad = getAuthenticatorDescription(systemAccountType,
                    accountTypes);
            AccountData data = new AccountData(a[i].name, ad);
            accounts.add(data);
        }
        AccountList.set(new AccountAdapter(mContext, accounts));
	}

	private static AuthenticatorDescription getAuthenticatorDescription(String type,
            AuthenticatorDescription[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            if (dictionary[i].type.equals(type)) {
                return dictionary[i];
            }
        }
        // No match found
        throw new RuntimeException("Unable to find matching authenticator");
    }
	
	private class EmailLabelPair{
		private CharSequence label;
		public EmailLabelPair(int kind){
			label = ContactsContract.CommonDataKinds.Email
				.getTypeLabel(mContext.getResources(), kind, 
						mContext.getString(R.string.undefinedTypeLabel).toString());
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return label.toString();
		}
	}
	
	private class PhoneLabelPair{
		private CharSequence label;
		public PhoneLabelPair(int kind){
			label = ContactsContract.CommonDataKinds.Phone
				.getTypeLabel(mContext.getResources(), kind, 
						mContext.getString(R.string.undefinedTypeLabel).toString());
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return label.toString();
		}
	}
	
	private class AccountData {
        private String mName;
        private String mType;
        private CharSequence mTypeLabel;
        private Drawable mIcon;

        /**
         * @param name The name of the account. This is usually the user's email address or
         *        username.
         * @param description The description for this account. This will be dictated by the
         *        type of account returned, and can be obtained from the system AccountManager.
         */
        public AccountData(String name, AuthenticatorDescription description) {
            mName = name;
            if (description != null) {
                mType = description.type;

                // The type string is stored in a resource, so we need to convert it into something
                // human readable.
                String packageName = description.packageName;
                PackageManager pm = mContext.getPackageManager();

                if (description.labelId != 0) {
                    mTypeLabel = pm.getText(packageName, description.labelId, null);
                    if (mTypeLabel == null) {
                        throw new IllegalArgumentException("LabelID provided, but label not found");
                    }
                } else {
                    mTypeLabel = "";
                }

                if (description.iconId != 0) {
                    mIcon = pm.getDrawable(packageName, description.iconId, null);
                    if (mIcon == null) {
                        throw new IllegalArgumentException("IconID provided, but drawable not " +
                                "found");
                    }
                } else {
                    mIcon = mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
                }
            }
        }

        public String getName() {
            return mName;
        }

        public String getType() {
            return mType;
        }

        public CharSequence getTypeLabel() {
            return mTypeLabel;
        }

        public Drawable getIcon() {
            return mIcon;
        }

        public String toString() {
            return mName;
        }
    }
    private class AccountAdapter extends ArrayAdapter<AccountData> {
        public AccountAdapter(Context context, ArrayList<AccountData> accountData) {
            super(context, android.R.layout.simple_spinner_item, accountData);
            setDropDownViewResource(R.layout.account_entry);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            // Inflate a view template
            if (convertView == null) {
                LayoutInflater layoutInflater = mContext.getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.account_entry, parent, false);
            }
            TextView firstAccountLine = (TextView) convertView.findViewById(R.id.firstAccountLine);
            TextView secondAccountLine = (TextView) convertView.findViewById(R.id.secondAccountLine);
            ImageView accountIcon = (ImageView) convertView.findViewById(R.id.accountIcon);

            // Populate template
            AccountData data = getItem(position);
            firstAccountLine.setText(data.getName());
            secondAccountLine.setText(data.getTypeLabel());
            Drawable icon = data.getIcon();
            if (icon == null) {
                icon = mContext.getResources().getDrawable(android.R.drawable.ic_menu_search);
            }
            accountIcon.setImageDrawable(icon);
            return convertView;
        }
    }
}
