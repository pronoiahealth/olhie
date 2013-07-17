package com.pronoiahealth.olhie.client.pages.main;

import javax.enterprise.context.Dependent;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@Dependent
public class AcceptOfferDialog extends DialogBox {
	private String channelId;
	private String offererName;
	private AcceptOfferDialogCloseHandler closeHandler;
	private SimplePanel msgPanel;
	private Button yesBtn;
	private Button noBtn;

	public AcceptOfferDialog() {
		// Dialog properties
		setAutoHideEnabled(false);
		setModal(false);
		setAnimationEnabled(true);

		// Caption
		this.setText("Time to Chat?");

		// Style
		getElement().setAttribute("style", "z-index: 30001; opacity: .70;");

		// Set up the dialog box contents
		VerticalPanel vp = new VerticalPanel();
		vp.setHeight("200");
		vp.setWidth("150px");
		vp.setSpacing(10);

		// Build Chat widget
		vp.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		this.msgPanel = new SimplePanel();
		vp.add(msgPanel);

		// Button panel
		HorizontalPanel hp = new HorizontalPanel();
		vp.add(hp);
		hp.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

		// Yes
		this.yesBtn = new Button("Yes");
		yesBtn.setIcon(IconType.PLUS_SIGN);
		yesBtn.setSize(ButtonSize.SMALL);
		yesBtn.setType(ButtonType.PRIMARY);
		yesBtn.setStyleName("ph-ChatDialog-AcceptDialog-Btns", true);

		// No
		this.noBtn = new Button("No");
		noBtn.setIcon(IconType.MINUS_SIGN);
		noBtn.setSize(ButtonSize.SMALL);
		noBtn.setType(ButtonType.PRIMARY);
		noBtn.setStyleName("ph-ChatDialog-AcceptDialog-Btns", true);

		// Add buttons to dialog
		hp.add(yesBtn);
		hp.add(noBtn);

		// Set widget
		setWidget(vp);
	}

	public void init(final String channelId, final String offererName,
			final AcceptOfferDialogCloseHandler closeHandler) {
		this.channelId = channelId;
		this.offererName = offererName;
		this.closeHandler = closeHandler;

		// Set message panel text
		msgPanel.add(new HTML(getUserFromUserKey(offererName) + " wants to chat?"));

		// Add button handlers
		yesBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closeHandler.close(channelId, offererName, true);
			}
		});

		noBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				closeHandler.close(channelId, offererName, false);
			}
		});
	}

	private String getUserFromUserKey(String userKey) {
		int userIdStart = userKey.lastIndexOf("(");
		String userId = userKey
				.substring(userIdStart + 1, userKey.length() - 1);
		String name = userKey.substring(0, userIdStart - 1);
		return name;
	}

}
