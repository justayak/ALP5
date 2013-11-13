package ueb03;


// usage:  java Check text from to
//   e.g.  java Check text.txt 1 1000

import java.io.RandomAccessFile;
import java.io.*;

public class Check {
    static String dictionary;
    public static void main(String[] arg) throws Exception  {
        RandomAccessFile text = new RandomAccessFile(arg[0], "r");
        long from = Long.parseLong(arg[1]);
        long to   = Long.parseLong(arg[2]);
        dictionary = "classes/english.dic";
        new FileInputStream(dictionary);
        System.out.println("*** from " + from + " to " + to + " started alright");

        text.seek(from);
        while(true) {  // handle lines
            long next = text.getFilePointer();
            if(next > to) break;
            String line = text.readLine();
            if(line == null) break;
            StreamTokenizer parse = new StreamTokenizer(
                    new StringReader(line));
            parse.lowerCaseMode(true);
            parse.whitespaceChars('/','/');
            parse.whitespaceChars('-','-');
            parse.whitespaceChars('.','.');
            line: while(true) {  // handle words
                int type = parse.nextToken();
                switch(type) {
                    case StreamTokenizer.TT_WORD:
                        handle(parse.sval); continue;
                    case StreamTokenizer.TT_EOF:
                        break line;
                    default: continue;  }  }  }
        System.out.println("*** from " + from + " to " + to + " done");
    } // end main

    static void handle(String word) throws Exception {
        InputStream in = new FileInputStream(dictionary);
        BufferedReader dict =  new BufferedReader(
                new InputStreamReader(in));
        while(true) { // blunt sequential dictionary search
            String line = dict.readLine();
            if(line == null) break;
            else if(word.compareTo(line) < 0)   break;
            else if(word.compareTo(line)== 0)   return;
            else /* word.compareTo(line) > 0 */ continue;  }
        System.out.println(word);
    } // end handle
}  // end Check
