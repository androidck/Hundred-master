package com.community.hundred.modules.ui.main.fragment.ranking;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.community.hundred.R;
import com.community.hundred.common.base.BasePresenter;
import com.community.hundred.common.base.MyLazyFragment;
import com.community.hundred.common.constant.EventBusConstant;
import com.community.hundred.common.constant.HttpConstant;
import com.community.hundred.modules.adapter.RankingAdapter;
import com.community.hundred.modules.eventbus.SpecialWrap;
import com.community.hundred.modules.manager.LoginUtils;
import com.community.hundred.modules.ui.main.MainActivity;
import com.community.hundred.modules.ui.main.fragment.entry.CircleChildEntry;
import com.community.hundred.modules.ui.main.fragment.entry.RankingEntry;
import com.community.hundred.modules.ui.main.fragment.presenter.SpecialChildPresenter;
import com.community.hundred.modules.ui.main.fragment.presenter.view.ISpecialChildView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

// 排行
public class RankingFragment extends MyLazyFragment<MainActivity, ISpecialChildView, SpecialChildPresenter> {

    @BindView(R.id.recyclerView)
    SwipeRecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private View headView;

    private RankingAdapter adapter;

    private RadioGroup radioGroup;

    private TextView tv_charm, tv_tycoon;
    private RadioButton rd_day, rd_week, rd_month, rd_all;

    private CircleImageView img_first_user_head, img_second_user_head, img_third_user_head;
    private TextView tv_first_nickname, tv_second_nickname, tv_third_nickname;
    private TextView tv_first_money, tv_second_money, tv_third_money;

    // 魅力榜还是土豪榜
    private int charmAndTycoon = 0;

    private List<RankingEntry> list = new ArrayList<>();

    @Override
    protected SpecialChildPresenter createPresenter() {
        return new SpecialChildPresenter(mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        headView = getLayoutInflater().inflate(R.layout.view_header_ranking, recyclerView, false);
        recyclerView.addHeaderView(headView);
        adapter = new RankingAdapter(mActivity, list);
        recyclerView.setAdapter(adapter);

        radioGroup = headView.findViewById(R.id.ra_group);
        tv_charm = headView.findViewById(R.id.tv_charm);
        tv_tycoon = headView.findViewById(R.id.tv_tycoon);

        rd_day = headView.findViewById(R.id.rd_day);
        rd_week = headView.findViewById(R.id.rd_week);
        rd_month = headView.findViewById(R.id.rd_month);
        rd_all = headView.findViewById(R.id.rd_all);

        img_first_user_head = headView.findViewById(R.id.img_first_user_head);
        img_second_user_head = headView.findViewById(R.id.img_second_user_head);
        img_third_user_head = headView.findViewById(R.id.img_third_user_head);

        tv_first_nickname = headView.findViewById(R.id.tv_first_nickname);
        tv_second_nickname = headView.findViewById(R.id.tv_second_nickname);
        tv_third_nickname = headView.findViewById(R.id.tv_third_nickname);

        tv_first_money = headView.findViewById(R.id.tv_first_money);
        tv_second_money = headView.findViewById(R.id.tv_second_money);
        tv_third_money = headView.findViewById(R.id.tv_third_money);

        // 魅力榜点击
        tv_charm.setOnClickListener(v -> {
            tv_charm.setBackgroundResource(R.drawable.shape_select_title_bg);
            tv_tycoon.setBackgroundResource(R.drawable.shape_unselect_title_bg);
            tv_charm.setTextColor(getResources().getColor(R.color.colorAccent));
            tv_tycoon.setTextColor(Color.parseColor("#b9b9b9"));
            rd_day.setChecked(true);
            charmAndTycoon = 0;
        });

        // 土豪榜
        tv_tycoon.setOnClickListener(v -> {
            tv_tycoon.setBackgroundResource(R.drawable.shape_select_title_bg);
            tv_charm.setBackgroundResource(R.drawable.shape_unselect_title_bg);
            tv_tycoon.setTextColor(getResources().getColor(R.color.colorAccent));
            tv_charm.setTextColor(Color.parseColor("#b9b9b9"));
            rd_day.setChecked(true);
            charmAndTycoon = 1;
            adapter.setType(charmAndTycoon);
            list.clear();
            getReward(HttpConstant.dsRiBangURL);
        });

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rd_day:
                    if (charmAndTycoon == 0) {

                    } else {
                        adapter.setType(charmAndTycoon);
                        list.clear();
                        getReward(HttpConstant.dsRiBangURL);
                    }
                    break;
                case R.id.rd_week:
                    if (charmAndTycoon == 0) {

                    } else {
                        adapter.setType(charmAndTycoon);
                        list.clear();
                        getReward(HttpConstant.dsZhouBangURL);
                    }
                    break;
                case R.id.rd_month:
                    if (charmAndTycoon == 0) {

                    } else {
                        adapter.setType(charmAndTycoon);
                        list.clear();
                        getReward(HttpConstant.dsYueBangURL);
                    }
                    break;
                case R.id.rd_all:
                    if (charmAndTycoon == 0) {

                    } else {
                        adapter.setType(charmAndTycoon);
                        list.clear();
                        getReward(HttpConstant.dsZongBangURL);
                    }
                    break;
            }
        });

        // 关注按钮点击事件
        adapter.setOnItemAttentionListener(position -> {
            if (LoginUtils.getInstance().isLogin()) {
                RankingEntry entry = list.get(position);
                if (LoginUtils.getInstance().getUid().equals(entry.getUid())) {
                    toast("不能关注自己");
                } else {
                    if ("1".equals(entry.getIsgz())) {
                        // 取消关注
                        mPresenter.setEscAttention(entry.getUid());
                    } else {
                        mPresenter.setAttention(entry.getUid());
                    }
                }
                mPresenter.setOnSuccessListener(state -> {
                    if (state == 1) {
                        entry.setIsgz("1");
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                    } else {
                        entry.setIsgz("0");
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(SpecialWrap.getInstance(EventBusConstant.FOLLOW_REFRESH));
                    }
                });
            } else {
                notLogin();
            }
        });


    }

    @Override
    protected void initData() {
        if (charmAndTycoon == 1) {
            list.clear();
            getReward(HttpConstant.dsRiBangURL);
        }
    }

    public void getReward(String url) {
        mPresenter.getReward(url);
        mPresenter.setOnRankingListener(list1 -> {
            for (int i = 3; i < list1.size(); i++) {
                list.add(list1.get(i));
            }
            adapter.notifyDataSetChanged();
            // 第一名
            tv_first_nickname.setText(list.get(0).getName());
            tv_first_money.setText("打赏" + list.get(0).getZonjine() + "元");
            Glide.with(mActivity).load(list.get(0).getImage()).placeholder(R.mipmap.ic_launcher).into(img_first_user_head);
            // 第二名
            tv_second_nickname.setText(list.get(1).getName());
            tv_second_money.setText("打赏" + list.get(1).getZonjine() + "元");
            Glide.with(mActivity).load(list.get(1).getImage()).placeholder(R.mipmap.ic_launcher).into(img_second_user_head);
            // 第三名
            tv_third_nickname.setText(list.get(2).getName());
            tv_third_money.setText("打赏" + list.get(2).getZonjine() + "元");
            Glide.with(mActivity).load(list.get(2).getImage()).placeholder(R.mipmap.ic_launcher).into(img_third_user_head);

        });
    }


    public static RankingFragment getInstance() {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }
}
