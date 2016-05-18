package com.lbis.model.view;

import com.lbis.model.Item;

public interface ListItemAbs {

	public String getMainText();

	public String getSubText();

	public String getCoolText();

	public String getPicturePath();

	public Long getId();

	public Long getClickListenerId();

	public Item getItem();

	public String getSearchText();

	public String getAddedText();

}
