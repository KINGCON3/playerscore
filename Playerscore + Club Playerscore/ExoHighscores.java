package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.pow;

class ExoHighscores {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_WHITE = "\u001B[97m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_BRIGHT_BLACK = "\u001B[90m";

    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String TEXT_BG_BLUE = "\u001B[44m";
    public static final String TEXT_BRIGHT_BG_BLACK = "\u001B[100m";
    public static final String TEXT_BG_YELLOW = "\u001B[43m";
    public static final String TEXT_BRIGHT_BG_RED = "\u001B[41m";
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";

    public static void main(String[] args) {


        ArrayList<Level> levels = new ArrayList<>();
        HashMap<String, Player> players = new HashMap<>();

        try {
            File directory = new File("highscoredata/");
            File[] contents = directory.listFiles();

            for (File fname : contents) {
                Level level = new Level(fname.getName());
                levels.add(level);

                BufferedReader in = new BufferedReader(new FileReader(fname));

                int rank = 1;
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }

                    // 0 country, 1 colour, 2 time, 3+4 date+time, 5+ name
                    String[] lineparts = line.split(" ");
                    System.out.println(lineparts[0]);
                    System.out.println(lineparts[1]);
                    System.out.println(lineparts[2]);
                    System.out.println(lineparts[3]);
                    System.out.println(lineparts[4]);
                    System.out.println(lineparts[5]);

                    String country = lineparts[0];
                    int time_ms = Integer.parseInt(lineparts[2].replace(".", ""));
                    String datetime = lineparts[3] + " " + lineparts[4];

                    // since java can't limit the splits, we need to check manually where the name starts and substr from there (+4 for 4 spaces)
                    String name = "";
                    name = line.substring(lineparts[0].length() + lineparts[1].length() + lineparts[2].length() + lineparts[3].length() + lineparts[4].length() + 4);
                    name = name.trim();

                    if (!players.containsKey(name)) {
                        players.put(name, new Player(name, lineparts[0]));
                    }
                    Player player = players.get(name);
                    Highscore highscore = new Highscore(level, player, time_ms, datetime, rank, country);

                    player.highscores.put(level, highscore);
                    level.rankedHighscores.put(rank, highscore);

                    rank++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        HashMap<String, Double> playerTime = new HashMap<>();
        //new hashmap
        System.out.format("%-3s %-5s %-5s %-3s %-15s %-11s %-4s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-5s %-3s",
                "WRs:", "Pos:", "Tier:", "Reg:", "Name:", "Score:", "Rem:",
                "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "E11", "E12",
                "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10", "M11", "M12",
                "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10", "H11", "H12" + "\n");

        String firstPlace = "ItsZap";
        double firstScore = 0.0;

        double D1 = 0, D2 = 0, D3 = 0, G1 = 0, G2 = 0, G3 = 0, S1 = 0, S2 = 0, S3 = 0, B1 = 0, B2 = 0, B3 = 0, ST = 0;

        double E1all = 0, E2all = 0, E3all = 0, E4all = 0, E5all = 0, E6all = 0, E7all = 0, E8all = 0, E9all = 0, E10all = 0, E11all = 0, E12all = 0,
                M1all = 0, M2all = 0, M3all = 0, M4all = 0, M5all = 0, M6all = 0, M7all = 0, M8all = 0, M9all = 0, M10all = 0, M11all = 0, M12all = 0,
                H1all = 0, H2all = 0, H3all = 0, H4all = 0, H5all = 0, H6all = 0, H7all = 0, H8all = 0, H9all = 0, H10all = 0, H11all = 0, H12all = 0;

        for (String key : players.keySet()) {
            if (players.get(key) == null) {
                System.out.println("Warning: player name not found");
            }
            double playerscore = 0;
            for (Level level : levels) {
                boolean found = false;
                double WR = level.rankedHighscores.get(1).time_ms / 1000.0;
                double our_time = -1;  // java requires a value because the stupid compiler doesn't get that in all cases, it will have been initialized when we use it...
                for (Highscore highscore : level.rankedHighscores.values()) {
                    if (highscore.player == players.get(key)) {
                        our_time = highscore.time_ms / 1000.0;
                        //System.out.println(our_time + " on " + level.identifier);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //our_time = (level.rankedHighscores.get(100).time_ms / 1000.0) + 200.000;
                    our_time = 999999999;
                    //System.out.println("not found, using " + our_time + " on " + level.identifier);
                }
                double penalty = java.lang.Math.min(our_time - WR, 99999999);


                //playerscore += 1000 - penalty * 1000; <- max 33k score

                double hundredth = level.rankedHighscores.get(100).time_ms / 1000.0 + 0.00000000001;

                double tempScore = 1000 - (penalty * 1000) * ((1 / (hundredth - WR)) / 1 / 16);
                if (tempScore < 0) {
                    level.rankedpbs.put(key, 0.0);
                } else {
                    DecimalFormat df = new DecimalFormat("##.#");
                    double submit = Double.parseDouble(df.format(tempScore));
                    level.rankedpbs.put(key, submit);
                    playerscore += tempScore;
                }

                //playerscore += penalty; <- seconds away from wr
            }
            //System.out.println("Off-WR cumulative time for " + key + ": " + playerscore + " s");
            DecimalFormat df = new DecimalFormat("##.###");
            playerscore = Double.parseDouble(df.format(playerscore));
            playerTime.put(key, playerscore);
            //Add Name and playerscore(time) to a new hashmap.
        }

        Object[] a = playerTime.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Double>) o2).getValue()
                        .compareTo(((Map.Entry<String, Double>) o1).getValue());
            }
        });
        int count = 1;

        String rank = "";

        int courses = 36;
        int onek = 1000;
        int starter = 20;
        double multiplier = 1.5;
        double sPlus = (onek - starter * pow(multiplier, -1.1)) * courses,
                s = (onek - starter * pow(multiplier, 1.1)) * courses,
                aPlus = (onek - starter * pow(multiplier, 2.5)) * courses,
                aa = (onek - starter * pow(multiplier, 4.3)) * courses,
                bPlus = (onek - starter * pow(multiplier, 4.8)) * courses,
                bb = (onek - starter * pow(multiplier, 5.9)) * courses,
                cPlus = (onek - starter * pow(multiplier, 6.9)) * courses,
                c = (onek - starter * pow(multiplier, 7.5)) * courses,
                dPlus = (onek - starter * pow(multiplier, 7.8)) * courses,
                d = (onek - starter * pow(multiplier, 8.5)) * courses,
                ePlus = (onek - starter * pow(multiplier, 9)) * courses,
                ee = (onek - starter * pow(multiplier, 9.5)) * courses;

        for (Object e : a) {
            Double playerPoints = ((Map.Entry<String, Double>) e).getValue();
            if (playerPoints >= sPlus) {
                rank = "S+";
                System.out.print(RED_BACKGROUND_BRIGHT);
                System.out.print(TEXT_BLACK);
                D1 = count;
            } else if (playerPoints >= s) {
                rank = "S";
                System.out.print(RED_BACKGROUND_BRIGHT);
                System.out.print(TEXT_BLACK);
                D2 = count;
            } else if (playerPoints >= aPlus) {
                rank = "A+";
                System.out.print(TEXT_BRIGHT_BG_RED);
                System.out.print(TEXT_BLACK);
                D3 = count;
            } else if (playerPoints >= aa) {
                rank = "A";
                System.out.print(TEXT_BRIGHT_BG_RED);
                System.out.print(TEXT_BLACK);
                G1 = count;
            } else if (playerPoints >= bPlus) {
                rank = "B+";
                System.out.print(TEXT_BG_YELLOW);
                System.out.print(TEXT_BLACK);
                G2 = count;
            } else if (playerPoints >= bb) {
                rank = "B";
                System.out.print(TEXT_BG_YELLOW);
                System.out.print(TEXT_BLACK);
                G3 = count;
            } else if (playerPoints >= cPlus) {
                rank = "C+";
                System.out.print(ANSI_GREEN_BACKGROUND);
                System.out.print(TEXT_BLACK);
                S1 = count;
            } else if (playerPoints >= c) {
                rank = "C";
                System.out.print(ANSI_GREEN_BACKGROUND);
                System.out.print(TEXT_BLACK);
                S2 = count;
            } else if (playerPoints >= dPlus) {
                rank = "D+";
                System.out.print(TEXT_BG_BLUE);
                System.out.print(TEXT_BLACK);
                S3 = count;
            } else if (playerPoints >= d) {
                rank = "D";
                System.out.print(TEXT_BG_BLUE);
                System.out.print(TEXT_BLACK);
                B1 = count;
            } else if (playerPoints >= ePlus) {
                rank = "E+";
                System.out.print(CYAN_BACKGROUND);
                System.out.print(TEXT_BLACK);
                B2 = count;
            } else if (playerPoints >= ee) {
                rank = "E";
                System.out.print(CYAN_BACKGROUND);
                System.out.print(TEXT_BLACK);
                B3 = count;
            } else {
                rank = "F";
                ST = count;
                System.out.print(CYAN_BACKGROUND_BRIGHT);
                System.out.print(TEXT_BLACK);
                //System.out.print(TEXT_RESET);
            }
            count += 1;
            double E1 = 0, E2 = 0, E3 = 0, E4 = 0, E5 = 0, E6 = 0, E7 = 0, E8 = 0, E9 = 0, E10 = 0, E11 = 0, E12 = 0,
                    M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0, M6 = 0, M7 = 0, M8 = 0, M9 = 0, M10 = 0, M11 = 0, M12 = 0,
                    H1 = 0, H2 = 0, H3 = 0, H4 = 0, H5 = 0, H6 = 0, H7 = 0, H8 = 0, H9 = 0, H10 = 0, H11 = 0, H12 = 0;
            for (Level level : levels) {
                if (level.getIdentifier().equals("f98b8b5b-0e97-4b67-9b53-6a8cec883203_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E1 = ((Map.Entry<String, Double>) entry).getValue();
                            E1all = max(largest(E1, E1all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E1all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("f59ab9b3-be2b-457b-9447-151bfbfc60c8_v2")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E2 = ((Map.Entry<String, Double>) entry).getValue();
                            E2all = max(largest(E2, E2all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E2all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("308703f3-2472-4c02-96c1-51a68c95f69d_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E3 = ((Map.Entry<String, Double>) entry).getValue();
                            E3all = max(largest(E3, E3all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E3all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("73e9a4e5-a889-4e86-a2de-74dfebcab9fd_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E4 = ((Map.Entry<String, Double>) entry).getValue();
                            E4all = max(largest(E4, E4all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E4all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("cc23da23-df8b-4146-9047-ce0d32b909f6_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E5 = ((Map.Entry<String, Double>) entry).getValue();
                            E5all = max(largest(E5, E5all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E5all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("0947a2d5-84de-4312-a88a-2e0aaa3dbe4a_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E6 = ((Map.Entry<String, Double>) entry).getValue();
                            E6all = max(largest(E6, E6all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E6all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("48bcbe39-88b1-4de0-8010-96f56dbb3321_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E7 = ((Map.Entry<String, Double>) entry).getValue();
                            E7all = max(largest(E7, E7all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E7all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("1b280df4-7178-404d-b49b-96dc7e193688_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E8 = ((Map.Entry<String, Double>) entry).getValue();
                            E8all = max(largest(E8, E8all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E8all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("29ed42d1-4516-4892-aa15-9bc32e3c2ec0_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E9 = ((Map.Entry<String, Double>) entry).getValue();
                            E9all = max(largest(E9, E9all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E9all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("756876fd-dfc7-4430-8dd1-33798c7af4ad_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E10 = ((Map.Entry<String, Double>) entry).getValue();
                            E10all = max(largest(E10, E10all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E10all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("1d1b39eb-eb3f-4121-85af-7ddf1a81f396_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E11 = ((Map.Entry<String, Double>) entry).getValue();
                            E11all = max(largest(E11, E11all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E11all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("e9000a30-a0d2-4b9f-8d1c-de7d9c1f0bd5_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            E12 = ((Map.Entry<String, Double>) entry).getValue();
                            E12all = max(largest(E12, E12all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), E12all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("38b3e50a-9953-4936-8b82-081e7c7e6ead_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M1 = ((Map.Entry<String, Double>) entry).getValue();
                            M1all = max(largest(M1, M1all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M1all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("2d42ba13-8442-4fbd-b526-af460e0f1480_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M2 = ((Map.Entry<String, Double>) entry).getValue();
                            M2all = max(largest(M2, M2all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M2all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("acd93df5-0b3e-44e3-964b-d197c18c692d_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M3 = ((Map.Entry<String, Double>) entry).getValue();
                            M3all = max(largest(M3, M3all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M3all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("44094aa1-1aa2-4475-ad86-a0173f786524_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M4 = ((Map.Entry<String, Double>) entry).getValue();
                            M4all = max(largest(M4, M4all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M4all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("9f10ce76-2bba-4998-97ee-a5a368b53f1d_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M5 = ((Map.Entry<String, Double>) entry).getValue();
                            M5all = max(largest(M5, M5all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M5all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("0dabdaae-6b9f-46d3-9856-86bb7323aa5c_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M6 = ((Map.Entry<String, Double>) entry).getValue();
                            M6all = max(largest(M6, M6all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M6all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("f9e8dcd6-379a-4519-84e4-c1e5c61bf8ed_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M7 = ((Map.Entry<String, Double>) entry).getValue();
                            M7all = max(largest(M7, M7all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M7all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("bf0d5cdf-ffa0-4110-a5f2-7b851de64d33_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M8 = ((Map.Entry<String, Double>) entry).getValue();
                            M8all = max(largest(M8, M8all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M8all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("c11ee3e9-96a5-4174-ab6c-1849e0a9d3c1_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M9 = ((Map.Entry<String, Double>) entry).getValue();
                            M9all = max(largest(M9, M9all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M9all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("ea7b5b26-f61a-4ab1-920c-79eb950509f8_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M10 = ((Map.Entry<String, Double>) entry).getValue();
                            M10all = max(largest(M10, M10all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M10all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("45a879f4-9c8b-41bd-bbab-92031a15275e_v2")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M11 = ((Map.Entry<String, Double>) entry).getValue();
                            M11all = max(largest(M11, M11all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M11all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("c58f9e5f-23e8-433a-8fd9-09d38d47c67c_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            M12 = ((Map.Entry<String, Double>) entry).getValue();
                            M12all = max(largest(M12, M12all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), M12all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("1045bbcf-e605-46db-9d50-c525acb22e5f_v2")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H1 = ((Map.Entry<String, Double>) entry).getValue();
                            H1all = max(largest(H1, H1all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H1all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("bc8f13a4-d73a-473a-afe1-78867d4755a6_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H2 = ((Map.Entry<String, Double>) entry).getValue();
                            H2all = max(largest(H2, H2all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H2all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("97be4c7d-0a2d-4c1e-a844-6a1db4b4d740_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H3 = ((Map.Entry<String, Double>) entry).getValue();
                            H3all = max(largest(H3, H3all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H3all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("e287ada4-9939-4725-9f66-8fb14d6834f1_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H4 = ((Map.Entry<String, Double>) entry).getValue();
                            H4all = max(largest(H4, H4all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H4all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("2deccc40-217d-4853-b62b-1e70cf47888e_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H5 = ((Map.Entry<String, Double>) entry).getValue();
                            H5all = max(largest(H5, H5all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H5all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("3e62afb5-0700-4054-b841-475ca1fef08f_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H6 = ((Map.Entry<String, Double>) entry).getValue();
                            H6all = max(largest(H6, H6all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H6all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("643380e5-891a-417e-934d-ba79e23db3eb_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H7 = ((Map.Entry<String, Double>) entry).getValue();
                            H7all = max(largest(H7, H7all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H7all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("8dd20df7-8388-436e-9933-afa394377c5f_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H8 = ((Map.Entry<String, Double>) entry).getValue();
                            H8all = max(largest(H8, H8all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H8all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("0cb1ef10-68bf-4d13-83a5-13c98e9b8d74_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H9 = ((Map.Entry<String, Double>) entry).getValue();
                            H9all = max(largest(H9, H9all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H9all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("3f7a4d9a-e5e7-46f2-8fd2-1cd4ea9cdd91_v2")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H10 = ((Map.Entry<String, Double>) entry).getValue();
                            H10all = max(largest(H9, H9all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H10all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("7120a6dd-22e4-40f4-9629-e2ba9f836d9d_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H11 = ((Map.Entry<String, Double>) entry).getValue();
                            H11all = max(largest(H11, H11all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H11all);
                            break;
                        }
                    }
                } else if (level.getIdentifier().equals("3701462f-9f38-4e12-acf6-7e907df06b36_v1")) {
                    Object[] b = level.rankedpbs.entrySet().toArray();
                    for (Object entry : b) {
                        if (Objects.equals(((Map.Entry<String, Double>) entry).getKey(), ((Map.Entry<String, Double>) e).getKey())) {
                            H12 = ((Map.Entry<String, Double>) entry).getValue();
                            H12all = max(largest(H12, H12all, firstPlace, ((Map.Entry<String, Double>) e).getKey()), H12all);
                            break;
                        }
                    }
                }
            }

            String country = players.get(((Map.Entry<String, Double>) e).getKey()).getCountry();
            if (country == null) {
                country = "xx";
            }

            int rem = 0;
            int wrs = 0;
            if (true == true) {
                rem += isO(E1);
                rem += isO(E2);
                rem += isO(E3);
                rem += isO(E4);
                rem += isO(E5);
                rem += isO(E6);
                rem += isO(E7);
                rem += isO(E8);
                rem += isO(E9);
                rem += isO(E10);
                rem += isO(E11);
                rem += isO(E12);
                rem += isO(M1);
                rem += isO(M2);
                rem += isO(M3);
                rem += isO(M4);
                rem += isO(M5);
                rem += isO(M6);
                rem += isO(M7);
                rem += isO(M8);
                rem += isO(M9);
                rem += isO(M10);
                rem += isO(M11);
                rem += isO(M12);
                rem += isO(H1);
                rem += isO(H2);
                rem += isO(H3);
                rem += isO(H4);
                rem += isO(H5);
                rem += isO(H6);
                rem += isO(H7);
                rem += isO(H8);
                rem += isO(H9);
                rem += isO(H10);
                rem += isO(H11);
                rem += isO(H12);
                wrs += isWr(E1);
                wrs += isWr(E2);
                wrs += isWr(E3);
                wrs += isWr(E4);
                wrs += isWr(E5);
                wrs += isWr(E6);
                wrs += isWr(E7);
                wrs += isWr(E8);
                wrs += isWr(E9);
                wrs += isWr(E10);
                wrs += isWr(E11);
                wrs += isWr(E12);
                wrs += isWr(M1);
                wrs += isWr(M2);
                wrs += isWr(M3);
                wrs += isWr(M4);
                wrs += isWr(M5);
                wrs += isWr(M6);
                wrs += isWr(M7);
                wrs += isWr(M8);
                wrs += isWr(M9);
                wrs += isWr(M10);
                wrs += isWr(M11);
                wrs += isWr(M12);
                wrs += isWr(H1);
                wrs += isWr(H2);
                wrs += isWr(H3);
                wrs += isWr(H4);
                wrs += isWr(H5);
                wrs += isWr(H6);
                wrs += isWr(H7);
                wrs += isWr(H8);
                wrs += isWr(H9);
                wrs += isWr(H10);
                wrs += isWr(H11);
                wrs += isWr(H12);
            }

            if ((((Map.Entry<String, Double>) e).getKey()).equals(firstPlace)) {
                firstScore = ((Map.Entry<String, Double>) e).getValue();
            }

            //  if (country.equals("AU")) {
            //if ((((Map.Entry<String, Double>) e).getKey()).equals("SuperFire")) {
                System.out.format("%-4s %-5s %-5s %-4s %-15s %-11s %-5s" +
                                "%-5s %-5s %-5s %-5s %-5s %-5s %-5s " +
                                "%-5s %-5s %-5s %-5s %-5s %-5s %-5s " +
                                "%-5s %-5s %-5s %-5s %-5s %-5s %-5s " +
                                "%-5s %-5s %-5s %-5s %-5s %-5s %-5s " +
                                "%-5s %-5s %-5s %-5s %-5s %-5s %-5s %-2s",
                        wrs, count - 1 + ".", rank, country, ((Map.Entry<String, Double>) e).getKey(), ((Map.Entry<String, Double>) e).getValue(), rem,
                        (int) E1, (int) E2, (int) E3, (int) E4, (int) E5, (int) E6, (int) E7, (int) E8, (int) E9, (int) E10, (int) E11, (int) E12,
                        (int) M1, (int) M2, (int) M3, (int) M4, (int) M5, (int) M6, (int) M7, (int) M8, (int) M9, (int) M10, (int) M11, (int) M12,
                        (int) H1, (int) H2, (int) H3, (int) H4, (int) H5, (int) H6, (int) H7, (int) H8, (int) H9, (int) H10, (int) H11, (int) H12 + "\n");
            //}
        }
        //Hashmap sorter taken from internet (87 - 97) ^ https://stackoverflow.com/questions/21054415/how-to-sort-a-hashmap-by-the-integer-value

        DecimalFormat df = new DecimalFormat("##.##");
        double DD1 = D1;
        double DD2 = (D2 - DD1);
        double DD3 = (D3 - DD2);
        double GG1 = G1 - DD3;
        double GG2 = G2 - GG1;
        double GG3 = G3 - GG2;
        double SS1 = (int) S1 - GG3;
        double SS2 = (int) S2 - SS1;
        double SS3 = (int) S3 - SS2;
        double BB1 = (int) B1 - SS3;
        double BB2 = (int) B2 - BB1;
        double BB3 = (int) B3 - BB2;
        double SST = (int) ST - BB3;
        System.out.println(TEXT_RESET);

        DecimalFormat dt = new DecimalFormat("##.#");
        System.out.println("S+: " + Double.parseDouble(dt.format(sPlus)) + " " + DD1 + " users (Top " + Double.parseDouble(df.format((double) 100 * D1 / (count - 1))) + "%)");
        System.out.println("S:  " + Double.parseDouble(dt.format(s)) + " " + DD2 + " users (Top " + Double.parseDouble(df.format((double) 100 * D2 / (count - 1))) + "%)");
        System.out.println("A+: " + Double.parseDouble(dt.format(aPlus)) + " " + DD3 + " users (Top " + Double.parseDouble(df.format((double) 100 * D3 / (count - 1))) + "%)");
        System.out.println("A:  " + Double.parseDouble(dt.format(aa)) + " " + GG1 + " users (Top " + Double.parseDouble(df.format((double) 100 * G1 / (count - 1))) + "%)");
        System.out.println("B+: " + Double.parseDouble(dt.format(bPlus)) + " " + GG2 + " users (Top " + Double.parseDouble(df.format((double) 100 * G2 / (count - 1))) + "%)");
        System.out.println("B:  " + Double.parseDouble(dt.format(bb)) + " " + GG3 + " users (Top " + Double.parseDouble(df.format((double) 100 * G3 / (count - 1))) + "%)");
        System.out.println("C+: " + Double.parseDouble(dt.format(cPlus)) + " " + SS1 + " users (Top " + Double.parseDouble(df.format((double) 100 * S1 / (count - 1))) + "%)");
        System.out.println("C:  " + Double.parseDouble(dt.format(c)) + " " + SS2 + " users (Top " + Double.parseDouble(df.format((double) 100 * S2 / (count - 1))) + "%)");
        System.out.println("D+: " + Double.parseDouble(dt.format(dPlus)) + " " + SS3 + " users (Top " + Double.parseDouble(df.format((double) 100 * S3 / (count - 1))) + "%)");
        System.out.println("D:  " + Double.parseDouble(dt.format(d)) + " " + BB1 + " users (Top " + Double.parseDouble(df.format((double) 100 * B1 / (count - 1))) + "%)");
        System.out.println("E+: " + Double.parseDouble(dt.format(ePlus)) + "  " + BB2 + " users (Top " + Double.parseDouble(df.format((double) 100 * B2 / (count - 1))) + "%)");
        System.out.println("E:  " + Double.parseDouble(dt.format(ee)) + "  " + BB3 + " users (Top " + Double.parseDouble(df.format((double) 100 * B3 / (count - 1))) + "%)");
        System.out.println("F:          " + SST + " users (Top " + Double.parseDouble(df.format((double) 100 * ST / (count - 1))) + "%)");

        System.out.println(firstPlace + ": " + firstScore);
        System.out.print("Rest Of World: ");
        System.out.println(E1all + E2all + E3all + E4all + E5all + E6all + E7all + E8all + E9all + E10all + E11all + E12all +
                M1all + M2all + M3all + M4all + M5all + M6all + M7all + M8all + M9all + M10all + M11all + M12all +
                H1all + H2all + H3all + H4all + H5all + H6all + H7all + H8all + H9all + H10all + H11all + H12all);

    }

    public static double largest(double level, double all, String first, String current) {
        if (!first.equals(current) && level >= all) {
            return level;
        } else {
            return all;
        }
    }

    public static int isO(double level) {
        if (level == 0.0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int isWr(double level) {
        if (level == 1000.0) {
            return 1;
        } else {
            return 0;
        }
    }

    static class Level {
        public String identifier;
        public HashMap<Integer, Highscore> rankedHighscores;
        public HashMap<String, Double> rankedpbs;

        public Level(String identifier) {
            this.identifier = identifier;
            this.rankedHighscores = new HashMap<>();
            this.rankedpbs = new HashMap<>();
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    static class Player {
        public String name;
        public String country;
        public HashMap<Level, Highscore> highscores;

        public Player(String name, String country) {
            this.name = name;
            this.highscores = new HashMap<>();
            this.country = country;
        }

        public String getCountry() {
            return country;
        }
    }

    static class Highscore {
        public int time_ms;
        public Level level;
        public Player player;
        public String datetime;
        public int rank;
        public String country;

        public Highscore(Level level, Player player, int time_ms, String datetime, int rank, String country) {
            this.level = level;
            this.player = player;
            this.time_ms = time_ms;
            this.datetime = datetime;
            this.rank = rank;
            this.country = country;
        }
    }
}
