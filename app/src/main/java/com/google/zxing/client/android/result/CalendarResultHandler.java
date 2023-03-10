/*
 * Copyright (C) 2008 ZXing authors
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

package com.google.zxing.client.android.result;

import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.mor.sa.android.activities.R;

import android.app.Activity;
import android.content.Intent;

import java.text.DateFormat;
import java.util.Date;

/**
 * Handles calendar entries encoded in QR Codes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CalendarResultHandler extends ResultHandler {

  private static final int[] buttons = {
      R.string.button_add_calendar
  };

  public CalendarResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return buttons.length;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    CalendarParsedResult calendarResult = (CalendarParsedResult) getResult();
    
  }

  /**
   * Sends an intent to create a new calendar event by prepopulating the Add Event UI. Older
   * versions of the system have a bug where the event title will not be filled out.
   *
   * @param summary A description of the event
   * @param start   The start time
   * @param allDay  if true, event is considered to be all day starting from start time
   * @param end     The end time (optional)
   * @param location a text description of the event location
   * @param description a text description of the event itself
   */
  private void addCalendarEvent(String summary,
                                Date start,
                                boolean allDay,
                                Date end,
                                String location,
                                String description) {
    Intent intent = new Intent(Intent.ACTION_INSERT);
    intent.setType("vnd.android.cursor.item/event");
    long startMilliseconds = start.getTime();
    intent.putExtra("beginTime", startMilliseconds);
    if (allDay) {
      intent.putExtra("allDay", true);
    }
    long endMilliseconds;
    if (end == null) {
      if (allDay) {
        // + 1 day
        endMilliseconds = startMilliseconds + 24 * 60 * 60 * 1000;
      } else {
        endMilliseconds = startMilliseconds;
      }
    } else {
      endMilliseconds = end.getTime();
    }
    intent.putExtra("endTime", endMilliseconds);
    intent.putExtra("title", summary);
    intent.putExtra("eventLocation", location);
    intent.putExtra("description", description);
    launchIntent(intent);
  }


  @Override
  public CharSequence getDisplayContents() {

    
    return "";
  }

  private static String format(boolean allDay, Date date) {
    if (date == null) {
      return null;
    }
    DateFormat format = allDay
        ? DateFormat.getDateInstance(DateFormat.MEDIUM)
        : DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    return format.format(date);
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_calendar;
  }
}
