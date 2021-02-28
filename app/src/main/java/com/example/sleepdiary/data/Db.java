package com.example.sleepdiary.data;

/**
 * Application database tables
 */
public abstract class Db {

    /**
     * User table
     */
    public static class user {
        public static final String TABLE_NAME  = "user";
        public static final String COLUMN_ID   = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GOAL = "goal";

        /**
         * Creates SQL string for table creation, resets
         * StringBuilder.
         * @param sb Empty StringBuilder
         * @return SQL statement for user table creation.
         */
        public static String createTableSQL(StringBuilder sb) {
            sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
                    .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(COLUMN_NAME).append(" TEXT NOT NULL,")
                    .append(COLUMN_GOAL).append(" INTEGER")
                    .append(");");
            String SQL_Statement = sb.toString();
            sb.delete(0, sb.length());
            return SQL_Statement;
        }
    }

    /**
     * Sleep table
     */
    public static class sleep {
        public static final String TABLE_NAME        = "sleep";
        public static final String COLUMN_ID         = "id";
        public static final String COLUMN_USER_ID    = "user_id";
        public static final String COLUMN_END_TIME   = "end_timestamp";
        public static final String COLUMN_START_TIME = "start_timestamp";
        public static final String COLUMN_QUALITY    = "quality";

        /**
         * Creates SQL string for table creation, resets StringBuidler
         * @param sb Empty StringBuilder
         * @return SQL statement of sleep table creation
         */
        public static String createTableSQL(StringBuilder sb) {
            sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (")
                    .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(COLUMN_USER_ID).append(" INTEGER NOT NULL,")
                    .append(COLUMN_QUALITY).append(" INTEGER NOT NULL,")
                    .append(COLUMN_START_TIME).append(" INTEGER NOT NULL,")
                    .append(COLUMN_END_TIME).append(" INTEGER NOT NULL,")
                    .append("FOREIGN KEY (").append(COLUMN_USER_ID).append(") REFERENCES ")
                    .append(Db.user.TABLE_NAME).append("(").append(Db.user.COLUMN_ID).append(")")
                    .append(");");
            String SQL_Statement = sb.toString();
            sb.delete(0, sb.length());
            return SQL_Statement;
        }
    }
}
