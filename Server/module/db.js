const mysql = require('mysql2/promise');

const db_config = {
    host: 'localhost',
    user: 'root',
    password: 'mysql',
    port: 3307,
    connectionLimit: 10,
    database: 'hiauction'
}

module.exports = mysql.createPool(db_config);
