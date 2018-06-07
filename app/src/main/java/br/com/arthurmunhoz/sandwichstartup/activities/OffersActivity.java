package br.com.arthurmunhoz.sandwichstartup.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.model.Offer;
import br.com.arthurmunhoz.sandwichstartup.utils.Callbacks;
import br.com.arthurmunhoz.sandwichstartup.utils.Utils;

public class OffersActivity extends AppCompatActivity
{
    //Declaring variables
    TextView offerOneText;
    TextView offerTwoTitle;
    TextView offerOneTitle;
    TextView offerTwoText;
    TextView offerThreeTitle;
    TextView offerThreeText;
    TextView offersHeader;
    TextView tvTitle;
    ProgressBar progressBar;
    List<Offer> offersList = new ArrayList<>();
    Callbacks callbacks;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        setUpUi();
        setButtonClicks();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Populating list
        if (Utils.isConnectionAvailable(context))
        {
            requestOffersListJob();
        }
        else
        {
            Toast.makeText(context, "Internet indisponível! Conecte seu smarthphone e tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpUi()
    {
        context = this;

        //Attaching XML objects
        offersHeader = findViewById(R.id.offersHeader);
        progressBar = findViewById(R.id.progressBar);
        offerOneText = findViewById(R.id.offerOneText);
        offerTwoTitle = findViewById(R.id.offerTwoTitle);
        offerOneTitle = findViewById(R.id.offerOneTitle);
        offerTwoText = findViewById(R.id.offerTwoText);
        offerThreeTitle = findViewById(R.id.offerThreeTitle);
        offerThreeText = findViewById(R.id.offerThreeText);

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
            tvTitle.setText("Promoções");

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

    @SuppressLint("StaticFieldLeak")
    private void requestOffersListJob()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                //Managing UI elements
                progressBar.setVisibility(View.VISIBLE);
                offerOneText.setVisibility(View.GONE);
                offerOneTitle.setVisibility(View.GONE);
                offerThreeText.setVisibility(View.GONE);
                offerThreeTitle.setVisibility(View.GONE);
                offerTwoText.setVisibility(View.GONE);
                offerTwoTitle.setVisibility(View.GONE);
                offersHeader.setVisibility(View.GONE);

                callbacks = new Callbacks()
                {
                    @Override
                    public void onRequestFailed()
                    {
                        Toast.makeText(context, getString(R.string.http_response_failed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestSuccess()
                    {

                    }
                };
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                offersList = Utils.requestOffersList(callbacks);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                offerOneTitle.setText(offersList.get(0).getName());
                offerOneText.setText(offersList.get(0).getDescription());

                offerTwoTitle.setText(offersList.get(1).getName());
                offerTwoText.setText(offersList.get(1).getDescription());

                offerThreeTitle.setText(offersList.get(2).getName());
                offerThreeText.setText(offersList.get(2).getDescription());

                //Managing UI elements
                progressBar.setVisibility(View.GONE);
                offerOneText.setVisibility(View.VISIBLE);
                offerOneTitle.setVisibility(View.VISIBLE);
                offerThreeText.setVisibility(View.VISIBLE);
                offerThreeTitle.setVisibility(View.VISIBLE);
                offerTwoText.setVisibility(View.VISIBLE);
                offerTwoTitle.setVisibility(View.VISIBLE);
                offersHeader.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}
