package com.example.happer.model;

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happer.R;

public class Main extends Activity {
	private Button submit;
	private Button share;
	private EditText edittxt;
	private TextView aboutUs;
	private String aboutUsText = "SOME TEXT ABOUT";

	private Intent sendIntent;

	private String priwords;
	private String secwords;
	private String resultWord;
	private String shareWord;

	private Random r = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		submit = (Button) findViewById(R.id.submitbutton);
		edittxt = (EditText) findViewById(R.id.editbeforshare);

		share = (Button) findViewById(R.id.update);
		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				shareWord = resultWord + " - Generiert mit Happr!";
				sendIntent = new Intent(android.content.Intent.ACTION_SEND);
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, shareWord);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});

		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getJsonData();
				edittxt.setText(resultWord);

			}
		});
	}

	protected String getJsonData() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		String response = null;

		try {
			
			response = CustomHttpClient.executeHttpPost(
					"http://domain.com/jsongrab.php", postParameters);

			String result = response.toString();

			try {
				JSONArray jArray = new JSONArray(result);
				int size = jArray.length();
				int randomObjectIndex = r.nextInt(size);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray
							.getJSONObject(randomObjectIndex);

					priwords = json_data.getString("priwordsentry");
					secwords = json_data.getString("secwordentry");

					resultWord = priwords + secwords;

				}
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}

			try {
			} catch (Exception e) {
				Log.e("log_tag", "Error in Display!" + e.toString());
			}
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection!!" + e.toString());
			makeToast("Keine Internetverbindung.");
		}
		return resultWord;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			openAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openAbout() {
		setContentView(R.layout.activity_main_about);
		aboutUs = (TextView) findViewById(R.id.aboutUs);
		aboutUs.setText(Html.fromHtml(aboutUsText));
	}

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

	}

}
