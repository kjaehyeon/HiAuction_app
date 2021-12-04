module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.get('/bids', async (req, res) => {
        const {query: {user_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT I.It_id, I.Name AS Item_name, I.Current_price,'
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
                }
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

    return router;
};