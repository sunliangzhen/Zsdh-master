package com.toocms.dink5.mylibrary.commonutils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

public class Commonly {
    public Commonly() {
    }

    public static String getTextByNull(String oldText, String newText) {
        return TextUtils.isEmpty(oldText)?newText:oldText;
    }

    public static String getViewText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Spanned stringToSpan(String src) {
        return src == null?null: Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<font color=").append(color).append(">").append(src).append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        String space = "&nbsp;";
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < number; ++i) {
            result.append("&nbsp;");
        }

        return result.toString();
    }
}
