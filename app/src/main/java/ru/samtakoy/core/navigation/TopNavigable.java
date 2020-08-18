package ru.samtakoy.core.navigation;

public interface TopNavigable {

    default boolean isNavigationItemSet() {
        return true;
    }

    int getTopMenuItemId();
}
