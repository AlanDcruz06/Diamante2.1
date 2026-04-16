import java.util.*;

public class SofaScore {

    static class Time{
        String nome;
        int pontos, vitorias, empates, derrotas, golsPro, golsContra;
        public Time(String nome) {
            this.nome = nome;
        }
        int saldoGols() {
            return golsPro - golsContra;
        }
        String categoria() {
            if (pontos >= 20) return "LIDER";
            if (pontos >= 14) return "G4 - Zona de classificação";
            if (pontos >= 8)  return "MEIO DA TABELA";
            if (pontos >= 4)  return "ALERTA";
            return "REBAIXAMENTO";
        }

    }

    public static String[] parsearPartida(String linha) {

        String[] partes = linha.split(":");

        if (partes.length != 4) {

            System.out.println("Linha invalida:" + linha);
            return null;
        }

        for (int i = 0; i < partes.length; i++) {

            partes[i] = partes[i].trim().toUpperCase();
        }

        return partes;
    }

    public static void main(String[] args) {

        System.out.println("Campeonato 2026");

        String[] partidas = {

                "Flamengo:3:1:Palmeiras",
                "Corinthians:0:0:São Paulo",
                "Atletico-MG:2:4:Santos",
                "Palmeiras:1:0:Corinthians",
                "São Paulo:3:2:Flamengo",
                "Santos:4:1:Atletico-MG",
                "Flamengo:2:0:Corinthians",
                "Palmeiras:1:4:Santos",
                "São Paulo:0:0:Atletico-MG",
                "Corinthians:1:4:Santos",
                "Atletico-MG:0:2:Flamengo",
                "Santos:4:1:São Paulo",
                "Erros: "
        };

        Map<String, Time> tabela = new HashMap<>();

        int i = 0;

        while (i < partidas.length) {

            String[] dados = parsearPartida(partidas[i]);

            if (dados != null) {

                String casa = dados[0];
                int golsCasa = Integer.parseInt(dados[1]);
                int golsFora = Integer.parseInt(dados[2]);
                String fora = dados[3];

                tabela.putIfAbsent(casa, new Time(casa));
                tabela.putIfAbsent(fora, new Time(fora));

                Time timeCasa = tabela.get(casa);
                Time timeFora = tabela.get(fora);

                timeCasa.golsPro += golsCasa;
                timeCasa.golsContra += golsFora;

                timeFora.golsPro += golsFora;
                timeFora.golsContra += golsCasa;

                if (golsCasa > golsFora) {

                    timeCasa.pontos += 3;
                    timeCasa.vitorias++;

                    timeFora.derrotas++;

                } else if (golsCasa < golsFora) {

                    timeFora.pontos += 3;
                    timeFora.vitorias++;

                    timeCasa.derrotas++;

                } else {

                    timeCasa.pontos += 1;
                    timeFora.pontos += 1;

                    timeCasa.empates++;
                    timeFora.empates++;
                }
            }

            i++;
        }

List <Time> listaTimes = new ArrayList<>(tabela.values());

        System.out.println("Time com mais gols:");

        listaTimes.stream()
                .max(Comparator.comparingInt(t -> t.golsPro))
                .ifPresent(t -> System.out.println(t.nome));

        double mediaGols = listaTimes.stream()
                .mapToInt(t -> t.golsPro)
                .sum() / (double) partidas.length;

        System.out.println("Media de gols por partida:");
        System.out.println(mediaGols);

        System.out.println("Times em rebaixamento:");

        listaTimes.stream()
                .filter(t -> t.pontos < 4)
                .forEach(t -> System.out.println(t.nome));

        System.out.println("CAMPEONATO BRASILEIRO 2026");

        System.out.println(
                String.format(
                        "%-3s | %-15s | %-3s | %-2s | %-2s | %-2s | %-3s | %-15s ",
                        "POS: ",
                        "TIME: ",
                        "PTS: ",
                        "V: ",
                        "E: ",
                        "D: ",
                        "SG: ",
                        "CATEGORIA: "
        )
        );

        final int[] posicao = {1};

        listaTimes.stream()
                .sorted(Comparator.comparingInt((Time t) -> t.pontos).reversed())
                .forEach(t -> {

                    String saldo;

                    if (t.saldoGols() > 0)
                        saldo = "+" + t.saldoGols();
                    else
                        saldo = "" + t.saldoGols();

                    System.out.println(
                            String.format(
                                    "%-3d | %-15s | %-3d | %-2d | %-2d | %-2d | %-3s | %-15s",
                                    posicao[0]++,
                                    t.nome,
                                    t.pontos,
                                    t.vitorias,
                                    t.empates,
                                    t.derrotas,
                                    saldo,
                                    t.categoria()
                            )
                    );
                });

    }
}