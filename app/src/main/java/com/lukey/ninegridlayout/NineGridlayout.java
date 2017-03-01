package com.lukey.ninegridlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * creator  Lukey on 2016/6/14
 */
public class NineGridlayout extends ViewGroup {

 private int gap = 6; //图片之间的间隔
    public int edge = 0; //左右的间隔
    private int columns;//
    private int rows;//
    private List<Image> listData;
    private int totalWidth;

    public NineGridlayout(Context context) {
        super(context);
        totalWidth = ScreenTools.instance(context).getScreenWidth();
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        totalWidth = ScreenTools.instance(context).getScreenWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1) - 2 * edge) / 3;
        int singleHeight = singleWidth;

        if (childrenCount == 1){
            //根据子view数量确定高度
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = oneImageHeight(listData.get(0));
            setLayoutParams(params);

            CustomImageView childrenView = (CustomImageView) getChildAt(0);
            childrenView.setImageUrl(((Image) listData.get(0)).getUrl());
            int[] position = findPosition(0);
            int left = (singleWidth + gap) * position[1] + edge;
            int top = (singleHeight + gap) * position[0];

            int right = left + oneImageWidth(listData.get(0));
            int bottom = top + oneImageHeight(listData.get(0));

            childrenView.layout(left, top, right, bottom);
        } else {
            //根据子view数量确定高度
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = singleHeight * rows + gap * (rows - 1);
            setLayoutParams(params);
            CustomImageView childrenView;
            int[] position;
            int left,top,right,bottom;
            for (int i = 0; i < childrenCount; i++) {
                 childrenView = (CustomImageView) getChildAt(i);
                childrenView.setImageUrl(((Image) listData.get(i)).getUrl());
                 position = findPosition(i);
                 left = (singleWidth + gap) * position[1] + edge;
                 top = (singleHeight + gap) * position[0];
                 right = left + singleWidth;
                 bottom = top + singleHeight;

                childrenView.layout(left, top, right, bottom);
            }
        }
    }

    private int oneImageWidth(Image image){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }

        return imageWidth;
    }

    private int oneImageHeight(Image image){
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        if (image.getWidth() <= image.getHeight()) {
            if (imageHeight > totalWidth) {
                imageHeight = totalWidth;
                imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
            }
        } else {
            if (imageWidth > totalWidth) {
                imageWidth = totalWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }
        }

        return imageHeight;
    }


    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }


    public void setImagesData(List<Image> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局
        generateChildrenLayout(lists.size());

        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            CustomImageView iv;
            while (i < lists.size()) {
                 iv = generateImageView(i);
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();

            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                CustomImageView iv;
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    iv = generateImageView(oldViewCount+i);
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView(final int i) {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onClick(v,i);
                }
            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        public void onClick(View v, int position);
    }

}
