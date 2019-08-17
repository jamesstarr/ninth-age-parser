
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jim.ninthage.opennlp;

import com.google.common.collect.Lists;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TIntArrayList;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.sentdetect.*;
import opennlp.tools.sentdetect.lang.Factory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringUtil;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Splitter {
    /**
     * The maximum entropy model to use to evaluate contexts.
     */
    private MaxentModel model;

    /**
     * The feature context generator.
     */
    private final SDContextGenerator cgen;

    /**
     * The {@link EndOfSentenceScanner} to use when scanning for end of sentence offsets.
     */
    private final EndOfSentenceScanner scanner;


    private final boolean useTokenEnd;

    /**
     * Initializes the current instance.
     *
     * @param model the {@link SentenceModel}
     */
    public Splitter(SentenceModel model) {
        SentenceDetectorFactory sdFactory = model.getFactory();
        this.model = model.getMaxentModel();
        cgen = sdFactory.getSDContextGenerator();
        scanner = sdFactory.getEndOfSentenceScanner();
        useTokenEnd = sdFactory.isUseTokenEnd();
    }

    /**
     * @deprecated Use a {@link SentenceDetectorFactory} to extend
     * SentenceDetector functionality.
     */
    public Splitter(SentenceModel model, Factory factory) {
        this.model = model.getMaxentModel();
        // if the model has custom EOS characters set, use this to get the context
        // generator and the EOS scanner; otherwise use language-specific defaults
        char[] customEOSCharacters = model.getEosCharacters();
        if (customEOSCharacters == null) {
            cgen = factory.createSentenceContextGenerator(model.getLanguage(),
                    getAbbreviations(model.getAbbreviations()));
            scanner = factory.createEndOfSentenceScanner(model.getLanguage());
        } else {
            cgen = factory.createSentenceContextGenerator(
                    getAbbreviations(model.getAbbreviations()), customEOSCharacters);
            scanner = factory.createEndOfSentenceScanner(customEOSCharacters);
        }
        useTokenEnd = model.useTokenEnd();
    }

    private static Set<String> getAbbreviations(Dictionary abbreviations) {
        if (abbreviations == null) {
            return Collections.emptySet();
        }
        return abbreviations.asStringSet();
    }

    /**
     * Detect sentences in a String.
     *
     * @param sb The string to be processed.
     * @return A string array containing individual sentences as elements.
     */
    public List<String> sentDetect(String sb) {
        TIntArrayList ender2 = new TIntArrayList();
        TDoubleArrayList probOfSplit = new TDoubleArrayList();
        ender2.add(0);

        for (int i = 1; i < sb.length(); i++) {
            double[] probs = model.eval(cgen.getContext(sb, i));
            probOfSplit.add(probs[1]);
        }

        for (int i = 0; i < probOfSplit.size(); i++) {
            double prob = probOfSplit.get(i);
            if(prob > 0.65) {
                ender2.add(i);
            }
        }

        return sub(sb, ender2);
    }

    private int getFirstWS(String s, int pos) {
        while (pos < s.length() && !StringUtil.isWhitespace(s.charAt(pos)))
            pos++;
        return pos;
    }

    private int getFirstNonWS(String s, int pos) {
        while (pos < s.length() && StringUtil.isWhitespace(s.charAt(pos)))
            pos++;
        return pos;
    }

    /**
     * Detect the position of the first words of sentences in a String.
     *
     * @param s The string to be processed.
     * @return A integer array containing the positions of the end index of
     * every sentence
     */
    public List<Span> sentPosDetect(String s) {
        StringBuilder sb = new StringBuilder(s);
        List<Integer> enders = scanner.getPositions(s);
        List<Integer> positions = new ArrayList<>(enders.size());
        TDoubleArrayList sentProbs = new TDoubleArrayList(enders.size());



        for (int i = 0, end = enders.size(), index = 0; i < end; i++) {
            int cint = enders.get(i);
            // skip over the leading parts of non-token final delimiters
            int fws = getFirstWS(s, cint + 1);
            if (i + 1 < end && enders.get(i + 1) < fws) {
                continue;
            }
            if (positions.size() > 0 && cint < positions.get(positions.size() - 1)) continue;

            double[] probs = model.eval(cgen.getContext(sb, cint));
            String bestOutcome = model.getBestOutcome(probs);

            if (isAcceptableBreak(s, index, cint)) {
                if (index != cint) {
                    if (useTokenEnd) {
                        positions.add(getFirstNonWS(s, getFirstWS(s, cint + 1)));
                    } else {
                        positions.add(getFirstNonWS(s, cint + 1));
                    }
                    sentProbs.add(probs[model.getIndex(bestOutcome)]);
                }

                index = cint + 1;
            }
        }

        int[] starts = new int[positions.size()];
        for (int i = 0; i < starts.length; i++) {
            starts[i] = positions.get(i);
        }

        // string does not contain sentence end positions
        if (starts.length == 0) {

            // remove leading and trailing whitespace
            int start = 0;
            int end = s.length();

            while (start < s.length() && StringUtil.isWhitespace(s.charAt(start)))
                start++;

            while (end > 0 && StringUtil.isWhitespace(s.charAt(end - 1)))
                end--;

            if (end - start > 0) {
                sentProbs.add(1d);
                return Collections.singletonList(new Span(start, end));
            } else
                return Collections.emptyList();
        }

        // Convert the sentence end indexes to spans

        boolean leftover = starts[starts.length - 1] != s.length();
        List<Span> spans = new ArrayList<>(leftover ? starts.length + 1 : starts.length);

        for (int si = 0; si < starts.length; si++) {
            int start;

            if (si == 0) {
                start = 0;
            } else {
                start = starts[si - 1];
            }

            // A span might contain only white spaces, in this case the length of
            // the span will be zero after trimming and should be ignored.
            Span span = new Span(start, starts[si]).trim(s);
            if (span.length() > 0) {
                spans.add(span);
            } else {
                sentProbs.remove(si);
            }
        }

        if (leftover) {
            Span span = new Span(starts[starts.length - 1], s.length()).trim(s);
            if (span.length() > 0) {
                spans.add(span);
                sentProbs.add(1d);
            }
        }
        /*
         * set the prob for each span
         */
        for (int i = 0; i < spans.size(); i++) {
            double prob = sentProbs.get(i);
            spans.set(i, new Span(spans.get(i), prob));

        }

        return spans;
    }

    public List<String> sub(String sb, TIntArrayList ender){

        TIntIterator iterator = ender.iterator();
        List<String> splits = Lists.newArrayList();
        int start = 0;
        int previous = 0;
        while(iterator.hasNext()){
            int nextIndex = iterator.next();
            if(nextIndex - previous < 50) {
                previous = nextIndex;
                continue;
            } else if(previous - start < 100) {
                previous = nextIndex;
                continue;
            } else {
                splits.add(sb.substring(start, previous));
                start = previous;
                previous = nextIndex;
            }
        }
        splits.add(sb.substring(start));
        return splits.stream()
                .filter((token) -> !token.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Allows subclasses to check an overzealous (read: poorly
     * trained) model from flagging obvious non-breaks as breaks based
     * on some boolean determination of a break's acceptability.
     *
     * <p>The implementation here always returns true, which means
     * that the MaxentModel's outcome is taken as is.</p>
     *
     * @param s              the string in which the break occurred.
     * @param fromIndex      the start of the segment currently being evaluated
     * @param candidateIndex the index of the candidate sentence ending
     * @return true if the break is acceptable
     */
    protected boolean isAcceptableBreak(String s, int fromIndex, int candidateIndex) {
        return true;
    }

    public static SentenceModel train(
            ObjectStream<SentenceSample> samples,
            SentenceDetectorFactory sdFactory,
            TrainingParameters mlParams
    ) throws IOException {

        Map<String, String> manifestInfoEntries = new HashMap<>();

        // TODO: Fix the EventStream to throw exceptions when training goes wrong
        ObjectStream<Event> eventStream = new SDEventStream(samples,
                sdFactory.getSDContextGenerator(), sdFactory.getEndOfSentenceScanner());

        EventTrainer trainer = TrainerFactory.getEventTrainer(mlParams, manifestInfoEntries);

        MaxentModel sentModel = trainer.train(eventStream);

        return new SentenceModel("en", sentModel, manifestInfoEntries, sdFactory);
    }

}
