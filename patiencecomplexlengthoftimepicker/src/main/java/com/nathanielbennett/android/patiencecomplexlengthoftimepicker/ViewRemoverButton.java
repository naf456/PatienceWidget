package com.nathanielbennett.android.patiencecomplexlengthoftimepicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ViewRemoverButton extends FrameLayout {

    private View mViewToRemove;
    private OnViewRemovedListener mOnViewRemovedListener;
    private boolean mRemoveSelf = false;

    public ViewRemoverButton(Context context) {
        super(context);
        initialize();
    }

    public ViewRemoverButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ViewRemoverButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize(){
        inflate(getContext(), R.layout.datetime_selector_segment_remover, this);
        View clearButton = findViewById(R.id.view_destroyer_clear_btn);
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewToRemove != null) {
                    ((ViewGroup) mViewToRemove.getParent()).removeView(mViewToRemove);

                    if(mRemoveSelf)
                        ((ViewGroup)getParent()).removeView(ViewRemoverButton.this);

                    if (mOnViewRemovedListener != null)
                        mOnViewRemovedListener.onRemoved(mViewToRemove);

                    mViewToRemove = null;
                }
            }
        });
    }

    public void setViewToRemove(View viewToRemove){
        mViewToRemove = viewToRemove;
    }

    public void setOnViewRemovedListener(OnViewRemovedListener listener) {
        mOnViewRemovedListener = listener;
    }

    public void setRemoveSelf(boolean removeSelf){
        mRemoveSelf = removeSelf;
    }


    public abstract static class OnViewRemovedListener {
        abstract void onRemoved(View viewDestroyed);
    }
}
