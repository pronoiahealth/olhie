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
		bookCovers.add(new BookCover("Olhie/images/bookcover-green1.png", "Green 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-green2.png", "Green 2"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-green3.png", "Green 3"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-brown1.png", "Brown 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover-mauve1.png", "Mauve 1"));
		bookCovers.add(new BookCover("Olhie/images/wood1.png", "Wood 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover0.png", "Brown 2"));
		bookCovers.add(new BookCover("Olhie/images/bookcover2.png", "Brown 3"));
		bookCovers.add(new BookCover("Olhie/images/bookcover3.png", "Blue 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover4.png", "Grey 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover5.png", "Blue-Grey 1"));
		bookCovers.add(new BookCover("Olhie/images/bookcover6.png", "Green 4"));
		bookCovers.add(new BookCover("Olhie/images/bookcover7.png", "Grey Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcover8.png", "Brown Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcover9.png", "Light Brown Designed"));
		bookCovers.add(new BookCover("Olhie/images/bookcoverX.png", "Orange Designed"));
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
