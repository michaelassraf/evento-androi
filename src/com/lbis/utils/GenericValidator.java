/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lbis.utils;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * This class contains basic methods for performing validations.
 * 
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 */
public class GenericValidator implements Serializable {

	private static final long serialVersionUID = -7212095066891517618L;

	/**
	 * UrlValidator used in wrapper method.
	 */
	private static final UrlValidator URL_VALIDATOR = new UrlValidator();

	/**
	 * <p>
	 * Checks if the field isn't null and length of the field is greater than zero not including whitespace.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @return true if blank or null.
	 */
	public static boolean isBlankOrNull(String value) {
		return ((value == null) || (value.trim().length() == 0));
	}

	/**
	 * <p>
	 * Checks if the value matches the regular expression.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param regexp
	 *            The regular expression.
	 * @return true if matches the regular expression.
	 */
	public static boolean matchRegexp(String value, String regexp) {
		if (regexp == null || regexp.length() <= 0) {
			return false;
		}

		return Pattern.matches(regexp, value);
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(byte value, byte min, byte max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(int value, int min, int max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(float value, float min, float max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(short value, short min, short max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(long value, long min, long max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * <p>
	 * Checks if a value is within a range (min &amp; max specified in the vars attribute).
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum value of the range.
	 * @param max
	 *            The maximum value of the range.
	 * @return true if the value is in the specified range.
	 */
	public static boolean isInRange(double value, double min, double max) {
		return ((value >= min) && (value <= max));
	}

	/**
	 * Checks if the field is a valid credit card number.
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @return true if the value is valid Credit Card Number.
	 */

	/**
	 * <p>
	 * Checks if a field has a valid e-mail address.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @return true if the value is valid Email Address.
	 */
	public static boolean isEmail(String value) {
		return EmailValidator.getInstance().isValid(value);
	}

	/**
	 * <p>
	 * Checks if a field is a valid url address.
	 * </p>
	 * If you need to modify what is considered valid then consider using the UrlValidator directly.
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @return true if the value is valid Url.
	 */
	public static boolean isUrl(String value) {
		return URL_VALIDATOR.isValid(value);
	}

	/**
	 * <p>
	 * Checks if the value's length is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum length.
	 * @return true if the value's length is less than the specified maximum.
	 */
	public static boolean maxLength(String value, int max) {
		return (value.length() <= max);
	}

	/**
	 * <p>
	 * Checks if the value's adjusted length is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum length.
	 * @param lineEndLength
	 *            The length to use for line endings.
	 * @return true if the value's length is less than the specified maximum.
	 */
	public static boolean maxLength(String value, int max, int lineEndLength) {
		int adjustAmount = adjustForLineEnding(value, lineEndLength);
		return ((value.length() + adjustAmount) <= max);
	}

	/**
	 * <p>
	 * Checks if the value's length is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum length.
	 * @return true if the value's length is more than the specified minimum.
	 */
	public static boolean minLength(String value, int min) {
		return (value.length() >= min);
	}

	/**
	 * <p>
	 * Checks if the value's adjusted length is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum length.
	 * @param lineEndLength
	 *            The length to use for line endings.
	 * @return true if the value's length is more than the specified minimum.
	 */
	public static boolean minLength(String value, int min, int lineEndLength) {
		int adjustAmount = adjustForLineEnding(value, lineEndLength);
		return ((value.length() + adjustAmount) >= min);
	}

	/**
	 * Calculate an adjustment amount for line endings.
	 * 
	 * See Bug 37962 for the rational behind this.
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param lineEndLength
	 *            The length to use for line endings.
	 * @return the adjustment amount.
	 */
	private static int adjustForLineEnding(String value, int lineEndLength) {
		int nCount = 0;
		int rCount = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '\n') {
				nCount++;
			}
			if (value.charAt(i) == '\r') {
				rCount++;
			}
		}
		return ((nCount * lineEndLength) - (rCount + nCount));
	}

	// See http://issues.apache.org/bugzilla/show_bug.cgi?id=29015 WRT the
	// "value" methods

	/**
	 * <p>
	 * Checks if the value is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum numeric value.
	 * @return true if the value is &gt;= the specified minimum.
	 */
	public static boolean minValue(int value, int min) {
		return (value >= min);
	}

	/**
	 * <p>
	 * Checks if the value is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum numeric value.
	 * @return true if the value is &gt;= the specified minimum.
	 */
	public static boolean minValue(long value, long min) {
		return (value >= min);
	}

	/**
	 * <p>
	 * Checks if the value is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum numeric value.
	 * @return true if the value is &gt;= the specified minimum.
	 */
	public static boolean minValue(double value, double min) {
		return (value >= min);
	}

	/**
	 * <p>
	 * Checks if the value is greater than or equal to the min.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param min
	 *            The minimum numeric value.
	 * @return true if the value is &gt;= the specified minimum.
	 */
	public static boolean minValue(float value, float min) {
		return (value >= min);
	}

	/**
	 * <p>
	 * Checks if the value is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum numeric value.
	 * @return true if the value is &lt;= the specified maximum.
	 */
	public static boolean maxValue(int value, int max) {
		return (value <= max);
	}

	/**
	 * <p>
	 * Checks if the value is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum numeric value.
	 * @return true if the value is &lt;= the specified maximum.
	 */
	public static boolean maxValue(long value, long max) {
		return (value <= max);
	}

	/**
	 * <p>
	 * Checks if the value is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum numeric value.
	 * @return true if the value is &lt;= the specified maximum.
	 */
	public static boolean maxValue(double value, double max) {
		return (value <= max);
	}

	/**
	 * <p>
	 * Checks if the value is less than or equal to the max.
	 * </p>
	 * 
	 * @param value
	 *            The value validation is being performed on.
	 * @param max
	 *            The maximum numeric value.
	 * @return true if the value is &lt;= the specified maximum.
	 */
	public static boolean maxValue(float value, float max) {
		return (value <= max);
	}

}
