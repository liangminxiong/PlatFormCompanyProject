package com.common.utils;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewUtils {


    private static String mCountWhat = "0";

    /*设置RelativeLayout height width*/
    public static void setRLHightOrWidth(RelativeLayout relativeLayout, int height, int width) {
        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        relativeLayout.setLayoutParams(params);
    }

    /*LinearLayout height width*/
    public static void setLLHightOrWidth(LinearLayout linearLayout, int height, int width) {
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);
    }

    /*TextView height width*/
    public static void setTVHightOrWidth(TextView textView, int height, int width) {
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        params.height = height;
        params.width = width;
        textView.setLayoutParams(params);
    }

    /*TextView height width*/
    public static void setTvSizeColor(TextView textView, float size, int color) {
        textView.setTextSize(size);
        textView.setTextColor(color);
    }



    /*ImageView height width*/
    public static void setIVHightOrWidth(ImageView imageView, int height, int width) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.height = height;
        params.width = width;
        imageView.setLayoutParams(params);
    }

    /*EditView height width*/
    public static void setEditHightOrWidth(EditText editText, int height, int width) {
        ViewGroup.LayoutParams params = editText.getLayoutParams();
        params.height = height;
        params.width = width;
        editText.setLayoutParams(params);
    }

    /*PopupWindow是否显示*/
    public static boolean isPopuwindowShowing(PopupWindow popupwindow) {
        if (popupwindow != null) {
            return popupwindow.isShowing();
        }
        return false;
    }

    /*LinearLayout 显示隐藏*/
    public static void setLlInVisible(LinearLayout linearLayout, boolean visible) {
        int type = 0;
        if (visible) {
            type = View.INVISIBLE;
        } else {
            type = View.VISIBLE;
        }
        linearLayout.setVisibility(type);
    }

    /*RelativeLayout 显示隐藏*/
    public static void setRlInVisible(RelativeLayout relativeLayout, boolean visible) {
        int type;
        if (visible) {
            type = View.INVISIBLE;
        } else {
            type = View.VISIBLE;
        }
        relativeLayout.setVisibility(type);
    }


    /*Butto 显示隐藏*/
    public static void setBtnInVisible(Button view, boolean visible) {
        int type;
        if (visible) {
            type = View.INVISIBLE;
        } else {
            type = View.VISIBLE;
        }
        view.setVisibility(type);
    }

    /*TextView 显示不可见*/
    public static void setTvVisibleOrGone(TextView view, boolean visible) {
        int type;
        if (visible) {
            type = View.VISIBLE;
        } else {
            type = View.GONE;
        }
        view.setVisibility(type);
    }

    /*TextView 显示不可见*/
    public static void setTvInVisible(TextView view, boolean visible) {
        int type;
        if (visible) {
            type = View.INVISIBLE;
        } else {
            type = View.VISIBLE;
        }
        view.setVisibility(type);
    }

    /*ImageView 显示不可见*/
    public static void setIvInVisible(ImageView view, boolean visible) {
        int type;
        if (visible) {
            type = View.INVISIBLE;
        } else {
            type = View.VISIBLE;
        }
        view.setVisibility(type);
    }

    /*ImageView 显示不可见*/
    public static void setIvVisible(ImageView view, boolean visible) {
        int type;
        if (visible) {
            type = View.VISIBLE;
        } else {
            type = View.INVISIBLE;
        }
        view.setVisibility(type);
    }

    /*TextView 显示隐藏*/
    public static void setTvIsGone(TextView view, boolean visible) {
        int type;
        if (visible) {
            type = View.GONE;
        } else {
            type = View.VISIBLE;
        }
        view.setVisibility(type);
    }

    /*剩下多少字可填*/
    public static String showHowManyCountEdit(final EditText editText, final int maxLength) {
        try {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Editable editable = editText.getText();
                    int length = editable.length();
                    if (length > 0) {
                        if (length >= maxLength) {
                            int selEndIndex = Selection.getSelectionEnd(editable);
                            String str = editable.toString();
                            //截取新字符串
                            String newStr = str.substring(0, maxLength);
                            editText.setText(newStr);
                            editable = editText.getText();
                            //新字符串的长度
                            int newLen = editable.length();
                            //旧光标位置超过字符串长度
                            if (selEndIndex > newLen) {
                                selEndIndex = editable.length();
                            }
                            //设置新光标所在的位置
                            Selection.setSelection(editable, selEndIndex);
                        }

                        mCountWhat = String.valueOf(maxLength - length);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mCountWhat;
    }

    public static String showHowManyCountTIEdit(TextInputEditText editText, final int countTotal) {
        try {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int length = s.length();
                    if (length > 0) {
                        mCountWhat = String.valueOf(countTotal - length);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mCountWhat;
    }

}
