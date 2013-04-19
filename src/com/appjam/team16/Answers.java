package com.appjam.team16;

import java.util.Date;

import android.text.format.DateFormat;

public class Answers {

	private long startTime;
	private long endTime;
	private long pauseStart;
	private long pauseEnd;
	private long totalPauseTime;
	private String totalTime;
	private int yesNoAnswer;
	private int seekBarValue;
	private boolean seen;

	public Answers() {
		totalPauseTime = 0;
		startTime = 0;
		endTime = 0;
		pauseStart = 0;
		pauseEnd = 0;
		totalTime = "";
		yesNoAnswer = 0;
		seekBarValue = 0;
		seen = false;
	}

	// if the user pressed yes, returns 1 for true, else returns 0 for false
	public int isYesNo(boolean answer) {
		seen = true;
		if (answer) {
			return 1;
		} else {
			return 0;
		}
	}

	// returns whether the question was seen already
	public boolean isSeen() {
		return seen;
	}

	// calculates the total time spent on the question
	public String getTotalTime() {
		long sum = (endTime - startTime - totalPauseTime) / 1000;
		totalTime = (String) DateFormat.format("kk:mm:ss", sum);
		return totalTime;
	}

	// finds the seekbar value 1-7
	public int getSeekBarValue() {
		seen = true;
		int value = 1;
		while (seekBarValue > 0) {
			seekBarValue = seekBarValue / 7 - value;
			value++;
		}
		return value;
	}

	// stores the start time of when the question began
	public void setStartTime(long startValue) {
		startTime = startValue;
	}

	// store the end time of when the question ends
	public void setEndTime(long endValue) {
		endTime = endValue;
	}

	// marks the beginning of when the start of a pause is
	public void setPauseStart(long pauseStartValue) {
		pauseStart = pauseStartValue;
	}

	// marks the ending of when the end of a pause is
	public void setPauseEnd(long pauseEndValue) {
		pauseEnd = pauseEndValue;
		totalPauseTime = totalPauseTime + (pauseEnd - pauseStart);
	}

	public void setSeekBarValue(int seekValue) {
		seekBarValue = seekValue;
	}

}
