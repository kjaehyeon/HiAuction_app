module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();
    const signin = require('./signin');
    const signup = require('./signup');

    router.get('/signin', (req, res) => {
        console.log(1);
    });
    router.post('/signup', (req, res) => {

    });

    return router;
};