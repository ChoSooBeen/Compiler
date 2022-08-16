package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./samples/badop.decaf");
        Scanner scanner = new Scanner(file);
        String inputText = scanner.nextLine();

        ArrayList<Pair<String, String>> symbolList = LexicalAnalyzer.lex(inputText);

        LLParser.parse(symbolList);
    }
}
