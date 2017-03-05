package com.gani.lib.ui.list;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

import com.gani.lib.logging.GLog;
import com.gani.lib.select.DtoBindingHolder;

import java.util.List;

public abstract class DtoRecyclerAdapter<DO, VH
    extends DtoBindingHolder<DO>> extends RecyclerView.Adapter<VH> {
//    implements Filterable, CursorFilter.CursorFilterClient {
//  private boolean mDataValid;
//  private int mRowIDColumn;
//  private Cursor mCursor;
//  private ChangeObserver mChangeObserver;
//  private DataSetObserver mDataSetObserver;
//  private CursorFilter mCursorFilter;
//  private FilterQueryProvider mFilterQueryProvider;
//
//  public DtoRecyclerAdapter(Cursor cursor) {
//    init(cursor);
//  }
//
//  void init(Cursor c) {
//    boolean cursorPresent = c != null;
//    mCursor = c;
//    mDataValid = cursorPresent;
//    mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
//
//    mChangeObserver = new ChangeObserver();
//    mDataSetObserver = new MyDataSetObserver();
//
//    if (cursorPresent) {
//      if (mChangeObserver != null) c.registerContentObserver(mChangeObserver);
//      if (mDataSetObserver != null) c.registerDataSetObserver(mDataSetObserver);
//    }
//  }

  private List<DO> objects;

  protected DtoRecyclerAdapter(List<DO> objects) {
    this.objects = objects;
  }

  public void initForList(RecyclerView recyclerView) {
    initForList(recyclerView, true);
  }

  public RecyclerListHelper initForList(RecyclerView recyclerView, boolean withSeparator) {
    Context context = recyclerView.getContext();
    if (withSeparator) {
      recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }
    recyclerView.setAdapter(this);
    return new RecyclerListHelper(recyclerView);
  }

  @Override
  public final void onBindViewHolder(VH holder, int i) {
    holder.update(getItem(i));
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

//  /**
//   * @see android.widget.ListAdapter#getItemId(int)
//   */
//  @Override
//  public long getItemId(int position) {
////    if (mDataValid && mCursor != null) {
////      if (mCursor.moveToPosition(position)) {
////        return mCursor.getLong(mRowIDColumn);
////      } else {
////        return 0;
////      }
////    } else {
////      return 0;
////    }
//    return objects.get(position);
//  }

  public DO getItem(int position) {
    return objects.get(position);
  }

//  public Cursor getCursor() {
//    return mCursor;
//  }

//  /**
//   * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
//   * closed.
//   *
//   * @param cursor The new cursor to be used
//   */
//  public void changeCursor(Cursor cursor) {
//    Cursor old = swapCursor(cursor);
//    if (old != null) {
//      old.close();
//    }
//  }
//
//  /**
//   * Swap in a new Cursor, returning the old Cursor.  Unlike
//   * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
//   * closed.
//   *
//   * @param newCursor The new cursor to be used.
//   * @return Returns the previously set Cursor, or null if there wasa not one.
//   * If the given new Cursor is the same getInstance is the previously set
//   * Cursor, null is also returned.
//   */
//  public Cursor swapCursor(Cursor newCursor) {
//    if (newCursor == mCursor) {
//      return null;
//    }
//    Cursor oldCursor = mCursor;
//    if (oldCursor != null) {
//      if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
//      if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
//    }
//    mCursor = newCursor;
//    if (newCursor != null) {
//      if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
//      if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
//      mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
//      mDataValid = true;
//      // notify the observers about the new cursor
//      notifyDataSetChanged();
//    } else {
//      mRowIDColumn = -1;
//      mDataValid = false;
//      // notify the observers about the lack of a data set
//      // notifyDataSetInvalidated();
//      notifyItemRangeRemoved(0, getItemCount());
//    }
//    return oldCursor;
//  }

//  /**
//   * <p>Converts the cursor into a CharSequence. Subclasses should override this
//   * method to convert their results. The default implementation returns an
//   * empty String for null values or the default String representation of
//   * the value.</p>
//   *
//   * @param cursor the cursor to convert to a CharSequence
//   * @return a CharSequence representing the value
//   */
//  public CharSequence convertToString(Cursor cursor) {
//    return cursor == null ? "" : cursor.toString();
//  }

//  /**
//   * Runs a query with the specified constraint. This query is requested
//   * by the filter attached to this adapter.
//   * <p>
//   * The query is provided by a
//   * {@link FilterQueryProvider}.
//   * If no provider is specified, the createCurrent cursor is not filtered and returned.
//   * <p>
//   * After this method returns the resulting cursor is passed to {@link #changeCursor(Cursor)}
//   * and the previous cursor is closed.
//   * <p>
//   * This method is always executed on a background thread, not on the
//   * application's main thread (or UI thread.)
//   * <p>
//   * Contract: when constraint is null or empty, the original results,
//   * prior to any filtering, must be returned.
//   *
//   * @param constraint the constraint with which the query must be filtered
//   * @return a Cursor representing the results of the new query
//   * @see #getFilter()
//   * @see #getFilterQueryProvider()
//   * @see #setFilterQueryProvider(FilterQueryProvider)
//   */
//  public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
//    if (mFilterQueryProvider != null) {
//      return mFilterQueryProvider.runQuery(constraint);
//    }
//
//    return mCursor;
//  }
//
//  public Filter getFilter() {
//    if (mCursorFilter == null) {
//      mCursorFilter = new CursorFilter(this);
//    }
//    return mCursorFilter;
//  }
//
//  /**
//   * Returns the query filter provider used for filtering. When the
//   * provider is null, no filtering occurs.
//   *
//   * @return the createCurrent filter query provider or null if it does not exist
//   * @see #setFilterQueryProvider(FilterQueryProvider)
//   * @see #runQueryOnBackgroundThread(CharSequence)
//   */
//  public FilterQueryProvider getFilterQueryProvider() {
//    return mFilterQueryProvider;
//  }
//
//  /**
//   * Sets the query filter provider used to filter the createCurrent Cursor.
//   * The provider's
//   * {@link FilterQueryProvider#runQuery(CharSequence)}
//   * method is invoked when filtering is requested by a client of
//   * this adapter.
//   *
//   * @param filterQueryProvider the filter query provider or null to remove it
//   * @see #getFilterQueryProvider()
//   * @see #runQueryOnBackgroundThread(CharSequence)
//   */
//  public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
//    mFilterQueryProvider = filterQueryProvider;
//  }
//
//  /**
//   * Called when the {@link ContentObserver} on the cursor receives a change notification.
//   * Can be implemented by sub-class.
//   *
//   * @see ContentObserver#onChange(boolean)
//   */
//  protected void onContentChanged() {
//
//  }

//  private class ChangeObserver extends ContentObserver {
//    public ChangeObserver() {
//      super(new Handler());
//    }
//
//    @Override
//    public boolean deliverSelfNotifications() {
//      return true;
//    }
//
//    @Override
//    public void onChange(boolean selfChange) {
//      onContentChanged();
//    }
//  }
//
//  private class MyDataSetObserver extends DataSetObserver {
//    @Override
//    public void onChanged() {
//      mDataValid = true;
//      notifyDataSetChanged();
//    }
//
//    @Override
//    public void onInvalidated() {
//      mDataValid = false;
//      // notifyDataSetInvalidated();
//      notifyItemRangeRemoved(0, getItemCount());
//    }
//  }

}