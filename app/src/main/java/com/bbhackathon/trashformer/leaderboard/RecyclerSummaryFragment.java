package com.bbhackathon.trashformer.leaderboard;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbhackathon.trashformer.R;
import com.bbhackathon.trashformer.databinding.RecyclerSummaryFragmentBinding;
import com.bbhackathon.trashformer.entity.RecycleItemPercentageEntity;
import com.bbhackathon.trashformer.entity.UserProfileTable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSummaryFragment extends Fragment {
    private static String TAG = RecyclerSummaryFragment.class.getSimpleName();

    private RecycleItemPercentageEntity recycleItemPercentageEntity;
    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerSummaryFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.recycler_summary_fragment, container, false);
        View view = binding.getRoot();
        recycleItemPercentageEntity = new RecycleItemPercentageEntity();
        //here data must be an instance of the class MarsDataProvider
        binding.setRecycleItemCount(recycleItemPercentageEntity);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBarChart();
        initBarChartData();
    }

    public void setRecycleAmount(UserProfileTable userProfile){
        recycleItemPercentageEntity.setRecyclePercentage(userProfile);
    }

    private void initBarChart() {
        //柱状图
        barChart = (BarChart) getView().findViewById(R.id.report_barChart);
        //设置柱状图点击的时候，的回调函数
//        barChart.setOnChartValueSelectedListener(this);
        //柱状图的阴影
        barChart.setDrawBarShadow(false);
        //设置柱状图Value值显示在柱状图上方 true 为显示上方，默认false value值显示在柱状图里面
        barChart.setDrawValueAboveBar(true);
        // 设置是否可以缩放图表
        barChart.setScaleEnabled(false);
        //Description Label 是否可见
        barChart.getDescription().setEnabled(false);
        // 设置最大可见Value值的数量 针对于ValueFormartter有效果
        barChart.setMaxVisibleValueCount(200);
        // 二指控制X轴Y轴同时放大
        barChart.setPinchZoom(false);
        //是否显示表格背景颜色
        barChart.setDrawGridBackground(false);
        //设置X轴显示文字旋转角度-60意为逆时针旋转60度
//        barChart.getXAxis().setLabelRotationAngle(-5);
        //關閉圖例
        barChart.getLegend().setEnabled(false);
        barChart.setExtraOffsets(0,0,0,10f);


        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //X轴纵向分割线，一般不设置显示
        xAxis.setDrawGridLines(false);
        // X轴显示Value值的精度，与自定义X轴返回的Value值精度一致
        xAxis.setGranularity(1f);
        //X轴横坐标显示的数量
        xAxis.setLabelCount(7);
        //X轴最大坐标
        xAxis.setAxisMaximum(8f);
        //X轴最小坐标
        xAxis.setAxisMinimum(0f);
        xAxis.setTextSize(24f);
        xAxis.setAxisLineWidth(2.0f);
        xAxis.setYOffset(20.0f);
        xAxis.setAxisLineColor(getContext().getResources().getColor(R.color.textInputLayoutBorder_3C4349));
        xAxis.setValueFormatter(new xAxisCallCountFormatter(barChart));
    }

    private void initBarChartData(){
        //模拟数据
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        yVals1.add(new BarEntry(1.0f, 30));
        yVals1.add(new BarEntry(2.0f, 21));
        yVals1.add(new BarEntry(3.0f, 52));
        yVals1.add(new BarEntry(4.0f, 5));
        yVals1.add(new BarEntry(5.0f, 5));
        yVals1.add(new BarEntry(6.0f, 40));
        yVals1.add(new BarEntry(7.0f, 30));

        BarDataSet set1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            //设置有四种颜色
            set1.setColors(new int[]{getContext().getResources().getColor(R.color.bar_purple_C9C6F7), getContext().getResources().getColor(R.color.bar_purple_C9C6F7),
                    getContext().getResources().getColor(R.color.bar_purple_C9C6F7),getContext().getResources().getColor(R.color.bar_purple_C9C6F7),
                    getContext().getResources().getColor(R.color.bar_purple_C9C6F7), getContext().getResources().getColor(R.color.bar_purple_C9C6F7),
                    getContext().getResources().getColor(R.color.bar_yellow_FCDC85)});
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(20f);
            data.setBarWidth(0.6f);
            data.setValueFormatter(new CallCountValueFormatter());
            data.setHighlightEnabled(false);
            //设置数据
            barChart.setData(data);
        }
    }

    public class xAxisCallCountFormatter implements IAxisValueFormatter {
        private final BarLineChartBase<?> mChart;

        public xAxisCallCountFormatter(BarLineChartBase<?> chart) {
            this.mChart = chart;

        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.i(TAG, "getFormattedValue-------------" + value);
            Log.i(TAG, "mChart.getVisibleXRange()-------------" + mChart.getVisibleXRange());

            List<String[]> xAxisTextList = new ArrayList<>();
            xAxisTextList.add(new String[]{"三", "四", "五", "六", "日", "一", "二"});

            if (value == 0.0 || value == 8.0) {
                return "";
            } else if(value > 0 && value < 8){
                return xAxisTextList.get(0)[Math.round(value)-1];
            } else {
                return "";
            }
        }
    }

    public class CallCountValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            int i  = (int) value;
            return String.valueOf(i);
        }
    }


}
