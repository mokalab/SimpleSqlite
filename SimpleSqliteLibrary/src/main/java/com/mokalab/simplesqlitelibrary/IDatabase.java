package com.mokalab.simplesqlitelibrary;

import org.jetbrains.annotations.NotNull;

/**
 * Implement this interface with your Database Model.
 * It will be used by {@link DatabaseHelper} to
 * create/upgrade your databases.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-09.
 */
public interface IDatabase {

    /**
     * Returns the Name of the Database.
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public String getDatabaseName();

    /**
     * Returns the Version of the Database.
     * <br><br>
     * Created by Pirdad S.
     */
    public int getDatabaseVersion();

    /**
     * Returns the Database Table Models.
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public ITable[] getDatabaseTables();
}
