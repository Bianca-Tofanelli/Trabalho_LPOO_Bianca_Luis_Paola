package lpoo;

import lpoo.phyx.Scene;
import lpoo.util.SceneReader;
import lpoo.util.SceneReport;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Erro: Forneça o nome do arquivo de cena como argumento.");
            System.out.println("Uso: java lpoo.Main <caminho_do_arquivo.txt>");
            return;
        }
        String arquivoEntrada = args[0];
        try {
            Scene cena = SceneReader.readScene(arquivoEntrada);
            String arquivoSaida = cena.getName() + "_relatorio.txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(arquivoSaida))) {
                // Passa a lista de atores e o escritor para o seu método write
                SceneReport.write(cena.getActors(), writer);
            }
            System.out.println("Sucesso! Relatório gerado em: " + arquivoSaida);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro durante a execução:");
            e.printStackTrace();
        }
    }
}
