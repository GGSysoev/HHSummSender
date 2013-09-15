package ru.hh.summsender;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class SummaryActivity extends SherlockFragmentActivity {

	public static String SUMM_EXTRA_NAME = 
			"ru.hh.summsender.summaryName";
	public static String SUMM_EXTRA_DATE = 
			"ru.hh.summsender.summaryDate";
	public static String SUMM_EXTRA_SEX = 
			"ru.hh.summsender.summarySex";
	public static String SUMM_EXTRA_POSITION = 
			"ru.hh.summsender.summaryPosition";
	public static String SUMM_EXTRA_SALARY = 
			"ru.hh.summsender.summarySalary";
	public static String SUMM_EXTRA_PHONE = 
			"ru.hh.summsender.summaryPhone";
	public static String SUMM_EXTRA_EMAIL = 
			"ru.hh.summsender.summaryEmail";
	
	private static int SUMM_VIEWER_ACTIVITY = 0x100;
	
	private EditText etName;
	private EditText etDate;
	private RadioButton rbMan;
	private EditText etPosition;
	private EditText etSalary;
	private EditText etPhone;
	private EditText etEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle(getResources().getString(R.string.activity_sender));
		setContentView(R.layout.activity_summary);
		
		etName = (EditText)findViewById(R.id.et_name);
		etDate = (EditText)findViewById(R.id.et_date);
		rbMan = (RadioButton)findViewById(R.id.rb_man);
		etPosition = (EditText)findViewById(R.id.et_position);
		etSalary = (EditText)findViewById(R.id.et_salary);
		etPhone = (EditText)findViewById(R.id.et_phone);
		etEmail = (EditText)findViewById(R.id.et_email);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.summary, menu);
		return true;
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "birthdatePicker");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			sendSummary();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == SUMM_VIEWER_ACTIVITY) {

			if (resultCode == RESULT_OK) {
				String message = data.getStringExtra(SummViewerActivity.SUMMARY_RESULT);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Ответ работодателя");
				builder.setMessage(message);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			} else {
				String message = "Отзыв не был получен";
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Ответ работодателя");
				builder.setMessage(message);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}
	}
	
	private void sendSummary() {
		if (etName.getText().toString().equals("")
				|| etDate.getText().toString().equals("")
				|| etName.getText().toString().equals("")
				|| etPosition.getText().toString().equals("")
				|| etSalary.getText().toString().equals("")
				|| etPhone.getText().toString().equals("")
				|| etEmail.getText().toString().equals("")) {
			Toast.makeText(this, "Все поля обязательны для заполнения",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			new AcceptDialog().show(getSupportFragmentManager(),
					"acceptSending");
		}

	}
	
	private void sendIntents() {
		Intent intent = new Intent(this, SummViewerActivity.class);
		intent.putExtra(SUMM_EXTRA_NAME, etName.getText().toString());
		intent.putExtra(SUMM_EXTRA_DATE, etDate.getText().toString());
		intent.putExtra(SUMM_EXTRA_SEX, rbMan.isChecked());
		intent.putExtra(SUMM_EXTRA_POSITION, etPosition.getText().toString());
		intent.putExtra(SUMM_EXTRA_SALARY, etSalary.getText().toString());
		intent.putExtra(SUMM_EXTRA_PHONE, etPhone.getText().toString());
		intent.putExtra(SUMM_EXTRA_EMAIL, etEmail.getText().toString());
		startActivityForResult(intent, SUMM_VIEWER_ACTIVITY);
	}

	public static class AcceptDialog extends SherlockDialogFragment implements
			DialogInterface.OnClickListener {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Внимание!");
			builder.setMessage("После отправки данные нельзя будет изменить");
			builder.setPositiveButton("Продолжить", this);
			builder.setNegativeButton("Отмена", null);
			return builder.create();
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			((SummaryActivity)getActivity()).sendIntents();
		}
		
	}
	
	public static class DatePickerFragment extends SherlockDialogFragment implements
			DatePickerDialog.OnDateSetListener {

		EditText etDate;
		EditText etPosition;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			etDate = (EditText)getActivity().findViewById(R.id.et_date);
			etPosition = (EditText)getActivity().findViewById(R.id.et_position);
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			//trying to restore data
			if (!etDate.getText().toString().equals("")) {
				String strDate = etDate.getText().toString();
				String[] strArray = strDate.split("\\.");
				year = Integer.parseInt(strArray[2]);
				month = Integer.parseInt(strArray[1]) - 1;
				day = Integer.parseInt(strArray[0]);
			}
			
			BirthDatePickerDialog dpd = new BirthDatePickerDialog(getActivity(), this, year, month, day);
			return dpd;
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			String d = day < 10 ? "0" + day : "" + day;
			String m = month + 1 < 10 ? "0" + (month + 1) : "" + (month + 1);
			((EditText)getActivity().findViewById(R.id.et_date)).setText(d + "." + m + "." + year);
			etPosition.findFocus();
		}
		
		public class BirthDatePickerDialog extends DatePickerDialog {

			public BirthDatePickerDialog(Context context,
					OnDateSetListener callBack, int year, int monthOfYear,
					int dayOfMonth) {
				super(context, callBack, year, monthOfYear, dayOfMonth);
				setTitle(getResources().getString(R.string.birthdate));
			}
			
			@Override
			public void onDateChanged(DatePicker view, int year, int month,
					int day) {
				super.onDateChanged(view, year, month, day);
				setTitle(getResources().getString(R.string.birthdate));
			}
			
		}
	}
}
