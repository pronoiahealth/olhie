package com.pronoiahealth.olhie.client.widgets.bookscroller;

public class NavDataHolder {
	private int scroll;
	private int idxClicked;
	private int factor;

	public NavDataHolder() {
	}

	public NavDataHolder(int scroll, int idxClicked, int factor) {
		super();
		this.scroll = scroll;
		this.idxClicked = idxClicked;
		this.factor = factor;
	}

	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}

	public int getIdxClicked() {
		return idxClicked;
	}

	public void setIdxClicked(int idxClicked) {
		this.idxClicked = idxClicked;
	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

}
