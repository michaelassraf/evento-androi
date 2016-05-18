package com.lbis.model.view;

import java.util.LinkedList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbis.mazeltov.R;
import com.lbis.server.WebReciever;
import com.lbis.server.WebRequester;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class AutoCompleteAdapter<CLASSTYPE extends ListItemAbs, ACTIONCLASSTYPE extends WebRequester<?, ?, ?>> extends ArrayAdapter<CLASSTYPE> implements Filterable {
	private LinkedList<CLASSTYPE> objectsList;
	private ACTIONCLASSTYPE actions;
	private Context context;
	AutoCompleteOnClickListener<CLASSTYPE> listener;
	LinkedList<CLASSTYPE> expectedList;
	ListAdapterAbs<CLASSTYPE> expectedAdapter;

	public AutoCompleteAdapter(Context context, int textViewResourceId, ACTIONCLASSTYPE actions, AutoCompleteOnClickListener<CLASSTYPE> listener) {
		super(context, textViewResourceId);
		objectsList = new LinkedList<CLASSTYPE>();
		this.actions = actions;
		this.context = context;
		this.listener = listener;
	}

	public AutoCompleteAdapter(Context context, int textViewResourceId, ACTIONCLASSTYPE actions, LinkedList<CLASSTYPE> expectedList, ListAdapterAbs<CLASSTYPE> expectedAdapter) {
		super(context, textViewResourceId);
		objectsList = new LinkedList<CLASSTYPE>();
		this.actions = actions;
		this.context = context;
		this.expectedList = expectedList;
		this.expectedAdapter = expectedAdapter;
	}

	@Override
	public int getCount() {
		if (objectsList != null)
			return objectsList.size();
		else
			return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.autocomplete_list_view, null);

		TextView autocompleteTitle = (TextView) vi.findViewById(R.id.autocomplete_title);
		TextView autocompleteDetails = (TextView) vi.findViewById(R.id.autocomplete_details);
		ImageView autocompletePicture = (ImageView) vi.findViewById(R.id.autocomplete_picutre);
		final CLASSTYPE autocompleteObject = objectsList.get(position);
		autocompleteTitle.setText(autocompleteObject.getMainText());
		autocompleteDetails.setText(autocompleteObject.getSearchText());
		Utils.getInstance().loadDesignedImageView(autocompletePicture, autocompleteObject.getPicturePath(), parent.getContext(), 3, 6, false, false, false, PostType.Image, null, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 30, Utils.getInstance().getScreenSizes(parent.getContext())[0] / 30);

		vi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isExistInList(autocompleteObject.getId())) {
					expectedList.add(autocompleteObject);
					expectedAdapter.notifyDataSetChanged();
				}
			}
		});
		return vi;
	}

	private boolean isExistInList(Long id) {
		if (expectedList != null && expectedList.size() > 0)
			for (CLASSTYPE object : expectedList)
				if (object.getId() == id)
					return true;
		return false;
	}

	@Override
	public CLASSTYPE getItem(int index) {
		return null;
	}

	@Override
	public Filter getFilter() {

		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						try {
							objectsList = (LinkedList<CLASSTYPE>) actions.searchForObjects(constraint.toString(), WebReciever.getInstance().getConnection(), context);
						} catch (Exception e) {
							Log.i("Error", e.getMessage());
						}
						// Now assign the values and count to the
						// FilterResults object
						if (objectsList != null) {
							filterResults.values = objectsList;
							filterResults.count = objectsList.size();
						}
					}
					return filterResults;
				}
				return null;
			}

			@Override
			protected void publishResults(CharSequence contraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return myFilter;
	}
}
