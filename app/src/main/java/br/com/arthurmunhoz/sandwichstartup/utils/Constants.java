package br.com.arthurmunhoz.sandwichstartup.utils;

import android.net.Network;

public class Constants
{
    // Base URL for requests to the server
    public static final String BASE_URL = "http://192.168.0.17:8080";

    // Para obter os lanches:
    // GET http://localhost:8080/api/lanche
    public static final String SANDWICH_LIST = "/api/lanche";

    // Para obter os ingredientes do lanche, e calcular seu preço:
    // GET http://localhost:8080/api/ingrediente/de/<id_lanche>
    public static final String SANDWICH_INGREDIENTS = "/api/ingrediente/de/";

    // Para obter dados de um lanche:
    // GET http://localhost:8080/api/lanche/<id_lanche>
    public static final String SANDWICH_INFO = "/api/lanche/";

    // Para obter todos os ingredientes:
    // GET http://localhost:8080/api/ingrediente
    public static final String INGREDIENTS_LIST = "/api/ingrediente";

    // Para adicionar um lanche ao carrinho de compras:
    // PUT http://localhost:8080/api/pedido/<id_lanche>
    public static final String ADD_SANDWICH = "/api/pedido/";

    // Para adicionar um lanche personalizado carrinho de compras:
    // PUT  http://localhost:8080/api/pedido/<id_lanche> e, no corpo
    // da requisição, um único parâmetro extras, que é um JsonArray
    // com ids dos ingredientes extras (extras=[1,2,3], por exemplo)
    public static final String ADD_CUSTOM_SANDWICH = "/api/pedido/";

    // Para obter as promoções:
    // http://localhost:8080/api/promocao
    public static final String OFFERS = "/api/promocao";

    // Para obter os itens pedidos:
    // http://localhost:8080/api/pedido
    public static final String ITEMS_ORDER = "/api/pedido";
}
