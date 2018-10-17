package sample.constantes;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Configs {
    
    public static final String[] KEYWORDS = new String[] {
          "Mover","MoverCon","Saltar","SaltarCon","Derecha","Izquierda","Arriba","Abajo",
            "KDER","KIZQ","KBAJ","KARR"
    };


    public static String palabras = "(Estado|fin|Entidad|>)";




    public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public static final String FUNCTIONS_PATTERN = palabras;
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String BRACKET_PATTERN = "\\[|\\]";
    public static final String SEMICOLON_PATTERN = "\\;";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    public static final String NORMAL_PATTERN = "(?!Estado|fin|Entidad)(\\w+|\\.|\\:)(?!Estado|fin|Entidad)";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<NORMAL>" + NORMAL_PATTERN + ")"
                    + "|(?<FUNCTION>" + FUNCTIONS_PATTERN + ")"
    );

    public static final String sampleCode = String.join("\n", new String[] {
            "Entidad.Player",
            "",
            "Estado Inicio:",
            "\tMoverCon KDER Derecha 5",
            "\tSaltarCon KARR 10",
            "         fin",
            "",
            "Estado Jugando:",
            "        fin",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    });

    ///para pintar las palabras
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            matcher.group("NORMAL") != null ? "normal":
                                                                                matcher.group("FUNCTION") != null ? "function":


                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }


    public static String[] EXPRECIONES = {
            "^(Entidad)[\\.][a-zA-Z]+",
            "^(Estado)[\\s][a-zA-Z]+(:)$",
            "[\\t](>)[\\s](MoverCon)[\\s](KDER|KIZQ|KARR|KABA)[\\s](Derecha|Izquierda|Arriba|Abajo)[\\s]\\d+",
            "(([\\s]+(fin)[\\s]+)|([\\s]+(fin)))",
            "[\\t](>)[\\s](Mover)[\\s](Derecha|Izquierda|Arriba|Abajo)[\\s][\\d]+",
            "[\\t](>)[\\s](SaltarCon)[\\s](KDER|KIZQ|KARR|KABA)[\\s]\\d+"
    };


}
