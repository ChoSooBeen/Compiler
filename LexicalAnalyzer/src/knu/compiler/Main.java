package knu.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        String inputText = "";
        List<String> memory = new ArrayList<>();

        File file = new File("tests/samples/program3.decaf");
        Scanner scanner = new Scanner(file);

        inputText = scanner.nextLine();
        while (scanner.hasNextLine()) {
            inputText = inputText + "\n" + scanner.nextLine();
        }

        int lineNum = 1;
        int colNum = 0;

        char[] string = inputText.toCharArray();

        for (int i = 0; i < string.length; i++) {
            boolean unknown_char = true;
            colNum++;

            if (string[i] >= '0' && string[i] <= '9') {
                String temp = "";
                int decimal = 0;
                double f = 0.0;

                while (i < string.length && string[i] >= '0' && string[i] <= '9') {
                    temp += string[i];
                    i++;
                    colNum++;
                }
                if(temp.equals("0") && (string[i] == 'x'|| string[i] == 'X')) { //16진수
                    temp += string[i];
                    i++;
                    colNum++;
                    while(i < string.length && ((string[i] >= '0' && string[i] <= '9')
                            || (string[i] >= 'a' && string[i] <= 'f')
                            || (string[i] >= 'A' && string[i] <= 'F'))){
                        temp += string[i];
                        i++;
                        colNum++;
                    }
                    String temp2 = temp.substring(2);
                    decimal = Integer.parseInt(temp2,16);
                }

                // How to deal with floating-point numbers?
                if(string[i] == '.') {
                    temp += string[i];
                    i++;
                    while(i < string.length && string[i] >= '0' && string[i] <= '9') {
                        temp += string[i];
                        i++;
                        colNum++;
                    }
                    f = Double.parseDouble(temp);
                    if (string[i] == 'e' || string[i] == 'E') {
                        temp += string[i];
                        temp += string[i+1];
                        char plus_minus = string[i+1];
                        String temp2 ="";
                        i = i + 2;
                        colNum = colNum + 2;
                        while(i < string.length && string[i] >= '0' && string[i] <= '9') {
                            temp += string[i];
                            temp2 += string[i];
                            i++;
                            colNum++;
                        }
                        int temp3 = Integer.parseInt(temp2);
                        if (plus_minus == '+') {
                            f = f * Math.pow(10, temp3);
                        }
                        else {
                            f = f * Math.pow(10, -temp3);
                        }
                    }
                }
                if(decimal != 0) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_IntConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, decimal));
                }
                else if(f != 0.0) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_DoubleConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, f));
                }
                else
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_IntConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp));

                unknown_char = false;
            }

            if ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_' || (string[i] >= 'a' && string[i] <= 'z')) {
                String temp = "";
                temp += string[i];

                i++;
                colNum++;
                while (i < string.length && ((string[i] >= 'A' && string[i] <= 'Z') || string[i] == '_'
                        || (string[i] >= 'a' && string[i] <= 'z')
                        || (string[i] >= '0' && string[i] <= '9'))) {
                    temp += string[i];
                    i++;
                    colNum++;
                }

                // 식별자, 키워드 구분
                if (temp.equals("if") || temp.equals("else") || temp.equals("for") || temp.equals("while") || temp.equals("void") ||
                        temp.equals("class") || temp.equals("extends") || temp.equals("implements") || temp.equals("interface") ||
                        temp.equals("Print") || temp.equals("break") || temp.equals("return") || temp.equals("this") ||
                        temp.equals("new") || temp.equals("ReadInteger") || temp.equals("ReadLine") || temp.equals("NewArray") ||
                        temp.equals("int") || temp.equals("double") || temp.equals("bool") || temp.equals("string") ||
                        temp.equals("id") || temp.equals("null")){
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_%5$s", temp, lineNum, colNum - temp.length(), colNum - 1, temp.substring(0, 1).toUpperCase() + temp.substring(1)));
                } else if (temp.equals("true") || temp.equals("false")) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_BoolConstant (token value: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, temp));
                }
                else {  // 키워드(타입), 식별자
                    // How to deal with identifiers?
                    int t = 0;
                    if(temp.length() < 32 && ((temp.charAt(t) >= 'A' && temp.charAt(t) <= 'Z')
                        || (temp.charAt(t) >= 'a' && temp.charAt(t) <= 'z')
                        || temp.charAt(t) == '_')) {
                        t++;
                        if(temp.length() != 1) {
                            while ((temp.charAt(t) >= 'A' && temp.charAt(t) <= 'Z')
                                    || (temp.charAt(t) >= 'a' && temp.charAt(t) <= 'z')
                                    || temp.charAt(t) == '_'
                                    || (temp.charAt(t) >= '0' && temp.charAt(t) <= '9')) {
                                t++;
                                if (t == temp.length())
                                    break;
                            }
                        }
                        if(!memory.contains(temp)) {
                            memory.add(temp);
                        }
                        System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_Identifier (token address: %5$s)", temp, lineNum, colNum - temp.length(), colNum - 1, memory.indexOf(temp)+1));
                    }
                }

                if (i >= string.length) {
                    break;
                } else {
                    i--;
                    colNum--;
                }
                unknown_char = false;
            }

            if (string[i] == '"') {
                String temp = "\"";
                i++;
                colNum++;
                boolean is_terminated = true;

                while (i < string.length && string[i] != '"') {
                    if (string[i] == '\n') {
                        System.out.println(String.format("\n*** Error line %d.", lineNum));
                        System.out.println(String.format("*** Unterminated string constant: %s\n", temp));
                        is_terminated = false;
                        break;
                    }
                    temp += string[i];
                    i++;
                    colNum++;
                }

                if (i < string.length &&  string[i] == '"') {
                    temp += string[i];
                }

                if (is_terminated) {
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is T_StringConstant (token value: %5$s)", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                }

                if (i >= string.length) {
                    break;
                }

                unknown_char = false;
            }

            if (string[i] == '+' || string[i] == '-' || string[i] == '*' || string[i] == '%' || string[i] == ';' || string[i] == '('
                    || string[i] == ')' || string[i] == '{' || string[i] == '}' || string[i] == ',' || string[i] == '.' || string[i] == '[' || string[i] == ']') {
                String temp = "" + string[i];
                System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum, colNum + temp.length() - 1, temp));
                unknown_char = false;
            }

            if (string[i] == '=' || string[i] == '!' || string[i] == '<' || string[i] == '>') {
                String temp = "" + string[i];
                if (string[i+1] == '=') {
                    temp += string[i+1];
                    i++;
                    colNum++;
                }
                System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                unknown_char = false;
            }

            if (string[i] == '&') {
                String temp = "" + string[i];
                if (string[i+1] == '&') {
                    temp += string[i + 1];
                    i++;
                    colNum++;
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum - temp.length() + 1, colNum, temp));
                    unknown_char = false;
                }
            }

            if (string[i] == '|') {
                String temp = "" + string[i];
                if (string[i+1] == '|') {
                    temp += string[i+1];
                    i++;
                    colNum++;
                    System.out.println(String.format("%1$-14s line %2$d cols %3$d-%4$d is '%5$s'", temp, lineNum, colNum, colNum + temp.length() - 1, temp));
                    unknown_char = false;
                }
            }

            // How to deal with single-line and multi-line comments?
            if(string[i] == '/') {
                i++;
                if(string[i] == '/') {
                    i++;
                    while(string[i] != '\n') {
                        i++;
                    }
                }
                else if(string[i] == '*') {
                    i++;
                    while(string[i] != '*') {
                        i++;
                    }
                    while (string[i] != '*' && string[i] != '/') {
                        i++;
                    }
                    while (string[i] != '/') {
                        i++;
                    }
                    while (string[i] != '\n') {
                        i++;
                    }
                }
            }

            if (string[i] == '\n') {
                colNum = 0;
                lineNum++;
            } else {
                if (unknown_char && string[i] != ' ') {
                    System.out.println(String.format("\n*** Error line %d.", lineNum));
                    System.out.println(String.format("*** Unrecognized char: '%c'\n", string[i]));
                }
            }
        }
    }
}
