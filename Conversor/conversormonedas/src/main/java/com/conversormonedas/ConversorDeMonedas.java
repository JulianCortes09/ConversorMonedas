package com.conversormonedas;
    
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class ConversorDeMonedas {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese la moneda de origen (Ej: USD, EUR, COP)");
        String monedaOrigen = scanner.nextLine();

        System.out.println("Ingrese la moneda de destino (Ej: USD, EUR, COP)");
        String monedaDestino = scanner.nextLine();

        System.out.println("Ingrese la cantidad de dinero a convertir");
        double cantidad = scanner.nextDouble();

        String apiKey = "108286abd017b9bcd414b301";
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + monedaOrigen + "/" + monedaDestino + "/" + cantidad;

        URL obj = URI.create(url).toURL();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine())!= null) {
                response.append(line);
            }
            reader.close();

            String responseBody = response.toString();
            double resultado = obtenerTasaDeCambio(responseBody);
            System.out.println("El resultado de la conversión es: " + resultado + " " + monedaDestino);
        } else {
            System.out.println("Error: " + responseCode);
        }
    }

    private static double obtenerTasaDeCambio(String responseBody) {
        String[] partes = responseBody.split(",");
        for (String parte : partes) {
            if (parte.contains("conversion_result")) {
                String[] valores = parte.split(":");
                String valor = valores[1].trim();
                valor = valor.replace("}", "");
                return Double.parseDouble(valor);
            }
        }
        return 0.0;
    }
}