package com.pronoiahealth.olhie.client.widgets.bookscroller;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.pronoiahealth.olhie.client.shared.vo.BookDisplay;

public class BookScroller extends Widget {

	/**
	 * Some components that use this class will be ApplicationScoped. We don't
	 * want to do a re-bind of gQuery events on every call to this methods
	 * onLoad handler. We only want to do it once.
	 */
	private boolean hasBeenAttached = false;

	private String containerId;

	/**
	 * Root element
	 */
	private DivElement rootDiv;

	private BookScrollerCache cache;
	private BookScrollerOptions opts;

	public BookScroller(String containerId, List<BookDisplay> books) {
		super();
		this.containerId = containerId;

		// Create root element
		Document doc = Document.get();
		this.rootDiv = doc.createDivElement();
		this.setElement(rootDiv);
		rootDiv.setId(containerId);
		rootDiv.setClassName("ca-container");

		// Now the wrapper
		DivElement wrapperDiv = doc.createDivElement();
		rootDiv.appendChild(wrapperDiv);
		wrapperDiv.setClassName("ca-wrapper");

		for (BookDisplay book : books) {
			// Item
			DivElement itemDiv = doc.createDivElement();
			wrapperDiv.appendChild(itemDiv);
			itemDiv.setClassName("ca-item");

			// Main for item
			DivElement mainDiv = doc.createDivElement();
			itemDiv.appendChild(mainDiv);
			mainDiv.addClassName("ca-item-main");

			// Book (icon) view for mainDiv
			DivElement bookIconDiv = doc.createDivElement();
			mainDiv.appendChild(bookIconDiv);
			bookIconDiv.addClassName("ca-icon");

			// h3 with title
			HeadingElement h3Title = doc.createHElement(3);
			mainDiv.appendChild(h3Title);
			h3Title.setInnerText(book.getBook().getBookTitle());

			// h4 with author name
			HeadingElement h4Author = doc.createHElement(4);
			mainDiv.appendChild(h4Author);
			SpanElement h4SpanChild = doc.createSpanElement();
			h4Author.appendChild(h4SpanChild);
			h4SpanChild.setInnerText(book.getAuthorFullName());

			// link to more content
			AnchorElement aMore = doc.createAnchorElement();
			mainDiv.appendChild(aMore);
			aMore.setHref("#");
			aMore.setClassName("ca-more");
			aMore.setInnerText("more...");

			// Contenet wrapper
			DivElement contentWrapperDiv = doc.createDivElement();
			itemDiv.appendChild(contentWrapperDiv);
			contentWrapperDiv.addClassName("ca-content-wrapper");

			// Content
			DivElement contentDiv = doc.createDivElement();
			contentWrapperDiv.appendChild(contentDiv);
			contentDiv.addClassName("ca-content");

			// h6 Title again
			HeadingElement h6Content = doc.createHElement(6);
			contentDiv.appendChild(h6Content);
			h6Content.setInnerHTML(book.getBook().getBookTitle());

			// link to close
			AnchorElement aClose = doc.createAnchorElement();
			contentDiv.appendChild(aClose);
			aClose.setHref("#");
			aClose.setClassName("ca-close");
			aClose.setInnerText("close");

			// Introduction div
			DivElement contentTxt = doc.createDivElement();
			contentDiv.appendChild(contentTxt);
			contentTxt.setClassName("ca-content-text");

			// Introduction
			ParagraphElement intro = doc.createPElement();
			contentTxt.appendChild(intro);
			contentTxt.setInnerText(book.getBook().getIntroduction());

			// list
			UListElement list = doc.createULElement();
			contentDiv.appendChild(list);

			// Link to open directly
			LIElement li = doc.createLIElement();
			list.appendChild(li);
			AnchorElement aBookLink = doc.createAnchorElement();
			li.appendChild(aBookLink);
			aBookLink.setHref("#");
			aBookLink.setInnerText("View Book");
		}

		this.cache = new BookScrollerCache();
		this.opts = new BookScrollerOptions();
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		if (this.hasBeenAttached == false) {
			this.hasBeenAttached = true;
		}

		final GQuery $el = $(rootDiv);
		final GQuery $wrapper = $el.find("div.ca-wrapper");
		GQuery $items = $wrapper.children("div.ca-item");

		// Config cache
		cache.setItemW($items.width());
		cache.setTotalItems($items.length());

		// Scroll settings
		// control the scroll value
		if (opts.getScroll() < 1) {
			opts.setScroll(1);
		} else if (opts.getScroll() > 3) {
			opts.setScroll(3);
		}

		// hide the items except the first 3
		$wrapper.css(CSS.OVERFLOW.with(Overflow.HIDDEN));

		// the items will have position absolute
		// calculate the left of each item
		$items.each(new Function() {
			@Override
			public Object f(Element e, int i) {
				$(e).css(
						Properties.create("position: 'absolute', left: " + i
								* cache.getItemW() + "px"));
				return null;
			}
		});

		// Nav buttons
		// add navigation buttons
		if (cache.getTotalItems() > 3) {
			$el.prepend("<div class=\"ca-nav\"><span class=\"ca-nav-prev\">Previous</span>"
					+ "<span class=\"ca-nav-next\">Next</span></div>");

			GQuery $navPrev = $el.find("span.ca-nav-prev");
			GQuery $navNext = $el.find("span.ca-nav-next");

			// navigate left
			$navPrev.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					if (cache.isAnimating() == true) {
						return false;
					} else {
						cache.setAnimating(true);
						navigate(-1, $el, $wrapper, opts, cache);
						return true;
					}
				}
			});

			// navigate left
			$navNext.bind(Event.ONCLICK, new Function() {
				@Override
				public boolean f(Event e) {
					if (cache.isAnimating() == true) {
						return false;
					} else {
						cache.setAnimating(true);
						navigate(1, $el, $wrapper, opts, cache);
						return true;
					}
				}
			});
		}

		// More button
		$el.find("a.ca-more").live(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (cache.isAnimating() == true) {
					return false;
				} else {
					cache.setAnimating(true);
				}
				
				$(e).hide();
				GQuery $item = $(e).closest("div.ca-item");
				Element el = $item.elements()[0];
				openItem( $wrapper, el, opts, cache );
				return false;
			}
		});

		// Close button
		$el.find("a.ca-close").live(Event.ONCLICK, new Function() {
			@Override
			public boolean f(Event e) {
				if (cache.isAnimating() == true) {
					return false;
				} else {
					cache.setAnimating(true);
				}
				
				GQuery $item = $(e).closest("div.ca-item");
				Element el = $item.elements()[0];
				closeItems( $wrapper, el, opts, cache );
				return false;
			}
		});

		// Mouse wheel
		$el.bind(Event.ONMOUSEWHEEL, new Function() {
			@Override
			public boolean f(Event e) {
				// Check to see what is happening currently
				if (cache.isAnimating() == true) {
					return false;
				} else {
					cache.setAnimating(true);
				}
				
				int mVel = e.getMouseWheelVelocityY();
				if (mVel > 0) {
					navigate(-1, $el, $wrapper, opts, cache);
				} else {
					navigate(1, $el, $wrapper, opts, cache);
				}
				return false;
			}

		});

	}

	private void navigate(final int dir, final GQuery $el,
			final GQuery $wrapper, final BookScrollerOptions opts,
			final BookScrollerCache cache) {

		final NavDataHolder holder = new NavDataHolder(opts.getScroll(), 0, 1);

		if (cache.isExpanded() == true) {
			// Always 1 in full mode
			holder.setScroll(1);

			// 3 times bigger than 1 collapsed item
			holder.setFactor(3);

			// index of clicked item
			holder.setIdxClicked(cache.getIdxClicked());
		}

		// Clone the elements on the right / left and append / prepend them
		// according to dir and scroll
		if (dir == 1) {
			$wrapper.find("div.ca-item:lt(" + holder.getScroll() + ")").each(
					new Function() {
						@Override
						public Object f(Element e, int i) {
							GQuery $q = $(e);
							double pos = (-1 * (holder.getScroll() - i + holder
									.getIdxClicked()))
									* cache.getItemW()
									* holder.getFactor();
							$q.clone().css(CSS.LEFT.with(Length.px(pos)))
									.appendTo($wrapper);
							return null;
						}
					});
		} else {
			final GQuery first = $wrapper.children().eq(0);
			int findPos = cache.getTotalItems() - 1 - holder.getScroll();
			$wrapper.find("div.ca-item:gt(" + findPos + ")").each(
					new Function() {
						@Override
						public Object f(com.google.gwt.user.client.Element e,
								int i) {
							GQuery $el = $(e);
							double pos = (-1 * (holder.getScroll() - i + holder
									.getIdxClicked()))
									* cache.getItemW()
									* holder.getFactor();
							$el.clone().css(CSS.LEFT.with(Length.px(pos)))
									.insertBefore($el);
							return null;
						}
					});
		}

		// animate the left of each item
		// the calculations are dependent on dir and on the cache.expanded value
		$wrapper.find("div.ca-item").each(new Function() {
			@Override
			public Object f(Element e, int i) {
				final GQuery $item = $(e);
				String propStrVal = dir == 1 ? "-="
						+ (cache.getItemW() * holder.getFactor() * holder
								.getScroll()) + "px" : "+="
						+ (cache.getItemW() * holder.getFactor() * holder
								.getScroll()) + "px";
				$item.stop().animate("left: '" + propStrVal + "'",
						opts.getSliderSpeed(), opts.getSliderEasing(),
						new Function() {
							@Override
							public void f(Element e) {
								if (dir == 1
										&& $item.position().left < (-1
												* holder.getIdxClicked()
												* cache.getItemW() * holder
													.getFactor())
										|| dir == -1
										&& $item.position().left > ((cache
												.getTotalItems() - 1 - holder
												.getIdxClicked())
												* cache.getItemW() * holder
													.getFactor())) {
									// remove the item that was cloned
									$item.remove();
								}
								cache.setAnimating(false);
							}
						});
				return null;
			}
		});

	}

	private void openItem(final GQuery $wrapper, final Element itemSelected,
			final BookScrollerOptions opts, final BookScrollerCache cache) {

		cache.setIdxClicked($wrapper.index(itemSelected));
		final GQuery $item = $(itemSelected);

		// the item's position (1, 2, or 3) on the viewport (the visible items)
		cache.setWinpos(getWinPos($item.position().left, cache));
		$wrapper.find("div.ca-item").not($item).hide();
		String propsStr = "width: '" + cache.getItemW() * 2 + "px', left: '"
				+ cache.getItemW() + "px'";

		$item.find("div.ca-content-wrapper")
				.css(CSS.LEFT.with(Length.px(cache.getItemW())))
				.stop()
				.animate(propsStr, opts.getItemSpeed(), opts.getItemEasing())
				.end()
				.stop()
				.animate("left	: '0px'", opts.getItemSpeed(),
						opts.getItemEasing(), new Function() {
							@Override
							public void f(Element e) {
								cache.setAnimating(false);
								cache.setExpanded(true);
								openItems($wrapper, itemSelected, opts, cache);
							}
						});
	}

	private void openItems(final GQuery $wrapper, Element openedItem,
			final BookScrollerOptions opts, final BookScrollerCache cache) {

		final int openedIdx = $wrapper.index(openedItem);
		$wrapper.find("div.ca-item").each(new Function() {
			@Override
			public Object f(Element e, int i) {
				GQuery $item = $(e);
				int idx = $wrapper.index(e);
				if (idx != openedIdx) {
					double leftPos = -1 * (openedIdx - idx) * cache.getItemW()
							* 3;
					$item.css(CSS.LEFT.with(Length.px(leftPos)))
							.show()
							.find("div.ca-content-wrapper")
							.css(Properties.create("left: " + cache.getItemW()
									+ "px, width: " + cache.getItemW() * 2
									+ "px"));

					// hide more link
					toggleMore($item, false);
				}
				return null;
			}
		});
	}

	private void toggleMore(GQuery $item, boolean show) {
		GQuery $more = $item.find("a.ca-more");
		if (show == true) {
			$more.show();
		} else {
			$more.hide();
		}
	}

	private void closeItems(final GQuery $wrapper, Element openedItemElem,
			BookScrollerOptions opts, final BookScrollerCache cache) {

		final int openedIdx = $wrapper.index(openedItemElem);
		GQuery $openedItem = $(openedItemElem);
		$openedItem
				.find("div.ca-content-wrapper")
				.stop()
				.animate("width: '0px'", opts.getItemSpeed(),
						opts.getItemEasing())
				.end()
				.stop()
				.animate(
						"left: '" + cache.getItemW() * (cache.getWinpos() - 1)
								+ "px'", opts.getItemSpeed(),
						opts.getItemEasing(), new Function() {
							@Override
							public void f(Element e) {
								cache.setAnimating(false);
								cache.setExpanded(false);
							}
						});

		toggleMore($openedItem, true);

		$wrapper.find("div.ca-item").each(new Function() {
			@Override
			public Object f(Element e, int i) {
				GQuery $item = $(e);
				int idx = $wrapper.index(e);
				if (idx != openedIdx) {
					double pos = (cache.getWinpos() - 1) - (openedIdx - idx)
							* cache.getItemW();

					$item.find("div.ca-content-wrapper")
							.css(CSS.WIDTH.with(Length.px(0))).end()
							.css(CSS.LEFT.with(Length.px(pos))).show();

					// show more link
					toggleMore($item, true);
				}
				return null;
			}
		});
	}

	private int getWinPos(int val, BookScrollerCache cache) {
		if (val == cache.getItemW()) {
			return 2;
		} else if (val == (cache.getItemW() * 2)) {
			return 3;
		} else {
			return 1;
		}
	}

}
