package com.pronoiahealth.olhie.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.pronoiahealth.olhie.client.shared.vo.BookCategory;
import com.pronoiahealth.olhie.client.shared.vo.BookCover;

@ApplicationScoped
public class TempCoverBinderHolder {
	private List<BookCategory> bookCategories;
	private List<BookCover> bookCovers;

	public TempCoverBinderHolder() {
		// Categories
		this.bookCategories = new ArrayList<BookCategory>();
		bookCategories.add(new BookCategory("black", "Interface"));
		bookCategories.add(new BookCategory("yellow", "Legal"));

		// Covers
		this.bookCovers = new ArrayList<BookCover>();
		bookCovers.add(new BookCover("Olhie/images/p1.png", "Paper"));
		bookCovers.add(new BookCover("Olhie/images/paper.png", "Paper 1"));
		bookCovers.add(new BookCover("Olhie/images/paper1.png", "Paper 2"));
	}
	
	public List<BookCategory> getCategories() {
		return bookCategories;
	}
	
	public List<BookCover> getCovers() {
		return bookCovers;
	}
	
	public BookCover getCoverByName(String name) {
		for (BookCover cover : bookCovers) {
			if (cover.getCoverName().equals(name)) {
				return cover;
			}
		}
		return null;
	}
	
	public BookCategory getCategoryByName(String name) {
		for (BookCategory cat : bookCategories) {
			if (cat.getCatagory().equals(name)) {
				return cat;
			}
		}
		return null;
	}

}
