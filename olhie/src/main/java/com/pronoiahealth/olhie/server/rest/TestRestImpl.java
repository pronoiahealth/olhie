package com.pronoiahealth.olhie.server.rest;

import com.pronoiahealth.olhie.client.shared.rest.TestRest;

public class TestRestImpl implements TestRest {

	public TestRestImpl() {
	}

	@Override
	public String getTest() {
		return "OK";
	}

}
