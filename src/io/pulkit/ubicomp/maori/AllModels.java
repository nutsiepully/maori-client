package io.pulkit.ubicomp.maori;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AllModels {

    private DatabaseHelper databaseHelper;

    public AllModels(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void add(Model model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", model.getName());
        contentValues.put("version", model.getVersion());
        contentValues.put("current", model.isActive());
        contentValues.put("payload", model.getPayload());

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.insert("models", null, contentValues);
        db.close();
    }

    public Model getActive(String name) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from models where name = ? and current = true", new String[]{name});

        if (cursor == null) throw new ModelNotFoundException(name);
        cursor.moveToFirst();

        Model model = new Model(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                intToBoolean(cursor.getInt(3)), cursor.getBlob(4));

        db.close();
        return model;
    }

    public List<ModelInfo> getModelInfo() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name, version, active from models", null);

        List<ModelInfo> modelInfoList = new ArrayList<ModelInfo>();
        while (cursor.moveToNext()) {
            modelInfoList.add(new ModelInfo(cursor.getString(0),
                    cursor.getString(1), intToBoolean(cursor.getInt(2))));
        }

        db.close();
        return modelInfoList;
    }

    private boolean intToBoolean(int intValue) {
        return intValue != 0 ? true : false;
    }

    public void delete(String name, String version) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("models", "name = ? and version = ?", new String[]{name, version});
    }

    public void updateActiveState(String name, String version, boolean active) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("active", active);
        db.update("models", contentValues, "name = ? and version = ?", new String[] {name, version});
    }
}
