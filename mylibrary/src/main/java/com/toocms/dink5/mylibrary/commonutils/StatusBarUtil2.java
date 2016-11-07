package com.toocms.dink5.mylibrary.commonutils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class StatusBarUtil2 {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    public StatusBarUtil2() {
    }

    public static void setColor(Activity activity, int color) {
        setColor(activity, color, 112);
    }

    public static void setColor(Activity activity, int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(-2147483648);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            View statusView = createStatusBarView(activity, color, statusBarAlpha);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            setRootView(activity);
        }

    }

    public static void setColorNoTranslucent(Activity activity, int color) {
        setColor(activity, color, 0);
    }

    public static void setColorDiff(Activity activity, int color) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            View statusView = createStatusBarView(activity, color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            setRootView(activity);
        }
    }

    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, 112);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            setTransparent(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparent(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            setRootView(activity);
        }
    }

    public static void setTranslucentDiff(Activity activity) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            setRootView(activity);
        }

    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 112);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(-2147483648);
                activity.getWindow().clearFlags(67108864);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(67108864);
            }

            View statusBarView = createStatusBarView(activity, color);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.addView(statusBarView, 0);
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, int color) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            View statusBarView = createStatusBarView(activity, color);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.addView(statusBarView, 0);
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }

    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, 112);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (VERSION.SDK_INT >= 19) {
            setTransparentForDrawerLayout(activity, drawerLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if (VERSION.SDK_INT >= 19) {
            if (VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(-2147483648);
                activity.getWindow().clearFlags(67108864);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(67108864);
            }

            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }
    }

    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if (VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }

    }

    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(16908290);
        if (contentView.getChildCount() > 1) {
            contentView.removeViewAt(1);
        }

        contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
    }

    private static View createStatusBarView(Activity activity, int color) {
        View statusBarView = new View(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static View createStatusBarView(Activity activity, int color, int alpha) {
        View statusBarView = new View(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(16908290)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    @TargetApi(19)
    private static void transparentStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(-2147483648);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().addFlags(134217728);
            activity.getWindow().setStatusBarColor(0);
        } else {
            activity.getWindow().addFlags(67108864);
        }

    }

    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        View statusBarView = new View(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    private static int calculateStatusColor(int color, int alpha) {
        float a = 1.0F - (float) alpha / 255.0F;
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        red = (int) ((double) ((float) red * a) + 0.5D);
        green = (int) ((double) ((float) green * a) + 0.5D);
        blue = (int) ((double) ((float) blue * a) + 0.5D);
        return -16777216 | red << 16 | green << 8 | blue;
    }
}
