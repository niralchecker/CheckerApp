package com.checker.sa.android.helper;

import java.util.ArrayList;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.checker.sa.android.data.Answers;
import com.checker.sa.android.data.LoopsEntry;
import com.mor.sa.android.activities.JobDetailActivity;
import com.mor.sa.android.activities.R;

public class UIHelper {
    public static ArrayList<Answers> convertLoopToAnswers(
            ArrayList<LoopsEntry> loopData, Answers notFilledAnswer) {
        ArrayList<Answers> Ansnew = new ArrayList<Answers>();
        for (int i = 0; i < loopData.size(); i++) {
            Answers objAns = new Answers();
            if (loopData.get(i).getColumnData() != null
                    && loopData.get(i).getColumnData().toCharArray().length > 0)
                objAns.setAnswerID((i + (1000 / (i + 1)) + loopData.get(i)
                        .getColumnData().toCharArray()[0])
                        + "");
            else
                objAns.setAnswerID(i + 1000 + "");
            objAns.setAnswer(loopData.get(i).getColumnData());
            objAns.setAnswerCode(loopData.get(i).getColumnData());
            objAns.setBold("1");
            objAns.setColor("#000000");
            objAns.setItalics("0");
            objAns.setUnderline("0");

            objAns.setAdditionalInfoMandatory(null);
            objAns.setAllowCheckerToChange(null);
            objAns.setAllowCheckerToChangeType(null);
            objAns.setAltAnswer(null);
            objAns.setAnswerDisplayCondition(null);
            objAns.setAutoAssignAction(null);
            objAns.setAutoCreateAction(null);
            objAns.setAutoDueDate(null);
            objAns.setChangeWeightTo(null);
            objAns.setDefaultTask(null);
            objAns.setExclude(null);
            objAns.setHideAdditionalInfo(null);
            objAns.setIconLink(null);
            objAns.sethIconName(null);
            objAns.setJumpTo(null);
            objAns.setListJumpParams(null);
            objAns.setListTask(null);
            objAns.setMandatory(null);
            objAns.setMi(null);
            objAns.setTaskTypeLink(null);
            objAns.setTrainingToTake(null);
            objAns.setUpdateSubchapterGradeWith(null);
            objAns.setValue(null);
            Ansnew.add(objAns);
        }
        if (notFilledAnswer != null) {
            Ansnew.add(notFilledAnswer);
        }

        return Ansnew;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (params.height > 400)
            params.height = 400;
        listView.setLayoutParams(params);
    }

    public static int getScreenWidth(Context ctx) {

        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();

        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        double screenInches = Math.sqrt(x + y);

        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;

        if (screenInches < 6.00 && width > 1024.0) {
            width = width / 2;
        }

        return width;
    }

    public static int getJobSpinnerLayoutSize(Context ctx, int modeSelect) {

        int width = getScreenWidth(ctx);
        float offset = 0;
        if (modeSelect == 3) {
            if (width >= 900) {
                return R.layout.custom_spinner_row_two_hebrew;
            } else if (width >= 700) {
                return R.layout.custom_spinner_row_two_hebrew;
            } else if (width >= 500) {
                return R.layout.custom_spinner_row_one_hebrew;
            } else
                return R.layout.custom_spinner_row_one_hebrew;
        } else {
            if (width >= 900) {
                return R.layout.custom_spinner_row_two;
            } else if (width >= 700) {
                return R.layout.custom_spinner_row_two;
            } else if (width >= 500) {
                return R.layout.custom_spinner_row_one;
            } else {
                return R.layout.custom_spinner_row_one;
            }
        }
    }

    public static int getSpinnerLayoutSize(Context ctx, int modeSelect) {

        int width = getScreenWidth(ctx);
        float offset = 0;
        if (modeSelect == 3) {
            if (width >= 900) {
                return R.layout.custom_spinner_row_four_hebrew;
            } else if (width >= 700) {
                return R.layout.custom_spinner_row_three_hebrew;
            } else if (width >= 500) {
                return R.layout.custom_spinner_row_two_hebrew;
            } else
                return R.layout.custom_spinner_row_one_hebrew;
        } else {
            if (width >= 900) {
                return R.layout.custom_spinner_row_four;
            } else if (width >= 700) {
                return R.layout.custom_spinner_row_three;
            } else if (width >= 500) {
                return R.layout.custom_spinner_row_two;
            } else {
                return R.layout.custom_spinner_row_one;
            }
        }
    }

