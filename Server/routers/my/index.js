module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.get('/bids', async (req, res) => {
        const {query: {user_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT I.It_id, I.Name Item_name, I.Current_price,'
                                                + ' I.Img, I.Is_end, A.Name Address,'
                                                + ' B.B_id, B.Price, B.Create_date'
                                                + ' FROM BID B, ITEM I, ADDRESS A'
                                                + ' WHERE B.It_id = I.It_id'
                                                + ' AND I.Ad_id = A.Ad_id'
                                                + ' AND B.U_id = ?'
                                                + ' ORDER BY B.Create_date DESC', [user_id]);

            const bid_list = result.map((bid) => {
                return {
                    item_id: bid.It_id,
				    item_name: bid.Item_name,
                    item_price: bid.Current_price,
                    img_url: bid.Img,
                    is_end: bid.Is_end,
                    address: bid.Address,
                    bid_id: bid.B_id,
                    bid_price: bid.Price,
                    bid_created_date: bid.Create_date.toLocaleDateString(),
                };
            });

            res.status(200).json({
                bid_list
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.get('/items', async (req, res) => {
        const {query: {user_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT I.It_id, I.Name Item_name, I.Current_price,'
                                                + ' I.Img, I.Is_end, I.Create_date, A.Name Address'
                                                + ' FROM ITEM I, Address A'
                                                + ' WHERE I.Ad_id = A.Ad_id'
                                                + ' AND U_id = ?', [user_id]);
            
            const item_list = result.map((item) => {
                return {
                    item_id: item.It_id,
                    item_name: item.Item_name,
                    item_price: item.Current_price,
                    img_url: item.Img,
                    is_end: item.Is_end,
                    created_date: item.Create_date.toLocaleDateString(),
                    address: item.Address
                };
            });

            res.status(200).json({
                item_list
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.delete('/item', async (req, res) => {
        const {query: {item_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            await conn.query('DELETE FROM ITEM'
                                + ' WHERE It_id = ?', [item_id]);
            
            res.status(200).json({
                message: 'accepted'
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.put('/item/:action', async (req, res) => {
        const {params: {action}} = req;
        let conn = null;
        
        try {
            conn = await pool.getConnection(async conn => conn);
            switch (action) {
                case 'extension':
                    const {body: {item_id: extension_item_id, expired_date}} = req;
                    await conn.query('UPDATE ITEM SET Expire_date = ?'
                                    + ' WHERE It_id = ?', [expired_date, extension_item_id]);
                    break;
                case 'completion':
                    const {body: {item_id: completion_item_id}} = req;
                    await conn.query(`UPDATE ITEM SET Is_end = '4'`
                                    + ' WHERE It_id = ?', [completion_item_id]);
                    break;
            }

            res.status(200).json({
                message: 'accepted'
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.put('/info', async (req, res) => {
        const {body: {id, password, email, description}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            await conn.query('UPDATE MEMBER'
                                + ' SET Pw = ?, Email = ?, Description = ?'
                                + ' WHERE U_id = ?', [password, email, description, id]);
            res.status(200).json({
                message: 'accepted'
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/rating', async (req, res) => {
        const {body: {seller_id, buyer_id, score, description}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            await conn.query('INSERT INTO RATING(S_id, Buy_id, Score, Description)'
                            + ' VALUES(?, ?, ?, ?)', [seller_id, buyer_id, score, description]);
            
            res.status(200).json({
                message: 'accepted'
            });
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