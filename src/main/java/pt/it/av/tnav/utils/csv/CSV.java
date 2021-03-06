package pt.it.av.tnav.utils.csv;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * CSV parser based on RFC 4180
 *
 * @author Mário Antunes
 */
public class CSV {
    public static final char CR = 0x000D, LF = 0x000A, COMMA = 0x002C, DQUOTE = 0x0022;
    private boolean hasHeader = false;
    private List<CSVRecord> records;

    public CSV(final List<CSVRecord> records, final boolean hasHeader) {
        this.records = records;
        this.hasHeader = hasHeader;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public CSVRecord getHeader() {
        if(hasHeader && records.size() > 0) {
            return records.get(0);
        } else {
            return null;
        }
    }

    /**
     * 
     * @param r
     * @return
     * @throws IOException
     */
    public static CSV read(final Reader r) throws IOException {
        return read(r, false);
    }

    /**
     * 
     * @param r
     * @param hasHeader
     * @return
     * @throws IOException
     */
    public static CSV read(final Reader r, final boolean hasHeader) throws IOException {
        List<CSVRecord> records = new ArrayList<>();
        Deque<STATE> state = new ArrayDeque<>();
        state.push(STATE.END);
        StringBuilder data = new StringBuilder();
        CSVRecord record = null;
        
        int i = 0;
        char c = ' ';

        try {
            while ((i = r.read()) != -1) {
                System.err.println("STATE = "+state.peek().name());
                c = (char) i;
                System.err.println("C = " + c);
                switch (state.peek()) {
                    case END:
                    {
                        switch(c) {
                            case CR:
                            case LF:
                            case COMMA:
                            state.push(STATE.ERROR);
                            break;
                            default:
                            record = new CSVRecord();
                            state.push(STATE.TEXTDATA);
                            data.setLength(0);
                            data.append(c);
                        }
                        break;
                    }
                    case TEXTDATA:{
                        switch(c) {
                            case CR:
                            state.pop();
                            state.push(STATE.CR);
                            record.add(new CSVField(data.toString()));
                            data.setLength(0);
                            break;

                            case LF:
                            state.push(STATE.ERROR);
                            break;

                            case COMMA:
                            state.pop();
                            state.push(STATE.COMMA);
                            record.add(new CSVField(data.toString()));
                            data.setLength(0);
                            break;

                            default:
                            data.append(c);
                            break;
                        
                        }
                        break;
                    }
                    case CR: {
                        switch(c) {
                            case LF:
                            state.pop();
                            state.add(STATE.LF);
                            records.add(record);
                            record = new CSVRecord();
                        }
                        break;
                    }

                    case COMMA: {
                        switch(c) {
                            case CR:
                            case LF:
                            case COMMA:
                            state.push(STATE.ERROR);
                            break;
                            default:
                            state.pop();
                            state.push(STATE.TEXTDATA);
                            data.append(c);
                        }
                        break;
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("BUFFER: " + c);
            e.printStackTrace();
        }

        if(state.peek() == STATE.TEXTDATA) {
            state.pop();
            record.add(new CSVField(data.toString()));
            data.setLength(0);
            records.add(record);
        }

        if (state.peek() != STATE.END) {
            while (!state.isEmpty()) {
                System.err.println("STATE -> " + state.peek().name());
                state.pop();
            }
            throw new IOException();
        }

        return new CSV(records, hasHeader);
    }

    public void write(Writer w) throws IOException {
        int size = records.size();
        System.err.println("Size = "+size);
        for (int i = 0; i < size - 1; i++) {
            CSVRecord r = records.get(i);
            r.write(w);
            w.append(CR);
            w.append(LF);
        }

        if (size > 0) {
            CSVRecord r = records.get(size - 1);
            r.write(w);
        }
    }

    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        try {
            write(w);
        } catch (IOException e) {
            // should not occur...
        }
        return w.toString();
    }

    private enum STATE {
        END, RECORD, DQUOTE, TEXTDATA, TWODQUOTE, COMMA, CR, LF, ERROR
    }

    public static class CSVField implements CharSequence {
        private final String textData;
        
        public CSVField(final String textData) {
            this.textData = textData;
        }

        @Override
        public int length() {
            return textData.length();
        }

        @Override
        public char charAt(int index) {
            return textData.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return textData.subSequence(start, end);
        }

        @Override
        public String toString() {
            return textData;
        }
    }

    public static class CSVRecord extends ArrayList<CSVField> {
        private static final long serialVersionUID = 1L;

        public void write(Writer w) throws IOException {
            int size = this.size();
            for(int i = 0; i < size - 1; i++) {
                w.append(get(i).toString());
                w.append(COMMA);
            }

            if(size > 0) {
                w.append(get(size - 1));
            }
        }
    }
}
