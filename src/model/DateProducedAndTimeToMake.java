package model;

import java.util.Date;

public final class DateProducedAndTimeToMake {
	public static Date dateProduced;
	public static int minutesToMake;

	public DateProducedAndTimeToMake(Date _dateProduced, int _minutesToMake) {
		dateProduced = _dateProduced;
		minutesToMake = _minutesToMake;
	}

	public DateProducedAndTimeToMake() {
		dateProduced = new Date();
		minutesToMake = 0;
	}

	@Override
	public String toString() {
		return "<html>Date produced: " + dateProduced.toString() + "<br>Minutes to make: " + minutesToMake;
	}
	
	public boolean equals(DateProducedAndTimeToMake other) {
		boolean equal = true;
		
		if (!this.dateProduced.equals(other.dateProduced)) {
			equal = false;
		}
		
		if (this.minutesToMake != other.minutesToMake) {
			equal = false;
		}
		
		return equal;
	}
}