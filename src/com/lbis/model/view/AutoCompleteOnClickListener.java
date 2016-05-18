package com.lbis.model.view;

import android.view.View.OnClickListener;

public abstract class AutoCompleteOnClickListener<OBJECTTYPE> implements OnClickListener {
	OBJECTTYPE handledObject;

	public OBJECTTYPE getHandledObject() {
		return handledObject;
	}

	public void setHandledObject(OBJECTTYPE handledObject) {
		this.handledObject = handledObject;
	}
}
