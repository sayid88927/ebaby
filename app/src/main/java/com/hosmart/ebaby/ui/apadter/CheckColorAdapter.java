package com.hosmart.ebaby.ui.apadter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hosmart.ebaby.R;
import com.hosmart.ebaby.bean.CheckColorBean;

import java.util.List;

public class CheckColorAdapter extends BaseQuickAdapter<CheckColorBean, BaseViewHolder> {
    private List<CheckColorBean> data;
    private static  int type;  // 0 选择颜色 1 选择声音
    public static final int checkColor = 1;
    public static final int checkVoice = 2;


    public static   int getType(){
        return  type;
    }

    public CheckColorAdapter(List<CheckColorBean> data, int type) {
        super(R.layout.item_check_color, data);
        this.data = data;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CheckColorBean item) {
        ImageView ivCheckColor = helper.getView(R.id.iv_check_color);
        if (item.isCheckStart())
            ivCheckColor.setBackgroundResource(item.getSelectedDrawable());
        else
            ivCheckColor.setBackgroundResource(item.getUnSelectedDrawable());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(item);
                }
            }
        });
    }


    private onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(CheckColorBean item);
    }

    public void onItemClick(onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}
