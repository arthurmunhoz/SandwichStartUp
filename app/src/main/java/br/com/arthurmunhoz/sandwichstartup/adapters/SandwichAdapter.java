package br.com.arthurmunhoz.sandwichstartup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.model.Sandwich;

public class SandwichAdapter extends RecyclerView.Adapter<SandwichAdapter.SandwichViewHolder>
{
    //Declaring variables
    private SandwichAdapterClickHandler mClickHandler;
    List<Sandwich> sandwichList;
    Context ctx;
    Sandwich sandwich = new Sandwich();

    public SandwichAdapter(Context context, SandwichAdapterClickHandler clickHandler)
    {
        this.mClickHandler = clickHandler;
        this.ctx = context;
    }

    @NonNull
    @Override
    public SandwichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutId = R.layout.custom_sandwich_item_layout;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        return new SandwichViewHolder(view);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final SandwichViewHolder holder, int position)
    {
        Context context = holder.ivPhoto.getContext();
        sandwich = sandwichList.get(position);

        holder.tvIngredients.setText(sandwich.getIngredients());
        holder.tvName.setText(sandwich.getName());
        holder.tvPrice.setText(sandwich.getPrice());

        Picasso.with(context).load(sandwich.getImage())
                .fit()
                .placeholder(ctx.getDrawable(R.drawable.app_icon))
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount()
    {
        if (sandwichList != null)
        {
            return sandwichList.size();
        }

        return 0;
    }

    /********************************
     Interface for item clicks
     *********************************/
    public interface SandwichAdapterClickHandler
    {
        void onClick(Sandwich sandwich);
    }

    /*************************
     VIEW HOLDER
     **************************/
    public class SandwichViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //Declaring variables
        ImageView ivPhoto;
        TextView tvName;
        TextView tvPrice;
        TextView tvIngredients;

        public SandwichViewHolder(View itemView)
        {
            super(itemView);

            ivPhoto = itemView.findViewById(R.id.item_image);
            tvName = itemView.findViewById(R.id.item_name);
            tvPrice = itemView.findViewById(R.id.item_price);
            tvIngredients = itemView.findViewById(R.id.item_ingredients_field);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            Sandwich sandwich = sandwichList.get(adapterPosition);
            mClickHandler.onClick(sandwich);
        }
    }

    public void setSandwichData(List<Sandwich> sandwichDataList)
    {
        sandwichList = sandwichDataList;
        notifyDataSetChanged();
    }

}
