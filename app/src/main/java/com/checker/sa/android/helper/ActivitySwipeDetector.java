package com.checker.sa.android.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mor.sa.android.activities.QuestionnaireActivity;

public class ActivitySwipeDetector implements View.OnTouchListener {

 private Activity activity;
 static final int MIN_DISTANCE = 5;
 private float downX, downY, upX, upY;

 public ActivitySwipeDetector(final Activity activity) { 

  this.activity = activity;
 }

 public final void onRightToLeftSwipe() {

  Log.i("SWIPE","RightToLeftSwipe!");
 }

 public void onLeftToRightSwipe(){
  Log.i("SWIPE", "LeftToRightSwipe!");
 }

 public void onTopToBottomSwipe(){

  ((QuestionnaireActivity)activity).TopToBottomSwipe();
  Log.i("SWIPE", "onTopToBottomSwipe!");
 }

 public void onBottomToTopSwipe(){
  ((QuestionnaireActivity)activity).BottomToTopSwipe();
  Log.i("SWIPE", "onBottomToTopSwipe!");
 }

 public boolean onTouch(View v, MotionEvent event) {
  switch(event.getAction()){
  case MotionEvent.ACTION_DOWN: {
   downX = event.getX();
   downY = event.getY();
    return true;
  }
  case MotionEvent.ACTION_UP: {
   upX = event.getX();
   upY = event.getY();

   float deltaX = downX - upX;
   float deltaY = downY - upY;

   // swipe horizontal?
   if(Math.abs(deltaX) > MIN_DISTANCE){
    // left or right
    if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
    if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }
   } else { Log.i("SWIPE", "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE); }

   // swipe vertical?
   if(Math.abs(deltaY) > MIN_DISTANCE){
    // top or down
    if(deltaY < 0) { this.onTopToBottomSwipe(); return true; }
    if(deltaY > 0) { this.onBottomToTopSwipe(); return true; }
   } else { Log.i("SWIPE", "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE); }

       return true;
  }
  }
  return false;
 }
}