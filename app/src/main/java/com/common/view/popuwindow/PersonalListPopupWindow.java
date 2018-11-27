package com.common.view.popuwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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
import com.yuefeng.cartreeList.common.Node;
import com.yuefeng.cartreeList.common.OnTreeNodeClickListener;
import com.yuefeng.commondemo.R;
import com.yuefeng.features.adapter.CarListSelectAdapter;
import com.yuefeng.features.modle.carlist.CarListSelectBean;
import com.yuefeng.personaltree.ChangePersonalTreeRecyclerAdapter;
import com.yuefeng.utils.PersonalDatasUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 树形列表
 * <p>
 */

public class PersonalListPopupWindow extends PopupWindow {
    private Context mContext;
    private boolean isSingle;
    private OnItemClickListener mOnItemClickListener;
    private LinearLayoutCompat llPopupRoot;
    private boolean isShowAniming;//show动画是否在执行中
    private boolean isHideAniming;//hide动画是否在执行中
    private View rootView;
    private TextView tv_title;
    private TextView tv_setting;
    private ImageView iv_search;
    public TextInputEditText tv_search_txt;
    public RecyclerView recyclerview;
    public RecyclerView recyclerview_after;
    private String key;
    private CarListSelectAdapter adapterSelect;
    private List<CarListSelectBean> listData = new ArrayList<>();
    private String userId;
    private List<Node> carDatas;
    private String name;
    private ChangePersonalTreeRecyclerAdapter treeListAdapter;
    private String personalName;
    private StringBuffer stringBuffer;
    private StringBuffer stringBufferFlag;
    private StringBuffer stringBufferTerflag;
    private String selectTreeName;
    private String useridFlag;
    private String terflag;
    private String terminal;
    private int positionTemp;

