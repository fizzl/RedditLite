package net.fizzl.redditlite.engine;

public class ListItem {
	private String subreddit;
	private String selftextHtml;
	private String selftext;
	private String linkFlairText;
	private String id;
	private boolean clicked;
	private String title;
	private int score;
	private boolean over18;
	private boolean hidden;
	private String thumbnail;
	private boolean isSelf;
	private String permalink;
	private String name;
	private float created;
	private float createdUtc;
	private String url;
	private String author;
	public String getSubreddit() {
		return subreddit;
	}
	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	public String getSelftextHtml() {
		return selftextHtml;
	}
	public void setSelftextHtml(String selftextHtml) {
		this.selftextHtml = selftextHtml;
	}
	public String getSelftext() {
		return selftext;
	}
	public void setSelftext(String selftext) {
		this.selftext = selftext;
	}
	public String getLinkFlairText() {
		return linkFlairText;
	}
	public void setLinkFlairText(String linkFlairText) {
		this.linkFlairText = linkFlairText;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isClicked() {
		return clicked;
	}
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isOver18() {
		return over18;
	}
	public void setOver18(boolean over18) {
		this.over18 = over18;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getPermalink() {
		return permalink;
	}
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCreated() {
		return created;
	}
	public void setCreated(float created) {
		this.created = created;
	}
	public float getCreatedUtc() {
		return createdUtc;
	}
	public void setCreatedUtc(float createdUtc) {
		this.createdUtc = createdUtc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
}
