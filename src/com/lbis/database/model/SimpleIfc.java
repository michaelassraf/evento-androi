package com.lbis.database.model;

public interface SimpleIfc<CLASSTYPE> {

	abstract Class<CLASSTYPE> getClassType();

	abstract Long getId();

}
