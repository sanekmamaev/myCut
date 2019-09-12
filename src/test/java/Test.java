import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;


import java.io.*;

class TestCut {
    String read(String file) {
        StringBuilder builder = new StringBuilder();
        try {
            FileInputStream ofile = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(ofile));
            String strLine;
            String sep = "";
            while ((strLine = br.readLine()) != null) {
                builder.append(sep);
                sep = "\n";
                builder.append(strLine);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в входном файле");
        }
        return builder.toString();
    }

    @BeforeAll
    static void write() {
        String in = "Скажи-ка, дядя, ведь не даром\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки боевые,\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!";
        try (FileWriter writer = new FileWriter("input.txt", false)) {
            writer.write(in);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    

    @Test
    void testSymbolAndWord() {
        Cut cut = new Cut("input.txt", "output.txt", false, "0-10");
        cut.cutInfo();
        assertEquals("Скажи-ка, \nМосква, сп\nФранцузу о\nВедь были \nДа, говоря\nНедаром по\nПро день Б", read("output.txt"));
        cut = new Cut("input.txt", "output.txt", true, "0-10");
        cut.cutInfo();
        assertEquals("Скажи-ка, дядя, ведь не даром\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки боевые,\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!", read("output.txt"));
        cut = new Cut ("input.txt","output.txt",false, "4-7");
        cut.cutInfo();
        assertEquals("и-к\n" +"ва,\n" + "цуз\n" + " бы\n" + "гов\n" + "ром\n" + "ден", read("output.txt"));

    }

    @Test
    void testRange() {
        Cut cut = new Cut("input.txt", "output.txt", true, "100-10");
        assertThrows(IllegalArgumentException.class, cut::cutInfo);
        cut = new Cut("input.txt", "output.txt", true, "abcd");
        assertThrows(IllegalArgumentException.class, cut::cutInfo);
        cut = new Cut("input.txt", "output.txt", false, "0-1000");
        cut.cutInfo();
        assertEquals("Скажи-ка, дядя, ведь не даром\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки боевые,\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!", read("output.txt"));
        cut = new Cut("input.txt", "output.txt", false, "-1000");
        cut.cutInfo();
        assertEquals("Скажи-ка, дядя, ведь не даром\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки боевые,\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!", read("output.txt"));
    }

    @Test
    void testConsoleToFile() throws FileNotFoundException {
        InputStream old = System.in;
        System.setIn(new FileInputStream("input.txt"));
        Cut cut = new Cut(null, "output.txt", true, "0-4");
        cut.cutInfo();
        assertEquals("Скажи-ка, дядя, ведь не\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!", read("output.txt"));
        System.setIn(old);
    }

    @Test
    void testFileToConsole() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(out));
        Cut cut = new Cut("input.txt", null, true, "0-10");
        cut.cutInfo();
        assertEquals("Скажи-ка, дядя, ведь не даром\n" +
                "Москва, спаленная пожаром,\n" +
                "Французу отдана?\n" +
                "Ведь были ж схватки боевые,\n" +
                "Да, говорят, еще какие!\n" +
                "Недаром помнит вся Россия\n" +
                "Про день Бородина!", out.toString());
        System.setOut(old);
    }

    @Test
    void testConsoleToConsole() throws FileNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        InputStream oldIn = System.in;
        System.setOut(new PrintStream(out));
        System.setIn(new FileInputStream("input.txt"));
        Cut cut = new Cut(null, null, false, "0-10");
        cut.cutInfo();
        assertEquals("Скажи-ка, \nМосква, сп\nФранцузу о\nВедь были \nДа, говоря\nНедаром по\nПро день Б", out.toString());
        System.setIn(oldIn);
        System.setOut(oldOut);
    }
}