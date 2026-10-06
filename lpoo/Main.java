package lpoo;

import lpoo.phyx.Scene;
import lpoo.util.SceneReader;
import lpoo.util.SceneReport;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
// Classe principal que gerencia a execução a partir da linha de comando
public class Main {
    public static void main(String[] args) {
        // vê se o caminho do arquivo foi fornecido
        if (args.length == 0) {
            System.out.println("Error: Provide the scene file name as an argument");
            System.out.println("Use: java lpoo.Main <caminho_do_arquivo.txt>");
            return;
        }

        String arquivoEntrada = args[0];

        try {
            // Lê  o arquivo de texto
            Scene cena = SceneReader.readScene(arquivoEntrada);
            String arquivoSaida = cena.getName() + "_relatorio.txt";

            // cria o arquivo de saída e aciona para gerar o relatório
            try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoSaida))) {
                // Passa a lista de atores e o escritor para o seu método write
                SceneReport.write(cena.getActors(), writer);
            }

            System.out.println("Success! Report generated on: " + arquivoSaida);

        } catch (Exception e) {
            // exibe se houver algum erro
            System.out.println("An error occurred during execution:");
            e.printStackTrace();
        }

    }
} // Main
