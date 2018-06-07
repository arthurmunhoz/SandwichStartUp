package br.com.arthurmunhoz.sandwichstartup.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.arthurmunhoz.sandwichstartup.model.Ingredient;
import br.com.arthurmunhoz.sandwichstartup.model.Offer;
import br.com.arthurmunhoz.sandwichstartup.model.Sandwich;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils
{
    /*
     *   @Function:
     *           isConnectionAvailable
     *   @Description:
     *           Checks for internet connectivity, returning true if niternet is available
     *           and false otherwise
     */
    /* ***************************************
             IS CONNECTION AVAILABLE
     ************************************** */
    public static boolean isConnectionAvailable(Context context)
    {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = null;
        if (conMgr != null)
        {
            i = conMgr.getActiveNetworkInfo();
        }

        return i != null && i.isConnected() && i.isAvailable();
    }

    /* ***************************************
              REQUEST SANDWICH LIST
     ************************************** */
    public static List<Sandwich> requestSandwichesList(Callbacks callbacks)
    {
        // =====================
        // Declaring variables
        // =====================
        final List<Sandwich> sandwichList = new ArrayList<>();

        JSONArray jsonArrayIngredients = null;
        JSONArray jsonArraySandwiches = null;

        JSONObject objectSandwiches = null;
        JSONObject objectIngredients = null;

        final String[] jsonDataSandwich = {null};
        final String[] jsonDataIngredients = {null};

        String urlS = Constants.BASE_URL + Constants.SANDWICH_LIST;
        String urlI = Constants.BASE_URL + Constants.INGREDIENTS_LIST;

        HttpUrl routeS = HttpUrl.parse(urlS);
        HttpUrl routeI = HttpUrl.parse(urlI);

        final Request requestSandwiches = new Request.Builder().url(routeS).get().build();
        final Request requestIngredients = new Request.Builder().url(routeI).get().build();

        final Response[] responseSandwiches = {null};
        final Response[] responseIngredients = {null};

        //Declaring HTTP client from OkHttp API
        final OkHttpClient clientSand = new OkHttpClient();
        final OkHttpClient clientIng = new OkHttpClient();


        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }

        // =====================
        // Getting SANDWICHES
        // =====================
        try
        {
            responseSandwiches[0] = clientSand.newCall(requestSandwiches).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (responseSandwiches[0] != null)
        {
            if (responseSandwiches[0].isSuccessful())
            {
                try
                {
                    jsonDataSandwich[0] = responseSandwiches[0].body().string();
                    jsonArraySandwiches = new JSONArray(jsonDataSandwich[0]);
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArraySandwiches.length(); i++)
                {
                    Sandwich sandwich = new Sandwich();

                    try
                    {
                        objectSandwiches = jsonArraySandwiches.getJSONObject(i);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    sandwich.setId(objectSandwiches.optInt("id"));
                    sandwich.setIngredients(objectSandwiches.optString("ingredients"));
                    sandwich.setImage(objectSandwiches.optString("image"));
                    sandwich.setName(objectSandwiches.optString("name"));

                    sandwichList.add(sandwich);
                }
            }
            else
            {
                callbacks.onRequestFailed();
            }
        }
        else
        {
            callbacks.onRequestFailed();
        }

        // =====================
        // Getting INGREDIENTS
        // =====================
        try
        {
            responseIngredients[0] = clientIng.newCall(requestIngredients).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (responseIngredients[0] != null)
        {
            if (responseIngredients[0].isSuccessful())
            {
                try
                {
                    jsonDataIngredients[0] = responseIngredients[0].body().string();
                    jsonArrayIngredients = new JSONArray(jsonDataIngredients[0]);
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArrayIngredients.length(); i++)
                {
                    try
                    {
                        objectIngredients = jsonArrayIngredients.getJSONObject(i);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                callbacks.onRequestFailed();
            }
        }
        else
        {
            callbacks.onRequestFailed();
        }

        for (int i = 0; i < sandwichList.size(); i++)
        {
            calculateSandPrice(sandwichList.get(i), jsonArrayIngredients);
            parseIngredients(sandwichList.get(i), jsonArrayIngredients);
        }

        return sandwichList;
    }

    /* ***************************************
                PARSE INGREDIENTS
     ************************************** */
    private static void parseIngredients(Sandwich sandwich, JSONArray jsonArrayIngredients)
    {
        String auxString = sandwich.getIngredients()
                                    .replaceAll("[\\[]", "")
                                    .replaceAll(",", "")
                                    .replaceAll("]", "");

        StringBuilder ingredients = new StringBuilder();

        for (int i = 0; i < auxString.length(); i++)
        {
            JSONObject object = null;

                for (int j = 0; j < jsonArrayIngredients.length(); j++)
                {
                    try
                    {
                        object = jsonArrayIngredients.getJSONObject(j);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    if (object != null)
                    {
                        char character = auxString.charAt(i);

                        if (character == object.optString("id").charAt(0))
                        {
                            ingredients.append(object.optString("name") + "\n");
                        }
                    }
                }
        }

        sandwich.setIngredients(ingredients.toString());
    }

    /* ***************************************
             CALCULATE SAND. PRICE
     ************************************** */
    private static void calculateSandPrice(Sandwich sandwich, JSONArray jsonArrayIngredients)
    {
        String floatConverter;
        String auxString = sandwich.getIngredients()
                .replaceAll("[\\[]", "")
                .replaceAll(",", "")
                .replaceAll("]", "");

        float price = 0;

        for (int i = 0; i < auxString.length(); i++)
        {
            JSONObject object = null;

            for (int j = 0; j < jsonArrayIngredients.length(); j++)
            {
                try
                {
                    object = jsonArrayIngredients.getJSONObject(j);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                if (object != null)
                {
                    char character = auxString.charAt(i);

                    if (character == object.optString("id").charAt(0))
                    {
                        floatConverter = object.optString("price");
                        price += Float.valueOf(floatConverter.trim());
                    }
                }
            }
        }
        String aux;
        StringBuilder builder = new StringBuilder();
        builder.append(price);

        if (builder.substring(builder.indexOf("."), (builder.length())).length() > 2 )
        {
            aux = builder.substring(0, builder.indexOf(".") + 2);
            builder = new StringBuilder();
            sandwich.setPrice(builder.append("R$ ").append(aux).append(0).toString());
        }
        else
        {
            builder = new StringBuilder();
            sandwich.setPrice(builder.append("R$ ").append(price).append(0).toString());
        }
    }

    /* ***************************************
             REQUEST OFFERS LIST
    ************************************** */
    public static List<Offer> requestOffersList(Callbacks callbacks)
    {
        // =====================
        // Declaring variables
        // =====================
        final List<Offer> offers = new ArrayList<>();

        JSONArray jsonArrayOffers = null;

        JSONObject objectOffers = null;

        String offerData = null;

        String url = Constants.BASE_URL + Constants.OFFERS;

        HttpUrl route = HttpUrl.parse(url);

        final Request requestOffers = new Request.Builder().url(route).get().build();

        Response responseSandwiches = null;

        //Declaring HTTP client from OkHttp API
        final OkHttpClient client = new OkHttpClient();

        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }

        // =====================
        // Getting SANDWICHES
        // =====================
        try
        {
            responseSandwiches = client.newCall(requestOffers).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (responseSandwiches != null)
        {
            if (responseSandwiches.isSuccessful())
            {
                try
                {
                    offerData = responseSandwiches.body().string();
                    jsonArrayOffers = new JSONArray(offerData);
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArrayOffers.length(); i++)
                {
                    Offer offer = new Offer();

                    try
                    {
                        objectOffers = jsonArrayOffers.getJSONObject(i);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    offer.setDescription(objectOffers.optString("description"));
                    offer.setName(objectOffers.optString("name"));

                    offers.add(offer);
                }
            }
            else
            {
                callbacks.onRequestFailed();
            }
        }
        else
        {
            callbacks.onRequestFailed();
        }

        return offers;
    }

    /* ***************************************
             REQUEST INGREDIENTS LIST
    ************************************** */
    public static List<Ingredient> requestIngredientsList(Callbacks callbacks, Sandwich sandwich)
    {
        // =====================
        // Declaring variables
        // =====================
        final List<Ingredient> ingredients = new ArrayList<>();

        JSONArray jsonArrayIngredients = null;

        JSONObject objectIngredients = null;

        String ingredientData = null;

        String url = Constants.BASE_URL + Constants.INGREDIENTS_LIST;

        HttpUrl route = HttpUrl.parse(url);

        final Request requestIngredients = new Request.Builder().url(route).get().build();

        Response responseIngredients = null;

        //Declaring HTTP client from OkHttp API
        final OkHttpClient client = new OkHttpClient();

        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }

        // =====================
        // Getting INGREDIENTS
        // =====================
        try
        {
            responseIngredients = client.newCall(requestIngredients).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (responseIngredients != null)
        {
            if (responseIngredients.isSuccessful())
            {
                try
                {
                    ingredientData = responseIngredients.body().string();
                    jsonArrayIngredients = new JSONArray(ingredientData);
                }
                catch (IOException | JSONException e)
                {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArrayIngredients.length(); i++)
                {
                    Ingredient ingredient = new Ingredient();

                    try
                    {
                        objectIngredients = jsonArrayIngredients.getJSONObject(i);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    ingredient.setId(objectIngredients.optInt("id"));
                    ingredient.setName(objectIngredients.optString("name"));
                    ingredient.setPrice(objectIngredients.optString("price"));
                    ingredient.setImage(objectIngredients.optString("image"));

                    if (sandwich.getBacon() > 0 && ingredient.getName().contentEquals("Bacon")
                            || sandwich.getCheese() > 0 && ingredient.getName().contentEquals("Queijo")
                            || sandwich.getBurger() > 0 && ingredient.getName().contentEquals("Hamburguer de Carne")
                            || sandwich.getBread() > 0 && ingredient.getName().contentEquals("Pão com gergelim")
                            || sandwich.getEgg() > 0 && ingredient.getName().contentEquals("Ovo")
                            || sandwich.getLettuce() > 0 && ingredient.getName().contentEquals("Alface"))
                    {
                        ingredient.setAmount(ingredient.getAmount() + 1);
                    }

                    ingredients.add(ingredient);
                }
            }
            else
            {
                callbacks.onRequestFailed();
            }
        }
        else
        {
            callbacks.onRequestFailed();
        }

        return ingredients;
    }

    /* ***************************************
             COUNT INGREDIENTS
     ************************************** */
    public static List<Sandwich> countIngredients(List<Sandwich> sandwichList)
    {
        for (int i=0; i<sandwichList.size(); i++)
        {
            int counter = 0;
            String ingredients = sandwichList.get(i).getIngredients();
            ingredients.replaceAll(System.lineSeparator(), " ");

            if (ingredients.contains("Alface"))
            {
                sandwichList.get(i).setLettuce(sandwichList.get(i).getLettuce()+1);
            }
            if (ingredients.contains("Hamburguer de Carne"))
            {
                sandwichList.get(i).setBurger(sandwichList.get(i).getBurger()+1);
            }
            if (ingredients.contains("Ovo"))
            {
                sandwichList.get(i).setEgg(sandwichList.get(i).getEgg()+1);
            }
            if (ingredients.contains("Queijo"))
            {
                sandwichList.get(i).setCheese(sandwichList.get(i).getCheese()+1);
            }
            if (ingredients.contains("Bacon"))
            {
                sandwichList.get(i).setBacon(sandwichList.get(i).getBacon()+1);
            }
            if (ingredients.contains("Pão com gergelim"))
            {
                sandwichList.get(i).setBread(sandwichList.get(i).getBread()+1);
            }
        }

        return sandwichList;
    }

    public static float calculateSandwichCustomizedPrice(Sandwich sandwich)
    {
        float price = 0;
        String ingredients = sandwich.getIngredients();
        String[] ingredientsArray = ingredients.split("\n");

        for (int i=0; i<ingredientsArray.length; i++)
        {
            switch(ingredientsArray[i])
            {
                case "Alface":
                    price += 0.40;
                    break;

                case "Ovo":
                    price += 0.80;
                    break;

                case "Queijo":
                    price += 1.50;
                    break;

                case "Hamburguer de Carne":
                    price += 3.00;
                    break;

                case "Pão com gergelim":
                    price += 1.00;
                    break;

                case "Bacon":
                    price += 2.00;
                    break;

                default:
                    price += 0.00;
            }
        }

        return price;
    }
}
