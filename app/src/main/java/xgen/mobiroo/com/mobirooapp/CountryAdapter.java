package xgen.mobiroo.com.mobirooapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xgen.mobiroo.com.mobirooapp.entity.Country;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    public List<Country> item;
    private Activity context;
    OnItemClickListener mItemClickListener;

    public CountryAdapter(List<Country> item, Activity context) {
        this.item = item;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Country country = item.get(position);
        holder.countries.setText(country.getCountryName());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView countries;
        public MyViewHolder(View itemView) {
            super(itemView);
            countries = (TextView) itemView.findViewById(R.id.countryName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
