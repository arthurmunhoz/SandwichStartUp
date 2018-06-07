package br.com.arthurmunhoz.sandwichstartup.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import br.com.arthurmunhoz.sandwichstartup.R;
import br.com.arthurmunhoz.sandwichstartup.adapters.IngredientAdapter;
import br.com.arthurmunhoz.sandwichstartup.model.Ingredient;
import br.com.arthurmunhoz.sandwichstartup.model.Sandwich;
import br.com.arthurmunhoz.sandwichstartup.utils.Callbacks;
import br.com.arthurmunhoz.sandwichstartup.utils.Utils;

public class CustomizeActivity extends AppCompatActivity
{
    //Declaring variables
    private Context context;
    private ProgressBar progressBar;
    private Button btnFinalize;
    private RecyclerView rvIngredients;
    private IngredientAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Callbacks ingredientsRequestcallbacks;
    private Sandwich sandwich = new Sandwich();
    private List<Ingredient> ingredientList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        setUpUi();
        setButtonClicks();

        linearLayoutManager = new LinearLayoutManager(context);
        customizeAdapterClicks();
        rvIngredients.setLayoutManager(linearLayoutManager);
        rvIngredients.setHasFixedSize(true);
        rvIngredients.setAdapter(mAdapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (getIntent().getExtras() != null)
        {
            sandwich = getIntent().getParcelableExtra("sandwich");
        }

        //Populating list
        if (Utils.isConnectionAvailable(context))
        {
            requestIngredientsListJob();
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
        rvIngredients = findViewById(R.id.rvIngredient);
        progressBar = findViewById(R.id.progressBar);
        btnFinalize = findViewById(R.id.btnFinalize);

        //Changing the color of the StatusBar and NavigationBar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(context, R.color.yellow_logo));
        window.setNavigationBarColor(ContextCompat.getColor(context, R.color.yellow_logo));
    }

    private void setButtonClicks()
    {
        btnFinalize.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("sandwichCustomized", sandwich);
                setResult(1, intent);
                onBackPressed();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void requestIngredientsListJob()
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();

                rvIngredients.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                //Defining return actions
                ingredientsRequestcallbacks = new Callbacks()
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
                ingredientList = Utils.requestIngredientsList(ingredientsRequestcallbacks, sandwich);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                mAdapter.setIngredientData(ingredientList);

                rvIngredients.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }


    private void customizeAdapterClicks()
    {
        mAdapter = new IngredientAdapter(this, new IngredientAdapter.IngredientAdapterClickHandler()
        {
            @Override
            public void onClick(final Ingredient ingredient, int position)
            {
                View layoutView = linearLayoutManager.findViewByPosition(position);

                layoutView.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (ingredient.getName())
                        {
                            case "Alface":
                                sandwich.setLettuce(sandwich.getLettuce() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Alface\n");
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                break;

                            case "Bacon":
                                sandwich.setBacon(sandwich.getBacon() + 1);
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Bacon\n");
                                break;

                            case "Hamburguer de Carne":
                                sandwich.setBurger(sandwich.getBurger() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Hamburguer de Carne\n");
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                break;

                            case "Ovo":
                                sandwich.setEgg(sandwich.getEgg() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Ovo\n");
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                break;

                            case "Pão com gergelim":
                                sandwich.setBread(sandwich.getBread() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Pão com gergelim\n");
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                break;

                            case "Queijo":
                                sandwich.setCheese(sandwich.getCheese() + 1);
                                sandwich.setIngredients(sandwich.getIngredients() + "Queijo\n");
                                ingredient.setAmount(ingredient.getAmount() + 1);
                                break;
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });

                layoutView.findViewById(R.id.btnRemove).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (ingredient.getName())
                        {
                            case "Alface":
                                if (sandwich.getLettuce() > 0)
                                {
                                    sandwich.setLettuce(sandwich.getLettuce() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Alface\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;

                            case "Bacon":
                                if (sandwich.getBacon() > 0)
                                {
                                    sandwich.setBacon(sandwich.getBacon() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Bacon\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;

                            case "Hamburguer de Carne":
                                if (sandwich.getBurger() > 0)
                                {
                                    sandwich.setBurger(sandwich.getBurger() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Hamburguer de Carne\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;

                            case "Ovo":
                                if (sandwich.getEgg() > 0)
                                {
                                    sandwich.setEgg(sandwich.getEgg() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Ovo\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;

                            case "Pão com gergelim":
                                if (sandwich.getBread() > 0)
                                {
                                    sandwich.setBread(sandwich.getBread() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Pão com gergelim\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;

                            case "Queijo":
                                if (sandwich.getCheese() > 0)
                                {
                                    sandwich.setCheese(sandwich.getCheese() - 1);
                                    sandwich.setIngredients(sandwich.getIngredients().replace("Queijo\n", ""));
                                    ingredient.setAmount(ingredient.getAmount() - 1);
                                }
                                break;
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
