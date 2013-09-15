package ru.hh.summsender;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class SummViewerActivity extends SherlockActivity {

	public static String SUMMARY_RESULT = "ru.hh.summsender.RESPONSE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summ_viewer);
		getSupportActionBar().setTitle(getResources().getString(R.string.activity_reciever));
		TextView tvInf =(TextView)findViewById(R.id.tv_inf);
		String str = "<u>" + getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_NAME) + "</u><br>" +
				"Дата рождения: " + getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_DATE) + "<br>" +
				"Пол: " + (getIntent().getBooleanExtra(SummaryActivity.SUMM_EXTRA_SEX, true) ? "М" : "Ж") + "<br>" +
				"Желаемая должность: " + getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_POSITION) + "<br>" +
				"Зарплата: " + getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_SALARY) + " руб.";
		tvInf.setText(Html.fromHtml(str));
		
		TextView tvTel =(TextView)findViewById(R.id.tv_app_number);
		TextView tvMail =(TextView)findViewById(R.id.tv_app_email);
		
		tvTel.setText(getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_PHONE));
		tvMail.setText(getIntent().getStringExtra(SummaryActivity.SUMM_EXTRA_EMAIL));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.summ_viewer, menu);
		return true;
	}
	
	public void sendResponse(View v) {
		if (((EditText) findViewById(R.id.et_response)).toString().equals("")) {
			Toast.makeText(this, "Отзыв не может быть пустой",
					Toast.LENGTH_LONG).show();
		} else {
			Intent intent = new Intent();
			intent.putExtra(SUMMARY_RESULT,
					((EditText) findViewById(R.id.et_response)).getText().toString());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
