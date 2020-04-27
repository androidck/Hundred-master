package com.community.hundred.modules.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.community.hundred.R;
import com.community.hundred.modules.ui.pay.entry.ItemModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BalanceRechargeAdapter extends RecyclerView.Adapter<BalanceRechargeAdapter.BaseViewHolder> {
    private ArrayList<ItemModel> dataList = new ArrayList<>();
    private int lastPressIndex = -1;
    private Context mContext;

    public void replaceAll(ArrayList<ItemModel> list, Context context) {
        mContext = context;
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public BalanceRechargeAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemModel.TWO:
                return new OneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.one, parent, false));
            case ItemModel.THREE:
                return new TWoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BalanceRechargeAdapter.BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position).data);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data) {
        }
    }

    private class OneViewHolder extends BaseViewHolder {
        private TextView tv;

        public OneViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.tv);

            itemView.setOnClickListener(v -> {
                Log.e("TAG", "OneViewHolder: ");
                notyfy = 0;
                int position = getAdapterPosition();
                ItemModel model = dataList.get(position);
                Log.e("TAG", "OneViewHolder: " + model.toString());
                EventBus.getDefault().post(model);
                if (lastPressIndex == position) {
                    lastPressIndex = -1;
                } else {
                    lastPressIndex = position;
                }
                notifyDataSetChanged();
            });
        }

        @Override
        void setData(Object data) {
            if (data != null) {
                String text = (String) data;
                tv.setText(text);
                if (getAdapterPosition() == lastPressIndex) {
                    tv.setSelected(true);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
                    tv.setBackground(mContext.getDrawable(R.drawable.shape_money_select_bg));
                } else {
                    tv.setSelected(false);
                    tv.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
                    tv.setBackground(mContext.getDrawable(R.drawable.shape_money_unselect_bg));
                }

            }


        }
    }

    int notyfy = 0;

    private class TWoViewHolder extends BaseViewHolder {
        private EditText et;
        private String chargeFunds;

        public TWoViewHolder(View view) {
            super(view);
            et = view.findViewById(R.id.et);
            final int position = getAdapterPosition();
            Log.e("TWoViewHolder", "TWoViewHolder: ");
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (lastPressIndex != position) {
                            notifyItemChanged(lastPressIndex);
                            lastPressIndex = position;
                        }
                    }
                }
            });
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence != null || !charSequence.equals("")) {
                        try {
                            int str = Integer.parseInt(String.valueOf(charSequence));
                            if (str < 9) {
                                et.setSelection(et.getText().length());
                            }
                            if (str > 2000) {
                                et.setText("2000");
                                et.setSelection(et.getText().length());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() > 0) {
                        String inputText = et.getText().toString().trim();
                        if (Double.parseDouble(inputText) < 9) {
                            chargeFunds = "9";
                        } else if (Double.parseDouble(inputText) > 2000) {
                            chargeFunds = "2000";
                        } else {
                            chargeFunds = inputText;
                        }
                    }

                    String funds = chargeFunds;
                    ItemModel model = new ItemModel(ItemModel.THREE, funds, false);
                    EventBus.getDefault().post(model);
                }
            });


        }


        @Override
        void setData(Object data) {
            super.setData(data);
            final int position = getAdapterPosition();
            if (position == lastPressIndex)
                et.requestFocus();
            else
                et.clearFocus();
        }
    }

}
