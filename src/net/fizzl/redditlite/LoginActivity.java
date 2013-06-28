package net.fizzl.redditlite;

import net.fizzl.redditlite.model.RedditAuthStore;
import net.fizzl.redditlite.model.RedditAuthentication;

import org.apache.http.cookie.Cookie;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity implements RedditAuthentication.RedditLoginCallback {
	private String mUsername;
	private String mPassword;
	private boolean mRemember;
	
	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;
	private CheckBox mRememberView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mUsernameView = (EditText) findViewById(R.id.username);
		mPasswordView = (EditText) findViewById(R.id.password);
		mRememberView = (CheckBox) findViewById(R.id.rememberme);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		RedditAuthStore auth = new RedditAuthStore(this);
		if(auth.getAuthCookie() != null) {
			loginDone();
		}
		else if(auth.getUsername() != null &&
				auth.getPassword() != null) {
			mUsername = auth.getUsername();
			mPassword = auth.getPassword();
		}
	}

	public void attemptLogin() {
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		showProgress(true);
		mUsername = mUsernameView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mRemember = mRememberView.isChecked();
		
		RedditAuthentication auth = new RedditAuthentication();
		auth.login(mUsername, mPassword, mRemember, this);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	private void loginDone() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle(R.string.error)
		.setMessage("Kaikki ok");
		alertBuilder.setNeutralButton(R.string.ok, null);
		AlertDialog alert = alertBuilder.create();
		alert.show();
	}

	@Override
	public void loginSuccess(Cookie authCookie, String modhash) {
		showProgress(false);
		RedditAuthStore store = new RedditAuthStore(this);
		if(mRemember) {
			store.saveStore(mUsername, mPassword, authCookie, modhash);
		}
		loginDone();
	}

	@Override
	public void loginFailed(Exception e, int message) {
		showProgress(false);
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setTitle(R.string.error)
		.setMessage(message);
		alertBuilder.setNeutralButton(R.string.ok, null);
		AlertDialog alert = alertBuilder.create();
		alert.show();
	}
}