    public static int getSpinnerLayout(Context ctx, int modeSelect) {
        return R.layout.item_drop_down;
//		if (modeSelect == 3) {
//			if (width >= 900) {
//				return R.layout.custom_spinner_row_four_hebrew;
//			}
//
//			else if (width >= 700) {
//				return R.layout.custom_spinner_row_three_hebrew;
//			}
//
//			else if (width >= 500) {
//				return R.layout.custom_spinner_row_two_hebrew;
//			}
//
//			else
//				return R.layout.custom_spinner_row_one_hebrew;
//		} else {
//			if (width >= 900) {
//				return R.layout.custom_spinner_row_four;
//			}
//
//			else if (width >= 700) {
//				return R.layout.custom_spinner_row_three;
//			}
//
//			else if (width >= 500) {
//				return R.layout.custom_spinner_row_two;
//			}
//
//			else {
//				return R.layout.custom_spinner_row_one;
//			}
//		}
    }


    public static int getListLayoutSize(Context ctx) {
        int width = getScreenWidth(ctx);
        float offset = 0;
        if (width >= 900) {
            if (Helper.getTheme(ctx) == 0)
                return R.layout.custom_spinner_row_three_night;
            else
                return R.layout.custom_spinner_row_three;
        } else if (width >= 700) {
            if (Helper.getTheme(ctx) == 0)
                return R.layout.custom_spinner_row_two_night;
            else

                return R.layout.custom_spinner_row_two;
        } else if (width >= 500) {
            if (Helper.getTheme(ctx) == 0)
                return R.layout.custom_spinner_row_one_night;
            else
                return R.layout.custom_spinner_row_one;
        } else if (Helper.getTheme(ctx) == 0)
            return R.layout.custom_spinner_row_one_night;
        else
            return R.layout.custom_spinner_row_one;

    }

    public static float getFontSize(Context ctx, float currentSize) {
        int width = getScreenWidth(ctx);
        float offset = 0;
        if (width > 1080) {
            offset = 23;
        } else if (width > 900) {
            offset = 21;
        } else if (width >= 700) {
            offset = 20;
        } else if (width >= 500) {
            offset = 18;
        } else
            offset = 16;

        return offset;
    }

    public static float getFontSizeBigger(Context ctx, float currentSize) {
        int width = getScreenWidth(ctx);
        float offset = 0;
        if (width >= 900) {
            offset = 23;
        } else if (width >= 700) {
            offset = 21;
        } else if (width >= 500) {
            offset = 19;
        } else
            offset = 17;

        return offset;
    }

    public static float getFontSizeTabs(Context ctx, float currentSize) {
        int width = getScreenWidth(ctx);
        float offset = 0;
        if (width >= 1090) {
            offset = 12;
        } else if (width >= 730) {
            offset = 11;
        } else if (width >= 500) {
            offset = 11;
        } else if (width >= 400) {
            offset = 9;
        } else
            offset = 8;

        return 10;
    }

    public static int getDisplacementSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        if (width > height) {
            return (int) (height / 4.5);
        } else
            return (int) (width / 4.5);
        // int density = ctx.getResources().getDisplayMetrics().densityDpi;
        //
        // int offset = 500;
        // switch (density) {
        // case DisplayMetrics.DENSITY_LOW:
        // offset = 200;
        // break;
        // case DisplayMetrics.DENSITY_MEDIUM:
        // offset = 300;
        // break;
        // case DisplayMetrics.DENSITY_HIGH:
        // offset = 400;
        // break;
        // case DisplayMetrics.DENSITY_XHIGH:
        // offset = 500;
        // break;
        // }
        //
        // return offset;
    }

    public static int getDisplacementSize(Context ctx, String txt) {
        WindowManager wm = (WindowManager) ctx
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        if (width > height) {

            return (int) (height / 4.5);
        } else
            return (int) (width / 4.5);
    }

}
