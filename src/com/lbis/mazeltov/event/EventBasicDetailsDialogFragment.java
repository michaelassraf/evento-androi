package com.lbis.mazeltov.event;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lbis.concurrency.AsyncTaskMap;
import com.lbis.mazeltov.R;
import com.lbis.model.Event;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.EventType;
import com.lbis.utils.Utils;
import com.lbis.view.components.TimePickerFragment;
import com.lbis.view.components.UnlimitedDatePickerFragment;

public class EventBasicDetailsDialogFragment extends EventDialogFragmentBase {

	EditText eventNameView;
	TextView eventTypeView;
	EditText eventDescriptionView;
	TextView eventStartDateView;
	TextView eventStartTimeView;
	AutoCompleteTextView eventLocationView;
	Button eventResetButton;
	Button eventCommitButton;
	View main;

	static EventBasicDetailsDialogFragment eventFragment;

	public static EventBasicDetailsDialogFragment getDialogInstance() {
		return new EventBasicDetailsDialogFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setRetainInstance(true);
		main = inflater.inflate(R.layout.event_basic_details_fragment, null);
		eventNameView = (EditText) main.findViewById(R.id.event_creation_name);
		eventDescriptionView = (EditText) main.findViewById(R.id.event_creation_description);
		eventStartDateView = (TextView) main.findViewById(R.id.event_creation_start_date);
		eventStartTimeView = (TextView) main.findViewById(R.id.event_creation_start_hour);
		eventTypeView = (TextView) main.findViewById(R.id.event_type);

		eventLocationView = (AutoCompleteTextView) main.findViewById(R.id.event_creation_location);
		eventLocationView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.location_autocomplete_list_item));
		eventLocationView.setDropDownBackgroundResource(android.R.color.transparent);

		eventLocationView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				getCurrentEditedEvent().setEventLocation((String) adapterView.getItemAtPosition(position));
			}

		});

		eventCommitButton = (Button) main.findViewById(R.id.event_creation_commit);
		eventResetButton = (Button) main.findViewById(R.id.event_creation_reset);
		eventFragment = this;
		eventResetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resetCurrentEditedEvent();
				initializeAllViews(eventFragment);
			}
		});

		eventCommitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Event event = getCurrentEditedEvent();
				event.setEventDescription(eventDescriptionView.getText().toString());
				event.setEventName(eventNameView.getText().toString());
				String partialValidation = event.partialValidation();
				if (partialValidation != null) {
					getMessageBar(getActivity()).show(partialValidation);
					return;
				}

				Utils.getInstance().showDialog((SherlockFragmentActivity) getActivity(), EventBasicDetailsDialogFragment.class.getName(), EventPictureChoserDialogFragment.class.getName(), EventPictureChoserDialogFragment.getDialogInstance());
			}
		});

		initializeAllViews(this);
		return main;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		getCurrentEditedEvent().setStartDate(Utils.getInstance().setChosenDate(getActivity(), eventStartDateView, year, monthOfYear, dayOfMonth) / 1000);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		eventStartTimeView.setText(hourOfDay + " : " + minute);
	}

	private void initializeAllViews(final EventBasicDetailsDialogFragment eventBasicDetails) {
		eventStartDateView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UnlimitedDatePickerFragment newFragment = new UnlimitedDatePickerFragment();
				newFragment.setOnDateSetListene(eventBasicDetails);
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
			}
		});

		eventStartTimeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TimePickerFragment newFragment = new TimePickerFragment();
				newFragment.setListener(eventBasicDetails);
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
			}
		});
		eventTypeView.setText(getResources().getString(R.string.event_type_hint));
		eventTypeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DialogFragment() {
					public void onCreate(Bundle savedInstanceState) {
						super.onCreate(savedInstanceState);
						setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Fragmanet_Dialog);
						setCancelable(true);
					};

					public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
						View main = inflater.inflate(R.layout.event_type_chooser, null);
						final ListView eventTypesView = (ListView) main.findViewById(R.id.event_type_chooser_list_view);
						eventTypesView.setLayoutParams(new LinearLayout.LayoutParams(Utils.getInstance().getScreenSizes(getActivity())[0] / 2, Utils.getInstance().getScreenSizes(getActivity())[1] / 2));
						ArrayList<String> eventTypesValues = new ArrayList<String>();
						for (EventType type : EventType.values())
							eventTypesValues.add(type.getEventTypeValue());

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.event_type_list_item, eventTypesValues);
						eventTypesView.setAdapter(adapter);
						eventTypesView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								log.info(EventType.values()[position]);
								getCurrentEditedEvent().setEventType(EventType.values()[position]);
								eventTypeView.setText((String) eventTypesView.getItemAtPosition(position));
								getDialog().dismiss();

							}
						});
						return main;
					};

				}.show(getActivity().getSupportFragmentManager(), "eventTypeChoser");

			}
		});

		eventNameView.setText("");
		eventDescriptionView.setText("");
		eventStartDateView.setText("");
		eventStartTimeView.setText("");
		eventLocationView.setText("");
	}

	class GetPlacesAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

		String PREDICTIONS_PROPERTY_VALUE = "predictions";
		String DESCRIPTION_PROPERTY_VALUE = "description";

		@Override
		protected ArrayList<String> doInBackground(String... args) {
			ArrayList<String> resultList = null;
			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();
			try {
				String aPICallUrl = Enums.GoogleMapsAPI.APIURL.getGoogleMapsAPI() + URLEncoder.encode(args[0], Encoding.UTF_8.toString().toLowerCase(Locale.US));
				URL url = new URL(aPICallUrl);
				conn = (HttpURLConnection) url.openConnection();
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
			} catch (Throwable th) {
				log.error("Error processing Places API URL", th);
				return resultList;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			try {
				JSONObject jsonObj = new JSONObject(jsonResults.toString());
				JSONArray predsJsonArray = jsonObj.getJSONArray(PREDICTIONS_PROPERTY_VALUE);
				resultList = new ArrayList<String>(predsJsonArray.length());
				for (int i = 0; i < predsJsonArray.length(); i++) {
					resultList.add(predsJsonArray.getJSONObject(i).getString(DESCRIPTION_PROPERTY_VALUE));
				}
			} catch (Throwable th) {
				log.error("Cannot process JSON results", th);
			}

			return resultList;

		}
	}

	class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						try {
							AsyncTaskMap.getAsyncHashMap().cancelAll();
							GetPlacesAsyncTask task = new GetPlacesAsyncTask();
							AsyncTaskMap.getAsyncHashMap().add(task);
							task.execute(constraint.toString());
							resultList = task.get();
							filterResults.values = resultList;
							filterResults.count = resultList.size();
						} catch (Throwable th) {
							log.error("Couldn't preform Google Places API call", th);
						}
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}
}
