package com.mor.sa.android.activities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.checker.sa.android.dialog.Revamped_Loading_Dialog;
import com.checker.sa.android.helper.Constants;
import com.checker.sa.android.helper.Helper;
import com.checker.sa.android.transport.Connector;
import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends Activity {

	TextView textSignup;
	EditText usernameSignup;
	EditText passwordSignup;
	EditText eMailSignup;
	EditText phoneSignup;
	EditText addressSignup;
	EditText aboutSignup;
	Button signupBtn;
	ImageView backIco;
	SharedPreferences myPrefs;

	String userName = "";
	String password = "";
	String userData = "";
	int app_id = 177;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myPrefs = getSharedPreferences("pref", MODE_PRIVATE);
		setContentView(R.layout.signup);
		

		textSignup = (TextView) findViewById(R.id.textSignup);
		usernameSignup = (EditText) findViewById(R.id.usernameSignup);
		passwordSignup = (EditText) findViewById(R.id.passwordSignup);
		eMailSignup = (EditText) findViewById(R.id.eMailSignup);
		phoneSignup = (EditText) findViewById(R.id.phoneSignup);
		addressSignup = (EditText) findViewById(R.id.addressSignup);
		aboutSignup = (EditText) findViewById(R.id.aboutSignup);
		signupBtn = (Button) findViewById(R.id.signupBtn);
		backIco = (ImageView) findViewById(R.id.backIco);

		loadViews();
		backIco.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settingintent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(settingintent);
				finish();
			}
		});

		signupBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!usernameSignup.getText().toString().equals("")
						&& !passwordSignup.getText().toString().equals("")
						&& !eMailSignup.getText().toString().equals("")
						&& !phoneSignup.getText().toString().equals("")
						&& !addressSignup.getText().toString().equals("")
						&& !aboutSignup.getText().toString().equals("")) {
					if (isEmailValid(eMailSignup.getText().toString())) {
						userData = "ew0KICAgICJjb250cm9sbGVyIjogIkNoZWNrZXIiLA0KICAgICJhY3Rpb24iOiAiY3JlYXRlcmVjb3JkIiwNCiAgICAiQ2hlY2tlcnMiOiB7DQogICAgICAgICJVc2VybmFtZSI6ICJkdW1teXVzZXJOYW1lIiwNCiAgICAgICAgIlBhc3N3b3JkIjogImR1bW15UGFzc3dvcmQiLA0KICAgICAgICAiRnVsbG5hbWUiOiAiZHVtbXlGdWxsTmFtZSIsDQogICAgICAgICJDaGVja2VyQ29kZSI6ICJ0ZXN0aW5nIDEyMyIsDQogICAgICAgICJTZXgiOiAiTSIsDQogICAgICAgICJSZWdpb25OYW1lIjogIkFsYXNrYSIsDQogICAgICAgICJDaXR5TmFtZSI6ICJWZWdhcyIsDQogICAgICAgICJDb3VudHJ5TmFtZSI6ICJBbWVyaWNhIiwNCiAgICAgICAgIkFkZHJlc3MiOiAiZHVtbXlBZGRyZXNzIiwNCiAgICAgICAgIkhvdXNlTnVtYmVyIjogInRlc3RpbmcgMTIzIiwNCiAgICAgICAgIlppcGNvZGUiOiAidGVzdGluZyAxMjMiLA0KICAgICAgICAiTGF0IjogMS4yLA0KICAgICAgICAiTG9uZyI6IDEuMiwNCiAgICAgICAgIlppcGNvZGVBZGRpdGlvbmFsIjogIlRoaXMgaXMgdGV4dCAxMjM0IiwNCiAgICAgICAgIlBob25lIjogImR1bW15UGhvbmUiLA0KICAgICAgICAiUGhvbmUyIjogInRlc3RpbmcgMTIzIiwNCiAgICAgICAgIlBob25lMyI6ICJ0ZXN0aW5nIDEyMyIsDQogICAgICAgICJFbWFpbCI6ICJkdW1teUVtYWlsIiwNCiAgICAgICAgIkJpcnRoRGF0ZSI6ICIyMDEwLTExLTI1IiwNCiAgICAgICAgIklkTnVtYmVyIjogInRlc3RpbmcgMTIzIiwNCiAgICAgICAgIlNTTiI6ICJ0ZXN0aW5nIDEyMyIsDQogICAgICAgICJJc0FjdGl2ZSI6IDEsDQogICAgICAgICJCbGFja0xpc3RlZCI6IDAsDQogICAgICAgICJVc2VWQVQiOiAxLA0KICAgICAgICAiSXNUZWxlcGhvbmljIjogMCwNCiAgICAgICAgIkNhblNlYXJjaCI6IDEsDQogICAgICAgICJQaG9uZUNvbnN0YW50Qm9udXMiOiA5LA0KICAgICAgICAiQWx0TGFuZ0xpbmsiOiA4NTIwMDIxLA0KICAgICAgICAiTGFuZ3VhZ2VPdmVycmlkZSI6IDg1MjAwMjEsDQogICAgICAgICJDYW5BZGQiOiAwLA0KICAgICAgICAiQ2FuSW5pdGlhdGVDcml0IjogMCwNCiAgICAgICAgIkNvbmZpcm1Bc3NpZ25tZW50IjogMSwNCiAgICAgICAgIk5vdGlmeUJ5U21zIjogMSwNCiAgICAgICAgIk5vdGlmeUJ5RW1haWwiOiAxLA0KICAgICAgICAiQ2FuRWRpdEF2YWlsYWJpbGl0eSI6IDEsDQogICAgICAgICJDYW5FZGl0U2VsZkluZm8iOiAxLA0KICAgICAgICAiQ2FuU2VlU2V0c1ByZXZpZXciOiAwLA0KICAgICAgICAiQ2FuU2VlQ3JpdEhpc3RvcnkiOiAwLA0KICAgICAgICAiQ2FuU2VlUmVmdW5kUmVwb3J0IjogMSwNCiAgICAgICAgIkNhbkFkZFJlZnVuZFJlY29yZHMiOiAwLA0KICAgICAgICAiQ2FuU2VlQnJhbmNoQWRkcmVzc2VzIjogMSwNCiAgICAgICAgIkNhbkFwcGx5Rm9yT3JkZXJzIjogMCwNCiAgICAgICAgIkFsbG93ZWRJcEFkZHJlc3NlcyI6ICIgIiwNCiAgICAgICAgIkNoZWNrZXJQcmlvcml0eSI6IDEyLA0KICAgICAgICAiQXZhaWxhYmlsaXR5UmFkaW91cyI6IDc4NiwNCiAgICAgICAgIk1vbnRobHlDcml0TGltaXQiOiA3ODYsDQogICAgICAgICJEYWlseUNyaXRMaW1pdCI6IDksDQogICAgICAgICJEYWlseVJlZ2lvbnNMaW1pdCI6IDEyLA0KICAgICAgICAiRGFpbHlDaXRpZXNMaW1pdCI6IDEyLA0KICAgICAgICAiVG90YWxDcml0cyI6IDc4NiwNCiAgICAgICAgIkFwcHJvdmVkQ3JpdHMiOiA3ODYsDQogICAgICAgICJEaXNhcHByb3ZlZENyaXRzIjogNzg2LA0KICAgICAgICAiQXV0b21hdGljQ3JpdEFwcHJvdmFsIjogMSwNCiAgICAgICAgIklzU2VsZlJlZ2lzdGVyZWQiOiAxLA0KICAgICAgICAiUmVnaXN0cmF0aW9uRGF0ZSI6ICIyMDExLTExLTA1IDI0OjAwOjEyIiwNCiAgICAgICAgIkNoZWNrZXJDb21tZW50cyI6ICJUaGlzIGlzIHRleHQgMTIzNCIsDQogICAgICAgICJJQ1EiOiAidGVzdGluZyAxMjMiLA0KICAgICAgICAiU2t5cGUiOiAidGVzdGluZyAxMjMiLA0KICAgICAgICAiTWVzc2VuZ2VyIjogInRlc3RpbmcgMTIzIiwNCiAgICAgICAgIlZPSVBQaG9uZSI6ICJ0ZXN0aW5nIDEyMyIsDQogICAgICAgICJMYXN0TGF0IjogMi4yLA0KICAgICAgICAiTGFzdExvbmciOiAyLjIsDQogICAgICAgICJSZWdpc3RyYXRpb25TdGF0dXMiOiA5LA0KICAgICAgICAiRW1haWxWYWxpZGF0ZWQiOiAiMjAxMS0xMS0wNSAyNDowMDoxMiINCiAgICB9LA0KICAgICJDdXN0b20iOiB7DQogICAgICAgICI1ODQiOiAidGVzdGluZyAxMjMiLA0KICAgICAgICAiNTg1IjogODUyMDAyMSwNCiAgICAgICAgIjU4NiI6ICJ0ZXN0aW5nIDEyMyIsDQogICAgICAgICI1ODciOiAidGVzdGluZyAxMjMiLA0KICAgICAgICAiNjUzIjogInRlc3RpbmcgMTIzIg0KICAgIH0NCn0=";
						userName = usernameSignup.getText().toString();
						password = passwordSignup.getText().toString();
						byte[] datatodecode = Base64.decode(userData,
								Base64.DEFAULT);
						try {
							userData = new String(datatodecode, "UTF-8");
							userData = userData.replace("dummyuserName",
									usernameSignup.getText().toString());
							userData = userData.replace("dummyPassword",
									passwordSignup.getText().toString());
							userData = userData.replace("dummyFullName",
									usernameSignup.getText().toString());
							userData = userData.replace("dummyPhone",
									phoneSignup.getText().toString());
							userData = userData.replace("dummyAddress",
									addressSignup.getText().toString());
							userData = userData.replace("dummyEmail",
									eMailSignup.getText().toString());

							Locale current = getResources().getConfiguration().locale;
							if (current.getDisplayLanguage().contains("fr")) {
								userData = userData.replace("223344", "18");
							} else {
								userData = userData.replace("223344", "39");
							}

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						byte[] data = null;
						try {
							data = userData.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (data != null) {
							userData = Base64.encodeToString(data,
									Base64.DEFAULT);
							Signup();
						}

					} else {
						showToast("Email format is not valid");
					}

				} else {
					showToast("All feilds are mandatory");
				}

			}
		});

	}

	public boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	private String SignupPost() {
		// Initialize the login data to POST
		List<NameValuePair> extraDataList = new ArrayList<NameValuePair>();

		extraDataList.add(new BasicNameValuePair("enc_request", userData));
		extraDataList.add(new BasicNameValuePair("app_id", app_id + ""));
		return Connector.postForm(Constants.getSignupURL(), extraDataList);
	}

	private void Signup() {
		class SignupTask extends AsyncTask<Void, Integer, String> {
			private String updateDate;

			@Override
			protected void onPreExecute() {
				// loaderlayout.setVisibility(RelativeLayout.VISIBLE);
				Revamped_Loading_Dialog.show_dialog(Signup.this, null);
				this.updateDate = null;
			}

			@Override
			protected void onPostExecute(String result) {
				// parse it here
				//
				if (result != null || result.length() != 0) {
					try {
						JSONObject jsonObj = new JSONObject(result);

						String success = jsonObj.getString("success");
						if (success.equals("true")) {
							setLogin(true);
							Intent intent = new Intent(
									Signup.this.getApplicationContext(),
									JobListActivity.class);
							intent.putExtra(Constants.IS_LOGIN, true);
							startActivity(intent);
							finish();
						} else {
							setLogin(false);
							showToast(jsonObj.getString("errormsg"));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Revamped_Loading_Dialog.hide_dialog();
			}

			@Override
			protected String doInBackground(Void... params) {
				String data = SignupPost();
				return data;
			}
		}
		new SignupTask().execute();
	}

	private void setLogin(boolean islogin) {
		SharedPreferences.Editor prefsEditor = myPrefs.edit();

		prefsEditor.putBoolean(Constants.ALREADY_LOGIN_STATUS, islogin);
		// prefsEditor.commit();
		// SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString(Constants.POST_FIELD_LOGIN_USERNAME, userName);
		prefsEditor.putString(Constants.POST_FIELD_LOGIN_PASSWORD, password);
		prefsEditor.commit();

	}
	private void loadViews(){
		Helper.changeTxtViewColor(textSignup);
		Helper.changeBtnColor(signupBtn);
	}

	private void showToast(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}
