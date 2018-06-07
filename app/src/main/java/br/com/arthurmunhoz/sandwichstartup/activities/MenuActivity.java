package br.com.arthurmunhoz.sandwichstartup.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.arthurmunhoz.sandwichstartup.R;

public class MenuActivity extends AppCompatActivity
{
    //Declaring variables
    private ImageView ivAppLogo;
    private Button btnSandwiches;
    private Button btnOfferss;

    /* *****************************
                ON CREATE
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setUpUi();
        setButtonClicks();
    }

    /* *****************************
                SET UP UI
     ******************************/
    private void setUpUi()
    {
        //Referencing XML objects
        ivAppLogo = findViewById(R.id.iv_logo);
        btnSandwiches = findViewById(R.id.btn_sandwiches);
        btnOfferss = findViewById(R.id.btn_sales);

        //Loading the logo into "ivAppLogo"
        Picasso.with(this).load(R.drawable.app_logo).resize(600, 900).centerInside().into(ivAppLogo);

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(MenuActivity.this, R.color.yellow_logo));
        }
    }

    /* *****************************
            SET BUTTON CLICKS
     ******************************/
    private void setButtonClicks()
    {
        btnSandwiches.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MenuActivity.this, SandwichesListActivity.class));
            }
        });

        btnOfferss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MenuActivity.this, OffersActivity.class));
            }
        });

    }

}
