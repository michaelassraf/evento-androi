package com.lbis.model.view;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapterAbs<LISTITEM extends ListItemAbs> extends BaseAdapter {

	protected LinkedList<LISTITEM> data;
	protected LayoutInflater inflater;
	protected Context ctx;

	public ListAdapterAbs(Context ctx, LinkedList<LISTITEM> data) {
		this.ctx = ctx;
		this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}

	public void setData(LinkedList<LISTITEM> newData) {
		this.data = newData;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addFirstItem(LISTITEM newItem) {
		data.addFirst(newItem);
	}

	public void addLastItem(LISTITEM newItem) {
		data.add(newItem);
	}

	public LISTITEM getItemAt(int index) {
		return data.get(index - 1);
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
