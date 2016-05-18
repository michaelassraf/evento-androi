package com.lbis.model.view;

import java.util.LinkedList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.handmark.pulltorefresh.library.SectionedBaseAdapter;
import com.handmark.pulltotrfresh.myviews.PinnedHeaderListView.PinnedSectionedHeaderAdapter;

//Write sectioned list item w/ header and content views.
public abstract class SectionListAdapterAbs<LISTITEM extends SectionListItemAbs<?, ?>> extends SectionedBaseAdapter implements PinnedSectionedHeaderAdapter, ListAdapter {

	protected LinkedList<LISTITEM> data;
	protected LayoutInflater inflater;
	protected FragmentActivity act;

	public SectionListAdapterAbs(FragmentActivity act, LinkedList<LISTITEM> data) {
		this.act = act;
		this.inflater = (LayoutInflater) act.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}

	public LinkedList<LISTITEM> getData() {
		return data;
	}

	public void setData(LinkedList<LISTITEM> data) {
		this.data = data;
	}

	@Override
	public Object getItem(int section, int position) {
		return position;
	}

	@Override
	public long getItemId(int section, int position) {
		return position;
	}

	@Override
	public int getSectionCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public int getCountForSection(int section) {
		return 1;
	}

	public void addFirstItem(LISTITEM newItem) {
		data.addFirst(newItem);
	}

	public void addLastItem(LISTITEM newItem) {
		data.addLast(newItem);
	}

	@Override
	public abstract View getSectionHeaderView(int section, View convertView, ViewGroup parent);

	@Override
	public int getSectionHeaderViewType(int section) {
		return 0;
	}

}
