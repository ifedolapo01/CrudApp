import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper  {


    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "UsersDB";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PHOTO = "photo";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";


    public DatabaseHandler(Context context) {
        super(context, AppConfig.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_PHOTO + " BLOB, "
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DEPARTMENT + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT)";
        db.execSQL(CREATE_USER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void adduser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY,userModel.getCategory());
        values.put(COLUMN_PHOTO, userModel.getByteBuffer());
        values.put(COLUMN_NAME, userModel.getName());
        values.put(COLUMN_DEPARTMENT, userModel.getDepartment());
        values.put(COLUMN_PHONE, userModel.getPhone());
        values.put(COLUMN_EMAIL, userModel.getEmail());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateUser(UserModel userModel, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY,userModel.getCategory());
        values.put(COLUMN_PHOTO, userModel.getByteBuffer());
        values.put(COLUMN_NAME, userModel.getName());
        values.put(COLUMN_DEPARTMENT, userModel.getDepartment());
        values.put(COLUMN_PHONE, userModel.getPhone());
        values.put(COLUMN_EMAIL, userModel.getEmail());

        // Updating Row
        db.update(TABLE_NAME,  values,"id="+id,null);
        db.close();
    }

    public UserModel getUser(int index) {

        String query = "SELECT "+ COLUMN_ID + ","
                + COLUMN_CATEGORY + ","
                + COLUMN_PHOTO + ","
                + COLUMN_NAME + ","
                + COLUMN_DEPARTMENT + ","
                + COLUMN_PHONE + ","
                + COLUMN_EMAIL +
                " FROM "+ TABLE_NAME +" WHERE "+ COLUMN_ID +" = "+ index;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        UserModel userModel = null;

        if (cursor.moveToFirst()) {
            userModel  = new UserModel();
            userModel.setId(Integer.parseInt(cursor.getString(0)));
            userModel.setCategory(cursor.getString(1));
            userModel.setByteBuffer(cursor.getBlob(2));
            userModel.setName(cursor.getString(3));
            userModel.setDepartment(cursor.getString(4));
            userModel.setPhone(cursor.getString(5));
            userModel.setEmail(cursor.getString(6));
        }

        return userModel;
    }

    public List<UserModel> allUsers() {

        SQLiteDatabase db = this.getWritableDatabase();

        List<UserModel> userModels = new ArrayList<>();

        String query = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID +" DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel userModel = new UserModel();
                userModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                userModel.setByteBuffer(cursor.getBlob(cursor.getColumnIndex(COLUMN_PHOTO)));
                userModels.add(userModel);
            } while (cursor.moveToNext());
        }
        return userModels;
    }

    public void deleteUser(int index) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query  = "DELETE FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID +" = " + index;

        db.execSQL(query);
        db.close();

    }

}