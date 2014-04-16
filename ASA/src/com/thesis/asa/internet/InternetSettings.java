/*******************************************************************************
 * Copyright (c) 2014 CodingBad.
 * All rights reserved.  This file is part of ASA.
 * 
 * ASA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASA.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 * Ayelén Chavez - ashy.on.line@gmail.com
 * Joaquín Rinaudo - jmrinaudo@gmail.com
 ******************************************************************************/
 
 
package com.thesis.asa.internet;

import java.util.Arrays;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.thesis.asa.Utilities;
import com.thesis.asa.Data.SecurityMode;
import com.thesis.asa.provider.SettingsDB;
import com.thesis.asa.resourcemvc.Resource;

public class InternetSettings extends Resource {

	public InternetSettings(Context c) {
		super(c);
	}

	protected static String[] columns = SettingsDB.INTERNET_TABLE_COLUMNS;

	@Override
	public String tableName() {
		return SettingsDB.INTERNET_TABLE;
	}

	@Override
	public String permissions() {
		return Manifest.permission.INTERNET;
	}

	public Object[] loadSettingsFromConfiguration(Object configuration) {
		String[] internetConfiguration = Utilities
				.stringToArray((String) configuration);
	
		return internetConfiguration;
	}
	
	@Override
	public ContentValues createDBEntry(String pkgName, String processes,
			Object[] selectedConfigurations) {
		ContentValues values = new ContentValues();
		values.put(SettingsDB.COL_PKG_NAME, pkgName);
		values.put(SettingsDB.COL_PROCESSES_NAMES, processes);

		for (int index = 0; index < columns.length; index++)
			values.put(columns[index], (String) selectedConfigurations[index]);

		return values;
	}

	@Override
	public Object configurationFromCursor(Cursor cursor) {

		String[] configuration = new String[columns.length];

		for (int index = 0; index < columns.length; index++) {
			String column = columns[index];
			int columnIndex = cursor.getColumnIndex(column);
			configuration[index] = cursor.getString(columnIndex);
		}

		return Arrays.toString(configuration);
	}

	@Override
	public Uri uri() {
		return Uri
				.parse("content://com.thesis.asa.settings/internet_settings");
	}

	@Override
	public Object[] getConfigurationByMode(SecurityMode mode, int isSystem) {
		String[] configuration = new String[columns.length];
		String option = null;
		switch (mode) {
		case PERMISSIVE:
			option = "Enable";
			break;
		case SECURE:
			if (isSystem == 1)
				option = "Enable";
			else
				option = "Disable";
			break;
		case PARANOID:
			option = "Disable";
			break;
		}

		for (int index = 0; index < columns.length; index++) {
			configuration[index] = option;
		}

		return configuration;
	}

}
