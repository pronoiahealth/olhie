package com.pronoiahealth.olhie.client.widgets.bookscroller;

public class BookScrollerCache {
	private int itemW;
	private boolean expanded;
	private int idxClicked;
	private int totalItems;
	private boolean animating;
	private int winpos;

	public BookScrollerCache() {
	}

	public int getItemW() {
		return itemW;
	}

	public void setItemW(int itemW) {
		this.itemW = itemW;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public int getIdxClicked() {
		return idxClicked;
	}

	public void setIdxClicked(int idxClicked) {
		this.idxClicked = idxClicked;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public boolean isAnimating() {
		return animating;
	}

	public void setAnimating(boolean animating) {
		this.animating = animating;
	}

	public int getWinpos() {
		return winpos;
	}

	public void setWinpos(int winpos) {
		this.winpos = winpos;
	}
}
