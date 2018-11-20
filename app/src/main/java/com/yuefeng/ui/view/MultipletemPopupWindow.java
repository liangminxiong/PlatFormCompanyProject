package com.yuefeng.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.common.utils.LogUtils;
import com.yuefeng.cartreeList.adapter.MultipletemRvAdapter;
import com.yuefeng.cartreeList.adapter.MultipletemSelectRvAdapter;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.modle.EventQuestionMsgBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 树形列表
 * <p>
 */

public class MultipletemPopupWindow extends PopupWindow {
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private LinearLayoutCompat llPopupRoot;
    private boolean isShowAniming;//show动画是否在执行中
    private boolean isHideAniming;//hide动画是否在执行中
    private View rootView;
    private TextView tv_title;
    private TextView tv_setting;
    public TextInputEditText tv_search_txt;
    public RecyclerView recyclerview;
    private String key;
    private List<EventQuestionMsgBean> listData = new ArrayList<>();
    private List<EventQuestionMsgBean> listDataSelect = new ArrayList<>();
    private ImageView iv_search;
    private String id;
    private boolean isAddTerminal;
    private boolean isSingle;
    private MultipletemRvAdapter mAdapter;
    private MultipletemSelectRvAdapter mSelectRvAdapter;

    private boolean isAdapter = true;
    private RelativeLayout mRl_search;
    private String mProblem;

    private int mPosition = -1;//保存当前选中的position 重点！

