package com.luoyingmm.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luoyingmm.R;
import com.luoyingmm.activity.WebActivity;
import com.luoyingmm.entity.TranslationData;
import com.luoyingmm.fragment.CollectFragment;
import com.luoyingmm.sql.DatabaseHelper;
import com.luoyingmm.util.DialogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TranslationData> data;
    private OnItemClickListener onItemClickListener;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    private Context mContext;

    public RecyclerViewAdapter(List<TranslationData> data, Context context) {
        this.data = data;
        this.mContext = context;
    }
    /**
     * 供外部调用设置监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 自定义的接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, String str);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.tv_1.setText(data.get(position).getTranslation());
        holder.tv_2.setText(data.get(position).getResult());

        holder.iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                String ocrText = holder.tv_2.getText().toString();
                ClipData mClipData = ClipData.newPlainText("OcrText", ocrText);
                clipboardManager.setPrimaryClip(mClipData);
                Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(data.size() == 0 )){
                    DialogUtil.showAlertDialog((Activity) mContext, R.mipmap.delete_unselect, "删除提示", "你确定要删除吗？",
                            "确定", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
                        @Override
                        public void clickPositive() {
                            if (!TextUtils.isEmpty(holder.tv_1.getText().toString()) || !TextUtils.isEmpty(holder.tv_2.getText().toString())) {
                                db.delete("translationData", "translation=?", new String[]{holder.tv_1.getText().toString()});
                            }
                            removeItem(position);
                            notifyItemRangeChanged(position, getItemCount() - position);
                            if (data.size() > 0){
                                CollectFragment.tv_collect.setVisibility(View.GONE);
                            }else {
                                CollectFragment.tv_collect.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void clickNegative() {
                            //negative
                        }
                    });

                }
            }

        });
        holder.ll_translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v,holder.tv_1.getText().toString() );
                }
            }
        });

        holder.iv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(data.size() == 0 )){
                    String url = "https://translate.google.cn/?sl=en&tl=zh-CN&text="+data.get(position).getTranslation()+"&op=translate";
                    Bundle bundle = new Bundle();
                    bundle.putString("url",url);
                    Intent intent = new Intent(mContext,WebActivity.class );
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    /**
     * 移除数据
     * @param position
     */
    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        databaseHelper = new DatabaseHelper(mContext, "TranslationData.db", null, 1);
        db = databaseHelper.getWritableDatabase();
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_1;
        TextView tv_2;
        ImageView iv_copy;
        ImageView iv_delete;
        ImageView iv_details;
        LinearLayout ll_translation;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_1  = itemView.findViewById(R.id.tv_1);
            tv_2  = itemView.findViewById(R.id.tv_2);
            iv_copy  = itemView.findViewById(R.id.iv_copy);
            iv_delete  = itemView.findViewById(R.id.iv_delete);
            ll_translation  = itemView.findViewById(R.id.ll_translation);
            iv_details  = itemView.findViewById(R.id.iv_details);
        }
    }
}
