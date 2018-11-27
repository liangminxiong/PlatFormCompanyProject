package com.yuefeng.photo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.common.utils.GlideUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yuefeng.commondemo.R;
import com.yuefeng.photo.bean.ImageInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiaoke on 2016/6/28.
 */
public class GvAdapter extends BaseAdapter {
    private Context context;
    private List<ImageInfo> list;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.zanwutupian)
            .showImageOnFail(R.mipmap.zanwutupian)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public GvAdapter(Context context, List<ImageInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ice_image_item_history_mygridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.loadImageViewLoading(holder.iv_image,list.get(position).getUrl(),R.mipmap.zanwutupian,R.mipmap.zanwutupian);
//        ImageLoader.getInstance().displayImage(list.get(position).getUrl(), holder.iv_image, optionsImag);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv)
        ImageView iv_image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
