package Parser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LLParser {
    private static String nextSymbol;
    private static int index = 0;
    private static ArrayList<Pair<String, String>> symbolList;
    private static HashMap<String, Integer> symbolMap;
    private static String dotFormat = "";
    private static ArrayList<String> valueList;

    public static void createDotGraph(String fileName)
    {
        for (String v : valueList) {
            if (v.charAt(0) > 'a' && v.charAt(0) < 'z') {
                if (v.length() > 2 && v.startsWith("eps"))
                    dotFormat += v + "[color=red];";
                else
                    dotFormat += v + "[color=green];";
            }
        }

        GraphViz gv=new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        String type = "png";
        gv.decreaseDpi();
        gv.decreaseDpi();

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString() + "/src/Parser/" + fileName + "."+ type;
        File out = new File(s);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }

    /**
     * 부모 노드의 번호, 부모노드 이름, 자식노드 이름을 입력해 그래프에 간선을 하나 추가하는 메소드
     * @param parentNum 부모노드의 번호
     * @param parentNode 부모노드의 이름
     * @param childNode 자식노드의 이름
     * @param childValue 자식노드가 id, Constant, Type인 경우 Value 입력, 그렇지 않을 땐 빈 문자열 "" 입력
     */
    static void addConnection(int parentNum, String parentNode, String childNode, String childValue) {
        symbolMap.put(childNode, symbolMap.get(childNode) + 1);
        String valueNode = "";

        if (childValue.equals(""))
            valueNode = childNode + symbolMap.get(childNode);
        else
            valueNode = childNode + symbolMap.get(childNode) + "_" + childValue;

        dotFormat += parentNode + parentNum + "->" + valueNode + ";";
        valueList.add(valueNode);
    }

    static void parse(ArrayList<Pair<String, String>> tokenList) {
        symbolList = tokenList;
        symbolList.add(new Pair<>("$", ""));
        nextSymbol = symbolList.get(index).getKey();
        valueList = new ArrayList<>();

        symbolMap = new HashMap<>();

        /**
         * GraphViz로 제작된 그래프에는 노드의 이름에 특수문자가 포함될 수 없습니다.
         * 따라서 각 터미널 기호들은 GraphViz로 출력시 다음과 같은 이름을 사용하도록 코드를 작성해주세요.
         *
         * || or / && and / == eq / != neq / < ll / <= le / > gg / >= ge /
         * + sum / - sub / * mul / '/' div / % mod / ! not / ( lP / ) rP /
         * Constant con / this ths / ReadInteger rdI / ReadLine rdL / new new /
         * id id / newArray nwA / , com / Type typ / epsilon eps
         *
         * 논터미널 기호의 이름은 첫 글자가 알파벳 대문자인 영문을 사용하도록 코드를 작성해주세요.
         */
        String[] symbolArray = new String[]{"E", "Ep", "F", "Fp", "W", "Wp", "V", "Vp", "U", "Up", "T", "Tp", "K",
                "or", "and", "eq", "neq", "ll", "le", "gg", "ge", "sum", "sub", "mul", "div", "mod", "not", "lP", "rP",
                "con", "ths", "rdI", "rdL", "new", "id", "nwA", "typ", "com", "eps"};

        /**
         * 같은 이름의 노드가 새로 생성될 시 번호를 붙여 구분하기 위한 symbolMap 생성.
         * Key : 노드 이름 (터미널, 논터미널 기호)
         * Value : 0부터 시작, 같은 기호의 노드가 하나씩 생성될 때마다 +1
         */
        for (String symbol : symbolArray) {
            symbolMap.put(symbol, 0);
        }

        symbolMap.put("E", 1);
        PE(symbolMap.get("E"));

        if(nextSymbol.equals("$")) {
            System.out.println("Accept");
            createDotGraph("parsetree");
        } else {
            error("$");
        }
    }

    /**
     * 논터미널 기호 E
     */
    static void PE(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"E", "F", "");
            PF(symbolMap.get("F"));
            addConnection(currentNodeNum,"E", "Ep", "");
            PE_prime(symbolMap.get("Ep"));
        }
        else {
            error("FIRST set of Non-terminal \'E\'");
        }
    }

    /**
     * 논터미널 기호 E'
     */
    static void PE_prime(int currentNodeNum) {
        if (nextSymbol.equals("||")) {
            addConnection(currentNodeNum, "Ep", "or", "");
            or();
            addConnection(currentNodeNum, "Ep", "F", "");
            PF(symbolMap.get("F"));
            addConnection(currentNodeNum, "Ep", "Ep", "");
            PE_prime(symbolMap.get("Ep"));
        } else if (nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Ep", "eps", "");
            epsilon();
        } else {
            error("|| or FOLLOW set of Non-terminal \'E'\'");
        }
    }

    /**
     * 논터미널 기호 F
     * 예시 코드에서만 정상 작동. 정답 코드에선 수정 필요.
     */
    static void PF(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"F", "W", "");
            PW(symbolMap.get("W"));
            addConnection(currentNodeNum,"F", "Fp", "");
            PF_prime(symbolMap.get("Fp"));
        }
        else {
            error("FIRST set of Non-terminal \'F\'");
        }
    }

    /**
     * 논터미널 기호 F'
     */
    static void PF_prime(int currentNodeNum) {
        if (nextSymbol.equals("&&")) {
            addConnection(currentNodeNum, "Fp", "and", "");
            and();
            addConnection(currentNodeNum, "Fp", "W", "");
            PW(symbolMap.get("W"));
            addConnection(currentNodeNum, "Fp", "Fp", "");
            PF_prime(symbolMap.get("Fp"));
        } else if (nextSymbol.equals("||") || nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Fp", "eps", "");
            epsilon();
        } else {
            error("&& or FOLLOW set of Non-terminal \'F'\'");
        }
    }

    /**
     * 논터미널 기호 W
     */
    static void PW(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"W", "V", "");
            PV(symbolMap.get("V"));
            addConnection(currentNodeNum,"W", "Wp", "");
            PW_prime(symbolMap.get("Wp"));
        }
        else {
            error("FIRST set of Non-terminal \'W\'");
        }
    }

    /**
     * 논터미널 기호 W'
     */
    static void PW_prime(int currentNodeNum) {
        if (nextSymbol.equals("==")) {
            addConnection(currentNodeNum, "Wp", "eq", "");
            eq();
            addConnection(currentNodeNum, "Wp", "V", "");
            PV(symbolMap.get("V"));
            addConnection(currentNodeNum, "Wp", "Wp", "");
            PW_prime(symbolMap.get("Wp"));
        } else if(nextSymbol.equals("!=")) {
            addConnection(currentNodeNum, "Wp", "neq", "");
            neq();
            addConnection(currentNodeNum, "Wp", "V", "");
            PV(symbolMap.get("V"));
            addConnection(currentNodeNum, "Wp", "Wp", "");
            PW_prime(symbolMap.get("Wp"));
        } else if (nextSymbol.equals("||") || nextSymbol.equals("&&") || nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Wp", "eps", "");
            epsilon();
        } else {
            error("== or != or FOLLOW set of Non-terminal \'W'\'");
        }
    }

    /**
     * 논터미널 기호 V
     */
    static void PV(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"V", "U", "");
            PU(symbolMap.get("U"));
            addConnection(currentNodeNum,"V", "Vp", "");
            PV_prime(symbolMap.get("Vp"));
        }
        else {
            error("FIRST set of Non-terminal \'V\'");
        }
    }

    /**
     * 논터미널 기호 V'
     */
    static void PV_prime(int currentNodeNum) {
        if (nextSymbol.equals("<")) {
            addConnection(currentNodeNum, "Vp", "ll", "");
            ll();
            addConnection(currentNodeNum, "Vp", "U", "");
            PU(symbolMap.get("U"));
            addConnection(currentNodeNum, "Vp", "Vp", "");
            PV_prime(symbolMap.get("Vp"));
        } else if(nextSymbol.equals("<=")) {
            addConnection(currentNodeNum, "Vp", "le", "");
            le();
            addConnection(currentNodeNum, "Vp", "U", "");
            PU(symbolMap.get("U"));
            addConnection(currentNodeNum, "Vp", "Vp", "");
            PV_prime(symbolMap.get("Vp"));
        } else if(nextSymbol.equals(">")) {
            addConnection(currentNodeNum, "Vp", "gg", "");
            gg();
            addConnection(currentNodeNum, "Vp", "U", "");
            PU(symbolMap.get("U"));
            addConnection(currentNodeNum, "Vp", "Vp", "");
            PV_prime(symbolMap.get("Vp"));
        } else if(nextSymbol.equals(">=")) {
            addConnection(currentNodeNum, "Vp", "ge", "");
            ge();
            addConnection(currentNodeNum, "Vp", "U", "");
            PU(symbolMap.get("U"));
            addConnection(currentNodeNum, "Vp", "Vp", "");
            PV_prime(symbolMap.get("Vp"));
        } else if (nextSymbol.equals("||") || nextSymbol.equals("&&") || nextSymbol.equals("==") || nextSymbol.equals("!=") ||
                nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Vp", "eps", "");
            epsilon();
        } else {
            error("> or >= or < or <= FOLLOW set of Non-terminal \'V'\'");
        }
    }

    /**
     * 논터미널 기호 U
     */
    static void PU(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"U", "T", "");
            PT(symbolMap.get("T"));
            addConnection(currentNodeNum,"U", "Up", "");
            PU_prime(symbolMap.get("Up"));
        }
        else {
            error("FIRST set of Non-terminal \'U\'");
        }
    }

    /**
     * 논터미널 기호 U'
     */
    static void PU_prime(int currentNodeNum) {
        if (nextSymbol.equals("+")) {
            addConnection(currentNodeNum, "Up", "sum", "");
            sum();
            addConnection(currentNodeNum, "Up", "T", "");
            PT(symbolMap.get("T"));
            addConnection(currentNodeNum, "Up", "Up", "");
            PU_prime(symbolMap.get("Up"));
        } else if(nextSymbol.equals("-")) {
            addConnection(currentNodeNum, "Up", "sub", "");
            sub();
            addConnection(currentNodeNum, "Up", "T", "");
            PT(symbolMap.get("T"));
            addConnection(currentNodeNum, "Up", "Up", "");
            PU_prime(symbolMap.get("Up"));
        } else if (nextSymbol.equals("||") || nextSymbol.equals("&&") || nextSymbol.equals("==") || nextSymbol.equals("!=") ||
                nextSymbol.equals(">") || nextSymbol.equals(">=") || nextSymbol.equals("<") || nextSymbol.equals("<=") ||
                nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Up", "eps", "");
            epsilon();
        } else {
            error("+ or - or FOLLOW set of Non-terminal \'U'\'");
        }
    }

    /**
     * 논터미널 기호 T
     */
    static void PT(int currentNodeNum) {
        if (nextSymbol.equals("-") || nextSymbol.equals("!") || nextSymbol.equals("(") || nextSymbol.equals("Constant") ||
                nextSymbol.equals("this") || nextSymbol.equals("ReadInteger") || nextSymbol.equals("ReadLine") ||
                nextSymbol.equals("new") || nextSymbol.equals("id") || nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"T", "K", "");
            PK(symbolMap.get("K"));
            addConnection(currentNodeNum,"T", "Tp", "");
            PT_prime(symbolMap.get("Tp"));
        }
        else {
            error("FIRST set of Non-terminal \'T\'");
        }
    }

    /**
     * 논터미널 기호 T'
     */
    static void PT_prime(int currentNodeNum) {
        if (nextSymbol.equals("*")) {
            addConnection(currentNodeNum, "Tp", "mul", "");
            mul();
            addConnection(currentNodeNum, "Tp", "K", "");
            PK(symbolMap.get("K"));
            addConnection(currentNodeNum, "Tp", "Tp", "");
            PT_prime(symbolMap.get("Tp"));
        } else if (nextSymbol.equals("/")) {
            addConnection(currentNodeNum, "Tp", "div", "");
            div();
            addConnection(currentNodeNum, "Tp", "K", "");
            PK(symbolMap.get("K"));
            addConnection(currentNodeNum, "Tp", "Tp", "");
            PT_prime(symbolMap.get("Tp"));
        } else if (nextSymbol.equals("%")) {
            addConnection(currentNodeNum, "Tp", "mod", "");
            mod();
            addConnection(currentNodeNum, "Tp", "K", "");
            PK(symbolMap.get("K"));
            addConnection(currentNodeNum, "Tp", "Tp", "");
            PT_prime(symbolMap.get("Tp"));
        } else if (nextSymbol.equals("||") || nextSymbol.equals("&&") || nextSymbol.equals("==") || nextSymbol.equals("!=") ||
                nextSymbol.equals(">") || nextSymbol.equals(">=") || nextSymbol.equals("<") || nextSymbol.equals("<=") ||
                nextSymbol.equals("+") || nextSymbol.equals("-") ||
                nextSymbol.equals(")") || nextSymbol.equals(",") || nextSymbol.equals("$")) {
            addConnection(currentNodeNum, "Tp", "eps", "");
            epsilon();
        } else {
            error("* or / or % or FOLLOW set of Non-terminal \'T'\'");
        }
    }

    /**
     * 논터미널 기호 K
     */
    static void PK(int currentNodeNum) {
        if (nextSymbol.equals("-")) {
            addConnection(currentNodeNum,"K", "sub", "");
            sub();
            addConnection(currentNodeNum,"K", "K", "");
            PK(symbolMap.get("K"));
        }
        else if (nextSymbol.equals("!")) {
            addConnection(currentNodeNum,"K", "not", "");
            not();
            addConnection(currentNodeNum,"K", "K", "");
            PK(symbolMap.get("K"));
        }
        else if (nextSymbol.equals("(")) {
            addConnection(currentNodeNum,"K", "lP", "");
            lp();
            addConnection(currentNodeNum,"K", "E", "");
            PE(symbolMap.get("E"));
            addConnection(currentNodeNum,"K", "rP", "");
            rp();
        }
        else if (nextSymbol.equals("Constant")) {
            addConnection(currentNodeNum,"K", "con", symbolList.get(index).getValue());
            con();
        }
        else if (nextSymbol.equals("id")) {
            addConnection(currentNodeNum,"K", "id", symbolList.get(index).getValue());
            id();
        }
        else if (nextSymbol.equals("this")) {
            addConnection(currentNodeNum,"K", "ths", "");
            this_fun();
        }
        else if (nextSymbol.equals("ReadInteger")) {
            addConnection(currentNodeNum,"K", "rdI", "");
            rdI();
            addConnection(currentNodeNum,"K", "lP", "");
            lp();
            addConnection(currentNodeNum,"K", "rP", "");
            rp();
        }
        else if (nextSymbol.equals("ReadLine")) {
            addConnection(currentNodeNum,"K", "rdL", "");
            rdL();
            addConnection(currentNodeNum,"K", "lP", "");
            lp();
            addConnection(currentNodeNum,"K", "rP", "");
            rp();
        }
        else if (nextSymbol.equals("new")) {
            addConnection(currentNodeNum,"K", "new", "");
            new_fun();
            addConnection(currentNodeNum,"K", "id", symbolList.get(index).getValue());
            id();
        }
        else if (nextSymbol.equals("NewArray")) {
            addConnection(currentNodeNum,"K", "nwA", "");
            nwA();
            addConnection(currentNodeNum,"K", "lP", "");
            lp();
            addConnection(currentNodeNum,"K", "E", "");
            PE(symbolMap.get("E"));
            addConnection(currentNodeNum,"K", "com", "");
            comma();
            addConnection(currentNodeNum,"K", "typ", symbolList.get(index).getValue());
            typ();
            addConnection(currentNodeNum,"K", "rP", "");
            rp();
        }
        else {
            error("FIRST set of Non-terminal \'K\'");
        }
    }

    /**
     * 터미널 기호 or
     */
    static void or() {
        if (nextSymbol.equals("||")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("||");
    }

    /**
     * 터미널 기호 and
     */
    static void and() {
        if (nextSymbol.equals("&&")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("&&");
    }

    /**
     * 터미널 기호 ==
     */
    static void eq() {
        if (nextSymbol.equals("==")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("==");
    }

    /**
     * 터미널 기호 !=
     */
    static void neq() {
        if (nextSymbol.equals("!=")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("!=");
    }

    /**
     * 터미널 기호 <
     */
    static void ll() {
        if (nextSymbol.equals("<")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("<");
    }

    /**
     * 터미널 기호 <=
     */
    static void le() {
        if (nextSymbol.equals("<=")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("<=");
    }

    /**
     * 터미널 기호 >
     */
    static void gg() {
        if (nextSymbol.equals(">")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error(">");
    }

    /**
     * 터미널 기호 >=
     */
    static void ge() {
        if (nextSymbol.equals(">=")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error(">=");
    }

    /**
     * 터미널 기호 +
     */
    static void sum() {
        if (nextSymbol.equals("+")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("+");
    }

    /**
     * 터미널 기호 -
     */
    static void sub() {
        if (nextSymbol.equals("-")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("-");
    }

    /**
     * 터미널 기호 *
     */
    static void mul() {
        if (nextSymbol.equals("*")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("*");
    }

    /**
     * 터미널 기호 /
     */
    static void div() {
        if (nextSymbol.equals("/")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("/");
    }

    /**
     * 터미널 기호 %
     */
    static void mod() {
        if (nextSymbol.equals("%")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("%");
    }

    /**
     * 터미널 기호 !
     */
    static void not() {
        if (nextSymbol.equals("!")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("!");
    }

    /**
     * 터미널 기호 (
     */
    static void lp() {
        if (nextSymbol.equals("(")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("(");
    }

    /**
     * 터미널 기호 )
     */
    static void rp() {
        if (nextSymbol.equals(")")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error(")");
    }

    /**
     * 터미널 기호 id
     */
    static void id() {
        if (nextSymbol.equals("id")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("id");
    }

    /**
     * 터미널 기호 Constant
     */
    static void con() {
        if (nextSymbol.equals("Constant")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("Constant");
    }

    /**
     * 터미널 기호 this
     */
    static void this_fun() {
        if (nextSymbol.equals("this")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("this");
    }

    /**
     * 터미널 기호 ReadInteger
     */
    static void rdI() {
        if (nextSymbol.equals("ReadInteger")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("ReadInteger");
    }

    /**
     * 터미널 기호 ReadLine
     */
    static void rdL() {
        if (nextSymbol.equals("ReadLine")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("ReadLine");
    }

    /**
     * 터미널 기호 new
     */
    static void new_fun() {
        if (nextSymbol.equals("new")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("new");
    }

    /**
     * 터미널 기호 newArray
     */
    static void nwA() {
        if (nextSymbol.equals("NewArray")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("NewArray");
    }

    /**
     * 터미널 기호 ,
     */
    static void comma() {
        if (nextSymbol.equals(",")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error(",");
    }

    /**
     * 터미널 기호 type
     */
    static void typ() {
        if (nextSymbol.equals("Type")) {
            if (index < symbolList.size() - 1)
                nextSymbol = symbolList.get(++index).getKey();
        }
        else
            error("Type");
    }

    /**
     * 터미널 기호 epsilon
     */
    static void epsilon() {}

    static void error(String expected) {
        System.out.println("Invalid input. Expected " + expected + ", but received " + nextSymbol + " at index " + index + ".");
        System.exit(-1);
    }
}

