package com.pronoiahealth.olhie.client.shared.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.errai.common.client.api.annotations.Portable;

import com.orientechnologies.orient.core.annotation.OId;
import com.orientechnologies.orient.core.annotation.OVersion;

/**
 * Bookassetdescription.java<br/>
 * Responsibilities:<br/>
 * 1.
 *
 * @author Alex Roman
 * @version 1.0
 * @since Jul 7, 2013
 *
 */
@Portable
public class Bookstarrating {

	@OId
	private String id;

	@OVersion
	private Long version;

	@NotNull
	private Date updatedDate;

	@NotNull
	private String bookId;

	@NotNull
	@Size(min = 6, max = 20, message = "Must be between 6 and 20 characters")
	private String userId;

	@NotNull
	private Integer stars = 0;

	public Bookstarrating() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

}
