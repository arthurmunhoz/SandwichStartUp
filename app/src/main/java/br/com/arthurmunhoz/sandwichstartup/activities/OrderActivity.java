package br.com.arthurmunhoz.sandwichstartup.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.adapters.SandwichAdapter;
import br.com.arthurmunhoz.sandwichstartup.model.Order;

public class OrderActivity extends AppCompatActivity
{
    //Declaring variables
    Context context;
    Order order = new Order();
    TextView tvTitle;
    TextView tvEmptyCart;
    Button btnPay;
    Button btnBackToShopping;
    RecyclerView rvOrder;
    LinearLayoutManager linearLayoutManager;
    SandwichAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setUpUi();
        setButtonClicks();

        mAdapter = new SandwichAdapter(this, null);
        linearLayoutManager = new LinearLayoutManager(context);
        rvOrder.setLayoutManager(linearLayoutManager);
        rvOrder.setHasFixedSize(true);
        rvOrder.setAdapter(mAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (getIntent().getExtras() != null)
        {
            order = getIntent().getParcelableExtra("order");

            mAdapter.setSandwichData(order.getList());
        }

        if (order.getList().isEmpty())
        {
            tvEmptyCart.setVisibility(View.VISIBLE);
            rvOrder.setVisibility(View.GONE);
        }
        else
        {
            tvEmptyCart.setVisibility(View.GONE);
            rvOrder.setVisibility(View.VISIBLE);
        }
    }

    private void setUpUi()
    {
        context = this;

        //Attaching XML objects
        btnPay.findViewById(R.id.btnPay);
        rvOrder.findViewById(R.id.rvOrder);
        tvEmptyCart.findViewById(R.id.tvEmptyCart);
        btnBackToShopping.findViewById(R.id.btnBackToShopping);

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
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_appbar_layout, null);
            actionBar.setElevation(20);
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.app_logo));
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            actionBar.setCustomView(mCustomView);
            actionBar.setDisplayShowCustomEnabled(true);
            Toolbar parent = (Toolbar) mCustomView.getParent();
            parent.setContentInsetsAbsolute(0,0);

            tvTitle = mCustomView.findViewById(R.id.tvTitle);

            RelativeLayout btn_backArrow = mCustomView.findViewById(R.id.btn_backArrow);
            btn_backArrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    finish();
                }
            });
        }
    }

    private void setButtonClicks()
    {

    }
}
