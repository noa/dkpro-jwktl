package de.tudarmstadt.ukp.jwktl.examples;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;

import de.tudarmstadt.ukp.jwktl.JWKTL;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEdition;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryEntry;
import de.tudarmstadt.ukp.jwktl.api.IWiktionaryPage;
import de.tudarmstadt.ukp.jwktl.api.PartOfSpeech;
import de.tudarmstadt.ukp.jwktl.api.filter.WiktionaryEntryFilter;
import de.tudarmstadt.ukp.jwktl.api.util.Language;

public class ExtractClosedClass {

    /** Runs the example.
     *  @param args name of the directory of parsed Wiktionary data. */
    public static void main(String[] args) {
        if (args.length != 1)
            throw new IllegalArgumentException("Too few arguments. "
                                               + "Required arguments: <PARSED-WIKTIONARY>");

        // Create new IWiktionaryEdition for our parsed data.
        String wktPath = args[0];
        IWiktionaryEdition wkt = JWKTL.openEdition(new File(wktPath));

        HashSet<PartOfSpeech> forbid = new HashSet<PartOfSpeech>();
        forbid.add(PartOfSpeech.PROPER_NOUN);
        forbid.add(PartOfSpeech.FIRST_NAME);
        forbid.add(PartOfSpeech.LAST_NAME);
        forbid.add(PartOfSpeech.TOPONYM);
        forbid.add(PartOfSpeech.ACRONYM);
        forbid.add(PartOfSpeech.PREFIX);
        forbid.add(PartOfSpeech.ABBREVIATION);
        forbid.add(PartOfSpeech.SUFFIX);
        forbid.add(PartOfSpeech.LETTER);
        forbid.add(PartOfSpeech.SYMBOL);
        forbid.add(PartOfSpeech.NUMBER);
        forbid.add(PartOfSpeech.PLACE_NAME_ENDING);

        // Iterate over all pages and count the pages, entries, and senses.
        int pageCount = 0;
        int entryCount = 0;
        int senseCount = 0;
        int closedClassCount = 0;
        HashMap<String, PartOfSpeech> dictionary = new HashMap<String, PartOfSpeech>();
//        try (PrintStream out = new PrintStream("en_closed_class_lemmas.txt")) {
            for (IWiktionaryPage page : wkt.getAllPages()) {
                for (IWiktionaryEntry entry : page.getEntries()) {

                    if(!(Language.ENGLISH == entry.getWordLanguage())) {
                        continue;
                    }

                    PartOfSpeech pos = entry.getPartOfSpeech();
                    if(!forbid.contains(pos)) {
                        String lemma = entry.getWord();
                        if(!dictionary.containsKey(lemma)) {
                            dictionary.put(lemma, pos);
                        }
                    }

                    senseCount += entry.getSenseCount();
                    entryCount++;
                }
                pageCount++;

            }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

            try (PrintStream out = new PrintStream("en_closed_class_lemmas.txt")) {
                for(String entry : dictionary.keySet()) {
                    out.println(entry + "\t" + dictionary.get(entry));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        System.out.println("Pages: " + pageCount);
        System.out.println("Entries: " + entryCount);
        System.out.println("Senses: " + senseCount);
        System.out.println("Closed-class words: " + closedClassCount);

        // Close the Wiktionary edition.
        wkt.close();
    }
}
