import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScenarioManager scenarioManager = new ScenarioManager("scenarios");

        // Запуск квеста
        scenarioManager.startScenario(scanner);
    }
}