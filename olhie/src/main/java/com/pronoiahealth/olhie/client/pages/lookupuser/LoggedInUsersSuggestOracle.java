package com.pronoiahealth.olhie.client.pages.lookupuser;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;

import com.pronoiahealth.olhie.client.shared.events.errors.ClientErrorEvent;
import com.pronoiahealth.olhie.client.shared.services.ConnectedUserService;
import com.pronoiahealth.olhie.client.shared.vo.ConnectedUser;
import com.pronoiahealth.olhie.client.widgets.suggestoracle.BlockingSuggestOracle;
import com.pronoiahealth.olhie.client.widgets.suggestoracle.GenericMultiWordSuggestion;

public class LoggedInUsersSuggestOracle extends BlockingSuggestOracle {

	@Inject
	private Caller<ConnectedUserService> connectedUserService;

	@Inject
	private Event<ClientErrorEvent> clientErrorEvent;

	public LoggedInUsersSuggestOracle() {
	}

	@Override
	public void sendRequest(final Request request, final Callback callback) {
		connectedUserService.call(new RemoteCallback<List<ConnectedUser>>() {
			@Override
			public void callback(List<ConnectedUser> response) {
				List<GenericMultiWordSuggestion<ConnectedUser>> retLst = new ArrayList<GenericMultiWordSuggestion<ConnectedUser>>();
				if (response != null) {
					for (ConnectedUser user : response) {
						GenericMultiWordSuggestion<ConnectedUser> sug = new GenericMultiWordSuggestion<ConnectedUser>(
								user);
						retLst.add(sug);
					}
				}
				Response resp = new Response();
				resp.setSuggestions(retLst);
				callback.onSuggestionsReady(request, resp);
			}
		}, new ErrorCallback() {
			@Override
			public boolean error(Object message, Throwable throwable) {
				String errMsg = throwable.getMessage();
				clientErrorEvent
						.fire(new ClientErrorEvent(
								errMsg == null ? "Unknown error has occured."
										: errMsg));
				return false;
			}
		}).getConnectedUsers(request.getQuery());
	}

}
