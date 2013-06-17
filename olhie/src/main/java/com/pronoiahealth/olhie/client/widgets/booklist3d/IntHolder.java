package com.pronoiahealth.olhie.client.widgets.booklist3d;

public class IntHolder {
	private int intVal = 0;

	public IntHolder() {
	}

	public int getIntVal() {
		return intVal;
	}

	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}

	public void subOne() {
		intVal--;
	}

	public void plusOne() {
		intVal++;
	}
}
