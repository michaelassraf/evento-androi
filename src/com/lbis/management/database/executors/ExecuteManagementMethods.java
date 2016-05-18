package com.lbis.management.database.executors;

import org.apache.log4j.Logger;

import android.content.Context;
import android.database.Cursor;

import com.lbis.model.Token;
import com.lbis.server.objects.ServerMeta;

public class ExecuteManagementMethods {

	final Logger log = Logger.getLogger(getClass().getSimpleName());
	static Token token = null;

	public Token getTokenAndUserId(Context context) {
		if (token == null) {
			Cursor curs = SQLiteManagementClient.getConnetion(context).rawQuery("SELECT * FROM Management", null);
			curs.moveToFirst();
			if (curs != null && curs.getCount() > 0) {
				token = new Token(curs.getString(1), curs.getLong(0));
				log.info("Token extracted is " + token);
			}
			curs.close();
		}
		return token;
	}

	public void setTokenAndUserId(Context context, ServerMeta meta) {
		if (token == null || !meta.getToken().getTokenValue().equals(token.getTokenValue())) {
			StringBuilder sb = new StringBuilder().append("INSERT OR REPLACE INTO Management (userId, tokenValue) VALUES(").append("'" + meta.getToken().getUserId() + "',").append("'" + meta.getToken().getTokenValue() + "')");
			SQLiteManagementClient.getConnetion(context).execSQL(sb.toString());
			log.info("New token successfully imported !");
		} else
			log.info("No need to replace token");
	}

}
