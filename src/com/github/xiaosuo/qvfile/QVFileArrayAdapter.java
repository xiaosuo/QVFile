package com.github.xiaosuo.qvfile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.xiaosuo.qvfile.QVFile;
import com.github.xiaosuo.qvfile.PrettyPrint;

public class QVFileArrayAdapter extends ArrayAdapter<QVFile> {
	private Context context;
	private int layoutResourceId;
	private QVFile[] files;
	
	/**
	 * Helper for the view.
	 */
	private static final class ViewHolder {
		public TextView	name;
		public TextView	size;
	}
	
	/**
	 * Constructor.
	 */
	public QVFileArrayAdapter(Context context, int layoutResourceId, QVFile[] files) {
		super(context, layoutResourceId, files);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.files = files;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView)row.findViewById(R.id.file_name);
			holder.size = (TextView)row.findViewById(R.id.file_size);
			row.setTag(holder);
		} else {
			holder = (ViewHolder)row.getTag();
		}
		QVFile file = files[position];
		String name = file.getName();
		// Don't show the file extension.
		int pos = name.lastIndexOf('.');
		if (pos > 0)
			name = name.substring(0, pos);
		holder.name.setText(name);
		holder.size.setText(PrettyPrint.length(file.length()));
		return row;
	}
}