    public PersonalListPopupWindow(Context context, List<Node> carDatas, boolean isSingle) {
        super(null, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mContext = context;
        this.carDatas = carDatas;
        this.isSingle = isSingle;
        //设置点击空白处消失
        setTouchable(true);
        setOutsideTouchable(true);
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
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_treeslist, null);
        setContentView(rootView);
        name = "";
        llPopupRoot = (LinearLayoutCompat) rootView.findViewById(R.id.ll_popup_root);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerview_after = (RecyclerView) rootView.findViewById(R.id.recyclerview_after);
        RelativeLayout iv_back = (RelativeLayout) rootView.findViewById(R.id.iv_back);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_search_txt = (TextInputEditText) rootView.findViewById(R.id.tv_search_txt);
        iv_search = (ImageView) rootView.findViewById(R.id.iv_search);
        tv_setting = (TextView) rootView.findViewById(R.id.tv_title_setting);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview_after.setLayoutManager(new LinearLayoutManager(mContext));
        initRecycleView();
        initSelectRecycleView();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                showSelectItemDatas("1");
                selectSingle(positionTemp, "1");
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
                dismiss();
                showSelectItemDatas("2");
                selectSingle(positionTemp, "2");
            }
        });

        initSeacherWatcher();
    }

    private void initSeacherWatcher() {
        tv_search_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    recyclerview.setVisibility(View.GONE);
                    iv_search.setVisibility(View.GONE);
                    recyclerview_after.setVisibility(View.VISIBLE);
                } else {
                    iv_search.setVisibility(View.VISIBLE);
                    recyclerview.setVisibility(View.VISIBLE);
                    recyclerview_after.setVisibility(View.GONE);
                }
                key = s.toString();
                searchList(key);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecycleView() {
        treeListAdapter = new ChangePersonalTreeRecyclerAdapter(recyclerview, mContext,
                carDatas, 1, R.drawable.list_fold, R.drawable.list_fold, isSingle,false);
//
        recyclerview.setAdapter(treeListAdapter);
        treeListAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                showSelectItemDatas("3");
            }

        });
    }

    /*点击车*/
    @SuppressLint("SetTextI18n")
    private void showSelectItemDatas(String type) {
        if (treeListAdapter == null) {
            return;
        }
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }

        if (stringBufferFlag == null) {
            stringBufferFlag = new StringBuffer();

        }
        if (stringBufferTerflag == null) {
            stringBufferTerflag = new StringBuffer();

        }

        final List<Node> allNodes = treeListAdapter.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).isChecked()) {
                personalName = allNodes.get(i).getName();
                userId = (String) allNodes.get(i).getId();
                terminal = (String) allNodes.get(i).getTerminalNO();
                String count = (String) allNodes.get(i).getCount();
                if (!TextUtils.isEmpty(personalName) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(terminal)) {
                    if (isSingle) {
                        stringBufferTerflag.setLength(0);
                        stringBufferFlag.setLength(0);
                        stringBuffer.setLength(0);
                        stringBuffer.append(personalName);
                        stringBufferFlag.append(userId);
                        stringBufferTerflag.append(terminal);
                    } else {
                        stringBuffer.append(personalName).append(",");
                        stringBufferFlag.append(userId).append(",");
                        stringBufferTerflag.append(terminal).append(",");
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(name)) {
            selectTreeName = name;
        } else {
            selectTreeName = stringBuffer.toString();
            useridFlag = stringBufferFlag.toString();
            terminal = stringBufferTerflag.toString();
        }
        if (!TextUtils.isEmpty(userId)) {
            if (mOnItemClickListener != null) {
                if (type.equals("1")) {
                    mOnItemClickListener.onGoBack(selectTreeName, useridFlag, terminal);
                } else if (type.equals("2")) {
                    mOnItemClickListener.onSure(selectTreeName, useridFlag, terminal);
                } else {
                    mOnItemClickListener.onSure(selectTreeName, useridFlag, terminal);
                    mOnItemClickListener.onGoBack(selectTreeName, useridFlag, terminal);
                }
            }
        }
        if (stringBuffer != null) {
            stringBuffer.setLength(0);
        }
        if (stringBufferFlag != null) {
            stringBufferFlag.setLength(0);
        }
        if (stringBufferTerflag != null) {
            stringBufferTerflag.setLength(0);
        }
    }


    private void initSelectRecycleView() {
        adapterSelect = new CarListSelectAdapter(R.layout.list_item, listData,1);
        recyclerview_after.setAdapter(adapterSelect);
//        adapterSelect.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                name = listData.get(position).getName();
//                userId = listData.get(position).getTerminal();
//                terminal = listData.get(position).getTerminal();
//                if (!TextUtils.isEmpty(userId)) {
////                    if (isShowing())
////                        dismiss();
//                    if (mOnItemClickListener != null)
//                        mOnItemClickListener.onSelectCar(name, userId, terminal);
//                    mOnItemClickListener.onGoBack(name, userId, terminal);
//                }
//            }
//        });
        adapterSelect.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                positionTemp = position;
                selectSingle(position, "3");

            }
        });
    }

    private void selectSingle(int position, String type) {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }

        if (stringBufferFlag == null) {
            stringBufferFlag = new StringBuffer();

        }
        if (stringBufferTerflag == null) {
            stringBufferTerflag = new StringBuffer();

        }
        if (stringBuffer != null) {
            stringBuffer.setLength(0);
        }
        if (stringBufferFlag != null) {
            stringBufferFlag.setLength(0);
        }
        if (stringBufferTerflag != null) {
            stringBufferTerflag.setLength(0);
        }
        if (listData.size() > 0) {
            name = listData.get(position).getName();
            userId = listData.get(position).getId();
            terminal = listData.get(position).getTerminal();
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(terminal)) {
                stringBuffer.append(name).append(",");
                stringBufferFlag.append(userId).append(",");
            }
            if (!TextUtils.isEmpty(userId)) {
                if (mOnItemClickListener != null) {
                    if (type.equals("1")) {
                        mOnItemClickListener.onGoBack(name, userId, terminal);
                    } else if (type.equals("2")) {
                        mOnItemClickListener.onSure(name, userId, terminal);
                    } else {
                        mOnItemClickListener.onSure(name, userId, terminal);
                        mOnItemClickListener.onGoBack(name, userId, terminal);
                    }
                }
            }
        }
    }


    private void searchList(String key) {
        if (carDatas.size() > 0) {
            List<CarListSelectBean> nodes = PersonalDatasUtils.carListSelect(carDatas, key);
            if (nodes.size() > 0) {
                listData.clear();
                listData.addAll(nodes);
                if (adapterSelect != null) {
                    adapterSelect.setNewData(listData);
                    recyclerview_after.setVisibility(View.VISIBLE);
                    adapterSelect.notifyDataSetChanged();
                }
            }
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
        void onGoBack(String listName, String userId, String terflag);

//        void onSearch(String key);

        void onSure(String listName, String userId, String terflag);

        void onSelectCar(String carNumber, String userId, String terflag);

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
                    PersonalListPopupWindow.super.dismiss();
                } else {
                    isShowAniming = false;
                }
            }
        });
        va.start();
    }
}
