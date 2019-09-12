import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cut {
    private String inName;
    private String outName;
    private boolean symOrWord;
    private String range;

    public Cut(String inFile, String outFile, boolean symOrWord, String range) {
        this.inName = inFile;
        this.outName = outFile;
        this.symOrWord = symOrWord;
        this.range = range;
    }

    public void writer(String result) {
        try (FileWriter writer = new FileWriter(outName, false)) {
            writer.write(result);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String cut(String line, List<Integer> rangeInt, boolean symbolOrWord) {
        String result;
        String line1 = line.trim();
        if (!symbolOrWord) {
            if (rangeInt.get(1) < line1.length()) result = line1.substring(rangeInt.get(0), rangeInt.get(1));
            else result = line1.substring(rangeInt.get(0));
        } else {
            StringBuilder builder = new StringBuilder();
            List<String> wordList = new ArrayList<String>(Arrays.asList(line1.split(" ")));
            for (int i = rangeInt.get(0); (i < rangeInt.get(1) && i < wordList.size()); i++) {
                builder.append(wordList.get(i));
                builder.append(' ');
            }
            result = builder.toString().trim();
        }
        return result;
    }

    private List<Integer> rangeInfo(String range) {
        if (!range.matches("([0-9\\-])+")) throw new IllegalArgumentException();
        int indOfSep = range.indexOf("-");
        List<Integer> list = new ArrayList<Integer>();
        if (indOfSep == 0) list.add(0);
        else list.add(Integer.parseInt(range.substring(0, indOfSep)));
        if (indOfSep == range.length() - 1) list.add(2147483646);
        else list.add(Integer.parseInt(range.substring(indOfSep + 1)));
        if (list.get(1) < list.get(0)) throw new IllegalArgumentException("Второе значение меньше первого");
        return list;
    }

    public void cutInfo() {
        List<String> list = new ArrayList<String>();

        try {
            BufferedReader br;
            if (inName != null) {
                FileInputStream file = new FileInputStream(inName);
                br = new BufferedReader(new InputStreamReader(file));
            } else br = new BufferedReader(new InputStreamReader(System.in));
            String textLine;
            while ((textLine = br.readLine()) != null) {
                list.add(textLine);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в получаемом файле");
        }
        List<Integer> rangeInt = rangeInfo(range);
        StringBuilder builder = new StringBuilder();
        String sep = "";
        for (String s : list) {
            builder.append(sep);
            sep = "\n";
            builder.append(cut(s, rangeInt, symOrWord));
        }
        if (outName == null) System.out.print(builder.toString());
        else writer(builder.toString());

    }
}

