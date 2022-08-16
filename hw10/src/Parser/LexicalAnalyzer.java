package Parser;

import java.util.*;

public class LexicalAnalyzer {

    public static ArrayList<Pair<String, String>> lex(String inputText){

        ArrayList<Pair<String, String>> token_list = new ArrayList<>();
        List<String> memory = new ArrayList<>();

        char[] string = inputText.toCharArray();

        for (int i = 0; i < string.length; i++) {
            boolean unknown_char = true;

            if ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_' || (string[i] >= 'a' && string[i] <= 'z')) {
                String temp = "";
                temp += string[i];

                i++;
                while (i < string.length && ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_'
                        || (string[i] >= 'a' && string[i] <= 'z')
                        || (string[i] >= '0' && string[i] <= '9'))) {
                    temp += string[i];
                    i++;
                }

                // 식별자, 키워드 구분
                if (temp.equals("true") || temp.equals("false")) {
                    // System.out.println("Boolean : " + temp + ", Token num : 5, Token value : " + temp);
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("if")) {
                    // System.out.println("Keyword : " + temp + ", Token num : 6");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("else")) {
                    // System.out.println("Keyword : " + temp + ", Token num : 7");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("for")) {
                    // System.out.println("Keyword : " + temp + ", Token num : 8");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("while")) {
                    // System.out.println("Keyword : " + temp + ", Token num : 9");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("void")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 10");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("class")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 11");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("extends")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 12");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("implements")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 13");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("interface")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 14");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("Print")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 15");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("break")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 16");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("return")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 17");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("this")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 18");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("new")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 19");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("ReadInteger")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 20");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("ReadLine")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 21");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("NewArray")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 22");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("int")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 23");
                    token_list.add(new Pair<>("Type", temp));
                }
                else if (temp.equals("double")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 24");
                    token_list.add(new Pair<>("Type", temp));
                }
                else if (temp.equals("string")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 25");
                    token_list.add(new Pair<>("Type", temp));
                }
                else if (temp.equals("bool")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 26");
                    token_list.add(new Pair<>("Type", temp));
                }
                else if (temp.equals("id")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 27");
                    token_list.add(new Pair<>("id", ""));
                }
                else if (temp.equals("intConstant")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 28");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("doubleConstant")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 29");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("boolConstant")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 30");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("stringConstant")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 31");
                    token_list.add(new Pair<>(temp, ""));
                }
                else if (temp.equals("null")) {
                    //System.out.println("Keyword : " + temp + ", Token num : 32");
                    token_list.add(new Pair<>(temp, ""));
                }
                else {  // 키워드(타입), 식별자
                    if (!memory.contains(temp)) {
                        memory.add(temp);   // 새로운 식별자는 메모리에 저장
                    }
                    // 식별자의 메모리 주소 판별 후 출력
                    //System.out.println("Identifier : " + temp + ", Token num : 1, Token value: " + (memory.indexOf(temp) + 1));
                    token_list.add(new Pair<>("id", temp));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == '"') {
                String temp = "";
                i++;
                while (i < string.length && string[i] != '"') {
                    if ((int) string[i] == 10) {
                        //System.out.println("Error : illegal newline in string constant \"" + temp);
                        System.exit(0);
                    }
                    else if (i == string.length - 1) {
                        //System.out.println("Error : unterminated string constant \""+ temp + string[i]);
                        System.exit(0);
                    }
                    else {
                        temp += string[i];
                    }
                    i++;
                }
                // System.out.println("String : " + temp + " Token num : 4 , Token value: " + temp);
                token_list.add(new Pair<>("Constant", temp));
                unknown_char = false;
            }

            if (i < string.length && (string[i] >= '0' && string[i] <= '9')) {
                String temp = "";

                while (i < string.length && (string[i] >= '0' && string[i] <= '9')) {
                    temp += string[i];
                    i++;
                }

                if (i < string.length && string[i] == '.') {
                    // System.out.print("Double : ");
                    temp += string[i];

                    i++;
                    while (i < string.length && (string[i] >= '0' && string[i] <= '9')) {
                        temp += string[i];
                        i++;
                    }

                    // System.out.print(temp);
                    // System.out.println(", Token num : 3 , Token value: " + temp);
                    token_list.add(new Pair<>("Constant", temp));
                }
                else {
                    // System.out.print("Integer : ");
                    // System.out.print(temp);
                    // System.out.println(", Token num : 2 , Token value: " + temp);
                    token_list.add(new Pair<>("Constant", temp));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == '+') {
                // System.out.print("Operator : +, ");
                // System.out.println("Token num : 33 ");
                token_list.add(new Pair<>("+", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '-') {
                //System.out.print("Operator : -, ");
                //System.out.println("Token num : 34 ");
                token_list.add(new Pair<>("-", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '*') {
                //System.out.print("Operator : *, ");
                //System.out.println("Token num : 35 ");
                token_list.add(new Pair<>("*", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '%') {
                //System.out.print("Operator : %, ");
                //System.out.println("Token num : 37 ");
                token_list.add(new Pair<>("%", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '=') {
                if (string[i+1] == '=') {
                    i++;
                    //System.out.print("Operator : ==, ");
                    //System.out.println("Token num : 38 ");
                    token_list.add(new Pair<>("==", ""));
                }
                else{
                    //System.out.print("Operator : =, ");
                    //System.out.println("Token num : 39 ");
                    token_list.add(new Pair<>("=", ""));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == '!') {
                if (string[i+1] == '=') {
                    i++;
                    //System.out.print("Operator : !=, ");
                    //System.out.println("Token num : 40 ");
                    token_list.add(new Pair<>("!=", ""));
                }
                else{
                    //System.out.print("Operator : !, ");
                    //System.out.println("Token num : 41 ");
                    token_list.add(new Pair<>("!", ""));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == ';') {
                //System.out.print("Delimiter : ;, ");
                //System.out.println("Token num : 42 ");
                token_list.add(new Pair<>(";", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '(') {
                //System.out.print("Delimiter : (, ");
                //System.out.println("Token num : 43 ");
                token_list.add(new Pair<>("(", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == ')') {
                //System.out.print("Delimiter : ), ");
                //System.out.println("Token num : 44 ");
                token_list.add(new Pair<>(")", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '{') {
                //System.out.print("Delimiter : {, ");
                //System.out.println("Token num : 45 ");
                token_list.add(new Pair<>("{", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '}') {
                //System.out.print("Delimiter : }, ");
                //System.out.println("Token num : 46 ");
                token_list.add(new Pair<>("}", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '<') {
                if (i+1 < string.length && string[i+1] == '=') {
                    i++;
                    //System.out.print("Operator : <=, ");
                    //System.out.println("Token num : 47 ");
                    token_list.add(new Pair<>("<=", ""));
                }
                else{
                    //System.out.print("Operator : <, ");
                    //System.out.println("Token num : 48 ");
                    token_list.add(new Pair<>("<", ""));
                }
                unknown_char = false;
            }

            if (i < string.length &&string[i] == '>') {
                if (i+1 < string.length && string[i+1] == '=') {
                    i++;
                    //System.out.print("Operator : >=, ");
                    //System.out.println("Token num : 49 ");
                    token_list.add(new Pair<>(">=", ""));
                }
                else{
                    //System.out.print("Operator : >, ");
                    //System.out.println("Token num : 50 ");
                    token_list.add(new Pair<>(">", ""));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == '&') {
                if (i+1 < string.length && string[i+1] == '&') {
                    i++;
                    //System.out.print("Operator : &&, ");
                    //System.out.println("Token num : 51 ");
                    token_list.add(new Pair<>("&&", ""));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == '|') {
                if (i+1 < string.length && string[i+1] == '|') {
                    i++;
                    //System.out.print("Operator : ||, ");
                    //System.out.println("Token num : 52 ");
                    token_list.add(new Pair<>("||", ""));
                }
                unknown_char = false;
            }

            if (i < string.length && string[i] == ',') {
                //System.out.print("Delimiter : ,, ");
                //System.out.println("Token num : 53 ");
                token_list.add(new Pair<>(",", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '.') {
                //System.out.print("Delimiter : ., ");
                //System.out.println("Token num : 54 ");
                token_list.add(new Pair<>(".", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == '[') {
                //System.out.print("Delimiter : [, ");
                //System.out.println("Token num : 55 ");
                token_list.add(new Pair<>("[", ""));
                unknown_char = false;
            }

            if (i < string.length && string[i] == ']') {
                //System.out.print("Delimiter : ], ");
                //System.out.println("Token num : 56 ");
                token_list.add(new Pair<>("]", ""));
                unknown_char = false;
            }



            // 주석과 '/' 오퍼레이터 구분
            if (i < string.length && string[i] == '/') {
                if (i+1 < string.length && string[i + 1] == '*') {
                    i = i + 2;
                    while (i < string.length - 1) {
                        if (string[i] == '*' && string[i + 1] == '/') {
                            i++;
                            break;
                        } else {
                            i++;
                        }
                    }
                } else if (i + 1 < string.length && string[i + 1] == '/') {
                    i = i + 2;
                    while (i < string.length - 1) {
                        if ((int) string[i] == 10) {    // 개행문자
                            break;
                        } else {
                            i++;
                        }
                    }
                } else {
                    //System.out.print("Operator : /, ");
                    //System.out.println("Token num : 36 ");
                    token_list.add(new Pair<>("/", ""));
                    unknown_char = false;
                }
            }

            if (i < string.length && unknown_char && string[i] != ' ' && (int) string[i] != 10) {
                //System.out.println("Unknown character : " + string[i]);
            }
        }

        return token_list;
    }
}
