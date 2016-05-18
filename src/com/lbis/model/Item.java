package com.lbis.model;

import com.lbis.utils.Enums;
import com.lbis.utils.Enums.ContentTypes;

public class Item {

	private Long itemId;
	private String itemUrl;
	private Enums.ContentTypes itemType;
	private Long connectedId;

	public Item(Long itemId, String itemUrl, ContentTypes itemType, Long connectedId) {
		this.itemId = itemId;
		this.itemUrl = itemUrl;
		this.itemType = itemType;
		this.connectedId = connectedId;
	}

	public Item(ContentTypes itemType, long connectedId) {
		this.itemType = itemType;
		this.connectedId = connectedId;
	}

	public Item() {
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public Enums.ContentTypes getItemType() {
		return itemType;
	}

	public void setItemType(Enums.ContentTypes itemType) {
		this.itemType = itemType;
	}

	public Long getConnectedId() {
		return connectedId;
	}

	public void setConnectedId(Long connectedId) {
		this.connectedId = connectedId;
	}

	public String getFormattedUrl() {
		if (this.itemUrl != null && this.itemUrl.contains("http"))
			return this.itemUrl;
		if (this.itemUrl != null && !this.itemUrl.contains("http"))
			return Enums.URLs.MediaPrefixURL.getValue() + this.itemUrl;
		return null;
	}
}
