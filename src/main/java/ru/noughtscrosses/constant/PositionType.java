package ru.noughtscrosses.constant;

import java.util.List;

public enum PositionType {
    L_1_1,L_1_2,L_1_3,//
    L_2_1,L_2_2,L_2_3,//
    L_3_1,L_3_2,L_3_3;

    public static List<List<PositionType>> victoryScheme = List.of(
        List.of(L_1_1,L_1_2,L_1_3),
        List.of(L_2_1,L_2_2,L_2_3),
        List.of(L_3_1,L_3_2,L_3_3),
        List.of(L_1_1,L_2_1,L_3_1),
        List.of(L_1_2,L_2_2,L_3_2),
        List.of(L_1_3,L_2_3,L_3_3),
        List.of(L_1_1,L_2_2,L_3_3),
        List.of(L_1_3,L_2_2,L_3_1));

}
