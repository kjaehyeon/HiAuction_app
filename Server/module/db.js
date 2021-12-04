const mysql = require('mysql2/promise');

const db_config = {
    host: process.env.MYSQL_HOST,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    port: process.env.MYSQL_PORT,
    connectionLimit: 10,
    database: process.env.DATABASE
}

module.exports = mysql.createPool(db_config);
