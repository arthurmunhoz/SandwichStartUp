####################################
## Relatório de entra             ##
## Autor: Arthur Regatieri Munhoz ##
## Data: 07/06/2018               ##
####################################

# Nome do app: SandwichStartUp

# Aplcaitivo desenvolvido em Java
# Arquitetura utilizada MVP
# Biblioteca utilizada para requisições ao servidor: OkHttp3
# Bibliote utilizada para carregamento de imagens: Picasso

# Funcionalidades a serem implementadas no futuro:
# - Clique longo em itens do carrinho de compras para deletá-los.
# - Login de usuários cadastrados
# - Implementação do pagamento
# - Melhorias na interface

# Justificativa para escolha do design de código:
# 
# A aplicação foi separada em diferentes packages, cada um com um propósito:
# 
# - adapters: contém os controladores das "recyclerviews";
# 
# - model: contém as classes dos objetos utilizados;
# 
# - activities: contém todos as activities, que fazem a conexão entre os métodos de processamentos de dados e a interface (UI);
# 
# - utils: contém as classes auxiliares à aplicação
#       - Callbacks: interface utilizada para tratar retorno das chamadas ao servidor;
#       - Utils: contém os métodos utilizados para as requisições ao servidor e funções auxiliares;
#       - Endpoint: contém as Strings utilizadas nas chamadas ao servidor
#
# - res/layouts: contém todos os layouts utilizados pelas activities
# - res/drawable: contém todas as imagens utilizadas pelas activities e layout customizado para botões
#
# Desta forma a aplicação fica desacoplada e de fácil manutenção e implementação de melhorias.

################################################################################################################################
#   OBS:                                                                                                                       #
#                                                                                                                              #
#   Em "Constants", a String [BASE_URL = "http://192.168.0.17:8080"] indica o IP e Porta nos quais o servidor está rodando.    #
#   É necessário também configurar o proxy no smarthphone para o endereço especificado em BASE_URL                             #
#                                                                                                                              #
################################################################################################################################
