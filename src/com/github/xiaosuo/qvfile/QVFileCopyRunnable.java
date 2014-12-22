package com.github.xiaosuo.qvfile;

import java.io.File;
import java.io.IOException;

import android.app.ProgressDialog;
import android.widget.Toast;
import android.app.Activity;

public class QVFileCopyRunnable implements Runnable {
	private Activity activity;
	private ProgressDialog dialog;
	private QVFile src;
	private File dest;
	private String content;

	/**
	 * Constructor.
	 */
	public QVFileCopyRunnable(Activity activity, ProgressDialog dialog,
			QVFile src, File dest, String content) {
		this.activity = activity;
		this.dialog = dialog;
		this.src = src;
		this.dest = dest;
		this.content = content;
	}

	@Override
	public void run() {
		try {
			src.copy(dest.getPath() + File.separator + src.getName(),
					new QVFile.ProgressIndicator() {
						@Override
						public void update(final int percent) {
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dialog.setProgress(percent);
								}
							});
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				Toast.makeText(activity,
						activity.getString(R.string.copied) + content + "!",
						Toast.LENGTH_SHORT)
					.show();
			}
		});
	}
}