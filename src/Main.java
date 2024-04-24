// bibliotes gson
import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();


        String endereco = "https://v6.exchangerate-api.com/v6/ba65798fa99dc0cbe314d619/latest/USD";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

        // double taxaUSD = jsonObject.getAsJsonObject("conversion_rates").get("USD").getAsDouble();

        System.out.println("""
                Bem vindo a esta aplicação de converter moeda
                por favor, siga todas as intruções ditas para que não haja problemas.
                
                para finalizar, digite "sair" a qualquer momento
                """);

        List<Map<String, Object>> conversoes = new ArrayList<>();


        while(true){


            try {
                System.out.println("""
                        \n
                        digite a sigla da moeda que gostaria de converter:
                        Ex.: para converter dólar, digite USD.
                                            
                        Dólar EUA (USD);
                        Euro (EUR);
                        Real (BRL);
                        peso argentino (ARS);
                        Libra Esterlina (GBP).""");
                String sigla1 = sc.nextLine();
                if (sigla1.equalsIgnoreCase("sair")) {
                    break;
                }

                double moeda1 = jsonObject.getAsJsonObject("conversion_rates").get(sigla1).getAsDouble();

                System.out.println("""
                        agora digite a sigla da moeda para qual moeda deve ser convertida:
                                            
                        Dólar EUA (USD);
                        Euro (EUR);
                        Real (BRL);
                        peso argentino (ARS);
                        Libra Esterlina (GBP).""");
                String sigla2 = sc.nextLine();
                if (sigla2.equalsIgnoreCase("sair")) {
                    break;
                }

                double moeda2 = jsonObject.getAsJsonObject("conversion_rates").get(sigla2).getAsDouble();

                System.out.println("Digite o valor a ser convertido");

                double valor = sc.nextDouble();
                converter(valor, moeda1, moeda2, sigla1, sigla2);
                LocalDateTime agora = LocalDateTime.now();
                String data = agora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                Map<String, Object> conversao = new HashMap<>();
                conversao.put("data", data);
                conversao.put("moeda_origem", sigla1);
                conversao.put("valor_origem", valor);
                conversao.put("moeda_destino", sigla2);
                conversao.put("valor_destino", Math.round(valor * (moeda2 / moeda1) * 100.0) / 100.0);

                conversoes.add(conversao);

            }catch (NullPointerException e){
                System.out.println("""
                        Erro: você digitou algo errado ou inválido;
                        
                        Identifique o problema e possíveis soluções:
                        
                        -> moeda não reconhecida:
                        certifique que a digitou em letra maiuscula. 
                        Ex.: 
                        certo: "USD"
                        errado: "usd"
                        
                        se não resolver, a moeda pode não estar no sistema.
                        
                        -> valor inválido:
                        certifique de usar "," e "." corretamente.
                        Ex.: 
                        certo: 5,50
                        errado: 5.50
                        
                        certo: 1.000,50
                        errado: 1,000.50
                        
                        certifique tambem de não utilizar letras ou quaisquer outro caractere.
                        
                        
                        """);
            }
            sc.nextLine();
        }
        File diretorio = new File("C:\\Users\\ffgus\\Desktop\\Conversor de moedas\\src\\historico");
        File arquivo = new File(diretorio, "conversoes.txt");
        FileWriter escrita = new FileWriter(arquivo, true);
        escrita.write("\n");
        escrita.write(gson.toJson(conversoes));
        escrita.close();
        System.out.println("Conversões adicionais salvas no arquivo.");
        sc.close();

    }
        public static void converter(double valor, double moeda1, double moeda2, String sigla1, String sigla2){
            System.out.println(valor + " em " + sigla1 + " equivale a " + (valor * (moeda2 / moeda1)) + " em " + sigla2);
        }
}
