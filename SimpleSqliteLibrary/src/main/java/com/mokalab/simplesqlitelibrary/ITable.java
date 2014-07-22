package com.mokalab.simplesqlitelibrary;

import org.jetbrains.annotations.NotNull;

/**
 * Use this interface to create your Tables. It gets used by
 * {@link TablesHelper} to generate drop/create
 * statements which in return gets used by the {@link DatabaseHelper}.
 * <br><br>
 * Must follow:<br>
 * - Table Name can't have spaces.<br>
 * - Columns must have the default "_id" column which you can use:<br>
 *     {@link TablesHelper#DEFAULT_ID_COLUMN_NAME}<br>
 *     {@link TablesHelper#DEFAULT_ID_COLUMN_TYPE}<br>
 *     {@link TablesHelper#DEFAULT_ID_COLUMN_OPTIONS}<br>
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-09.
 */
public interface ITable {

    /**
     * Name of the Table
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public String getTableName();

    /**
     * Name of the Columns<br>
     * Ex. Column name in <b>'title text not null'</b><br>
     * is <b>'title'</b>
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public String[] getColumnNames();

    /**
     * Type of the Columns<br>
     * Ex. Column type in <b>'title text not null'</b><br>
     * is <b>'text'</b>
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public String[] getColumnTypes();

    /**
     * [Optional] Options for the Columns<br>
     * Ex. Column options in <b>'title text not null'</b><br>
     * is <b>'not null'</b>
     *
     * <br><br>
     * Created by Pirdad S.
     */
    @NotNull
    public String[] getColumnOptions();
}
