package com.inf1nlty.extremeforgingadditions.api;

public interface EFAForgingTable {

    int MODE_UPGRADING = 0;
    int MODE_DEFUSING = 1;
    int MODE_APPLYING = 2;
    int MODE_FORGING = 3;
    int MODE_COUNT = 4;

    int efa$getForgingMode();

    void efa$setForgingMode(int mode);

    boolean efa$isForging();
}
