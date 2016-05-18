package com.lbis.utils;

import java.net.URL;

public class HTMLCreator {

	private static HTMLCreator htmlCreator;

	public static HTMLCreator getInstance() {
		if (htmlCreator == null)
			htmlCreator = new HTMLCreator();

		return htmlCreator;
	}

	public String getCroppedImageHTML(URL imageURL) {
		return getCroppedImageHTML(imageURL.toString());
	}

	public String getCroppedImageHTML(String imageURL) {
		StringBuilder sb = new StringBuilder().append("<html><body><img id=\"resizeImage\" src=\"").append(imageURL.toString()).append("\" style=\"width:200%;display:block;margin-left:auto;margin-right:auto;\"/></body></html>");
		return escapeHTML(sb.toString());
	}

	public String getEnlargedImageHTML(URL imageURL) {
		return getEnlargedImageHTML(imageURL.toString());
	}

	public String getEnlargedImageHTML(String imageURL) {
		return getEnlargedImageHTML(imageURL, 1.5);
	}

	public String getEnlargedImageHTML(String imageURL, double scale) {
		double scaling = scale * 100;
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><center><img id=\"resizeImage\" src=\"").append(imageURL).append("\" style=\"width: ").append(scaling).append("%;display: block;margin-left: auto;margin-right: auto;\" onclick=\"javascript:openPictureViewer('").append(imageURL).append("')\"/></center><script type=\"text/javascript\">function openPictureViewer(message){window.JSInterface.openPictureViewer(message);}</script></body></html>");
		return escapeHTML(sb.toString());
	}

	public String getRealSizedImage(URL imageURL) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><img id=\"resizeImage\" src=\"");
		sb.append(imageURL.toString());
		sb.append("\" style=\"display: block;margin-left: auto;margin-right: auto; align: center\"/></body></html>");
		return escapeHTML(sb.toString());
	}

	public String getFullScreenSizedImage(URL imageURL) {
		return getFullScreenSizedImage(imageURL.toString());
	}

	public String getFullScreenSizedImage(String imagePath) {
		StringBuilder sb = new StringBuilder().append("<html><body><center><img id=\"resizeImage\" src=\"").append(imagePath).append("\" style=\"width: ").append("100%;display: block;align: center;\" onclick=\"javascript:openPictureViewer('").append(imagePath).append("')\"/>").append("</center><script type=\"text/javascript\">function openPictureViewer(message){window.JSInterface.openPictureViewer(message);}</script></body></html>");
		// sb.append(";display: block;margin-left: auto;margin-right: auto; align: center\"/></body></html>");
		return escapeHTML(sb.toString());
	}

	public String bitLargedImageHTML(String imageURL) {
		// StringBuilder sb = new StringBuilder()
		// .append("<html><body><center><img onclick=\"javascript:openPictureViewer('")
		// .append(imageURL.toString())
		// .append("') id=\"resizeImage\" src=\"")
		// .append(imageURL.toString())
		// .append("\" style=\"width: ")
		// .append("150%;display: block;align: center;\"/></center><script type=\"text/javascript\">function openPictureViewer(message){window.JSInterface.openPictureViewer(message);}</script></body></html>");
		// //sb.append(";display: block;margin-left: auto;margin-right: auto; align: center\"/></body></html>");
		// return sb.toString();
		return getEnlargedImageHTML(imageURL.toString(), 3);
	}

	public String getFacebookProfileURL(int userId) {
		return getFacebookProfileURL("" + userId + "");
	}

	public String getFacebookProfileURL(String userId) {
		StringBuilder sb = new StringBuilder().append("https://graph.facebook.com/").append(userId).append("/picture?type=large");
		return sb.toString();
	}

	private String escapeHTML(String html) {
		StringBuilder buf = new StringBuilder(html.length());
		for (char c : html.toCharArray()) {
			switch (c) {
			case '#':
				buf.append("%23");
				break;
			case '%':
				buf.append("%25");
				break;
			case '\'':
				buf.append("%27");
				break;
			case '?':
				buf.append("%3f");
				break;
			default:
				buf.append(c);
				break;
			}
		}
		return buf.toString();
	}
}