package com.hotix.myhotixhousekeeping.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.models.Forecast;

import java.util.ArrayList;

import static com.hotix.myhotixhousekeeping.helpers.Utils.dateColored;

public class ForecastAdapter extends ArrayAdapter<Forecast> {

    Context mContext;
    private ArrayList<Forecast> dataSet;

    public ForecastAdapter(ArrayList<Forecast> data, Context context) {
        super(context, R.layout.item_forecast_row, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Forecast dataModel = getItem(position);

        ForecastAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ForecastAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_forecast_row, parent, false);

            viewHolder.forcast_date = (TextView) convertView.findViewById(R.id.tv_forcast_row_date);
            viewHolder.forcast_bchart = (BarChart) convertView.findViewById(R.id.bchart_forcast_row);
            viewHolder.forcast_pchart = (PieChart) convertView.findViewById(R.id.pchart_forcast_row);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ForecastAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.forcast_date.setText(Html.fromHtml(dateColored(dataModel.getDate(), "#ffffff", "#016064", "yyyyMMdd", true)));

        //BAR Chart Settings
        ArrayList<BarEntry> entriesArr = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesDep = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesRec = new ArrayList<BarEntry>();

        entriesArr.add(new BarEntry(0, Float.valueOf(dataModel.getArrCHB().replace(',', '.'))));
        entriesRec.add(new BarEntry(1, Float.valueOf(dataModel.getResCHB().replace(',', '.'))));
        entriesDep.add(new BarEntry(2, Float.valueOf(dataModel.getDepCHB().replace(',', '.'))));

        BarDataSet d = new BarDataSet(entriesArr, mContext.getResources().getString(R.string.text_arrivals));
        d.setColor(Color.rgb(104, 241, 175));
        d.setHighLightAlpha(255);
        BarDataSet d1 = new BarDataSet(entriesDep, mContext.getResources().getString(R.string.text_departures));
        d1.setColor(Color.RED);
        d1.setHighLightAlpha(255);
        BarDataSet d2 = new BarDataSet(entriesRec, mContext.getResources().getString(R.string.text_recouche));
        d2.setColor(Color.rgb(242, 247, 158));
        d2.setHighLightAlpha(255);

        BarData barData = new BarData(d, d1, d2);
        barData.setBarWidth(0.9f);

        viewHolder.forcast_bchart.setData(barData);
        viewHolder.forcast_bchart.setDrawGridBackground(false);
        viewHolder.forcast_bchart.getDescription().setEnabled(false);
        viewHolder.forcast_bchart.setDrawBarShadow(false);
        viewHolder.forcast_bchart.setFitBars(true);
        viewHolder.forcast_bchart.animateY(700);

        XAxis xAxis = viewHolder.forcast_bchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setEnabled(false);

        YAxis leftAxis = viewHolder.forcast_bchart.getAxisLeft();
        leftAxis.setLabelCount(3, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = viewHolder.forcast_bchart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //PIE Chart Settings
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((float) Float.valueOf(dataModel.getOccCHB().replace(',', '.')), mContext.getResources().getString(R.string.text_taux)));
        entries.add(new PieEntry((float) (100 - Float.valueOf(dataModel.getOccCHB().replace(',', '.'))), mContext.getResources().getString(R.string.text_reste)));
        PieDataSet pd = new PieDataSet(entries, "");

        pd.setSliceSpace(2f);
        pd.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(pd);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK);

        viewHolder.forcast_pchart.setData(pieData);

        viewHolder.forcast_pchart.animateY(900);

        viewHolder.forcast_pchart.getDescription().setEnabled(false);
        viewHolder.forcast_pchart.setHoleRadius(52f);
        viewHolder.forcast_pchart.setTransparentCircleRadius(57f);
        viewHolder.forcast_pchart.setCenterText(mContext.getString(R.string.text_occupation_rate));
        viewHolder.forcast_pchart.setCenterTextSize(9f);
        viewHolder.forcast_pchart.setUsePercentValues(true);
        viewHolder.forcast_pchart.setExtraOffsets(5, 10, 50, 10);
        viewHolder.forcast_pchart.setRotationEnabled(true);
        viewHolder.forcast_pchart.setHighlightPerTapEnabled(true);

        Legend l = viewHolder.forcast_pchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        // Return the completed view to render on screen(
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView forcast_date;
        BarChart forcast_bchart;
        PieChart forcast_pchart;
    }

}

