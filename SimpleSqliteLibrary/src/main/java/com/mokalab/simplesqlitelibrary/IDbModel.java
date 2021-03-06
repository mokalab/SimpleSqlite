package com.mokalab.simplesqlitelibrary;

/**
 * Implement this interface in your models that support Database Table association.<br>
 * It is used by {@link DbSelect}.
 *
 * <br><br>
 * Created by Pirdad S. on 2014-06-09.
 */
public interface IDbModel {

    /**
     * Returns the Database Table's Associated Id.
     * <br><br>
     * Created by Pirdad S.
     */
    public abstract long getDbAssociatedId();

    /**
     * Sets Database Table's Associated Id.
     * <br><br>
     * Created by Pirdad S.
     */
    public abstract void setDbAssociatedId(long dbAssociatedId);
}
