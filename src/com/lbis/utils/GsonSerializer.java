package com.lbis.utils;

import com.google.gson.Gson;

public class GsonSerializer {

	static Gson gson = null;

	public static Gson getInstance() {
		if (gson == null)
			gson = new Gson();

		return gson;
	}

}
