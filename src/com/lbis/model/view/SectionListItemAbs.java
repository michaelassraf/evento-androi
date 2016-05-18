package com.lbis.model.view;

public interface SectionListItemAbs<HEADERLISTITEM extends ListItemAbs, CONTENTLISTITEM extends ListItemAbs> {

	// protected HEADERLISTITEM headerListItem;
	// private CONTENTLISTITEM contentListItem;

	// public SectionListItemAbs(HEADERLISTITEM headerListItem,
	// CONTENTLISTITEM contentListItem) {
	// this.headerListItem = headerListItem;
	// this.contentListItem = contentListItem;
	// }

	public HEADERLISTITEM getHeaderListItem();

	public CONTENTLISTITEM getContentListItem();
}