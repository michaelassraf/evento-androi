package com.lbis.mazeltov.event;

import com.lbis.model.Event;
import com.lbis.model.view.DialogFragmentBase;

public class EventDialogFragmentBase extends DialogFragmentBase {

	static Event event = null;

	public static Event getCurrentEditedEvent() {
		if (event == null)
			event = new Event();
		return event;
	}

	public static void resetCurrentEditedEvent() {
		event = null;
	}

}
