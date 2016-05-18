package com.lbis.database.model;

import com.lbis.utils.GsonSerializer;

public abstract class ValueObjectAbs<CLASSTYPE extends SimpleIfc<CLASSTYPE>> {

	public String getObjectClassAsJSON() {
		return GsonSerializer.getInstance().toJson(this);
	}

	public CLASSTYPE getObjectFromJSON(String json, CLASSTYPE object) {
		return (CLASSTYPE) GsonSerializer.getInstance().fromJson(json, object.getClassType());
	}

}
