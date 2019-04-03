package com.example.crudcontry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

public class mydbhelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Nom de Ma base de donnée
    private static final String DATABASE_NAME = "countryData";

    // Nom de la table
    private static final String TABLE_COUNTRY= "Country";

    // les Nom des colonnes de ma table
    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "CountryName";
    private static final String POPULATION = "Population";

    // le constructeur de ma classe
    public mydbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ici on vas créer la table country
    // la méthode oncreate n'est applée qu'une seul fois dans le programme quand on instancis notre classe mydbhelper
    // elle peut étre appler dans upgrade méthode
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + TABLE_COUNTRY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + POPULATION + " TEXT,"
                + COUNTRY_NAME + " LONG" + ")";
        db.execSQL(CREATE_COUNTRY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprimer la table si elle exsiste
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        // recreer la
        onCreate(db);
    }
     /*
     * ici On Vas Ajouter Nos Méthode de CRUD */

    // Adding new country
    // on utilisant la classe country
    void addCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // ContentValues est un conteneur des Valeurs qui as 2 paramétre le nom de column et la valeur
        values.put(COUNTRY_NAME, country.getCountryName()); // Nom de la country
        values.put(POPULATION, country.getPopulation()); // La population

        // inserer la ligne avec la méthode insert qui prend en paramétre le nom de la table , les columns mais cette valeur est null si on veux inserer tous les champs , et aprés on donne les valeurs
        db.insert(TABLE_COUNTRY, null, values);
        //db.insert return un nombre
        db.close(); // fermer la connexion  avec la base de donnée
    }

    //Faire Retourner un seul country avec la méthode getcountry()
    Country getCountry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
// On Utilise l'objet Cursor qui vas contenir les données reçu
        // la méthode query qui retourne les données
        // le premier paramétre est le nom de la table , le 2 est les columns as affichée ? , le 3 est  lwhere , le 4 et les arguments de where (?)
        Cursor cursor = db.query(TABLE_COUNTRY, new String[] { KEY_ID,
                        COUNTRY_NAME, POPULATION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, POPULATION, null);
        if (cursor != null)
            cursor.moveToFirst();

        Country country = new Country(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getLong(2));
        return country;
    }


    // Getting All Countries
    public List<Country> getAllCountries() {
        // Méthode qui vas retourner tous les countries
        // Définir une liste des contry
        List<Country> countryList = new ArrayList<Country>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COUNTRY;

        SQLiteDatabase db = this.getWritableDatabase();
        // Rawquery est la méme que query sauf qu'il nécissite pas beaucoup de paramétre  utilise là si tu n'as qu'une seul condition
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looper et ajouter dans la liste
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(Integer.parseInt(cursor.getString(0)));
                country.setCountryName(cursor.getString(1));
                country.setPopulation(cursor.getLong(2));
                // Adding country to list
                countryList.add(country);
            } while (cursor.moveToNext());
        }

        // return country list
        return countryList;
    }

    // Updating single country
    public int updateCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, country.getCountryName());
        values.put(POPULATION, country.getPopulation());

        // updating row
        return db.update(TABLE_COUNTRY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
    }

    // Deleting single country
    public void deleteCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY, KEY_ID + " = ?",
                new String[] { String.valueOf(country.getId()) });
        db.close();
    }

    // Deleting all countries
    public void deleteAllCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRY,null,null);
        db.close();
    }

    // Getting countries Count
    public int getCountriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUNTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}