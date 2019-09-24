package trippingo.optaplanner.resources;

/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Day{

    private int dayOfYear;
    private static final DateTimeFormatter DAY_FORMATTER
            = DateTimeFormatter.ofPattern("E", Locale.ENGLISH);
    
    private int firstTravelSlot;
    private int lastTravelSlot;

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public String getDateString() {
        return DAY_FORMATTER.format(toDate());
    }

    public LocalDate toDate() {
        return LocalDate.ofYearDay(LocalDate.now().getYear(), dayOfYear);
    }

	public int getFirstTravelSlot() {
		return firstTravelSlot;
	}

	public void setFirstTravelSlot(int startTimeGrain) {
		this.firstTravelSlot = startTimeGrain;
	}

	public int getLastTravelSlot() {
		return lastTravelSlot;
	}

	public void setLastTravelSlot(int endTimeGrain) {
		this.lastTravelSlot = endTimeGrain;
	}

    
 
}

