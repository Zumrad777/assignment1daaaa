package Util;
// ⚠️ Если папка у тебя называется маленькими буквами (util), тогда здесь должно быть:
// package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter implements AutoCloseable {
    private final PrintWriter out;
    public CsvWriter(String file) throws IOException {
        this.out = new PrintWriter(new FileWriter(file));
    }

    public void header(String line) {
        out.println(line);
    }
    public void row(Object... values) {
        for (int i = 0; i < values.length; i++) {
            out.print(values[i]);
            if (i < values.length - 1) {
                out.print(",");
            }
        }
        out.println();
    }
    @Override
    public void close() {
        out.close();
    }
}
