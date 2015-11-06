package com.agible.ofb.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.agible.ofb.R;

/**
 * Created by seth on 5/8/15.
 */
public class MActionBar {
    public RelativeLayout actionBarLayout;
    public RelativeLayout actionBarParent;
    public ImageButton left;
    public Button center;
    public ImageButton right;
    public Context context;
    public View parent;


    public MActionBar(Context context, View parent, int resid){
        this.context = context;
        this.parent = parent;

        //find the actionbar by resid.
        actionBarParent = (RelativeLayout)parent.findViewById(resid);

        //initiate our image buttons.
        left = new ImageButton(context);
        center = new Button(context);
        right = new ImageButton(context);
        final int actionbarHeight = (int) Math.floor((getActionBarHeight() / 1.1));
        RelativeLayout.LayoutParams actionBarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, actionbarHeight);

        actionBarLayout = new RelativeLayout(context);
        actionBarParent.setPadding(0, getStatusBarHeight(), 0, 0);
        actionBarParent.addView(actionBarLayout, actionBarParams);
        //set the default image drawables.
        left.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_back));
        right.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_forward));
        center.setTransformationMethod(null);
        center.setText("Title");

        //setup our params for Left, Center and Right.
        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(actionbarHeight, actionbarHeight);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_START);

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(actionbarHeight, actionbarHeight);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);

        RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, actionbarHeight);
        centerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        centerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        left.setScaleType(ImageView.ScaleType.FIT_CENTER);
        left.setScaleX(1.1f);
        left.setScaleY(1.1f);

        right.setScaleType(ImageView.ScaleType.FIT_CENTER);
        right.setScaleX(1.1f);
        right.setScaleY(1.1f);

        //add the image buttons to it.
        actionBarLayout.addView(left, leftParams);
        actionBarLayout.addView(center, centerParams);
        actionBarLayout.addView(right, rightParams);

        resid = R.drawable.ab_item_bg;
        //setup the default item backgrounds.
        right.setBackground(context.getResources().getDrawable(resid));
        center.setBackground(context.getResources().getDrawable(resid));
        left.setBackground(context.getResources().getDrawable(resid));
    }

    //methods for setting the title and drawables.
    public void setTitle(String title){
        center.setText(title);
    }
    public void setTitleColor(int color){
        center.setTextColor(color);
    }

    public void setTitleSize(float size){
        center.setTextSize(size);
    }
    public void setLeftDrawable(int resid){
        left.setImageDrawable(context.getResources().getDrawable(resid));
    }
    public void setRightDrawable(int resid){
        right.setImageDrawable(context.getResources().getDrawable(resid));
    }

    //methods for setting backgrounds.
    public void setTitleBg(int resid){
        center.setBackground(context.getResources().getDrawable(resid));
    }
    public void setLeftBg(int resid){
        left.setBackground(context.getResources().getDrawable(resid));
    }
    public void setRightBg(int resid){
        right.setBackground(context.getResources().getDrawable(resid));
    }
    public void setBackground(int resid){
        actionBarParent.setBackground(context.getResources().getDrawable(resid));
    }



    //methods for handling the setting of onclick listeners.
    public void setLeftOnClickListener(ImageButton.OnClickListener listener){
        left.setOnClickListener(listener);
    }
    public void setCenterOnClickListener(Button.OnClickListener listener){
        center.setOnClickListener(listener);
    }
    public void setRightOnClickListener(ImageButton.OnClickListener listener){
        right.setOnClickListener(listener);
    }

    //getters..
    public ImageButton getLeft(){
        return left;
    }
    public ImageButton getRight(){
        return right;
    }
    public Button getCenter(){
        return center;
    }
    public RelativeLayout getActionBarLayout(){
        return actionBarLayout;
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight(){
        // Calculate ActionBar height
        int actionBarHeight = 50;

        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

}
