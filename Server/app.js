const express = require('express');
const bodyparser = require('body-parser');
const dotenv = require('dotenv');
const socketio = require('socket.io');
const fs = require('fs').promises;
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
app.use(express.static('uploads'));
app.use('/api/user', require('./routers/user')(pool));
app.use('/api/main', require('./routers/main')(pool));
app.use('/api/my', require('./routers/my')(pool));
app.use('/api/chat', require('./routers/chat')(pool));
app.get('/uploads/:file_name', async (req, res) => {
    try {
        const data = await fs.readFile(__dirname + `/uploads/${req.params.file_name}`);
        res.write(data);
        res.end();
    } catch (err) {
        console.log(err);
        res.status(500).json({
            message: err.message
        });
    }
});

const server = app.listen(PORT, () => {
    console.log(`Listening on port ${PORT}`);
});

const io = socketio(server);

io.on('connection', async (socket) => {
    let conn = null;
    try {
        conn = await pool.getConnection(async conn => conn);
        socket.on('join_room', (data) => {
            socket.join(data);

            socket.on('message', async (data) => {
                const {room_id, content, sender_id, receiver_id} = data;
                await conn.query('INSERT INTO CHAT(S_id, R_id, Content, Room_id)'
                            + ' VALUES(?, ?, ? ,?)', [sender_id, receiver_id, content, room_id]);
                io.to(room_id).emit('new Message', sender_id, new Date().toLocaleString(), content);
            });
    
            socket.on('disconnect', () => {
                console.log('disconnected');
            });
        });
        
    } catch (err) {
        console.log(err);
    } finally {
        conn.release();
    }
});