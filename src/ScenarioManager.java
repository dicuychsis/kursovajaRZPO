import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ScenarioManager {
    private String scenarioFolder;
    public ScenarioManager(String scenarioFolder) {
        this.scenarioFolder = scenarioFolder;
    }

    public void startScenario(Scanner scanner) {
        // Получение списка доступных сценариев
        Map<String, String> scenarios = getScenarios();

        // Выбор сценария пользователем
        System.out.println("Доступные сценарии:");
        for (String scenario : scenarios.keySet()) {
            System.out.println(scenario);
        }
        System.out.print("Выберите сценарий: ");
        String selectedScenario = scanner.nextLine();

        // Запуск выбранного сценария
        if (scenarios.containsKey(selectedScenario)) {
            runScenario(scenarios.get(selectedScenario), scanner);
        } else {
            System.out.println("Такого сценария не существует.");
        }
    }

    private Map<String, String> getScenarios() {
        Map<String, String> scenarios = new HashMap<>();
        File folder = new File(scenarioFolder);

        // Проверка наличия папки сценариев
        if (folder.exists() && folder.isDirectory()) {
            File[] scenarioFiles = folder.listFiles();

            // Сканирование папки сценариев
            if (scenarioFiles != null) {
                for (File file : scenarioFiles) {
                    if (file.isDirectory()) {
                        scenarios.put(file.getName(), file.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("Отсутствует папка 'scenarios'.");
        }

        return scenarios;
    }

    private void runScenario(String scenarioPath, Scanner scanner) {

        // Реализация выполнения сценария по заданному пути
        runBranch(scenarioPath, "start", scanner);

    }

    private void runBranch(String scenarioPath, String branchName, Scanner scanner) {
        try {
            String branchPath = Paths.get(scenarioPath, branchName + ".txt").toString();
            String choicesPath = Paths.get(scenarioPath, "choices.txt").toString();
            String content = Files.readString(Paths.get(branchPath));
            String choices = Files.readString(Paths.get(choicesPath));

            // Вывод текста сценария
            System.out.println(content);

            // Обработка вариантов выбора
            String[] contentLines = content.split("\n");
            int choicesStartIndex = -1;

            for (int i = 0; i < contentLines.length; i++) {
                if (contentLines[i].startsWith("Выберите действие:")) {
                    choicesStartIndex = i + 1;
                    break;
                }
            }

            if (choicesStartIndex != -1) {

                System.out.print("Ваш выбор: ");
                int choice = scanner.nextInt();

                String strChoice = Integer.toString(choice);

                String newBranchName = "";
                
                String[] choicesLines = choices.split("\n");
                for (int i = 0; i < choicesLines.length; i++) {
                    String[] choicesLine = choicesLines[i].split(":");
                    if (choicesLine[0].contains(branchName) && choicesLine[1].contains(strChoice)) {
                        newBranchName = choicesLine[2];
                        var newWord = new StringBuilder(newBranchName)
                                .deleteCharAt(newBranchName.length() - 1)
                                .toString();
                        newBranchName = newWord;
                    }
                }

                // Выполнение следующей ветки сценария
                runBranch(scenarioPath, newBranchName, scanner);

            } else {
                System.out.println("Сценарий завершен.");
            }

        } catch (Exception e) {
            System.out.println("Ошибка выполнения сценария: " + e.getMessage());
        }
    }
}