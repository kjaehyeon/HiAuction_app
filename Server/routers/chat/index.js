module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.get('/rooms', async (req, res) => {
        const {query: {user_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result1] = await conn.query('SELECT Room_id, Buy_id, Sell_id'
                                                + ' FROM ROOM'
                                                + ' WHERE Buy_id = ?'
                                                + ' OR Sell_id = ?', [user_id, user_id]);
            
            if (!result1.length) {
                res.status(400).json({
                    message: '조회된 방이 없습니다'
                });
            } else {
                const room_list = [];
                for (const room of result1) {
                    if (room.Buy_id === user_id) {
                        const [result2] = await conn.query('SELECT M.Name Other_name, M.U_id,'
                                                            + ' R.Room_id, R.Create_date,'
                                                            + ' I.It_id, I.Name Item_name,'
                                                            + ' I.Img, A.Name Address,'
                                                            + ' (SELECT ROUND(AVG(Score), 1) FROM RATING'
                                                            + ' WHERE S_id = I.U_id) Score,'
                                                            + ' (SELECT Content FROM CHAT'
                                                            + ' WHERE Room_id = R.Room_id'
                                                            + ' ORDER BY Reg_date DESC LIMIT 1) Content,'
                                                            + ' (SELECT Reg_date FROM CHAT'
                                                            + ' WHERE Room_id = R.Room_id'
                                                            + ' ORDER BY Reg_date DESC LIMIT 1) Reg_date'
                                                            + ' FROM ROOM R, ITEM I,'
                                                            + ' ADDRESS A, MEMBER M'
                                                            + ' WHERE R.It_id = I.It_id'
                                                            + ' AND I.Ad_id = A.Ad_id'
                                                            + ' AND R.Sell_id = M.U_id'
                                                            + ' AND R.Buy_id = ?'
                                                            + ' AND R.Room_id = ?', [user_id, room.Room_id]);
                        room_list.push({
                            other_name: result2[0].Other_name,
                            other_id: result2[0].U_id,
                            room_id: result2[0].Room_id,
                            item_id: result2[0].It_id,
                            item_name: result2[0].Item_name,
                            reg_date: !result2[0].Reg_date ? 
                                        result2[0].Create_date.toLocaleDateString() : 
                                        result2[0].Reg_date.toLocaleDateString(),
                            content: !result2[0].Content ? '' : result2[0].Content,
                            address: result2[0].Address,
                            score: result2[0].Score,
                            img_url: result2[0].Img
                        });
                    } else {
                        const [result2] = await conn.query('SELECT M.Name Other_name, M.U_id,'
                                                            + ' R.Room_id, R.Create_date,'
                                                            + ' I.It_id, I.Name Item_name,'
                                                            + ' I.Img, A.Name Address,'
                                                            + ' (SELECT ROUND(AVG(Score), 1) FROM RATING'
                                                            + ' WHERE S_id = I.U_id) Score,'
                                                            + ' (SELECT Content FROM CHAT'
                                                            + ' WHERE Room_id = R.Room_id'
                                                            + ' ORDER BY Reg_date DESC LIMIT 1) Content,'
                                                            + ' (SELECT Reg_date FROM CHAT'
                                                            + ' WHERE Room_id = R.Room_id'
                                                            + ' ORDER BY Reg_date DESC LIMIT 1) Reg_date'
                                                            + ' FROM ROOM R, ITEM I,'
                                                            + ' ADDRESS A, MEMBER M'
                                                            + ' WHERE R.It_id = I.It_id'
                                                            + ' AND I.Ad_id = A.Ad_id'
                                                            + ' AND R.Buy_id = M.U_id'
                                                            + ' AND R.Sell_id = ?'
                                                            + ' AND R.Room_id = ?', [user_id, room.Room_id]);
                        room_list.push({
                            other_name: result2[0].Other_name,
                            other_id: result2[0].U_id,
                            room_id: result2[0].Room_id,
                            item_id: result2[0].It_id,
                            item_name: result2[0].Item_name,
                            reg_date: !result2[0].Reg_date ? 
                                        result2[0].Create_date.toLocaleDateString() : 
                                        result2[0].Reg_date.toLocaleDateString(),
                            content: !result2[0].Content ? '' : result2[0].Content,
                            address: result2[0].Address,
                            score: result2[0].Score,
                            img_url: result2[0].Img
                        });
                    }
                }
                room_list.sort((x, y) => {
                    if (x.reg_date > y.reg_date) return -1;
                    if (x.reg_date === y.reg_date) return 0;
                    if (x.reg_date < y.reg_date) return 1;
                });
                res.status(200).json(
                    room_list
                );
            }
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.get('/chats', async (req, res) => {
        const {query: {room_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async con => conn);
            const [result] = await conn.query('SELECT S_id, Content, Reg_date'
                                            + ' FROM CHAT'
                                            + ' WHERE Room_id = ?'
                                            + ' ORDER BY Reg_date', [room_id]);
            const chat_list = result.map((chat) => {
                return {
                    sender_id: chat.S_id,
                    reg_date: chat.Reg_date.toLocaleString(),
                    content: chat.Content
                };
            });
            res.status(200).json(chat_list);
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });
    return router;
};