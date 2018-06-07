package br.com.arthurmunhoz.sandwichstartup.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.adapters.SandwichAdapter;
import br.com.arthurmunhoz.sandwichstartup.model.Sandwich;
import br.com.arthurmunhoz.sandwichstartup.utils.Callbacks;
import br.com.arthurmunhoz.sandwichstartup.utils.Utils;

public class SandwichesListActivity extends AppCompatActivity implements SandwichAdapter.SandwichAdapterClickHandler, Serializable
{
    //Declaring variables
    private RecyclerView rvSandwichList;
    private ProgressBar progressBar;
    private Context context;
    private SandwichAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Callbacks sandwichListaCallbacks;
    private List<Sandwich> sandwichList = new ArrayList<>();

    /* *****************************
                ON CREATE
     ******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwiches_list);

        setUpUi();
        setButtonClicks();

        mAdapter = new SandwichAdapter(this, this);
        linearLayoutManager = new LinearLayoutManager(context);
        rvSandwichList.setLayoutManager(linearLayoutManager);
        rvSandwichList.setHasFixedSize(true);
        rvSandwichList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //Populating list
        if (Utils.isConnectionAvailable(context))
        {
            requestSandwichListJob();
        }
        else
        {
            Toast.makeText(context, "Internet indisponível! Conecte seu smarthphone e tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    /* *****************************
             SET UP UI
    ******************************/
    private void setUpUi()
    {
        context = this;

        //Attaching XML objects
        rvSandwichList = findViewById(R.id.recycler_view_sandwiches);
        progressBar = findViewById(R.id.progressBar);

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SandwichesListActivity.this, R.color.yellow_logo));
        window.setNavigationBarColor(ContextCompat.getColor(SandwichesListActivity.this, R.color.yellow_logo));
    }

    /* *****************************
            SET BUTTON CLICKS
     ******************************/
    private void setButtonClicks()
    {

    }

    @Override
    public void onClick(Sandwich sandwich)
    {
        Intent intent = new Intent(SandwichesListActivity.this, SandwichDetailsActivity.class);
        intent.putExtra("sandwich", sandwich);

        startActivity(intent);
    }

    /* *****************************
       REQUEST SANDWICHES LIST JOB
     ******************************/
    @SuppressLint("StaticFieldLeak")
    private void requestSandwichListJob()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                //Managing UI elements
                progressBar.setVisibility(View.VISIBLE);
                rvSandwichList.setVisibility(View.GONE);

                //Defining return actions
                sandwichListaCallbacks = new Callbacks()
                {
                    @Override
                    public void onRequestFailed()
                    {
                        Toast.makeText(context, getString(R.string.http_response_failed), Toast.LENGTH_SHORT).show();
                        finish();
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
                sandwichList = Utils.requestSandwichesList(sandwichListaCallbacks);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                //Parsing ingredients
                sandwichList = Utils.countIngredients(sandwichList);

                mAdapter.setSandwichData(sandwichList);
                mAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
                rvSandwichList.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}