    public MultipletemPopupWindow(Context context, List<EventQuestionMsgBean> listData,
                                  boolean isSingle, boolean isAddTerminal) {
        super(null, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        mContext = context;
        this.listData = listData;
        this.isSingle = isSingle;
        this.isAddTerminal = isAddTerminal;
        //设置点击空白处消失
        setTouchable(true);
        setOutsideTouchable(false);
        setClippingEnabled(false);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int w = wm.getDefaultDisplay().getWidth();
        int h = wm.getDefaultDisplay().getHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(context.getResources().getColor(R.color.transition));//填充颜色
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));

        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_mutipletem, null);
        setContentView(rootView);

        llPopupRoot = (LinearLayoutCompat) rootView.findViewById(R.id.ll_popup_root);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        RelativeLayout iv_back = (RelativeLayout) rootView.findViewById(R.id.iv_back);
        mRl_search = (RelativeLayout) rootView.findViewById(R.id.rl_search);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_search_txt = (TextInputEditText) rootView.findViewById(R.id.tv_search_txt);
        iv_search = (ImageView) rootView.findViewById(R.id.iv_search);
        tv_setting = (TextView) rootView.findViewById(R.id.tv_title_setting);
        showMultipletemListData();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdapter) {
                    dismiss();
                } else {
                    isAdapter = true;
                    showMultipletemListData();
                }
            }
        });

        tv_search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdapter) {
                    dismiss();
                } else {
                    isAdapter = true;
                    showMultipletemListData();
                }
            }
        });

        initSeacherWatcher();
    }

    private void showMultipletemListData() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new MultipletemRvAdapter(R.layout.layout_rv_meltipletem_item, listData);

        recyclerview.setAdapter(mAdapter);
        listData.clear();
        for (int i = 0; i < 10; i++) {
            EventQuestionMsgBean bean = new EventQuestionMsgBean();
            bean.setId(i + "");
            bean.setProblem("态度 " + i);
            listData.add(bean);
        }
        mAdapter.setNewData(listData);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position != 0) {
                    mRl_search.setVisibility(View.VISIBLE);
                    initSelectListData(position);
                    isAdapter = false;
                }
            }
        });
    }

    private void initSelectListData(int position) {

        listDataSelect.clear();
        for (int i = 0; i < 10; i++) {
            EventQuestionMsgBean bean = new EventQuestionMsgBean();
            bean.setId(i + "");
            if (i == 0) {
                bean.setPid("");
            } else {
                bean.setPid(i + "");
            }
            bean.setState(i + "");
            bean.setProblem("态度 " + position + "++" + i);
            listDataSelect.add(bean);
        }

        mSelectRvAdapter = new MultipletemSelectRvAdapter(
                R.layout.layout_rv_item_mutipletemselect, listDataSelect,isSingle);
        recyclerview.setAdapter(mSelectRvAdapter);
        mSelectRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                isAdapter = true;
//                mRl_search.setVisibility(View.GONE);
//                showMultipletemListData();
                if (isSingle) {
                    mSelectRvAdapter.setSelection(position);
                } else {
                    LogUtils.d("onBindViewHolder =2222=" + position);
//                    RecyclerView.ViewHolder holder = recyclerview.findViewHolderForLayoutPosition(mPosition);
//                    ImageView mIvCheck = (ImageView) view.findViewById(R.id.iv_select_tree);
//                    String pid = listDataSelect.get(0).getPid();
//                    if (mPosition == position) {
//                        mIvCheck.setImageResource(R.drawable.list_select_nor);
//                        mPosition = -1;
//                    } else if (mPosition != -1) {
//                        if (holder != null) {//还在屏幕里
//                            mIvCheck.setImageResource(R.drawable.list_select_nor);
//                        } else {
//                            //些极端情况，holder被缓存在Recycler的cacheView里，
//                            //此时拿不到ViewHolder，但是也不会回调onBindViewHolder方法。所以add一个异常处理
//                            mSelectRvAdapter.notifyItemChanged(mPosition);
//                        }
//                        //设置新Item的勾选状态
//                        mPosition = position;
//                        mIvCheck.setImageResource(R.drawable.list_select_sel);
//                    } else {
//                        //设置新Item的勾选状态
//                        mPosition = position;
//                        mIvCheck.setImageResource(R.drawable.list_select_sel);
//                    }
                }
            }
        });
        mSelectRvAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String pid = listDataSelect.get(0).getPid();
                if (isSingle) {//单选
                    mSelectRvAdapter.setSelection(position);
                } else {
                }
            }

        });
    }

    //提供给外部Activity来获取当前checkBox选中的item，这样就不用去遍历了 重点！
    public int getSelectedPos() {
        return mPosition;
    }


    private void initSeacherWatcher() {
        try {
            tv_search_txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count > 0) {
                        iv_search.setVisibility(View.GONE);
                    } else {
                        iv_search.setVisibility(View.VISIBLE);
                    }
                    key = s.toString();
                    searchList(key);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*筛选数据*/
    private void searchList(String key) {
        if (listDataSelect.size() > 0 && mSelectRvAdapter != null) {
            List<EventQuestionMsgBean> listSelect = filterDatas(listDataSelect, key);
            mSelectRvAdapter.setNewData(listSelect);
        }
    }


    public void setTitleText(String titleText) {
        if (tv_title != null) {
            tv_title.setText(titleText);
        }
    }

    public void setSettingText(String setting) {
        if (tv_setting != null) {
            tv_setting.setText(setting);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (!isShowAniming) {
            isShowAniming = true;
            popupAnim(llPopupRoot, 0.0f, 1.0f, 300, true);
        }
    }

    @Override
    public void dismiss() {
        if (!isHideAniming) {
            isHideAniming = true;
            popupAnim(llPopupRoot, 1.0f, 0.0f, 300, false);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onGoBack(String name, String terminal, String id, boolean isGetDatas);

//        void onSearch(String key);

        void onSure(String name, String terminal, String id, boolean isGetDatas);

        void onSelectCar(String carNumber, String terminal, String id, boolean isGetDatas);

    }

    /**
     * popupWindow属性动画
     *
     * @param view     执行属性动画的view
     * @param start    start值
     * @param end      end值
     * @param duration 动画持续时间
     * @param flag     true代表show，false代表hide
     */
    private void popupAnim(final View view, float start, final float end, int duration, final
    boolean flag) {
        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
//                view.setPivotX(0);
//                view.setPivotY(view.getMeasuredHeight());
//                view.setTranslationY((1 - value) * view.getHeight());

                view.setPivotY(0);
                view.setPivotX(view.getMeasuredWidth());
                view.setTranslationX((1 - value) * view.getWidth());
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (!flag) {
                    isHideAniming = false;
                    MultipletemPopupWindow.super.dismiss();
                } else {
                    isShowAniming = false;
                }
            }
        });
        va.start();
    }


    private List<EventQuestionMsgBean> filterDatas(List<EventQuestionMsgBean> listDataSelect, String key) {
        List<EventQuestionMsgBean> list = new ArrayList<>();
        list.clear();
        for (EventQuestionMsgBean msgBean : listDataSelect) {
            mProblem = msgBean.getProblem();
            if (mProblem.contains(key)) {
                list.add(msgBean);
            }
        }
        return list;
    }

}
