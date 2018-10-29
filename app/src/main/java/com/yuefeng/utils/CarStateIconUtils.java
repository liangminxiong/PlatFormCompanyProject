package com.yuefeng.utils;

import android.text.TextUtils;

import com.yuefeng.commondemo.R;

public class CarStateIconUtils {

    /**
     * @param ang   角度
     * @param state
     * @return
     */
    public static int getImageInt(String state, double ang) {
        try {

            if ((ang >= 22.5) && (ang < 67.5)) {
                return switchTrafficDrawable(2, state);
            }
            if ((ang >= 67.5) && (ang < 112.5)) {
                return switchTrafficDrawable(3, state);
            }
            if ((ang >= 112.5) && (ang < 157.5)) {
                return switchTrafficDrawable(4, state);
            }
            if ((ang >= 157.5) && (ang < 200.5)) {
                return switchTrafficDrawable(5, state);
            }
            if ((ang >= 200.5) && (ang < 245.5)) {
                return switchTrafficDrawable(6, state);
            }
            if ((ang >= 245.5) && (ang < 292.5)) {
                return switchTrafficDrawable(7, state);
            }
            if ((ang >= 292.5) && (ang < 337.5)) {
                return switchTrafficDrawable(8, state);
            }
            if ((ang >= 337.5) && (ang < 360.0)) {
                return switchTrafficDrawable(1, state);
            }
            if ((ang >= 0) && (ang < 22.5)) {
                return switchTrafficDrawable(1, state);
            } else {
                return switchTrafficDrawable(1, state);
            }
        } catch (Exception e) {
            return switchTrafficDrawable(1, state);
        }
    }


    public static int switchTrafficDrawable(int flag, String state) {
        if (TextUtils.isEmpty(state)) {
            return R.drawable.guiji_xqc_01;
        }

        switch (flag) {
            case 1:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_01;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_01;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_01;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_01;
                } else {
                    return R.drawable.guiji_sdc_01;
                }

            case 2:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_02;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_02;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_02;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_02;
                } else {
                    return R.drawable.guiji_sdc_02;
                }

            case 3:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_03;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_03;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_03;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_03;
                } else {
                    return R.drawable.guiji_sdc_03;
                }
            case 4:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_04;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_04;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_04;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_04;
                } else {
                    return R.drawable.guiji_sdc_04;
                }
            case 5:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_05;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_05;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_05;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_05;
                } else {
                    return R.drawable.guiji_sdc_05;
                }
            case 6:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_06;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_06;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_06;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_06;
                } else {
                    return R.drawable.guiji_sdc_06;
                }
            case 7:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_07;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_07;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_07;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_07;
                } else {
                    return R.drawable.guiji_sdc_07;
                }
            case 8:
                if (state.contains("1")) {
                    return R.drawable.guiji_xqc_08;
                } else if (state.contains("2")) {
                    return R.drawable.guiji_ljc_08;
                } else if (state.contains("3")) {
                    return R.drawable.guiji_pwc_08;
                } else if (state.contains("4")) {
                    return R.drawable.guiji_ssc_08;
                } else {
                    return R.drawable.guiji_sdc_08;
                }
        }
        return R.drawable.guiji_xqc_01;
    }

}
