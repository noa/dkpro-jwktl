#! /usr/bin/env bash

mvn exec:java -Dexec.mainClass="de.tudarmstadt.ukp.jwktl.examples.Example1_ParseWiktionaryDump" -Dexec.args="enwiktionary-20150826-pages-articles.xml.bz2 en_dump_output true"

#eof
