package com.github.xiaosuo.qvfile;

import java.io.File;
import java.io.IOException;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.github.xiaosuo.qvfile.QVFile;
import com.github.xiaosuo.qvfile.QVFileArrayAdapter;
import com.github.xiaosuo.qvfile.QVFileCopyRunnable;

/**
 * Main Activity.
 */
public class MainActivity extends Activity implements OnItemClickListener {
	private QVFile[] files = null;

	private static final DialogInterface.OnClickListener dismissOnly = new DialogInterface.OnClickListener() {		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// Dismiss this dialog only.
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			files  = QVFile.listFiles();
		} catch (IOException e) {
			Log.e("qvfile", "Failed to list the files.");
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),
					getString(R.string.fail_to_list),
					Toast.LENGTH_SHORT)
				.show();
		}
		ListView fileList = (ListView)findViewById(R.id.file_list);
		fileList.setAdapter(new QVFileArrayAdapter(this,
				R.layout.list_item,
				files));
		fileList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent,
			View view,
			int position,
			long id) {
		final QVFile file = files[position];
		if (!file.isCompleted()) {
			(new AlertDialog.Builder(this)).setMessage(file.getName() + " " + getString(R.string.is_not_completed) + "!")
				.setNeutralButton(android.R.string.ok, dismissOnly).create()
				.show();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final File destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		final String content = " " + file.getName() + " " + getString(R.string.to) + " " + destination.getPath();
		builder.setMessage(getString(R.string.copy) + content + "?")
			.setPositiveButton(android.R.string.copy, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setTitle(R.string.copying);
					progressDialog.setMessage(getString(R.string.copying) + content + " ...");
					progressDialog.setIndeterminate(false);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.show();
					if (!destination.exists())
						destination.mkdir();
					(new Thread(new QVFileCopyRunnable(MainActivity.this,
							progressDialog,
							file,
							destination,
							content))).start();
				}
			})
			.setNegativeButton(android.R.string.cancel, dismissOnly).create()
			.show();
	}
}
