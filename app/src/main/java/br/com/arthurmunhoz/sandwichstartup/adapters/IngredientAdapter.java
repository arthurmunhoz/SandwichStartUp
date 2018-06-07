package br.com.arthurmunhoz.sandwichstartup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.model.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>
{
    //Declaring variables
    private IngredientAdapterClickHandler mClickHandler;
    List<Ingredient> ingredientsList;
    Context ctx;
    Ingredient ingredient = new Ingredient();
    ImageLoaderConfiguration configuration;

    public IngredientAdapter(Context ctx, IngredientAdapterClickHandler clickHandler)
    {
        this.mClickHandler = clickHandler;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.custom_ingredient_item_layout, parent, false);

        return new IngredientViewHolder(view, mClickHandler);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position)
    {
        ingredient = ingredientsList.get(position);

        holder.tvAmount.setText(String.valueOf(ingredient.getAmount()));

        Picasso.with(ctx).load(ingredient.getImage())
                .resize(120, 120)
                .placeholder(ctx.getDrawable(R.drawable.app_icon))
                .onlyScaleDown()
                .into(holder.ivIngredientPhoto);
    }

    @Override
    public int getItemCount()
    {
        if (ingredientsList != null)
        {
            return ingredientsList.size();
        }

        return 0;
    }

    /********************************
     Interface for item clicks
     *********************************/
    public interface IngredientAdapterClickHandler
    {
        void onClick(Ingredient ingredient, int position);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //Declaring variables
        private ImageView ivIngredientPhoto;
        private Button btnAdd;
        private Button btnRemove;
        private TextView tvAmount;
        private WeakReference<IngredientAdapterClickHandler> listenerRef;

        public IngredientViewHolder(View itemView, IngredientAdapterClickHandler clickHandler)
        {
            super(itemView);

            listenerRef = new WeakReference<>(clickHandler);
            ivIngredientPhoto = itemView.findViewById(R.id.ivIngredientPhoto);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvAmount = itemView.findViewById(R.id.tvAmount);

            btnAdd.setOnClickListener(this);
            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int adapterPosition = getAdapterPosition();
            Ingredient ingredient = ingredientsList.get(adapterPosition);

            mClickHandler.onClick(ingredient, adapterPosition);
        }
    }

    public void setIngredientData(List<Ingredient> ingList)
    {
        ingredientsList = ingList;
        notifyDataSetChanged();
    }

}
