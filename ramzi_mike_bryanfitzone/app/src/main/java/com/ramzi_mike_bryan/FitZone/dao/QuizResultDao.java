package com.ramzi_mike_bryan.FitZone.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QuizResultDao {

    private final FitZoneDbHelper dbHelper;

    public QuizResultDao(Context context) {
        dbHelper = FitZoneDbHelper.getInstance(context);
    }

    public void enregistrerResultat(String userId, String quizId, int score, int total) {

        String date = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm", Locale.CANADA
        ).format(new java.util.Date());

        ContentValues valeurs = new ContentValues();
        valeurs.put(FitZoneDbHelper.COL_USER_ID, userId);
        valeurs.put(FitZoneDbHelper.COL_QUIZ_ID, quizId);
        valeurs.put(FitZoneDbHelper.COL_SCORE, score);
        valeurs.put(FitZoneDbHelper.COL_TOTAL, total);
        valeurs.put(FitZoneDbHelper.COL_DATE, date);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertWithOnConflict(
                FitZoneDbHelper.TABLE_QUIZ_RESULTS,
                null,
                valeurs,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public QuizResult getResultat(String userId, String quizId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor curseur = db.query(
                FitZoneDbHelper.TABLE_QUIZ_RESULTS,
                null,
                FitZoneDbHelper.COL_USER_ID + " = ? AND " + FitZoneDbHelper.COL_QUIZ_ID + " = ?",
                new String[]{userId, quizId},
                null, null, null
        );

        QuizResult resultat = null;

        if (curseur.moveToFirst()) {

            resultat = new QuizResult(
                    quizId,
                    curseur.getInt(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_SCORE)),
                    curseur.getInt(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_TOTAL)),
                    curseur.getString(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_DATE))
            );
        }

        curseur.close();
        return resultat;
    }

    public Map<String, QuizResult> getResultatsUtilisateur(String userId) {

        Map<String, QuizResult> resultats = new HashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor curseur = db.query(
                FitZoneDbHelper.TABLE_QUIZ_RESULTS,
                null,
                FitZoneDbHelper.COL_USER_ID + " = ?",
                new String[]{userId},
                null, null, null
        );

        while (curseur.moveToNext()) {

            String quizId = curseur.getString(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_QUIZ_ID));

            resultats.put(quizId, new QuizResult(
                    quizId,
                    curseur.getInt(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_SCORE)),
                    curseur.getInt(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_TOTAL)),
                    curseur.getString(curseur.getColumnIndexOrThrow(FitZoneDbHelper.COL_DATE))
            ));
        }

        curseur.close();
        return resultats;
    }
}
