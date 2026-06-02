package com.inf1nlty.extremeforgingadditions.api;

public interface EFAForgingTableGui {

    int efa$getBlockX();

    int efa$getBlockY();

    int efa$getBlockZ();

    void efa$refreshBlockCoordinates();

    boolean efa$isAtBlock(int x, int y, int z);

    void efa$setBlockMode(int mode);
}
