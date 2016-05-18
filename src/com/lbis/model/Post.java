package com.lbis.model;

import java.util.Locale;

import android.text.format.DateUtils;

import com.lbis.database.model.KeyObjectIfc;
import com.lbis.database.model.SimpleIfc;
import com.lbis.database.model.ValueObjectAbs;
import com.lbis.model.view.ListItemAbs;
import com.lbis.model.view.PostContentListItem;
import com.lbis.model.view.PostHeaderListItem;
import com.lbis.model.view.PostListItem;
import com.lbis.utils.Enums;
import com.lbis.utils.Enums.PostType;
import com.lbis.utils.Utils;

public class Post extends ValueObjectAbs<Post> implements KeyObjectIfc, SimpleIfc<Post>, PostListItem, ListItemAbs {

	private long postId;
	private long postEventId;
	private String postEventName;
	private User postPoster;
	private String postTitle;
	private Item postContent;
	private Enums.PostType postType;
	private int postLikes;
	private long postCreateDate;

	public Post() {
	}

	public Post(long postEventId, String postTitle, PostType postType) {
		this.postEventId = postEventId;
		this.postTitle = postTitle;
		this.postType = postType;
	}

	public Post(long postId, long postEventId, String postEventName, User postPoster, long postPosterId, String postTitle, Item postContent, PostType postType, int postLikes, long postCreateDate) {
		this.postId = postId;
		this.postEventId = postEventId;
		this.postEventName = postEventName;
		this.postPoster = postPoster;
		this.postTitle = postTitle;
		this.postContent = postContent;
		this.postType = postType;
		this.postLikes = postLikes;
		this.postCreateDate = postCreateDate;
	}

	public Item getPostContent() {
		return postContent;
	}

	public void setPostContent(Item postContent) {
		this.postContent = postContent;
	}

	public long getPostEventId() {
		return postEventId;
	}

	public void setPostEventId(long postEventId) {
		this.postEventId = postEventId;

	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public User getPostPoster() {
		return postPoster;
	}

	public void setPostPoster(User postPoster) {
		this.postPoster = postPoster;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public Enums.PostType getPostType() {
		return postType;
	}

	public void setPostType(Enums.PostType postType) {
		this.postType = postType;
	}

	public int getPostLikes() {
		return postLikes;
	}

	public void setPostLikes(int postLikes) {
		this.postLikes = postLikes;
	}

	@Override
	public Class<Post> getClassType() {
		return Post.class;
	}

	@Override
	public long getObjectKey() {
		return getPostId();
	}

	@Override
	public Long getId() {
		return this.postId;
	}

	public String getPostEventName() {
		return postEventName;
	}

	public void setPostEventName(String postEventName) {
		this.postEventName = postEventName;
	}

	public long getPostCreateDate() {
		return postCreateDate;
	}

	public void setPostCreateDate(long postCreateDate) {
		this.postCreateDate = postCreateDate;
	}

	@Override
	public PostHeaderListItem getHeaderListItem() {
		return new PostHeaderListItem() {

			@Override
			public String getSubText() {
				return getPostPoster() == null ? "" : getPostPoster().getUserFirstName() + " " + getPostPoster().getUserLastName();
			}

			@Override
			public String getPicturePath() {
				if (getPostPoster() == null || getPostPoster().getPicturePath() == null)
					return Utils.getInstance().getUserProfile(null);
				if (getPostPoster().getPicturePath().contains("http"))
					return getPostPoster().getPicturePath();
				return Utils.getInstance().getUserProfile(getPostPoster() == null ? null : getPostPoster().getId());
			}

			@Override
			public String getMainText() {
				return getPostTitle();
			}

			@Override
			public Long getId() {
				return getPostId();
			}

			@Override
			public String getCoolText() {
				return getCoolTextString();
			}

			@Override
			public Long getClickListenerId() {
				return getPostEventId();
			}

			@Override
			public Item getItem() {
				return getPostContent();
			}

			@Override
			public String getSearchText() {
				return "";
			}

			@Override
			public String getAddedText() {
				return "";
			}

		};
	}

	@Override
	public PostContentListItem getContentListItem() {
		return new PostContentListItem() {

			@Override
			public String getSubText() {
				return getPostPoster() == null ? "" : getPostPoster().getUserFirstName() + " " + getPostPoster().getUserLastName();
			}

			@Override
			public String getPicturePath() {
				return getPostContent().getFormattedUrl();
			}

			@Override
			public String getMainText() {
				return getPostTitle();
			}

			@Override
			public Long getId() {
				return getPostId();
			}

			@Override
			public String getCoolText() {
				return getCoolTextString();
			}

			@Override
			public Long getClickListenerId() {
				return getPostEventId();
			}

			@Override
			public Item getItem() {
				return getPostContent();
			}

			@Override
			public String getSearchText() {
				return "";
			}

			@Override
			public String getAddedText() {
				return "";
			}

		};
	}

	private String getCoolTextString() {
		try {
			return new StringBuilder().append(getPostPoster().getMainText()).append(" posted new ").append(getPostType().toString().toLowerCase(Locale.getDefault())).append(" on ").append(getPostEventName()).append(" !").toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	@Override
	public String getTimeFromPost() {
		return DateUtils.getRelativeTimeSpanString(getPostCreateDate() * 1000).toString();
	}

	@Override
	public String getMainText() {
		return getMainText();
	}

	@Override
	public String getSubText() {
		return getPostTitle();
	}

	@Override
	public String getCoolText() {
		return getContentListItem().getCoolText();
	}

	@Override
	public String getPicturePath() {
		return getContentListItem().getPicturePath();
	}

	@Override
	public Long getClickListenerId() {
		return getContentListItem().getClickListenerId();
	}

	@Override
	public Item getItem() {
		return getContentListItem().getItem();
	}

	@Override
	public String getSearchText() {
		return "";
	}

	@Override
	public String getAddedText() {
		return "";
	}

}
