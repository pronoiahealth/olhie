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
		bookCategories.add(new BookCategory("black", "Interface", "white"));
		bookCategories.add(new BookCategory("yellow", "Legal", "purple"));

		// Covers
		this.bookCovers = new ArrayList<BookCover>();
		bookCovers.add(new BookCover("Olhie/images/bookcover-green1.png", "Olhie/images/bookcover-green1_30X40.png", "Green 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-mauve1.png", "Olhie/images/bookcover-mauve1_30X40.png", "Mauve 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-wood1.png", "Olhie/images/bookcover-wood1_30X40.png", "Wood 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover0.png", "Olhie/images/bookcover0_30X40.png", "Brown 2"));
		bookCovers.add(new BookCover("Olhie/images/bookcover2.png", "Olhie/images/bookcover2_30X40.png", "Brown 3"));
		bookCovers.add(new BookCover("Olhie/images/bookcover3.png", "Olhie/images/bookcover3_30X40.png", "Blue 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover4.png", "Olhie/images/bookcover4_30X40.png", "Grey 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover5.png", "Olhie/images/bookcover5_30X40.png", "Blue-Grey 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover6.png", "Olhie/images/bookcover6_30X40.png", "Green 4"));
		bookCovers.add(new BookCover("Olhie/images/bookcover7.png", "Olhie/images/bookcover7_30X40.png", "Grey Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcover8.png", "Olhie/images/bookcover8_30X40.png", "Brown Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcover9.png", "Olhie/images/bookcover9_30X40.png", "Light Brown Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcoverX.png", "Olhie/images/bookcoverX_30X40.png", "Orange Designed"));
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
