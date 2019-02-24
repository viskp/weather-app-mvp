package com.vishal.weatherapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishal.weatherapp.pojo.ForecastDataModel;

import java.util.List;

/**
 * Adapter class for showing list of forecast of a give city. This class expect a constructor data
 * in the form of {@link List<ForecastDataModel>} which holds all forecast data.
 *
 * @author Vishal - 24th Feb 2019
 * @since 1.0.0
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private List<ForecastDataModel> forecastData;

    public ForecastAdapter(List<ForecastDataModel> forecastData) {
        this.forecastData = forecastData;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View setMenuView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row,
                parent, false);
        return new ForecastHolder(setMenuView);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        holder.setForecastDataModel(forecastData.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastData.size();
    }


    static class ForecastHolder extends RecyclerView.ViewHolder {

        private TextView dayNameTV;
        private TextView conditionTextTV;
        private TextView minMaxTempTV;
        private ImageView conditionIconIV;

        private ForecastHolder(View itemView) {
            super(itemView);
            dayNameTV = itemView.findViewById(R.id.day_name);
            conditionTextTV = itemView.findViewById(R.id.condition_text);
            minMaxTempTV = itemView.findViewById(R.id.min_max_temp);
            conditionIconIV = itemView.findViewById(R.id.condition_icon);
        }

        private void setForecastDataModel(ForecastDataModel forecastDataModel) {
            dayNameTV.setText(forecastDataModel.getDay());
            conditionTextTV.setText(forecastDataModel.getCondition());
            minMaxTempTV.setText(forecastDataModel.getMinMaxTemp());
            conditionIconIV.setImageDrawable(forecastDataModel.getIcon());
        }
    }
}
