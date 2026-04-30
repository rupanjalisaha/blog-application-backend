package com.projects.sms.dtos;

import java.time.LocalDate;

public class SearchRequest {
	
	private String keyword;
    private String genre;

    // FREE tier allowed
    private String sortBy; // latest, likes

    // PREMIUM (ignored in free tier)
    private Long minViews;
    private Long maxViews;
    private Long authorId;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public Long getMinViews() {
		return minViews;
	}
	public void setMinViews(Long minViews) {
		this.minViews = minViews;
	}
	public Long getMaxViews() {
		return maxViews;
	}
	public void setMaxViews(Long maxViews) {
		this.maxViews = maxViews;
	}
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	private int page = 0;
    private int size = 10;

}
