const express = require('express');
const bodyparser = require('body-parser');
const dotenv = require('dotenv');
const app = express();
dotenv.config();

const pool = require('./module/db');
const PORT = process.env.PORT || 4000;

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    next();
});
app.use(bodyparser.urlencoded({extended: true}));
app.use(bodyparser.json());
app.use('/user', require('./routers/user')(pool));
app.use('/main', require('./routers/main')(pool));
app.use('/my', require('./routers/my')(pool));
app.use('/chat', require('./routers/chat')(pool));

app.listen(PORT, () => {
    console.log(`Listening on port ${PORT}`);
});