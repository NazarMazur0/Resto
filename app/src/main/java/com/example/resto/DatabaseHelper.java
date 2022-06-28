package com.example.resto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLES = "TABLES";
    public static final String ID = "ID";
    public static final String IS_BOOKED = "IS_BOOKED";
    public static final String CLIENT_NAME = "CLIENT_NAME";
    public static final String CLIENT_TELEPHONE = "CLIENT_TELEPHONE";
    public static final String BOOKING_TIME = "BOOKING_TIME";
    public static final String TABLE_NUMBER ="TABLE_NUMBER";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "table", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createQuery= "CREATE TABLE IF NOT EXISTS " + TABLES + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + IS_BOOKED + " BOOL," + CLIENT_NAME + " TEXT," + CLIENT_TELEPHONE + " INTEGER," + BOOKING_TIME + " TEXT,"+ TABLE_NUMBER +" INTEGER )";
        Log.d("query",createQuery);
        database.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
    public ArrayList<Table> selectBooked(){
        String select = "SELECT * FROM "+TABLES+" WHERE "+ IS_BOOKED+"=1";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor= database.rawQuery(select,null);
        ArrayList<Table> bookedTables = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                int tableId = cursor.getInt(0);
                String clientName=cursor.getString(2);
                long clientTelephone=cursor.getLong(3);
                String bookingTime=cursor.getString(4);
                int tableNumber=cursor.getInt(5);
                bookedTables.add(new Table(tableId,true,clientName,clientTelephone,bookingTime , tableNumber ) );
            } while (cursor.moveToNext());
        }
        else {
            Log.d("db","Databe connection failure");
        }
        cursor.close();
        database.close();
        return bookedTables;
    }
    public ArrayList<Table> selectUnBooked(){
        String select = "SELECT * FROM "+TABLES+" WHERE "+ IS_BOOKED+"=0";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor= database.rawQuery(select,null);
        ArrayList<Table> bookedTables = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                int tableId = cursor.getInt(0);
                int tableNumber=cursor.getInt(5);
                bookedTables.add(new Table(tableId,false,"",0,"",tableNumber ) );
            } while (cursor.moveToNext());
        }
        else {
            Log.d("db","Databe connection failure");
        }
        cursor.close();
        database.close();
        return bookedTables;
    }
    public boolean addOne(Table table){
        ContentValues cv = new ContentValues();
        cv.put(IS_BOOKED, table.getIsBooked());
        cv.put(CLIENT_NAME, table.getClientName());
        cv.put(CLIENT_TELEPHONE, table.getClientPhone());
        cv.put(BOOKING_TIME, table.getBookingTime());
        cv.put(TABLE_NUMBER,table.getTableNumber());
        Log.d("Dbtable=",""+table.getTableNumber());
        SQLiteDatabase db= getWritableDatabase();
        boolean isDone= db.insert(TABLES, null, cv) != -1;
        db.close();
        return isDone;
    }
    public boolean updateOne(Table table){
        ContentValues cv = new ContentValues();
        cv.put(IS_BOOKED, table.getIsBooked());
        cv.put(CLIENT_NAME, table.getClientName());
        cv.put(CLIENT_TELEPHONE, table.getClientPhone());
        cv.put(BOOKING_TIME, table.getBookingTime());
        SQLiteDatabase db= getWritableDatabase();
        boolean isDone= db.update(TABLES, cv, "id = ?",new String[] {String.format("%d" , table.getId(), Locale.ROOT)}) != -1;
        db.close();
        return isDone;
    }

    public boolean clearAll(){
        SQLiteDatabase db=getWritableDatabase();
        boolean isDone= db.delete(TABLES,null,null)==1;
        return isDone;
    }
    public Table getLast(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLES+" ORDER BY "+ID+" DESC LIMIT 1;" ,null);
        cursor.moveToFirst();
        int tableId = cursor.getInt(0);
        boolean isBooked=cursor.getInt(1)==1;
        String clientName=cursor.getString(2);
        long clientTelephone=cursor.getLong(3);
        String bookingTime=cursor.getString(4);
        int tableNumber=cursor.getInt(5);
        db.close();
        cursor.close();
        return new Table(tableId,isBooked,clientName,clientTelephone,bookingTime,tableNumber);
    }
    public Table getOne(int id){
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLES+" WHERE ID="+id,null);
        cursor.moveToFirst();
        int tableId = cursor.getInt(0);
        boolean isBooked=cursor.getInt(1)==1;
        String clientName=cursor.getString(2);
        long clientTelephone=cursor.getLong(3);
        String bookingTime=cursor.getString(4);
        int tableNumber=cursor.getInt(5);
        db.close();
        cursor.close();
        return new Table(tableId,isBooked,clientName,clientTelephone,bookingTime,tableNumber);
    }
}
