package com.example.globallogic.myapplication;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by globallogic on 22/8/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    Activity context;

    public MessageAdapter(Activity mContext){
        context = mContext;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = context.getLayoutInflater().inflate(R.layout.activity_check, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.MyViewHolder holder, int position) {
        LinkUtils.autoLink(holder.tvText, new LinkUtils.OnClickListener() {
            @Override
            public void onLinkClicked(String link) {
                Toast.makeText(context,"Linked Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClicked() {
                holder.rlTextParent.performClick();
            }

            @Override
            public void onLongClicked(){
            }
        });


        holder.rlTextParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Normal Text Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.rlTextParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "Long Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        View view;
        RelativeLayout rlTextParent;
        TextView tvText;

        public MyViewHolder(View view){
            super(view);
            tvText = (TextView) view.findViewById(R.id.tvText);
            rlTextParent = (RelativeLayout) view.findViewById(R.id.rlParentLayout);
        }
    }


}
