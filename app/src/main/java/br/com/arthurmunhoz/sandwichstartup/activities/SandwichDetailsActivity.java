package br.com.arthurmunhoz.sandwichstartup.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.model.Order;
import br.com.arthurmunhoz.sandwichstartup.model.Sandwich;
import br.com.arthurmunhoz.sandwichstartup.utils.Utils;

public class SandwichDetailsActivity extends AppCompatActivity
{
    //Declaring variables
    private final static int ACTIVITY_RESULT = 1;
    private Sandwich sandwich = new Sandwich();
    private ImageView ivPhoto;
    private TextView tvPrice;
    private TextView tvIngredients;
    private TextView tvTitle;
    private ImageView btnAddToCart;
    private ImageView btnCustomize;
    private Context context;
    private Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich_details);

        if (getIntent().getExtras() == null)
        {
            Toast.makeText(this, "Falha ao obter dados do lanche, tente novamente.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Bundle data = getIntent().getExtras();
            sandwich = data.getParcelable("sandwich");
        }

        setUpUi();
        setButtonClicks();
    }

    private void setUpUi()
    {
        context = this;

        //Attaching XML objects
        ivPhoto = findViewById(R.id.ivPhoto);
        tvPrice = findViewById(R.id.tvPrice);
        tvIngredients = findViewById(R.id.tvIngredients);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnCustomize = findViewById(R.id.btn_customize);

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(context, R.color.yellow_logo));
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.yellow_logo));

        //Customizing ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_appbar_with_cart_layout, null);
            actionBar.setElevation(20);
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.app_logo));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            actionBar.setCustomView(mCustomView);
            actionBar.setDisplayShowCustomEnabled(true);
            Toolbar parent = (Toolbar) mCustomView.getParent();
            parent.setContentInsetsAbsolute(0,0);

            tvTitle = mCustomView.findViewById(R.id.tvTitle);
            tvTitle.setText(sandwich.getName());

            RelativeLayout btn_backArrow = mCustomView.findViewById(R.id.btn_backArrow);
            btn_backArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    finish();
                }
            });

            RelativeLayout btnGoToCart = mCustomView.findViewById(R.id.btn_goToCart);
            btnGoToCart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    startActivity(new Intent(SandwichDetailsActivity.this, OrderActivity.class));
                }
            });
        }

        Picasso.with(context).load(sandwich.getImage()).into(ivPhoto);
        tvPrice.setText(sandwich.getPrice());
        tvIngredients.setText(sandwich.getIngredients());
        btnCustomize.setImageDrawable(this.getDrawable(R.drawable.customize));
        btnAddToCart.setImageDrawable(this.getDrawable(R.drawable.add_to_cart));
    }

    /* ***************************
           SET BUTTON CLICKS
    *************************** */
    private void setButtonClicks()
    {
        btnCustomize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SandwichDetailsActivity.this, CustomizeActivity.class);
                intent.putExtra("sandwich", sandwich);

                startActivityForResult(intent, ACTIVITY_RESULT);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                order.add(sandwich);

                Intent intent = new Intent(SandwichDetailsActivity.this, OrderActivity.class);
                intent.putExtra("order", order);

                startActivity(intent);
            }
        });
    }

    /* ***************************
           ON ACTIVITY RESULT
    *************************** */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {
            case ACTIVITY_RESULT:
                //getting customized sandwich
                sandwich = data.getParcelableExtra("sandwichCustomized");

                //Updating sandwich values
                if (!sandwich.getName().contains(" - do seu jeito"))
                {
                    sandwich.setName(sandwich.getName() + " - do seu jeito");
                }
                sandwich.setPrice(String.valueOf(Utils.calculateSandwichCustomizedPrice(sandwich)));

                //updating UI
                tvTitle.setText(sandwich.getName());
                tvIngredients.setText(sandwich.getIngredients());
                tvPrice.setText("R$ " + sandwich.getPrice());

                int k = 0;
                break;

        }
    }
}
