package com.cartrack.mobile.codingchallenge.login.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LoginTable::class], version = 1, exportSchema = false)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun loginDoa(): LoginDao

    companion object {
        private var INSTANCE: LoginDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context, scope: CoroutineScope): LoginDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoginDatabase::class.java,
                    "LOGIN_DATABASE"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(LoginDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class LoginDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.loginDoa()?.let { populateDatabase(it) }
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(loginDb: LoginDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            loginDb.deleteAllData()
            val data = LoginTable()

            data.username = "admin"
            data.password = "admin"
            data.country = "Singapore"
            loginDb.insertDetails(data)
            data.username = "guest"
            data.password = "guest"
            data.country = "Singapore"
            loginDb.insertDetails(data)
            data.username = "cartrack"
            data.password = "password@123"
            data.country = "Singapore"
            loginDb.insertDetails(data)
            data.username = "devtest"
            data.password = "password1"
            data.country = "India"
            loginDb.insertDetails(data)
        }
    }
}